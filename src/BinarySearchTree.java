public class BinarySearchTree {
    class Node{
        State state;
        Node left, right;

        public Node(State state){
            this.state = state;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;

    public BinarySearchTree(){
        this.root = null;
    }

    public BinarySearchTree(State s){
        this.root = new Node(s);
    }

    // insert a new state down the tree
    // could've overridden compareTo but this works fine
    public void insert(State s){
        this.root = insertHelper(this.root, s);
    }

    private Node insertHelper(Node node, State s){
        if(node == null)
            return new Node(s);
        int deltaF = s.getF() - node.state.getF();
        if(deltaF < 0)
            node.left = insertHelper(node.left, s);
        // this line gets stackoverflowed when trying hard 15-puzzles...
        // maybe not using a binary search tree could fix it?
        else if(deltaF > 0 || !s.getStateId().equals(node.state.getStateId()))
            node.right = insertHelper(node.right, s);
        return node;
    }

    // search (not necessary) (not updated)
    // public void search(State s){
    //     this.root = searchHelper(this.root, s);
    // }

    // private Node searchHelper(Node node, State s){
    //     if(node == null || node.state == s)
    //         return node;
    //     if(s.compareTo(node.state) < 0)
    //         return searchHelper(node.left, s);
    //     return searchHelper(node.right, s);
    // }

    // print all state f values
    public void printFs(){
        System.out.println(printFsHelper(this.root));
    }

    private String printFsHelper(Node node){
        if(node == null)
            return "";
        String output = "";
        if(node.left != null)
            output += printFsHelper(node.left) + ", ";
        output += node.state.getF();
        if(node.right != null)
            output += ", " + printFsHelper(node.right);
        return output; 
    }

    // leftmost leaf: find it, pop it, and return it
    public State leftmostState(boolean remove){
        if(this.root == null)
            return null;
        if(this.root.left == null){
            State s = this.root.state;
            if(remove)
                this.root = this.root.right;
            return s;
        }
        // then root.left is not null
        return leftmostStateHelper(this.root, remove);
    }

    private State leftmostStateHelper(Node node, boolean remove){
        if(node.left.left == null){
            State s = node.left.state;
            if(remove)
                node.left = node.left.right;
            return s;
        }
        // then node.left is not null
        return leftmostStateHelper(node.left, remove);
    }
}
