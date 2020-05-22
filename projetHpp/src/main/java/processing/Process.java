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
		 
		 List<Integer> scores=new ArrayList<>();
		 scores.addAll(mapOfIdsAndScores.values());
		 
		 List<Person> id=new ArrayList<>();
		 id.addAll(mapOfIdsAndScores.keySet());
		 
		 int cmpt=0;
		 int max=Collections.max(scores);
		 
		 while(cmpt<3 && scores.size()!=0 && max!=0) {
			 
			 if (Collections.frequency(scores, max)!=1) {
				 
				 List<Integer> restOfScores=new ArrayList<>();
				 List<Person> restOfIds=new ArrayList<>();
				 List<Integer> dates=new ArrayList<>();
				 
				 int index=scores.indexOf(max);
				 while(index!=-1) {
					 restOfScores.add(scores.get(index));
					 restOfIds.add(id.get(index));
					 dates.add(id.get(index).getDiagnosed_ts());
					 scores.remove(index);
					 id.remove(index);
					 index=scores.indexOf(max);
				 }
				 
				 while(cmpt<3 && restOfScores.size()!=0) {
					 int min=dates.indexOf(Collections.min(dates));
					 result.put(restOfIds.get(min),restOfScores.get(min));
					 restOfScores.remove(min);
					 restOfIds.remove(min);
					 dates.remove(min);
					 cmpt++;
					 
				 }
 			 }
			 else {
				 
				 int index=scores.indexOf(max);
				 result.put(id.get(index),max);
				 cmpt++;
				 scores.remove(index);
				 id.remove(index);
				 
			 }
			 
			 if (scores.size()!=0) {
				 max=Collections.max(scores);
			 }
			 
		 }
		 return result;
	 }
	 
	 public Map<Person,Integer> generateFinalResult(Map<Person,Integer> top3Spanish, int spanishContaminationDate, Map<Person,Integer> top3French, int frenchContaminationDate, Map<Person,Integer> top3Italian, int italianContaminationDate ){
		 
		 Map<Person,Integer> result= new LinkedHashMap<Person,Integer>();
		 
		 
		 List<ArrayList<Integer>> allScores=new ArrayList<ArrayList<Integer>>();
		 allScores.add(new ArrayList<>(top3French.values()));
		 allScores.add(new ArrayList<>(top3Spanish.values()));
		 allScores.add(new ArrayList<>(top3Italian.values()));
		 
		 List<ArrayList<Person>> id=new ArrayList<ArrayList<Person>>();
		 id.add(new ArrayList<>(top3French.keySet()));
		 id.add(new ArrayList<>(top3Spanish.keySet()));
		 id.add(new ArrayList<>(top3Italian.keySet()));
		 
		 List<Integer> indexes=new ArrayList<>();
		 indexes.add(0);
		 indexes.add(0);
		 indexes.add(0);
		 
		 int index;
		 int cmpt=0;
		 
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
		 
		 List<Integer> contaminationDates=new ArrayList<>();
		 contaminationDates.add(frenchContaminationDate);
		 contaminationDates.add(spanishContaminationDate);
		 contaminationDates.add(italianContaminationDate);
		 
		 Integer max=Collections.max(scores);
		 
		 while(cmpt<3 && max!=0) {
			 
			 if (Collections.frequency(scores, max)>1) {
				 if(Collections.frequency(scores, max)==3) {
					 int indMax=contaminationDates.indexOf(Collections.max(contaminationDates));
					 int indMin=contaminationDates.indexOf(Collections.min(contaminationDates));
					 int indMed=3-(indMax+indMin);
					 
				
					 result.put((id.get(indMin)).get(indexes.get(indMin)),(allScores.get(indMin)).get(indexes.get(indMin)));
					 indexes.set(indMin,indexes.get(indMin)+1);
					 cmpt++;
					 
					 if (cmpt<3) {
						 result.put((id.get(indMed)).get(indexes.get(indMed)),(allScores.get(indMed)).get(indexes.get(indMed)));
						 indexes.set(indMed,indexes.get(indMed)+1);
						 cmpt++;
					 }
					 if (cmpt<3) {
						 result.put((id.get(indMax)).get(indexes.get(indMax)),(allScores.get(indMax)).get(indexes.get(indMax)));
						 indexes.set(indMax,indexes.get(indMax)+1);
						 cmpt++;
					 }
				 }
				 else {
					 
					 int ind1=scores.indexOf(max);
					 int ind2=scores.lastIndexOf(max);
					 if(contaminationDates.get(ind1)<contaminationDates.get(ind2)){
						 
						 result.put((id.get(ind1)).get(indexes.get(ind1)),(allScores.get(ind1)).get(indexes.get(ind1)));
						 indexes.set(ind1,indexes.get(ind1)+1);
						 if (indexes.get(ind1)<allScores.get(ind1).size()) {
							 scores.set(ind1,(allScores.get(ind1)).get(indexes.get(ind1)));
						 }
						 else {
							 scores.set(ind1,0);
						 }
						 cmpt++; 
						 
						 
						 if(cmpt<3){
							 result.put((id.get(ind2)).get(indexes.get(ind2)),(allScores.get(ind2)).get(indexes.get(ind2)));
							 indexes.set(ind2,indexes.get(ind2)+1);
							 if (indexes.get(ind2)<allScores.get(ind2).size()) {
								 scores.set(ind2,(allScores.get(ind2)).get(indexes.get(ind2)));
							 }
							 else {
								 scores.set(ind2,0);
							 }
							 
							 cmpt++;
						 }
					 }
					 else{
						 
						 result.put((id.get(ind2)).get(indexes.get(ind2)),(allScores.get(ind2)).get(indexes.get(ind2)));
						 indexes.set(ind2,indexes.get(ind2)+1);
						 if (indexes.get(ind2)<allScores.get(ind2).size()) {
							 scores.set(ind2,(allScores.get(ind2)).get(indexes.get(ind2)));
						 }
						 else {
							 scores.set(ind2,0);
						 }
						 
						 cmpt++;
						 
						 
						 if(cmpt<3){
							 result.put((id.get(ind1)).get(indexes.get(ind1)),(allScores.get(ind1)).get(indexes.get(ind1)));
							 indexes.set(ind1,indexes.get(ind1)+1);
							 if (indexes.get(ind1)<allScores.get(ind1).size()) {
								 scores.set(ind1,(allScores.get(ind1)).get(indexes.get(ind1)));
							 }
							 else {
								 scores.set(ind1,0);
							 }
							 cmpt++; 
						 }
						 
					 }
					 
				 }
			 }
			 
			 else {
				 
				 index=scores.indexOf(max);
				 result.put((id.get(index)).get(indexes.get(index)),(allScores.get(index)).get(indexes.get(index)));
				 indexes.set(index,indexes.get(index)+1);
				 if (indexes.get(index)<allScores.get(index).size()) {
					 scores.set(index,(allScores.get(index)).get(indexes.get(index)));
				 }
				 else {
					 scores.set(index,0);
				 }
				 cmpt++;
				 
			 }
			 
			 max=Collections.max(scores);
		 }
				 
		 return result;
		 
	} 

	 
	 
}

