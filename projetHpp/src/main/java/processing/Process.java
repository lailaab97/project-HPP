package processing;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import beans.Person;
import structure.Node;
import structure.Tree;

/**
 * Process implements  the business logic of the program 
 * 
 * @author Team
 * 
 */
public class Process{

	/**
	 * This method Updates scores of different nodes of the tree
	 * @param int : the contamination date of the current case
	 * @param Node: presents a node , at the beginning it is the root of the tree
	 * @param List<Tree> : presents the list of trees elements
	 * @return  List<Tree> : presents the updated list of trees elements
	 */
	public List<Tree> updateScoreTree(int date, Node node , List<Tree> trees, Tree tree) {
		
		//we get the date of contamination
		int date_contamination = node.getPerson().getDiagnosed_ts();
		//if the node is not a leaf
	    if (!node.isLeaf())
		    {  
	    	//for every child of that node we update score of the tree in a recursive way
	    	for(Node n : node.getChildren())
			    {
			
			          trees = updateScoreTree(date,n,trees,tree);
			    }
	    }
		//if the difference between the date of contamination of the node (hence person) and the last date entered is
	    //between 7 (exclusive) and 14 (inclusive)
        if (( (date - date_contamination) > 604800 &&  (date - date_contamination) <= 1209600))
        { 
        	//we set the score at 4
            node.getPerson().setScore(4);
        }
        else
            {
        	//if this date is more than 14 days (exclusive)
	            if ((  (date - date_contamination) > 1209600))
	            {
	            	//we set the node's score to 0
	                node.getPerson().setScore(0);
	                //we delete the node in question
	                trees = deleteNode(date,node,trees,tree);               

	             }

            }

	    return trees;
    }

	
	/**
     * This method Updates scores of different trees of the list
     * @param int : the contamination date of the current case
     * @param List<Tree> : presents the list of trees elements
     * @return  List<Tree> : presents the updated list of trees elements
     */
    public List<Tree> updateScoreList(int date,List<Tree> trees) {
    		// To avoid confrontation, we transform the list of trees to an array
	        Tree[] treeList= trees.toArray(new Tree[trees.size()]);
	        //for avery tree in the list
	        for (Tree tree : treeList) {
	        	//we update the score 
	            trees = updateScoreTree(date,tree.getRoot(),trees,tree);
	        }
	        return trees;
        }
	
	/**
	 * This method computes the score of a chain , by calculating the sum of all its nodes
	 * @param Node : the root of the chain
	 * @return  int : presents the computed score
	 */ 
	 public int generateScore(Node rootNode) {
		 //we get the score of rootNode
		 int sum = rootNode.getPerson().getScore();
		 //if this rootNode is not a leaf
		 if(!rootNode.isLeaf())
			 {
			 //we add ip the score of all its existing children
				 for(Node ch : rootNode.getChildren())
			         {
						 sum += generateScore(ch);
			         }
			 }
		 return sum;
		 
	 }
	 
	 /**
	  * This method generate the map which contains all the roots of the chains of contamination and their scores
	  * @param trees : the list of trees containing chains of contamination
	  * @return Map<Person, Integer> : the map that contains roots of the chain and their scores 
	  * */
	 
	 public Map<Person, Integer> generate(List<Tree> trees) {
		 //where we are going to stock all scores
		 Map<Person, Integer> scores = new HashMap<Person, Integer>();
		 //for each tree in the list of trees we add to the map (root person , the score of the chain)
		 for(Tree t : trees) {
			 if(generateScore(t.getRoot()) != 0)
			scores.put(t.getRoot().getPerson(), generateScore(t.getRoot()));
	            }
	        return scores;
	        		
	        }
	 
	   /**
	    * This method deletes nodes and handles the new additions and deletions of the list of trees
	    * @param date : last date of the entery
	    * @param rootNode : the node we want to delete
	    * @param trees : the list of all the trees available
	    * @param tree : the tree where belong the node
	    * */
	 public List<Tree> deleteNode(int date,Node rootNode, List<Tree> trees,Tree tr)
	  {
		 //if the rootNode is not a leaf
	    	if(!rootNode.isLeaf())
	    	{
	    		//for each of each children still alive
	    			for(Node n : rootNode.getChildren())
	    			{		    					
	    				// Create new nodes only if they are still up to date
	    				if(date - n.getPerson().getDiagnosed_ts()<= 1209600){
							Tree tree = new Tree(n);
							//we add the new tree to the list of trees available
			    			trees.add(tree);			    			
	    				}
	    			}
	    	}
	    	//if the node we want to the delete is the root of a tree
	    	if(tr.getRoot().getPerson().getPerson_id() == rootNode.getPerson().getPerson_id()) {
	    		//we remove the tree from the list 
	    		trees.remove(tr);				
	    	}	    	
	    	return trees;
	    	}

