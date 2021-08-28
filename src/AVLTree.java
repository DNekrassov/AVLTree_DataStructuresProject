import java.util.Stack;

/**
 * AVLTree
 * An implementation of a AVL Tree with
 * distinct integer keys and info
 */

public class AVLTree {

    private IAVLNode root; //References AVLTree root
    private IAVLNode max;
    private IAVLNode min;

    /**
    AVLTree Constructor - initializes tree to be a virtual node
    Complexity: O(1)
     */
    public AVLTree(){
        this.root = new AVLNode();
        this.min = this.root;
        this.max = this.root;
    }


    /**
    Used in making new trees in Split()
    Complexity: O(1)
     */
    private AVLTree(IAVLNode root){
        this.root = root;
    }

    /**
    Used to add a node to an empty tree in Insert()
    Complexity: O(1)
     */
    private void initializeTree(IAVLNode root) {
        this.root = root;
        this.min = this.root;
        this.max = this.root;
    }

    /**
     * public boolean empty()
     * returns true if and only if the tree is empty
     * Complexity: O(1)
     */
    public boolean empty() {
        return this.root.getLeft() == null && this.root.getRight() == null; //true only if root is a virtual node
    }

    /**
     * public String search(int k)
     * returns the info of an item with key k if it exists in the tree
     * otherwise, returns null
     * Complexity: O(log n) - due to find(k), rest of operations are O(1).
     */
    public String search(int k) {
        IAVLNode found = find(k);
        if (found == null){
            return null;
        }
        return found.getValue();
    }

    /**
     * public int insert(int k, String i)
     * inserts an item with key k and info i to the AVL tree.
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were necessary.
     * promotion/rotation - counted as one rebalance operation, double-rotation is counted as 2.
     * returns -1 if an item with key k already exists in the tree.
     *
     * Complexity: O(log n) - see detailed explanation in PDF and shortened in code blocks.
     */
    public int insert(int k, String i) {

        //first insertion to the tree - O(1) operations
        if (this.empty()) {
            initializeTree(new AVLNode(k, i));
            return 0;
        }

        //finding parent of new node k - takes O(log n) operations - explanation in findParent()
        IAVLNode parent = findParent(k);
        if (parent == null){  //k is in tree
            return -1;
        }
        int balanceProcesses = 0;

        //creating new node and setting its relations with the parent - O(1) operations
        IAVLNode node = new AVLNode(k, i);
        node.setParent(parent);
        if (k < parent.getKey()){
            parent.setLeft(node);
        }
        else{
            parent.setRight(node);
        }

        //balancing the tree - takes O(log n) operations - explanation in BalanceTreeAfterInsert()
        //note: balancing is needed only if parent was a leaf, if parent was unary and became binary - all good.
        if (parent.getHeight() == 0){
            balanceProcesses = balanceTreeAfterInsert(node, balanceProcesses);
        }

        //fixing root, size, min, max after insertion - each takes O(log n) operations - detailed explanation in each.
        setRoot();
        updateSizeUntilTheRoot(node);
        setMin();
        setMax();

        return balanceProcesses;
    }

    /**
     *
     * @param node - node that is currently looked upon in the balancing process
     * @param balanceProcesses - counter used in recursive calls to count amount of balance operations used in the process
     *                         initialized to 0 in first call.
     * @return returns the amount of balance operations used in the insertion process.
     * Complexity: O(log n) -
     */
    private int balanceTreeAfterInsert(IAVLNode node, int balanceProcesses){
        // in the case we reach root
        if (node.getParent() == null){
            return 0;
        }
        // 3 cases as per class slide
        if (((AVLNode) node.getParent()).getRankDifL() == 0){
            if(((AVLNode) node.getParent()).getRankDifR() == 1){ //node parent is a 1,1 node - case 1 (Promote)
                ((AVLNode) node.getParent()).promote();
                return balanceTreeAfterInsert(node.getParent(), balanceProcesses+1);
            }
            else{ //node parent is a 1,2 node
                if(((AVLNode) node).getRankDifR() == 2){ //node is a 1,2 node  - case 2 (Rotate)
                    ((AVLNode) node.getParent()).demote();
                    rotateRight(node.getParent());
                    return balanceProcesses + 2;
                }
                else{ //node is a 2,1 node - case 3 (Double Rotate)
                    ((AVLNode) node.getParent()).demote();
                    ((AVLNode) node).demote();
                    ((AVLNode) node.getRight()).promote();
                    doubleRotateRight(node.getParent());
                    return balanceProcesses + 5;
                }
            }
        }
        //mirrored 3 cases
        else if (((AVLNode) node.getParent()).getRankDifR() == 0){
            if(((AVLNode) node.getParent()).getRankDifL() == 1){ //node parent is a 1,1 node - case 1 (Promote)
                ((AVLNode) node.getParent()).promote();
                return balanceTreeAfterInsert(node.getParent(), balanceProcesses+1);
            }
            else{ //node parent is a 2,1 node
                if(((AVLNode) node).getRankDifL() == 2){ //node is a 2,1 node - case 2 (Rotate)
                    ((AVLNode) node.getParent()).demote();
                    rotateLeft(node.getParent());
                    return balanceProcesses + 2;
                }
                else{ //node is a 1,2 node - case 3 (Double Rotate)
                    ((AVLNode) node.getParent()).demote();
                    ((AVLNode) node).demote();
                    ((AVLNode) node.getLeft()).promote();
                    doubleRotateLeft(node.getParent());
                    return balanceProcesses + 5;
                }
            }
        }
        return balanceProcesses;
    }


