package structure;

import java.util.List;



public class Tree {

    private Node root;
    
    public Tree(Node nodePerson)
    {
        this.root = nodePerson;
    }
    
    public Node getRoot() {
  		return this.root;
  	}

  	public void setRoot(Node root) {
  		this.root = root;
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
    
    
  

    
    
    public Node findNode(Node node,int id ) {
        //System.out.println("LOOKING FOR THIS ID "+node.getPerson().getPerson_id());

        if (node.getPerson().getPerson_id() == id) {
        //System.out.println("FOUND THIS ID "+node.getPerson().getPerson_id());
            return node;
            
        } else {
        	
        	if(!node.isLeaf())
        	{	
            for (Node child: node.getChildren()) {
                Node result = findNode(child,id);
                if (result != null) {
               //System.out.println("FOUND THIS ID"+result.getPerson().getPerson_id());
                    return result;
                }
            }
        }
        	
        }
    	//System.out.println("DIDNT FIND THIS ID "+id);

        return null;
    }
    



}
