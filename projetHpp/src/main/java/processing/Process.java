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
	public List<Tree> updateScoreTree(int date,Node node, List<Tree> trees) {
		
		int date_contamination=node.getPerson().getDiagnosed_ts();
		
	    if (!node.isLeaf())
		    {  for(Node n : node.getChildren())
			    {
			
			          trees = updateScoreTree(date,n,trees);
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
	                Node nodeToDelete = null;
	                for(Tree t : trees) {

	                	nodeToDelete = t.findNode(node, node.getPerson().getPerson_id());
	                    if(nodeToDelete != null)
	                    {
	                    	trees = t.deleteNode(date,nodeToDelete,trees);
	                    	break;
	                    }
	                }


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
	            trees = updateScoreTree(date,tree.getRoot(),trees);
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
	 
	   
	 
	 public List<Tree> process(Person person,List<Tree> trees)
	 {
		 Node pNode = new Node(person);
		 trees = updateScoreList(person.getDiagnosed_ts(),trees);
		 int id = person.getContaminated_by();
		 if(id == -1)
		 {
			 Tree tree = new Tree(pNode);
			 trees.add(tree);
			// System.out.println("A new tree of root "+pNode.getPerson().getPerson_id()+" is created !");

		 }
		 else
		 {
			 
			 Node n = null;
			 for(Tree t :trees)
			 {
				 if(t.findNode(t.getRoot(),id) != null)
				 {
					 n = t.findNode(t.getRoot(),id);
					// System.out.println("found the node " + n.getPerson().getPerson_id()); 
					 n.addChild(pNode);	
					// System.out.println("added the child " +n.getChildren().get(0).getPerson().getPerson_id() + " to the tree of root "+ t.getRoot().getPerson().getPerson_id());

					 break;
				 }
				// System.out.println("Searched all the trees, no "+id+" to be found");
			 }

			 if(n == null)
			 {
				// System.out.println("Didn't find the parent " + id);
				 Tree tree = new Tree(pNode);
				 trees.add(tree);
				// System.out.println("A new tree of root "+pNode.getPerson().getPerson_id()+" is created !");
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
	 
	 public Map<Person,Integer> generateFinalResult(Map<Person,Integer> top3Spanish, int spanishContaminationDate, Map<Person,Integer> top3French, int frenchContaminationDate, Map<Person,Integer> top3Italian, int italianContaminationDate ){
		 
		 Map<Person,Integer> result= new LinkedHashMap<Person,Integer>();
		 
		 // in everything that follows we always have :
		 // index 0 : france , index 1 : spain, index 2 : italy results
		 
		 //create list of lists of scores and ids
		// index 0 : french results , index 1 : spanish results, index 2 : italian results
		 
		 List<ArrayList<Integer>> allScores=new ArrayList<ArrayList<Integer>>();
		 allScores.add(new ArrayList<>(top3French.values()));
		 allScores.add(new ArrayList<>(top3Spanish.values()));
		 allScores.add(new ArrayList<>(top3Italian.values()));
		 
		 List<ArrayList<Person>> id=new ArrayList<ArrayList<Person>>();
		 id.add(new ArrayList<>(top3French.keySet()));
		 id.add(new ArrayList<>(top3Spanish.keySet()));
		 id.add(new ArrayList<>(top3Italian.keySet()));
		 
		 //create list of indexes to browse lists of score
		 List<Integer> indexes=new ArrayList<>();
		 indexes.add(0);
		 indexes.add(0);
		 indexes.add(0);
		 
		 int index;
		 
		 //initialize counter of elements in result
		 int cmpt=0;
		 
		 //create a list of scores where we add the first result of each country
		 // if list of scores is empty add 0
		 List<Integer> scores=new ArrayList<>();
		 if (allScores.get(0).size()!=0) {
			 scores.add(allScores.get(0).get(0));
		 } else {
			 scores.add(0);
		 }
		 if (allScores.get(1).size()!=0) {
			 scores.add(allScores.get(1).get(0));
		 } else {
			 scores.add(0);
		 }if (allScores.get(2).size()!=0) {
			 scores.add(allScores.get(2).get(0));
		 } else {
			 scores.add(0);
		 }
		 
		 //add contamination dates to a list in the right order of countries
		 List<Integer> contaminationDates=new ArrayList<>();
		 contaminationDates.add(frenchContaminationDate);
		 contaminationDates.add(spanishContaminationDate);
		 contaminationDates.add(italianContaminationDate);
		 
		 //look for max score
		 Integer max=Collections.max(scores);
		 
		 //while counter<3 (less than three elements in result)
		 //and all scores>0
		 while(cmpt<3 && max!=0) {
			 
			 // if max value occurs more than once
			 if (Collections.frequency(scores, max)>1) {
				// if max value occurs three times
				 if (Collections.frequency(scores, max)==3) {
					 
					 //get the index of min contamination date
					 int indMin=contaminationDates.indexOf(Collections.min(contaminationDates));
					 
					 //add the score 
					 result.put((id.get(indMin)).get(indexes.get(indMin)),(allScores.get(indMin)).get(indexes.get(indMin)));

					 //update index in list of scores of the country
					 indexes.set(indMin,indexes.get(indMin)+1);
					 
					 //update counter
					 cmpt++;
					 
					 // if there's still elements in the list of scores
					 if (indexes.get(indMin)<allScores.get(indMin).size()) {
						 
						 //add next best score to list of scores
						 scores.set(indMin,(allScores.get(indMin)).get(indexes.get(indMin)));
					 }
					 else {
						 
						 //else put 0 at the the index of the country
						 //no more scores
						 scores.set(indMin,0);
					 }
					 
				 }
			 // if max value occurs twice
			     else {
				 
					 // find the two indexes of max 
					 int ind1=scores.indexOf(max);
					 int ind2=scores.lastIndexOf(max);
					 
					 //compare contamination dates of two countries 
					 if(contaminationDates.get(ind1)<contaminationDates.get(ind2)){
						 
						 //add ind1 to result 
						 result.put((id.get(ind1)).get(indexes.get(ind1)),(allScores.get(ind1)).get(indexes.get(ind1)));
						 
						 //update index
						 indexes.set(ind1,indexes.get(ind1)+1);
						 
						 if (indexes.get(ind1)<allScores.get(ind1).size()) {
							 //add next best score to list of scores
							 scores.set(ind1,(allScores.get(ind1)).get(indexes.get(ind1)));
						 }
						 else {
							 //put 0 at index of country
							 scores.set(ind1,0);
						 }
						 
						 //update counter
						 cmpt++; 
						 
					 }
					 else{
						 
						 //add ind2 to result
						 result.put((id.get(ind2)).get(indexes.get(ind2)),(allScores.get(ind2)).get(indexes.get(ind2)));
						 
						 //update index 
						 indexes.set(ind2,indexes.get(ind2)+1);
						 
						 if (indexes.get(ind2)<allScores.get(ind2).size()) {
							 //add next best score to list of scores
							 scores.set(ind2,(allScores.get(ind2)).get(indexes.get(ind2)));
						 }
						 else {
							 //put 0 at index of country
							 scores.set(ind2,0);
						 }
						 
						 //update counter
						 cmpt++;
					 }
				}
			 // if max value occurs once
			 } else {
			 	
				 //get index of max value
				 index=scores.indexOf(max);
				 
				 //add result 
				 result.put((id.get(index)).get(indexes.get(index)),max);
				 
				 //update index in indexes list
				 indexes.set(index,indexes.get(index)+1);
				 
				// if there's still elements in the list of scores
				 if (indexes.get(index)<allScores.get(index).size()) {
					 
					 //add the next best element to list of scores
					 scores.set(index,(allScores.get(index)).get(indexes.get(index)));
				 }
				 else {
					 
					//else put 0 at the the index of the country
					 //no more scores
					 scores.set(index,0);
				 }
				 
				 //update counter 
				 cmpt++;
				 
			 }
			 
			 //update new max value
			 max=Collections.max(scores);
		 }
				 
		 return result;
		 
	} 

	 
	 
}