    /**
     * public int delete(int k)
     * deletes an item with key k from the binary tree, if it is there;
     * the tree must remain valid (keep its invariants).
     * returns the number of rebalancing operations, or 0 if no rebalancing operations were needed.
     * demotion/rotation - counted as one rebalance operation, double-rotation is counted as 2.
     * returns -1 if an item with key k was not found in the tree.
     *
     * The recursive method that re balance the tree is 'BalanceTreeAfterDelete' - the other methods called are in Time complexity of O(1).
     * Time complexity - O(log n)
     */
    public int delete(int k) {
        int[] arr = new int[1]; //keeps the number of operations
        IAVLNode x = find(k); 	//the node to be deleted
        if (x == null)			//No node with key k
            return -1;
        int num = ((AVLNode) x).nodeType(); // Type of a node
        IAVLNode success;
        if (num == 2) { //binary
            success = successor(x);
            exchange(x, success);  //replace x with success. success is still there
            num = ((AVLNode) success).nodeType(); //num is now -1/0/1
            deleteNode(success, num, arr);  // Delete the successor
        }
        else {							// Unary || Leaf
            deleteNode(x, num, arr);  
        }
        setRoot();
        setMax();
        setMin();

        return arr[0];
    }
    
    /**
     * The method gets: node to be deleted, type 'k' (UnaryR.L/Leaf), array with number of re balance operations which is empty
     * Time complexity - O(log n)
     */
    private void deleteNode(IAVLNode node, int k, int[] arr) {
        if (k == 0)					 // leaf
            deleteLeaf(node, arr); 
        if((k == 1) || (k == -1))
        	deleteUnary(node, arr);
    }

    
    /**
     * Handles the deletion of a leaf node
     * Time complexity - O(log n) - due to balanceTreeAfterDelete()
     */
    private void deleteLeaf(IAVLNode node, int[] arr) {
        IAVLNode x = node.getParent();

        if (x == null)  				//node is root
            this.root = new AVLNode();
        else {
            if (x.getLeft() == node) 	  //node is a left son
                x.setLeft(new AVLNode()); //replace it with leaf
            else						  //node is a right son
                x.setRight(new AVLNode()); 
      
		    balanceTreeAfterDelete(x, arr);		//fix the tree after the act of Deletion
		   
        }
    }
    
    /**
     * Handles the deletion of a unary node
     * Time complexity - O(log n) - due to balanceTreeAfterDelete()
     */
    private void deleteUnary(IAVLNode node, int[] arr) {
    	IAVLNode y; // node's son
    	if (node.getRight().getParent() == node)   //node has right son
    		y = node.getRight();
        else 									   //node has left son
        	y = node.getLeft();	
        y.setParent(node.getParent());
        if (node.getParent() != null) {
            if (node.getParent().getRight() == node) //node is right son
                node.getParent().setRight(y);
            else									//node is left son
                node.getParent().setLeft(y);
        }
        else {										//node is the root
            this.root = y;
        }
        balanceTreeAfterDelete(y.getParent(), arr);            //fix the tree after the act of Deletion
    }
    
    

