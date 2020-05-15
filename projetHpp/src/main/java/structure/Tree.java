package structure;

import java.util.ArrayList;
import java.util.List;

import beans.Person;

public class Tree {

    public Node root;
    
    public Tree(Node nodePerson)
    {
        this.root = nodePerson;
    }

    public void traverseTree(Node rootNode)//depth first
    {
    	if(rootNode!= null)
    	{
        System.out.println(rootNode.person.getPerson_id());        
        	if(!rootNode.isLeaf() & rootNode != null)
             {System.out.println("|");
        		for(Node ch : rootNode.children)
            		{
            	 		traverseTree(ch);
            		}
        }
    	}
    }
    
    
    public List<Tree> deleteNode(Node rootNode, List<Tree> trees)
    {
    	if(!rootNode.isLeaf()) {
    			for(Node n : rootNode.children) {
    				Tree tree = new Tree(n);
			    	trees.add(tree);
			    	System.out.println("A new Tree of root "+n.person.getPerson_id()+" is created");			
			    					}		    					
	    					}
         trees.remove(this);   			  		   	
    	return trees;

    }

    
    
    public Node findNode(Node node,int id ) {
    	
        if (node.person.getPerson_id() == id) {
            return node;
        } else {
        	
        	if(!node.isLeaf())
        	{	
            for (Node child: node.children) {
                Node result = findNode(child,id);
                if (result != null) {
                    return result;
                }
            }
        }
        	
        }
        return null;
    }
    

    public Node getRoot() {
  		return root;
  	}

  	public void setRoot(Node root) {
  		this.root = root;
  	}

  
}
