package structure;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import beans.Person;

public class NodeTest {

	Person person1;
	Person person2;
	Person person3;
	Person person4;
	Node node1;
	Node node2;
	Node node3;
	Node node4;
	
	@Before
	public void setUp() {
		person1=new Person(4, 1582161158, -1,"France",10);
		person2=new Person(5, 1582161159, 4,"France",10);
		person3=new Person(6, 1582161160, 4,"France",10);
		person4=new Person(7, 1582161161, 5,"France",10);
	    node1 =new Node(person1);
		node2=new Node(person2);
		node3=new Node(person3);
	    node4=new Node(person4);
	    
		
	}
	

	@Test
	public void testAddChild() {
		
		node1.addChild(node2);
		node1.addChild(node3);
		node2.addChild(node4);
		Assert.assertEquals(node1.children.size(),2);
		Assert.assertEquals(node2.children.size(),1);
		
	}

	@Test
	public void testIsLeaf() {
		node1.addChild(node2);
		Assert.assertEquals(node1.isLeaf(),false);
		Assert.assertEquals(node2.isLeaf(),true);
	}


	@Test
	public void testGetParent() {
		node1.addChild(node2);
		Assert.assertEquals(node2.parent,node1);
		
	}
	
	@After
	public void tearDown()  {
		person1=null;
		person2=null;
		person3=null;
		person4=null;
	    node1 =null;
		node2=null;
		node3=null;
	    node4=null;
	    
	}


	

}