    /**
     * update all the sizes until the root
     * The method used after re balancing completed
     * W.C. Time Complexity - O(log n)
     */
    private void updateSizeUntilTheRoot(IAVLNode node){
    	if(node == null)
    		return;
    	((AVLNode) node).setSize();
    	updateSizeUntilTheRoot(node.getParent()); // Recursive call
    }


    /**
     * The method re balance the tree recursively after 'delete' operation
     * 'x' is the node to be checked in the process, 'arr' keeps the number of operation made
     * Time complexity - O(log n)
     */
    private void balanceTreeAfterDelete(IAVLNode x, int[] arr) {
        if (x == null) // reached the root
            return;
        int difL, difR;
        IAVLNode tmp;
        
        difL = ((AVLNode) x).getRankDifL();
        difR = ((AVLNode) x).getRankDifR();
        if ((difL == 1 && difR == 2) || (difL == 2 && difR == 1)) { //problem solved
        	updateSizeUntilTheRoot(x);
        	return;
        }
        if ((difL == 2) && (difR == 2)) {
            ((AVLNode) x).demote();
            arr[0]++;
            ((AVLNode) x).setSize();
            balanceTreeAfterDelete(x.getParent(), arr); //problem solved or moved upper
        }
        // difL & difR : equal to (3 & 1) | (1 & 3) 
        // look on the son of the "deeper" which is the right one. later we will make the same logic for the left.
        else {
        
        if ((difL == 3) && (difR == 1)) {
            tmp = x.getRight();
            difL = ((AVLNode) tmp).getRankDifL();
            difR = ((AVLNode) tmp).getRankDifR();

            if (((difL == 1) && (difR == 1)) || ((difL == 2) && (difR == 1))) { //rotate left once
                rotateLeft(x); // tmp is the current root of the subTree
                arr[0] += 3;   // 1 rotate, 2 rank updates
                if ((difL == 1)) { //problem solved
                	((AVLNode) x).demote();
                    ((AVLNode) tmp).promote();
                    updateSizeUntilTheRoot(x);
                } else { 	// (difL == 2) && (difR == 1) problem solved or moved upper
                	((AVLNode) x).doubleDemote();
                    balanceTreeAfterDelete(tmp.getParent(), arr);  //tmp is the current root of the subTree.
                }
            } else { //((difL == 1) && (difR == 2))  //double rotate left, problem solved or moved upper
                doubleRotateLeft(x);
                ((AVLNode) x).doubleDemote(); 
                ((AVLNode) tmp).demote();
                ((AVLNode) tmp.getParent()).promote(); 
                arr[0] += 6;
                balanceTreeAfterDelete(tmp.getParent().getParent(), arr);
            }
        }
        else {//difR == 3 && difL == 1
            tmp = x.getLeft();
            difL = ((AVLNode) tmp).getRankDifL();
            difR = ((AVLNode) tmp).getRankDifR();
         
            if (((difL == 1) && (difR == 1)) || ((difL == 1) && (difR == 2))) { //rotate right once
                rotateRight(x);
                arr[0] += 3;
                if ((difR == 1)) { //problem solved
                	((AVLNode) x).demote(); 
                	((AVLNode) tmp).promote();
                    updateSizeUntilTheRoot(x);
                } else {
                	((AVLNode) x).doubleDemote();
                    balanceTreeAfterDelete(tmp.getParent(), arr);  //tmp is now the root of the mini subTree.
                }
            } else { // (difL == 2) && (difR == 1)
                doubleRotateRight(x);
                arr[0] += 2;
                ((AVLNode) x).doubleDemote();
                ((AVLNode) tmp).demote();
                ((AVLNode) tmp.getParent()).promote();
                arr[0] += 6;
                balanceTreeAfterDelete(tmp.getParent().getParent(), arr);
            }
        }
      }
    }
    
   /**
    * rotate left once the subTree of x 
    * @post the subtree is balanced
    * Time Complexity - O(1)
    */
    private void rotateLeft(IAVLNode x) {
        IAVLNode y = x.getRight();
        y.setParent(x.getParent()); //y point on x's parent
        if (y.getParent() != null) { //update the parent to point to his new son
            if (x.getParent().getLeft() == x)
                y.getParent().setLeft(y);
            else //x was a right son
                y.getParent().setRight(y);
        }
        x.setRight(y.getLeft());
        y.getLeft().setParent(x);
        y.setLeft(x);
        x.setParent(y);
  
        ((AVLNode)x).setSize();
        ((AVLNode)y).setSize();

    }

