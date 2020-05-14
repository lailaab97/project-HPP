package processing;

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
            
            if (date - date_contamination < 604800)
            { 
                node.getPerson().setScore(10);                      
            }
            else
                {
                if (( date - date_contamination >=604800 &&  date - date_contamination <1209600))
                {
                    node.getPerson().setScore(4);  
                }
                else 
                {
                    node.getPerson().setScore(0); 
                    for(Tree t : trees) {
                    	trees = t.deleteNode(node,trees);
                    }
                }
                }
        }
        else {
            
          for(Node n : node.children)
        {
             trees = updateScoreTree(date,n,trees);
        }                                                                  
    }            
        return trees;
    }

	public List<Tree> updateScoreList(int date,List<Tree> trees) {
        for (Tree tree : trees) {

            trees = updateScoreTree(date,tree.root,trees);
  }
        return trees;
        }
	
	
	 
	 public int generateScore(Node rootNode) {
		 int sum=rootNode.getPerson().getScore();
		 if(!rootNode.isLeaf())
		 {
		 for(Node ch : rootNode.children)
         {
			 sum += ch.getPerson().getScore();
			 sum = generateScore(ch);
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
		 return trees;

	 }
	 
	 public void output(Map<Person,Integer> map) {
		 map.forEach((k,v)->System.out.println(k.getCountry()+" , " + k.getPerson_id()+" , " +v));
	 }
	 
	 
}

