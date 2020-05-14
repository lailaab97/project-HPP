package processing;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import beans.Person;
import structure.Node;
import structure.Tree;

public class Process{

	
	public List<Tree> updateScoreTree(int date,Node node, List<Tree> trees) {

		if (node.isLeaf())
        {   
	        int date_contamination=node.getPerson().getDiagnosed_ts();

            if (date - date_contamination <= 604800)
            { 
                node.getPerson().setScore(10);                      
            }
            else
                {
                if (( (date - date_contamination) > 604800 &&  (date - date_contamination) <=1209600))
                {
                	//System.out.println(node.getPerson().getPerson_id()+" had the score "+node.getPerson().getScore());
                    node.getPerson().setScore(4);  
                	//System.out.println(node.getPerson().getPerson_id()+" have now the score "+node.getPerson().getScore());

                }
                else 
                {
                    node.getPerson().setScore(0);
                    for(Tree t : trees) {                    	
                    	node = t.findNode(node, node.getPerson().getPerson_id());
                    if(node != null)
                   {
                       trees = t.deleteNode(node,trees);
                       
                    break;
                    }
                    }
                    	
                    }
                }
                }
        
        else {
            
          for(Node n : node.children)
        {
              updateScoreTree(date,n,trees);
        }                                                                  
    }    
        return trees;
    }

	public List<Tree> updateScoreList(int date,List<Tree> trees) {
		Tree[] treeList= trees.toArray(new Tree[trees.size()]);
        for (Tree tree : treeList) {
            updateScoreTree(date,tree.getRoot(),trees);
        }
        return trees;
        }
	
	
	 
	 public int generateScore(Node rootNode) {
		 int sum = rootNode.getPerson().getScore();
		 if(!rootNode.isLeaf())
			 {
				 for(Node ch : rootNode.children)
			         {
						 sum += generateScore(ch);
			         }
			 }
		 return sum;
		 
	 }
	 
	 public Map<Person, Integer> generate(List<Tree> trees,String country) {
		 Map<Person, Integer> scores = new HashMap<Person, Integer>();
		 for(Tree t : trees) {
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

		 }
		 else
		 {
			 Node n = null;
			 for(Tree t :trees)
			 {
				 if(t.findNode(t.getRoot(),id) != null)
					 n = t.findNode(t.getRoot(),id);
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
		 //trees = updateScoreList(person.getDiagnosed_ts(),trees);
		 return trees;

	 }
	 
	 public void output(Map<Person,Integer> map) {
		 map.forEach((k,v)-> {System.out.println(k.getCountry()+" , " + k.getPerson_id()+" , " +v);});
	 }
	 
	 
}