    /**
     * rotate left once the subTree of x 
     * @post the subtree is balanced
     * Time Complexity - O(1)

     */
    private void rotateRight(IAVLNode x) {
        IAVLNode y = x.getLeft();
        y.setParent(x.getParent());
        if (y.getParent() != null) {
            if (x.getParent().getLeft() == x)
                y.getParent().setLeft(y);
            else
                y.getParent().setRight(y);
        }
        x.setLeft(y.getRight());
        y.getRight().setParent(x);
        y.setRight(x);
        x.setParent(y);
        
        ((AVLNode)x).setSize();
        ((AVLNode)y).setSize();
    }

    /**
     * The method Double rotates- right & left
     * @post the subtree is balanced
     * Time Complexity - O(1)
     */
    private void doubleRotateLeft(IAVLNode x) {
        IAVLNode y = x.getRight();
        rotateRight(y);
        rotateLeft(x);
    }
    
    /**
     * The method Double rotates- left & right  
     * @post the subtree is balanced
     * Time Complexity - O(1)
     */
    private void doubleRotateRight(IAVLNode x) {
        IAVLNode y = x.getLeft();
        rotateLeft(y);
        rotateRight(x);
    }


    /**
     * Concealing function for calls of find() from the root - implementation below
     * Used to find node with key k in Delete(), Split(), Search() methods
     * Complexity: O(log n) - see explanation below
     */
    private IAVLNode find(int k){
        return find(this.root, k);
    }

    /**
     * @param node - node used in recursive calls as per binary-tree searches
     * @param k - the key according to which the search is conducted
     * @return the node in which k is it's key, if not found - returns null
     * Complexity: O(log n) - same as BST search, as taught in class
     */
    private IAVLNode find(IAVLNode node, int k) {

        if (node.getKey() == -1) //we reached a virtual node, therefore k wasn't found
            return null;
        if (node.getKey() == k) //k was found - returns node
            return node;
        if (node.getKey() > k) // k is smaller than the current key - search in left-subtree
            return find(node.getLeft(), k);
        return find(node.getRight(), k); // k is larger than the current key - search in right-subtree
    }

    /**
     * Concealing function for calls of find_parent() from the root - implementation below
     * Used in insert() to find the parent of a new node with key k
     * @param k - the key of the new node of which we find its parent
     * @return parent of node with key k
     * Complexity: O(log n) - see explanation below
     */
    private IAVLNode findParent(int k){
        if (this.root.getKey() == k) {return null;}  //k is in tree
        if (this.root.getKey() > k){ //k should be in left subtree
            return findParent(this.root, this.root.getLeft(), k);
        }
        return findParent(this.root, this.root.getRight(), k);//k should be in right subtree
    }

    /**
     * Recursive BST search to find the parent of a new node that will have key k
     * Complexity: O(log n) - same as BST search, as taught in class
     */
    private IAVLNode findParent(IAVLNode parent, IAVLNode node, int k){
        //implement search in AVL tree until we reach a virtual node
        while(node.getKey() != -1){
            int key = node.getKey();
            if(key == k){ return null;} //k is in tree
            if(key > k) { //k should be in left subtree
                parent = node;
                node = node.getLeft();
            }
            else{ //k should be in right subtree
                parent = node;
                node = node.getRight();
            }
        }
        return parent;
    }

    /**
     *
     * @param x - node which we need to find its successor
     * @return successor of x, or a virtual node if x has no successor (x is a max-key node)
     * Complexity: O(log n) - as taught in class
     */
    private IAVLNode successor(IAVLNode x) {
        //case 1 - x has a right subtree, therefore successor should be the minimum node there
        if (x.getRight().getKey() != -1){
            return minByNode(x.getRight());
        }
        //case 2 - x doesn't have a right subtree - therefore the successor should be the first node where we "go right"
        //when we go up the tree from x
        while(x.getParent() != null){
            if(x.getParent().getLeft() == x){
                return x.getParent();
            }
            x = x.getParent();
        }
        return new AVLNode();
    }

    /**
     * @post 'x' holds the (key & value) of 'success'
     * Time Complexity - O(1) 
     */
    private void exchange(IAVLNode x, IAVLNode success) {
        ((AVLNode) (x)).setKey(success.getKey());		//Update key
        ((AVLNode) (x)).setValue(success.getValue());	//Update value
        
    }

