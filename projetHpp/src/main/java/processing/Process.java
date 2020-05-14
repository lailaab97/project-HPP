package processing;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Person;
import structure.Node;
import structure.Tree;

public class Process{

	
	
	 public void updateScore(int date,List<Tree> trees) {
	    	
	       for (Tree tree : trees) {
	    	  
	    	   for (Node node:tree.root.children)
	    	   {
	    		   
	    	   }	    	       	   	    	   
	       }
	 }
	 
	 public int generateScore(Node rootNode) {
		 int sum=0;
		 for(Node ch : rootNode.children)
         {
			 sum += ch.getPerson().getScore();
			 generateScore(ch);
         }
		 return sum;
	 }
	 
	 public Map<Integer, Integer> generate(List<Tree> trees,String country) {
		 Map<Integer, Integer> scores = new HashMap<Integer, Integer>();
		 for(Tree t : trees) {
			scores.put(t.getRoot().getPerson().getPerson_id(), generateScore(t.getRoot()));
	            }
	        return scores;
	        		
	        }
	 
	    	
	 public List<Tree> process(Person person,List<Tree> trees)
	 {
		 Node pNode = new Node(person);
		 updateScore(person.getDiagnosed_ts(),trees);
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
					 break;
				 }
			 if(n != null)
			 {
				 n.addChild(pNode);
			 }
			 else
			 {
				 Tree tree = new Tree(pNode);
				 trees.add(tree);
			 }
		 } 
		 return trees;

	 }
	 
	 
}

