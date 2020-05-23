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
	public List<Tree> updateScoreTree(int date,Node node, List<Tree> trees,Tree tree) {
		
		int date_contamination=node.getPerson().getDiagnosed_ts();
		
	    if (!node.isLeaf())
		    {  for(Node n : node.getChildren())
			    {
			
			          trees = updateScoreTree(date,n,trees,tree);
			    }
	    }
		
        if (( (date - date_contamination) > 604800 &&  (date - date_contamination) <= 1209600))
        { 
            node.getPerson().setScore(4);
        }
        else
            {
	            if ((  (date - date_contamination) > 1209600))
	            {
	                node.getPerson().setScore(0);
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
	        Tree[] treeList= trees.toArray(new Tree[trees.size()]);
	        for (Tree tree : treeList) {
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
		 int sum = rootNode.getPerson().getScore();
		 if(!rootNode.isLeaf())
			 {
				 for(Node ch : rootNode.getChildren())
			         {
						 sum += generateScore(ch);
			         }
			 }
		 return sum;
		 
	 }
	 
	 
	 public Map<Person, Integer> generate(List<Tree> trees,String country) {
		 Map<Person, Integer> scores = new HashMap<Person, Integer>();
		 for(Tree t : trees) {
			 if(generateScore(t.getRoot()) != 0)
			scores.put(t.getRoot().getPerson(), generateScore(t.getRoot()));
	            }
	        return scores;
	        		
	        }
	 
	   
	 public List<Tree> deleteNode(int date,Node rootNode, List<Tree> trees,Tree tr)
	  {
	    	if(!rootNode.isLeaf())
	    	{
	    			for(Node n : rootNode.getChildren())
	    			{		    					
	    				// Create new nodes only if they are still up to date
	    				if(date - n.getPerson().getDiagnosed_ts()<= 1209600){
							Tree tree = new Tree(n);
			    			trees.add(tree);			    			
	    				}
	    			}
	    	}
	    	if(tr.getRoot().getPerson().getPerson_id() == rootNode.getPerson().getPerson_id()) {
	    		trees.remove(tr);				
	    	}	    	
	    	return trees;
	    	}

	 public List<Tree> process(Person person,List<Tree> trees)
	 {
		 Node pNode = new Node(person);
		 trees = updateScoreList(person.getDiagnosed_ts(),trees);
		 int id = person.getContaminated_by();
		 if(id == -1)
		 {
			 Tree tree = new Tree(pNode);
			 trees.add(tree);

		 }
		 else
		 {
			 
			 Node n = null;
			 for(Tree t :trees)
			 {
				 n = t.findNode(t.getRoot(),id);
				 if(n != null)
				 {
					 
					 n.addChild(pNode);	
					

					 break;
				 }
			 }

			 if(n == null)
			 {
				 Tree tree = new Tree(pNode);
				 trees.add(tree);
				
			 }

		 }

		 return trees;

	 }

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