    /**
     * public String min()
     * Returns the info of the item with the smallest key in the tree,
     * or null if the tree is empty
     * Complexity: O(1) - since we keep record of the minimum node
     */
    public String min() {
        return this.min.getValue();
    }

    /**
     * Sets the minimum node of the tree - as part of insert/delete/split.
     * Complexity: O(log n) - since we need to traverse the tree to its leftmost node - due to minByNode().
     */
    private void setMin(){
    	if(this.empty())
    		this.min = this.root;
    	else
    		this.min = minByNode(this.root);
    }

    /**
     * Finds the leftmost node (unary/leaf) from a given node
     * Complexity: O(log n) - since we need to traverse the tree to its leftmost node
     */
    private IAVLNode minByNode(IAVLNode node){
        IAVLNode tmpNode = node;
        while (tmpNode.getLeft().getKey() != -1) //go to leftmost non-virtual node from node
            tmpNode = tmpNode.getLeft();
        return tmpNode;
    }


    /**
     * public String max()
     * Returns the info of the item with the largest key in the tree,
     * or null if the tree is empty
     * Time complexity - O(1) - since we keep record of the maximum node
     */
    public String max() {
        return this.max.getValue();
    }

    /**
     * Sets the maximum node of the tree - as part of insert/delete/split.
     * Complexity: O(log n) - since we need to traverse the tree to its rightmost node
     */
    private void setMax(){
        IAVLNode node = this.root;
        if (this.empty()){
        	this.max = this.root;
        }
        else {
        	while (node.getRight().getKey() != -1) //go to rightmost non-virtual node from node
        		node = node.getRight();
        	this.max = node;
        }
    }

    /**
     * Used inorder to simplify keysToArray() and infoToArray()
     * implementation similar to HW2 Q3b answer - using a stack to perform in-order, non-recursive write to array.
     * @return an array of nodes in the tree, in-order according to keys
     * Complexity: O(n) - as we traverse any given node a maximum of 3 times.
     */
    private IAVLNode[] nodesToArray(){
        IAVLNode[] arr = new IAVLNode[size()]; //create array in the size of the tree
        int pos = 0;
        Stack<IAVLNode> arrStack = new Stack<>();
        IAVLNode currentNode = this.root;
        while (true){
            if (currentNode.getKey() != -1){
                arrStack.push(currentNode);
                currentNode = currentNode.getLeft();
            }
            else if (!arrStack.empty()){
                currentNode = arrStack.pop();
                arr[pos] = currentNode;
                pos++;
                currentNode = currentNode.getRight();
            }
            else{
                break;
            }
        }
        return arr;
    }

    /**
     * public int[] keysToArray()
     * Returns a sorted array which contains all keys in the tree,
     * or an empty array if the tree is empty.
     * Complexity: O(n) - due to nodesToArray() and due to the loop going over it
     */
    public int[] keysToArray() {
        if (empty()){
            return new int[]{};
        }
        int[] arr = new int[size()]; //create array in the size of the tree
        IAVLNode[] nodeArr = nodesToArray();
        for( int i = 0 ; i < arr.length ; i++){
            arr[i] = nodeArr[i].getKey();
        }
        return arr;
    }

    /**
     * public String[] infoToArray()
     * Returns an array which contains all info in the tree,
     * sorted by their respective keys,
     * or an empty array if the tree is empty.
     * Complexity: O(n) - due to nodesToArray() and due to the loop going over it
     */
    public String[] infoToArray() {
        if (empty()){
            return new String[]{};
        }
        String[] arr = new String[size()]; //create array in the size of the tree
        IAVLNode[] nodeArr = nodesToArray();
        for( int i = 0 ; i < arr.length ; i++){
            arr[i] = nodeArr[i].getValue();
        }
        return arr;
    }

    /**
     * public int size()
     * Returns the number of nodes in the tree.
     * Complexity: O(1) - since we update the size of nodes in insertion/deletion/split/join.
     */
    public int size() {
        return ((AVLNode) this.root).getSize(); // size of tree = size of root
    }

    /**
     * public int getRoot()
     * Returns the root AVL node, or a virtual node if the tree is empty
     * Complexity: O(1)
     */
    public IAVLNode getRoot() {
        return this.root;
    }

