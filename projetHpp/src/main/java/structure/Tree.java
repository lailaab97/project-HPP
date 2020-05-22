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
    
    
    public List<Tree> deleteNode(int date,Node rootNode, List<Tree> trees)
    {
    	if(!rootNode.isLeaf())
    	{
    			for(Node n : rootNode.getChildren())
    			{		    					
    				// Create new nodes only if they are still up to date
    				if(date - n.getPerson().getDiagnosed_ts()<= 1209600){
						Tree tree = new Tree(n);
		    			trees.add(tree);
		    			//System.out.println("A new Tree of root "+n.getPerson().getPerson_id()+" is created++");

    				}

    			}
    	}
    	if(this.getRoot().getPerson().getPerson_id() == rootNode.getPerson().getPerson_id()) {
    		trees.remove(this);
			//System.out.println("The tree of "+this.getRoot().getPerson().getPerson_id()+" is removed");
    	}
			        	
    	
    	return trees;
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
