package processing;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import beans.Person;
import structure.Node;
import structure.Tree;

public class ProcessTest {

	
	Person person1,person2,person3,person4,person5,person6 ;
	Node node1,node2,node3,node4,node5,node6;
	Tree tree;
	Process process ;
	List<Tree> trees;
	
	@Before
	public void setUp() {
		person1=new Person(4, 1586488028, -1,"France",10);  //10-04
		person2=new Person(5, 1587179228, 4,"France",10);   //18-04
		person3=new Person(6, 1588216028, 4,"France",10);   //30-04
		person4=new Person(7, 1588388828, 6,"France",10);   //02-05
		person5=new Person(8, 1589080028, 7,"France",10);   //10-05
		person6=new Person(9, 1589944028, 9,"France",10);   //20-05
		
	    node1 =new Node(person1);
		node2=new Node(person2);
		node3=new Node(person3);
	    node4=new Node(person4);
	    node5=new Node(person5);
	    node6=new Node(person6);
	    tree =new Tree(node1);
	    process = new Process();
		
	}
	@Test
	public void testUpdateScoreTree() {
		
		int date;
		trees =new ArrayList<Tree>(Arrays.asList(tree));
		
		//testing a case that has more than 7 days 
		node1.addChild(node2);
		date =node2.getPerson().getDiagnosed_ts();
		trees=process.updateScoreTree(date,node1,trees,tree);
		Assert.assertEquals(trees.size(),1);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),4);
		
		
		//testing a case that has more than 14 days
		node1.addChild(node3);
		date=node3.getPerson().getDiagnosed_ts();
		trees=process.updateScoreTree(date,node1,trees,tree);
		//the root is deleted and two trees are created
		Assert.assertEquals(trees.size(),2);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),4);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getPerson_id(),5);
		Assert.assertEquals(trees.get(1).getRoot().getPerson().getScore(),10);
		Assert.assertEquals(trees.get(1).getRoot().getPerson().getPerson_id(),6);
		/*
		   testing the case
		       6
		      / \
		     8   7
		          \
		           9   
   		 
   		 nodes 6 and 7 should be deleted
   		               		
		*/
		
		trees.remove(0);
		
		tree=new Tree(node3);
		date=node4.getPerson().getDiagnosed_ts();
		node3.addChild(node4);
		trees=process.updateScoreTree(date,node3,trees,tree);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),10);
		
		node3.addChild(node5);
		date=node5.getPerson().getDiagnosed_ts();
		trees=process.updateScoreTree(date,node3,trees,tree);
		Assert.assertEquals(trees.size(),1);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),4);
		
		node4.addChild(node6);
		date=node6.getPerson().getDiagnosed_ts();
		trees=process.updateScoreTree(date,node3,trees,trees.get(0));
		
		Assert.assertEquals(trees.size(),2);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),10);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getPerson_id(),9);
		Assert.assertEquals(trees.get(1).getRoot().getPerson().getScore(),4);
		Assert.assertEquals(trees.get(1).getRoot().getPerson().getPerson_id(),8);
		
		
	}

	/*
	@Test
	public void testUpdateScoreList() {
		
		
		//node1.addChild(node2);
		System.out.println(node1.getChildren().size());
		//List<Tree> updateScoreList(int date,List<Tree> trees)
	}
	*/

	/*
	@Test
	public void testGenerateScore() {
		fail("Not yet implemented");
	}

	@Test
	public void testGenerate() {
		fail("Not yet implemented");
	}

	@Test
	public void testProcess() {
		fail("Not yet implemented");
	}

	@Test
	public void testOutput() {
		fail("Not yet implemented");
	}
	*/

}