    /**
     * Updates the root of the tree after balancing operations - goes from the previous root to the current root
     * Complexity: O(1) - since the root node can move a set amount of nodes due to balancing
     */
    private void setRoot(){
        IAVLNode tmp = this.root;
        while(tmp.getParent() != null){
            tmp = tmp.getParent();
        }
        this.root = tmp;
    }

    /**
     * public string split(int x)
     * splits the tree into 2 trees according to the key x.
     * Returns an array [t1, t2] with two AVL trees. keys(t1) < x < keys(t2).
     * precondition: search(x) != null (i.e. you can also assume that the tree is not empty)
     * postcondition: none
     * Complexity: O(log n) - explanation in PDF
     */
    public AVLTree[] split(int x) {
        IAVLNode node = find(x);

        //create new AVLTrees from the subtrees of x
        AVLTree t1 = new AVLTree(node.getLeft());
        AVLTree t2 = new AVLTree(node.getRight());

        //reset child-parent relations of x and it's subtrees
        node.getLeft().setParent(null);
        node.getRight().setParent(null);
        node.setLeft(new AVLNode());
        node.setRight(new AVLNode());

        while (node.getParent() != null){
            //join current subtree with left split-tree
            if(node.getParent().getRight() == node){
                node = node.getParent();
                node.getRight().setParent(null);
                node.setRight(new AVLNode());
                IAVLNode tmp = node.getLeft();
                IAVLNode tmp_node = new AVLNode(node.getKey(), node.getValue());
                node.getLeft().setParent(null);
                node.setLeft(new AVLNode());
                t1.join(tmp_node, new AVLTree(tmp));
            }
            //join current subtree with right split-tree
            else{
                node = node.getParent();
                node.getLeft().setParent(null);
                node.setLeft(new AVLNode());
                IAVLNode tmp = node.getRight();
                IAVLNode tmp_node = new AVLNode(node.getKey(), node.getValue());
                node.getRight().setParent(null);
                node.setRight(new AVLNode());
                t2.join(tmp_node, new AVLTree(tmp));
            }
        }
        //set min/max nodes of new trees
        t1.setMin();
        t1.setMax();
        t2.setMin();
        t2.setMax();

        return new AVLTree[]{t1, t2};
    }
    
    /**
     * public join(IAVLNode x, AVLTree t)
     * joins t and x with the tree.
     * Returns the complexity of the operation (|tree.rank - t.rank| + 1).
     * precondition: keys(x,t) < keys() or keys(x,t) > keys(). t/tree might be empty (rank = -1).
     * postcondition: none
     * 
     * Time complexity - O(|tree.rank - t.rank| + 1)
     */
    public int join(IAVLNode x, AVLTree t) {
        if ((this.empty()) || (t.empty())) {
            return joinWithEmpty(x, t);
        }
        int complex = Math.abs(this.root.getHeight() - t.root.getHeight()) + 1;

        if (t.root.getKey() > this.root.getKey()) {  // t.keys > x.key > this.keys
       	this.max = t.max; // Update MAX
            if (t.root.getHeight() > this.root.getHeight()) { //t is deeper
                joinOnLeft(this, x, t); //change t
                this.root = t.root; 
            } else if (t.root.getHeight() < this.root.getHeight()) { //this is deeper
                joinOnRight(this, x, t); //change this
            } else { //t.root.getHeight() == this.root.getHeight())
                joinInPlace(this, x, t);
                this.root = x; // x is now the root of the tree
            }
        } else { // t.keys < x.key <  thi.keys()
        	this.min = t.min; // Update MIN
            if (this.root.getHeight() > t.root.getHeight()) {
                joinOnLeft(t, x, this); //change this
            } else if (this.root.getHeight() < t.root.getHeight()) {
                joinOnRight(t, x, this);
                this.root = t.root;
            } else //(t.root.getHeight() = this.root.getHeight()) {
            {
                joinInPlace(t, x, this);
                this.root = x;
            }
        }
        ((AVLNode)x).setSize(); //in the method below there are the updates for the size of the rest of the tree
        //special case for join which is not included in insert
       
        specialCaseForJoin(x); //if we are in special case, handle it
        balanceTreeAfterInsert(x, 0); //fix as it was an insert. problem might occur in x's parent, or not at all.
        setRoot();
        updateSizeUntilTheRoot(x);
        return complex;
    }


