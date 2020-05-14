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
	    public List<Node> children = null;
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
	        person = nodePerson; 
	    }

	    /**
		 * Method addChild
		 * @param childNode
		 * @return 
		 */
		public void addChild(Node childNode)
	    {
				if(children == null)
					children = new ArrayList<Node>();
	            childNode.parent=this;
	            this.children.add(childNode);
	        
	    }
	    /**
		 * Method addChild
		 * @param 
		 * @return boolean
		 */		
		public boolean isLeaf()
		{
			if (children == null)
				return true;
			return false;
			
		}
		public boolean equals(Node node) {
			if(this.person.equals(node.person))
				return true;
			return false;
		}

		public Node getParent() {
			return parent;
		}

		public void setParent(Node parent) {
			this.parent = parent;
		}

		public List<Node> getChildren() {
			return children;
		}

		public void setChildren(List<Node> children) {
			this.children = children;
		}

		public Person getPerson() {
			return person;
		}

		public void setPerson(Person person) {
			this.person = person;
		}
		
}
