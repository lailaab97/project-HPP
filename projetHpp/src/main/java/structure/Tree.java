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
    
    
    public void deleteNode(Person person, Node rootNode, List<Tree> trees)
    {
    	if(rootNode.isLeaf())
    		{
    		if(rootNode.person.equals(person))
    			rootNode = null;
    		}
    		else
    		{
    			for(Node n : rootNode.children)
    			{
    				if(n.person.equals(person))
    					{
    					if(n.isLeaf())
        					n = null;
    					
    					else {
    					
    						//We create trees for all the children
    					for(Node node : n.children)
    					{
    						Tree tree = new Tree(node);
    						trees.add(tree);
    						System.out.println("A new Tree of root "+node.person.getPerson_id()+" is created");
    						traverseTree(node);

    					}
    					if(n.getParent() != null)
    					{
    						
						//We create trees for all the sisters
    					for(Node node : n.parent.children)
    					{
    						Tree tree = new Tree(node);
    						trees.add(tree);
    						if(node.person != null)
    						System.out.println("A new Tree of root "+node.person.getPerson_id()+" is created");
    						traverseTree(node);

    					}   
    					n.parent = null;
    					n = null;


    					}
    					}
    					}
    					else
    					{
    						deleteNode(person, n,trees);
    					}
    			}
    		}

    }
    public Node getRoot() {
  		return root;
  	}

  	public void setRoot(Node root) {
  		this.root = root;
  	}

  

	public static void main(String args[])
    {	
		
		List<Tree> trees =new ArrayList<Tree>();
		List<Person> persons = new ArrayList<Person>();
		Person person = new Person(4, 1582161158, -1,"France",10);
		persons.add(person);
		Person person1 = new Person(5, 1583091884, -1, "France", 10);
		persons.add(person1);
		Person person2 = new Person(9, 1585699579, 4, "France", 10);
		persons.add(person2);
		Person person3 = new Person(13, 1587417223, 4, "France", 10);
		persons.add(person3);
		Person person4 = new Person(14, 1587769422, 5, "France", 10);
		persons.add(person4);
		for(Person p: persons) {
		if(p.getContaminated_by() == -1)
		{
			Node rootNode = new Node(p);
	    	Tree tree = new Tree(rootNode);
	    	trees.add(tree);
		}
		}
	    Node personNode = new Node(person);

		
		Tree tree = new Tree(personNode);
	    Node person1Node = new Node(person1);
	    Node person2Node = new Node(person2);
	    Node person3Node = new Node(person3);
	    Node person4Node = new Node(person4);
	    
	    //tree.getRoot().addChild(person1Node);
	  //person1Node.addChild(person2Node);
	  //tree.root.addChild(person3Node);
	  //person1Node.addChild(person4Node);
	  // tree.deleteNode(person1,personNode,trees);
	  //tree.traverseTree(tree.root);
	    //System.out.println("the parent of 4 is "+person4Node.getParent().person.getPerson_id());
	    for(Tree t: trees)
	    System.out.println("My root is : "+t.getRoot().getPerson().getPerson_id());
    }
}
