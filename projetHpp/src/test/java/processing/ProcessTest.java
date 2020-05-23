package processing;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import beans.Person;
import structure.Node;
import structure.Tree;


public class ProcessTest {


	Person person1,person2,person3,person4,person5,person6,person7 ;
	Node node1,node2,node3,node4,node5,node6,node7;

	Process process ;
	List<Tree> trees;
	
	@Before
	public void setUp() {
		person1=new Person(4, 1586488028, -1,"France",10);  //10-04
		person2=new Person(5, 1587179228, 4,"France",10);   //18-04
		person3=new Person(6, 1588216028, 4,"France",10);   //30-04
		person4=new Person(7, 1588388828, 6,"France",10);   //02-05
		person5=new Person(8, 1589080028, 7,"France",10);   //10-05
		person6=new Person(9, 1589512028, 7,"France",10);   //15-05
		person7=new Person(10, 1589944028,8,"France",10);   //20-05

		
	    node1 =new Node(person1);
		node2=new Node(person2);
		node3=new Node(person3);
	    node4=new Node(person4);
	    node5=new Node(person5);
	    node6=new Node(person6);
	    node7=new Node(person7);
	    
	    process = new Process();
		
	}
	
	@Test
	public void testUpdateScoreTree() {
		
		int date;
		trees =new ArrayList<Tree>(Arrays.asList(new Tree(node1)));
		
		//testing a case that has more than 7 days 
		node1.addChild(node2);
		date =node2.getPerson().getDiagnosed_ts();
		trees=process.updateScoreTree(date,node1,trees,trees.get(0));
		Assert.assertEquals(trees.size(),1);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),4);
		
		
		//testing a case that has more than 14 days
		node1.addChild(node3);
		date=node3.getPerson().getDiagnosed_ts();
		trees=process.updateScoreTree(date,node1,trees,trees.get(0));
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
		          10   
   		 
   		 nodes 6 and 7 should be deleted
   		               		
		*/
		
		trees.remove(0);
		
		
		date=node4.getPerson().getDiagnosed_ts();
		node3.addChild(node4);
		trees=process.updateScoreTree(date,node3,trees,trees.get(0));
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),10);
		
		node3.addChild(node5);
		date=node5.getPerson().getDiagnosed_ts();
		trees=process.updateScoreTree(date,node3,trees,trees.get(0));
		Assert.assertEquals(trees.size(),1);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),4);
		
		node4.addChild(node7);
		date=node7.getPerson().getDiagnosed_ts();
		trees=process.updateScoreTree(date,node3,trees,trees.get(0));
		
		
		
		Assert.assertEquals(trees.size(),2);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),10);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getPerson_id(),10);
		Assert.assertEquals(trees.get(1).getRoot().getPerson().getScore(),4);
		Assert.assertEquals(trees.get(1).getRoot().getPerson().getPerson_id(),8);
		
		
	}
	


	@Test
	public void testGenerateScore() {
		
		trees =new ArrayList<Tree>();
		trees=process.process(person3,trees);
		trees=process.process(person4,trees);
		trees=process.process(person5,trees);
		Assert.assertEquals(process.generateScore(trees.get(0).getRoot()),18);
		
		
	}

	
	@Test
	public void testGenerate() {
		
		trees =new ArrayList<Tree>();
		Map<Person,Integer> expected;
		//the expected map
		expected=new HashMap<Person,Integer>();
		
		trees=process.process(person1,trees);
		expected.put(person1, 10);
		Assert.assertTrue(process.generate(trees,"France").equals(expected));
		
		trees=process.process(person2,trees);
		expected.put(person1, 14);
		Assert.assertTrue(process.generate(trees,"France").equals(expected));
		
		trees=process.process(person3,trees);
		expected.remove(person1);
		expected.put(person2, 4);
		expected.put(person3, 10);
		Assert.assertTrue(process.generate(trees,"France").equals(expected));
		
		trees=process.process(person4,trees);
		expected.put(person2, 4);
		expected.put(person3, 20);
		Assert.assertTrue(process.generate(trees,"France").equals(expected));
		
		trees=process.process(person5,trees);
		expected.remove(person2);
		expected.put(person3, 18);	
		Assert.assertTrue(process.generate(trees,"France").equals(expected));
			 
	}
    
	@Test
	public void testProcess() {
		trees =new ArrayList<Tree>();
		
		/* 
		           4		 
		 
		 */
		
		trees=process.process(person1,trees);		
		Assert.assertEquals(trees.size(),1);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),10);
		
		
		/* 
                    4
                   /
                  5

        */
		
		trees=process.process(person2,trees);
		Assert.assertEquals(trees.size(),1);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),4);
		Assert.assertEquals(trees.get(0).getRoot().getChildren().get(0).getPerson().getScore(),10);
		
		/*            
                     
                  5        6 
		 */
		
		trees=process.process(person3,trees);
		Assert.assertEquals(trees.size(),2);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),4);
		Assert.assertEquals(trees.get(1).getRoot().getPerson().getScore(),10);
		
		
		/*
		 
		          5         6
		                     \
		                      7
		 */
		
		trees=process.process(person4, trees);
		Assert.assertEquals(trees.size(),2);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),4);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getPerson_id(),5);
		
		Assert.assertEquals(trees.get(1).getRoot().getPerson().getScore(),10);
		Assert.assertEquals(trees.get(1).getRoot().getPerson().getPerson_id(),6);
		Assert.assertEquals(trees.get(1).getRoot().getChildren().get(0).getPerson().getPerson_id(),7);
		
		/*
		                      6
		                       \
		                        7
		                         \
		                          8 
		 */
		trees=process.process(person5, trees);
		Assert.assertEquals(trees.size(),1);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),4);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getPerson_id(),6);
		Assert.assertEquals(trees.get(0).getRoot().getChildren().get(0).getPerson().getPerson_id(),7);
		Assert.assertEquals(trees.get(0).getRoot().getChildren().get(0).getPerson().getScore(),4);
		Assert.assertEquals(trees.get(0).getRoot().getChildren().get(0).getChildren().get(0).getPerson().getPerson_id(),8);
	    Assert.assertEquals(trees.get(0).getRoot().getChildren().get(0).getChildren().get(0).getPerson().getScore(),10);
		
		
		/*
		                          7
		                         / \
		                        9   8
		 
		 
		 */
		trees=process.process(person6, trees);
		Assert.assertEquals(trees.size(),1);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getScore(),4);
		Assert.assertEquals(trees.get(0).getRoot().getPerson().getPerson_id(),7);
		Assert.assertEquals(trees.get(0).getRoot().getChildren().get(0).getPerson().getPerson_id(),8);
		Assert.assertEquals(trees.get(0).getRoot().getChildren().get(0).getPerson().getScore(),10);
		Assert.assertEquals(trees.get(0).getRoot().getChildren().get(1).getPerson().getPerson_id(),9);
		Assert.assertEquals(trees.get(0).getRoot().getChildren().get(1).getPerson().getScore(),10);
						 
	}

	
	@Test
	public void testDeleteNode() {
		trees=new ArrayList<Tree>();
		node1.addChild(node2);
		node1.addChild(node3);
		trees.add(new Tree(node1));
		process.deleteNode(node3.getPerson().getDiagnosed_ts(),node1, trees, trees.get(0)); 
		Assert.assertEquals(trees.size(),2);
		Assert.assertEquals(trees.get(0).getRoot(),node2);
		Assert.assertEquals(trees.get(1).getRoot(),node3);
	}
 
	@After
	public void tearDown()  {
		person1=null;
		person2=null;
		person3=null;
		person4=null;
		person5=null;
		person6=null;
		person7=null;
	    node1=null;
		node2=null;
		node3=null;
	    node4=null;
	    node5=null;
	    node6=null;
	    node7=null;
	    trees=null;
	    
	}
	

}

