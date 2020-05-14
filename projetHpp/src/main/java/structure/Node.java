package structure;

import java.util.ArrayList;
import java.util.List;

import beans.Person;

/**
 * Node represents a node in the non-binary Tree
 * 
 * @author Team
 * 
 */
public class Node {
	 /**
		 * parent: to keep track of the chain, this is the parent of the current Node
		 */
	    public Node parent;
		 /**
			 * children: list of all the current Node children
			 */
	    public List<Node> children = new ArrayList<Node>();
		 /**
			 * person: the current node's information; class Person
			 */
	    public Person person;//or any other property that the node should contain, like 'info'


		
	    /**
		 * Constructor
		 * @param person
		 * @return Node instance
		 */
	    public Node (Person nodePerson)
	    {
	        person=nodePerson; 
	    }

	    /**
		 * Method addChild
		 * @param childNode
		 * @return 
		 */
		public void addChild(Node childNode)
	    {
	            childNode.parent=this;
	            this.children.add(childNode);
	        
	    }
}
