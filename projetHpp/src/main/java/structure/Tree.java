package structure;

import beans.Person;

public class Tree {

	Person person = new Person(1, 1587417223, 0, "France", 10);
    public Node root = new Node(person);

    public Tree()
    {
        
        root.parent=null;
    }

    public void traverseTree(Node rootNode)//depth first
    {
        System.out.println(rootNode.person.getPerson_id()+"\n|");
        
        if(rootNode.children.size()!=0)
            for(Node ch : rootNode.children)
                traverseTree(ch);
    }


    public static void main(String args[])
    {
    	Person person1 = new Person(2, 1587417223, 0, "France", 10);
    	Person person2 = new Person(3, 1587417223, 0, "France", 10);
    	Person person3 = new Person(4, 1587417223, 0, "France", 10);
        Tree tree=new Tree();
        Node person1Node = new Node(person1);
        Node person2Node = new Node(person2);
        Node person3Node = new Node(person3);

        tree.root.addChild(person1Node);
        person1Node.addChild(person2Node);
        tree.root.addChild(person3Node);
        tree.traverseTree(tree.root);
    }
}
