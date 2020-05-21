package structure;

import java.util.ArrayList;
import java.util.List;

import beans.Person;

public class Tree {

    private Node root;
    
    public Tree(Node nodePerson)
    {
        this.root = nodePerson;
    }

    public void traverseTree(Node rootNode)//depth first
    {
    	if(rootNode!= null)
    	{
        System.out.println(rootNode.getPerson().getPerson_id());        
        	if(!rootNode.isLeaf() & rootNode != null)
             {System.out.println("|");
        		for(Node ch : rootNode.getChildren())
            		{
            	 		traverseTree(ch);
            		}
        }
    	}
    }
    
    
    public List<Tree> deleteNode(Node rootNode, List<Tree> trees)
    {
    	if(rootNode.isLeaf())
    		{
    			trees.remove(this);
    			//rootNode = null;
    		}
    		else
    		{
    			for(Node n : rootNode.getChildren())
    			{		    					
			    			Tree tree = new Tree(n);
			    			trees.add(tree);
			    			System.out.println("A new Tree of root "+n.getPerson().getPerson_id()+" is created");
			        		if(n.getParent() != null)
				    					{	
	        					for(Node n1 : n.getParent().getChildren())
			    				{

		    						if(!n1.equals(n)) {
			    						Tree tree1 = new Tree(n1);
			    						trees.add(tree1);
			    						if(n1.getPerson() != null)
			    						System.out.println("A new Tree of root "+n1.getPerson().getPerson_id()+" is created");
		    						}
		    					}
			    					}

			    					}
		    					trees.remove(this);
		
    			
    		}
		trees.remove(this);

    	return trees;
    	}


    

    
    
    public Node findNode(Node node,int id ) {
    	
        if (node.getPerson().getPerson_id() == id) {
        //	System.out.println("FOUND THIS ID "+node.getPerson().getPerson_id());
            return node;
            
        } else {
        	
        	if(!node.isLeaf())
        	{	
            for (Node child: node.getChildren()) {
                Node result = findNode(child,id);
                if (result != null) {
               // 	System.out.println("FOUND THIS ID"+result.getPerson().getPerson_id());
                    return result;
                }
            }
        }
        	
        }
    	//System.out.println("DIDNT FIND THIS ID "+id);

        return null;
    }
    

    public Node getRoot() {
  		return this.root;
  	}

  	public void setRoot(Node root) {
  		this.root = root;
  	}
  


}