   /**
    * in join, if there is a special case (extra to insert)- handle it.
    * Time Complexity - O(1)
    */
    private void specialCaseForJoin(IAVLNode x) {
    	if(x.getParent() == null) //x is the root
    		return;
    	int difL1, difR1, difL2, difR2;  	// ranks differences of x, and his parent
    	difL1 = ((AVLNode) x).getRankDifL();              
        difR1 = ((AVLNode) x).getRankDifR();
        difL2 = ((AVLNode) x.getParent()).getRankDifL();
        difR2 = ((AVLNode) x.getParent()).getRankDifR();
    	
        if(x.getParent().getLeft() == x) { //x is a left child
            if((difL1 == difR1) && (difL1==1) && (difL2 == 0) && (difR2 == 2)) { 
            	rotateRight(x.getParent());
            	x.setHeight(x.getHeight() + 1);
            	((AVLNode) x.getRight()).setSize();
            	((AVLNode) x).setSize();
            }
    	}
    	else if((difL1 == difR1) && (difL1==1) && (difL2 == 2) && (difR2 == 0)) { // mirrored case
    		rotateLeft(x.getParent());
        	x.setHeight(x.getHeight() + 1);
        	((AVLNode) x.getLeft()).setSize();
        	((AVLNode) x).setSize();
    	}
    }
    

    /**
     * @pre t1.keys() < x < t2. keys()
     * @pre t2.height > t1. height
     * Time Complexity - O(t2.rank - t1.rank + 1)
     */
    private static void joinOnLeft(AVLTree t1, IAVLNode x, AVLTree t2) {
        IAVLNode tmp = t2.getRoot();
        while (tmp.getLeft().getHeight() > t1.getRoot().getHeight()) { //Go down on the left side of t2
            tmp = tmp.getLeft();
        }
        x.setRight(tmp.getLeft());
        x.getRight().setParent(x);
        tmp.setLeft(x);
        x.setParent(tmp);
        x.setLeft(t1.root);
        t1.getRoot().setParent(x);
        x.setHeight(Math.max(x.getLeft().getHeight(), x.getRight().getHeight()) + 1);
    }

    /**
     * @pre t1.keys() < x < t2. keys()
     * @pre t1.height > t2.height
     * Time Complexity - O(t1.rank - t2.rank + 1)
     */
    private static void joinOnRight(AVLTree t1, IAVLNode x, AVLTree t2) {
        IAVLNode tmp = t1.getRoot();
        while (tmp.getRight().getHeight() > t2.getRoot().getHeight()) { //Go down on the right side of t1
            tmp = tmp.getRight();
        }
        x.setLeft(tmp.getRight());
        x.getLeft().setParent(x);
        tmp.setRight(x);
        x.setParent(tmp);
        x.setRight(t2.root);
        t2.getRoot().setParent(x);
        x.setHeight(Math.max(x.getLeft().getHeight(), x.getRight().getHeight()) + 1);
    }

    /**
     * @pre t1.keys < x < t2.keys
     * @pre t1.root.getHeight == t2.root.getHeight()
     * Time Complexity - O(1)
     */
    public static void joinInPlace(AVLTree t1, IAVLNode x, AVLTree t2) { 
        x.setLeft(t1.root);
        x.setRight(t2.root);
        t1.root.setParent(x);
        t2.root.setParent(x);
        x.setHeight(t1.root.getHeight() + 1);
    }

    /**
     * @pre (this || t) are empty
     * Time Complexity - O( | this.rank - t.rank | + 1 )
     */
    private int joinWithEmpty(IAVLNode x, AVLTree t) {
    
    	// UPDATE MIN/MAX
    	if (this.empty() && t.empty()) {
    		this.max = x;
    		this.min = x;
    	}
    	else if (this.empty()) { //t is not empty
    		if(x.getKey() < t.getRoot().getKey()) { 
    			this.max = t.max;
    			this.min = x;
    		}
    		else { // x is bigger than t keys
    			this.max = x;
    			this.min = t.min;
    		}
    	}
    	
    	int complex = Math.abs( this.root.getHeight() - t.getRoot().getHeight() ) + 1;
    	
        if (this.empty()) {
            this.root = t.getRoot();   
        }
        
        this.insert(x.getKey(), x.getValue()); //insert should update 'size' field
        return complex;
    }
    
    
    /**
     * public interface IAVLNode
     * ! Do not delete or modify this - otherwise all tests will fail !
     */
    public interface IAVLNode {
        public int getKey(); //returns node's key (for virtuval node return -1)