	 /**
	  * This method runs the algorithm to keep up with the trees still alive after each entry
	  * @param person : the new person that we are going to treat
	  * @param : trees : list of all the trees available
	  * */
	 public List<Tree> process(Person person,List<Tree> trees)
	 {
		 //we create a node for the person we are treating
		 Node pNode = new Node(person);
		 //we update the score of all trees available
		 trees = updateScoreList(person.getDiagnosed_ts(),trees);
		 //we get the id of the person which contaminated this person
		 int id = person.getContaminated_by();
		 //if the id == -1 which means "unknown"
		 if(id == -1)
		 {
			 //this means that this person is going to be a root tto a whole new tree
			 //we add this tree to the list of trees available
			 Tree tree = new Tree(pNode);
			 trees.add(tree);

		 }
		 else
		 {
			 //we create a null node
			 Node n = null;
			 //we browse through all the trees
			 for(Tree t :trees)
			 {
				 //we look if this person that contaminated exists in one of the trees
				 n = t.findNode(t.getRoot(),id);
				 //if we find the node, it means that the node is not null anymore
				 if(n != null)
				 {
					//we add this person as a child to the person that contaminated 
					 n.addChild(pNode);	
					 //and there is no need to browse in other trees
					 break;
				 }
			 }
			 //if n is still null it means that there is no node existing in all of the trees
			 if(n == null)
			 {
				 //so we add this person as a root to its own tree
				 Tree tree = new Tree(pNode);
				 trees.add(tree);
				
			 }

		 }

		 return trees;

	 }
/**
 * Generate the top 3 of a country
 * @param mapOfIdsAndScores : the map that contains all potential chains of contamiantion
 * @return map of the 3 top of the country
 * */
public Map<Person,Integer> generateResultByCountry (Map<Person, Integer> mapOfIdsAndScores) {
		 
		 Map<Person,Integer> result=new LinkedHashMap<Person,Integer>();
		 
		 //convert mapOfIdsAndScores to two lists of scores and ids
		 //each index in the id list correspond to its value in the scores list at the same index
		 List<Integer> scores=new ArrayList<>();
		 scores.addAll(mapOfIdsAndScores.values());
		 
		 List<Person> id=new ArrayList<>();
		 id.addAll(mapOfIdsAndScores.keySet());
		 
		//initialize counter of elements in result 
		 int cmpt=0;
		 
		 // search for max in scores
		 int max=Collections.max(scores);
		 
		 while(cmpt<3 && scores.size()!=0 && max!=0) {
			 
			 // if the maximum score exists more than once
			 if (Collections.frequency(scores, max)!=1) {
				 
				 // create variables to add ids that have the same score
				 List<Person> restOfIds=new ArrayList<>();
				 
				 //dates to store dates of contamination of root
				 List<Integer> dates=new ArrayList<>();
				 
				 // look for first index of max
				 int index=scores.indexOf(max);
				 
				 //while max exists in scores
				 while(index!=-1) {
					 //add id and date to lists
					 restOfIds.add(id.get(index));
					 dates.add(id.get(index).getDiagnosed_ts());
					 
					 //remove score and id from original lists
					 scores.remove(index);
					 id.remove(index);
					 
					 //update index of max
					 index=scores.indexOf(max);
				 }
				 
				 // while counter<3 : less than three elements in result
				 // and while there's still elements in the lists
				 while(cmpt<3 && restOfIds.size()!=0) {
					 
					 //get index of minimum of dates
					 int min=dates.indexOf(Collections.min(dates));
					 
					 //add score and id to result
					 result.put(restOfIds.get(min),max);
					 
					 //remove id and date from lists
					 restOfIds.remove(min);
					 dates.remove(min);
					 
					 //update counter
					 cmpt++;
					 
				 }
 			 }
			 // if max score only occures once
			 else {
				 
				 //get index of max
				 int index=scores.indexOf(max);
				 
				 //add id and score to result
				 result.put(id.get(index),max);
				 
				 //update counter after adding to result
				 cmpt++;
				 
				 //remove element from both lists to look for the new max
				 scores.remove(index);
				 id.remove(index);
				 
			 }
			 //update max value while if there's still elements in the score lists
			 if (scores.size()!=0) {
				 max=Collections.max(scores);
			 }
			 
		 }
		 return result;
	 }
	
 

	 
	
	 
	 
}

