package processing;
import java.util.HashMap;
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

            if (( (date - date_contamination) > 604800 &&  (date - date_contamination) <=1209600))
            { 
            	node.getPerson().setScore(4);  
            }
            else
                {
                if ((  (date - date_contamination) > 1209600))
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
        if (!node.isLeaf())    
        {  for(Node n : node.children)
        {
              updateScoreTree(date,n,trees);
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
			 System.out.println("A new tree of root "+pNode.getPerson().getPerson_id()+" is created !");

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
		 trees = updateScoreList(person.getDiagnosed_ts(),trees);

		 return trees;

	 }
	 
	 

	 
	 
}