        public String getValue(); //returns node's value [info] (for virtuval node return null)

        public void setLeft(IAVLNode node); //sets left child

        public IAVLNode getLeft(); //returns left child (if there is no left child return null)

        public void setRight(IAVLNode node); //sets right child

        public IAVLNode getRight(); //returns right child (if there is no right child return null)

        public void setParent(IAVLNode node); //sets parent

        public IAVLNode getParent(); //returns the parent (if there is no parent return null)

        public boolean isRealNode(); // Returns True if this is a non-virtual AVL node

        public void setHeight(int height); // sets the height of the node

        public int getHeight(); // Returns the height of the node (-1 for virtual nodes)
    }

    /**
     * public class AVLNode
     * If you wish to implement classes other than AVLTree
     * (for example AVLNode), do it in this file, not in
     * another file.
     * This class can and must be modified.
     * (It must implement IAVLNode)
     * 
     * All the methods in the class 'AVLNode' have time complexity of O(1)
     */
    public class AVLNode implements IAVLNode {

        private String value;
        private int key, rank, size;
        private IAVLNode left, right, parent;

        /**
         * constructor of non-virtual node, all the method in this class are O(1) time complexity
         */
        public AVLNode(int key, String value) { 
            this.key = key;
            this.value = value;
            this.rank = 0;
            this.size = 1;
            this.left = new AVLNode();
            this.right = new AVLNode();
            this.parent = null;
        }

        /**
         * Constructor of an external leaf
         */
        public AVLNode() { 
            this.key = -1;
            this.rank = -1;
            this.size = 0;
        }

        public int getKey() {
            return this.key;
        }

        public String getValue() {
            return this.value;
        }

        public int getSize() {
            return this.size;
        }
     
        /**
         * @pre 'this' is a real node
         * @pre node != null  
         */
        public void setLeft(IAVLNode node) {
            this.left = node;
        }

        public IAVLNode getLeft() {
            return this.left;
        }

        /**
         * @pre 'this' is a real node
         * @pre node != null  
         */
        public void setRight(IAVLNode node) {
            this.right = node;
        }

        public IAVLNode getRight() {
            return this.right;
        }
       
        /**
         *   @pre if (node != null) --> node.getKey != (-1)
         */
        public void setParent(IAVLNode node) {
            this.parent = node;
        }

        public IAVLNode getParent() {
            return this.parent;
        }

        /**
         *@pre this != null
         *@ret true if key != (-1)
         */
        public boolean isRealNode() {
            if (this.key == -1) 	//external leaf
                return false;
            return true;
        }
        
        /**
         *@pre height >= -1 
         */
        public void setHeight(int height) {
            this.rank = height;
        }

        public int getHeight() {
            return this.rank;
        }

        /**
         * @pre this != null & this.key != (-1)
         * @ret = 2 if 'this' is Binary node 
         * @ret = 0 if 'this' is leaf
         * @ret = 1 if 'this' is unary with left son 
         * @ret = (-1) if 'this' is unary with right son 
         */
        private int nodeType() {
            boolean l, r;
            l = this.left.isRealNode();
            r = this.right.isRealNode();
            if (l && r)
                return 2; //two sons
            if (l)
                return 1; //left son
            if (r)
                return -1; //right son
            return 0; //leaf
        }

        private void setKey(int key) {
            this.key = key;
        }

        private void setValue(String value) {
            this.value = value;
        }

        /**
         * @pre this.isRealNode()
         */
        private int getRankDifL() {
            return (this.rank - this.left.getHeight());
        }

        /**
         * @pre this.isRealNode()
         */
        private int getRankDifR() {
            return (this.rank - this.right.getHeight());
        }

        private void demote() { //demote by one
            this.setHeight(this.rank - 1);
        }

        private void promote() { //promote by one
            this.setHeight(this.rank + 1);
        }
        
        private void doubleDemote() { //demote by two
            this.setHeight(this.rank - 2);
        }

        /**
         * @pre this != null
         * @pre this.key != (-1)
         */
        private void setSize() {
            this.size = ((AVLNode)this.left).getSize() + ((AVLNode)this.right).getSize() + 1;
        }
		
    }


}
  

