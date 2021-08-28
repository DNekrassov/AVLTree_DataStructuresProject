import java.util.*;
import java.util.stream.Collectors;

class AVLTest extends AVLTree {

    private static int[] randomArray(int size, int min, int max) {
        int[] arr = new int[size];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = (int) (Math.random() * (max - min) + min);
        }

        return arr;
    }

    private static TestTree arrayToTree(int[] arr) {
        TestTree tree = new TestTree();
        for (int x : arr) {
            tree.insert(x, Integer.toString(x));
        }

        return tree;
    }

    void testEmpty() {
        AVLTree tree = new TestTree();

        assert (tree.empty()) : "Tree isn't empty";

        tree.insert(1, "1");
        assert (!tree.empty()) : "Tree empty after insert";

        tree.delete(1);
        assert (tree.empty()) : "Tree isn't empty after delete";
        System.out.println("testEmpty Passed!");
    }

    void testSearch() {
        AVLTree tree = new TestTree();

        assert (tree.search(1) == null) : "non-existent key found in search";

        tree.insert(1, "1");
        assert (tree.search(1) == "1") : "couldn't find existing key in search";

        tree.delete(1);
        assert (tree.search(1) == null) : "non-existent key found in search after deleted";
        System.out.println("testSearch Passed!");
    }

    void testInsert() {
        AVLTree tree = new TestTree();

        assert (tree.insert(1, "1") == 0) : "unexpected insert value";
        assert (tree.insert(1, "1") == -1) : "existing key insterted";
        assert (tree.insert(2, "2") == 1) : "unexpected insert value";
        // RR
        assert (tree.insert(3, "3") == 3) : "unexpected insert value";
        System.out.println("testInsert Passed!");
    }

    void testDelete() {
        AVLTree tree = new TestTree();

        assert (tree.delete(1) == -1) : "unexpected delete value";

        tree.insert(1, "1");
        assert (tree.delete(1) == 0) : "unexpected insert value";

        int i = 0;

        tree.insert(2, "2");
        tree.insert(1, "1");
        tree.insert(3, "3");
        tree.insert(4, "4");

        // should be RR rotation
        assert (tree.delete(1) == 3) : "unexpected delete value";

        int[] values = randomArray(10, 0, 100);
        tree = arrayToTree(values);
        List<Integer> valuesShuffled = Arrays.stream(values).boxed().collect(Collectors.toList());
        Collections.shuffle(valuesShuffled);
        for (int x : valuesShuffled) {

            tree.delete(x);

        }
        System.out.println("testDelete Passed!");
    }

    static AVLTree simulateProblematicTree() {
        AVLTree tree = new AVLTree();
        int[] keys = {5, 15, -1, 28, 1, 29, 1, -19, -15, 0, -17, 28, 13, 16, 0, -23, 8, -28, 12, 29, -13, -7, -10, 12, -12, -4, 3, 21, -4, -28, 6, -8, 29, 18, 4, -29, -1, -18, -8, -20, 20, 1, 7, -12, 24, 1, 4, -6, 8, 12, 22, -2, -9, -19, 16, -23, -5, 14, 14, -11, 23, 18, -25, 12, 9, 27, -20, -28, -9, 7, 6, -3, 7, 2, -29, 14, -7, -21, -27, 4, 24, -7, 1, 4, 28, -9, -21, -2, 6, -4, 20, -19, -26, -27, 12, 13, 8, -5, 18, -25, 14, 21, 23, -28, -15, -1, -18, 8, -7, -14, -21, -14, 23, -16, -14, 16, -24, -25, 12, 14, 24, -18, 0, 11, 12, 2, -13, 17, -12, -7, 13, 20, -2, -29, 27, -14, 1, -25, -19, 9, 10, -27, -15, 4, -21, 14, 14, 11, -5, 22, -7, 12, 4, -7, -4, 25, 8, 28, 25, -23, -11, -24, -13, -11, 15, 7, -15, 17, 18, -18, 0, 4, 6, -13, 0, 23, 19, 14, -20, -15, -26, 28, 23, -22, 5, 21, 10, 16, -10, -27, 21, -12, -16, -13, 25, 16, -25, -28, -29, -11, -16, 26, 6, 19, 25, 28, -7, 9, -18, -24, -14, -9, -27, 7, -17, -2, -28, -5, -22, -1, 13, -12, -2, 20, 13, 29, -25, -28, -6, 18, -22, 27, -20, -9, 29, -17, -22, 20, -7, 3, -1, 26, -2, -23, -15, -9, 0, 7, 11, -12, -23, -23, 1, -1, 22, -9, 17, 19, 14, -29, 17, -12, -1, -5, 1, -2, -8, -1, 15, 20, 23, -15, -26, -27, -8, 19, -10, 12, 7, 28, -22, 10, -26, -2, -10, -11, 3, -20, -25, -18, 11, 13, -27, 18, 11, -11, -20, 12, -12, 11, 3, 0, -7, 18, -20, -13, 12, -14, -19, 28, -19, 26, -1, 1, -10, -6, -16, 28, -3, 12, 27, 28, 5, 13, 22, -4, -22, 20, -16, 17, 7, -5, -9, -4, 5, -12, 29, 26, 24, 5, -14, -13, 22, 20, -29, 27, 15, 22, 7, -26, -3, 10, -19, -12, -13, 2, 3, -28, -28, 26, -15, -5, -22, -3, 29, 22, 13, -2};
        int i = 0;
        for (int k : keys) {
            if (k != -1) {
                if (k > 0) {
                    tree.insert(k, Integer.toString(k));
                } else {
                    tree.delete(-k);
                }
                if (!Test.testHeights(tree.getRoot())) {
                    System.out.println("failed heights after " + k);
                }
                if (!Test.testSizes(tree.getRoot())) {
                    System.out.println("failed size after " + k);
                }
                if (!Test.testParents(tree.getRoot())) {
                    System.out.println("failed parent after " + k);
                }
                try {
                    //BTreePrinter.printNode(tree.getRoot());
                } catch (Throwable ex) {
                    System.out.println("failed on index: " + i);
                }
            }
            i++;
        }
        return tree;
    }

    void testInsertAndDelete() {
        testInsertAndDelete(new AVLTree());
    }

    // Simulate random insert and delete operations
    void testInsertAndDelete(AVLTree tree) {

        for (int tries = 0; tries < 50; tries++) {
            int[] values = randomArray(10, -30, 30);
            int i = 0;
            List<Integer> valuesShuffled = Arrays.stream(values).boxed().collect(Collectors.toList());
            Collections.shuffle(valuesShuffled);

            List<Integer> valuesShuffled2 = new ArrayList<Integer>(valuesShuffled);
            Collections.shuffle(valuesShuffled2);
            for (int x : valuesShuffled) {
                i++;
                if ((i - 1) % 4 == 0) {
                    //BTreePrinter.printNode(tree.getRoot());
                }
                if (x < 0) {
                    System.out.println("deleting " + -x);
                    tree.delete(-x);
                } else {
                    System.out.println("inserting " + x);
                    tree.insert(x, Integer.toString(x));
                }
            }
        }
        System.out.println("testInsertAndDelete Passed!");
    }

    void testMinMax() {
        for (int i = 0; i < 10; i++) {
            int[] values = randomArray(100, 0, 100);

            AVLTree tree = arrayToTree(values);
            values = Arrays.stream(values).distinct().toArray();
            Arrays.sort(values);
            assert (tree.max().equals(Integer.toString(values[values.length - 1]))) : "unexpected max value";
            assert (tree.min().equals(Integer.toString(values[0]))) : "unexpected min value";

            tree.delete(Integer.parseInt(tree.max()));

            assert (tree.max().equals(Integer.toString(values[values.length - 2]))) : "unexpected max value";

            tree.delete(Integer.parseInt(tree.min()));
            assert (tree.min().equals(Integer.toString(values[1]))) : "unexpected min value";
        }

        // Empty tree
        AVLTree tree = new TestTree();
        assert (tree.max() == null) : "max should be null on empty tree";
        assert (tree.min() == null) : "min should be null on empty tree";
        System.out.println("testMinMax Passed!");
    }

    void testKeysToArray() {
        int[] values = randomArray(100, 0, 100);
        AVLTree tree = arrayToTree(values);
        Arrays.sort(values);
        int[] arr1 = tree.keysToArray(), arr2 = Arrays.stream(values).distinct().toArray();
        assert (arr1.length == arr2.length) : "keys array length different from expected value";
        for (int i = 0; i < arr1.length; i++)
            assert (arr1[i] == arr2[i]) : "key value different from expected value";
        System.out.println("testKeysToArray Passed!");
    }

    void testInfoToArray() {
        int[] values = randomArray(100, 0, 100);
        AVLTree tree = arrayToTree(values);
        Arrays.sort(values);
        Object[] arr1 = tree.infoToArray(), arr2 = Arrays.stream(values).distinct().mapToObj(x -> Integer.toString(x)).toArray();
        assert (arr1.length == arr2.length) : "info array length different from expected value";
        for (int i = 0; i < arr1.length; i++)
            assert (arr1[i].equals(arr2[i])) : "info value different from expected value";
        System.out.println("testInfoToArray Passed!");
    }

    void testSize() {
        int[] values = randomArray(100, 0, 100);
        AVLTree tree = arrayToTree(values);
        int realSize = (int) Arrays.stream(values).distinct().count();

        assert (tree.size() == realSize) : realSize + " vs " + tree.size();
        System.out.println("testSize Passed!");
    }

    void testGetRoot() {
        AVLTree tree = new TestTree();
        assert (null == tree.getRoot()) : "root isn't null on empty tree";

        tree.insert(1, "1");
        assert (1 == tree.getRoot().getKey()) : "unexpected root key";

        tree.insert(2, "2");
        assert (1 == tree.getRoot().getKey()) : "unexpected root key";

        tree.delete(1);
        assert (2 == tree.getRoot().getKey()) : "unexpected root key";

        assert (null == tree.getRoot().getParent()) : "root parent isn't null";
        System.out.println("testGetRoot Passed!");
    }

    void testJoin() {
        //BTreePrinter.printNode(tree1.getRoot());


        //BTreePrinter.printNode(trees[0].getRoot());
        //BTreePrinter.printNode(trees[1].getRoot());
//        int[] ret2 = new int[size];
//        for (int i=0; i < size; i++){
//            ret2[i] = i;
//        }
//        int[] tree1arr = trees[0].keysToArray();
//        int n = tree1arr.length;
//        int[] tree2arr = trees[1].keysToArray();
//        tree1arr = Arrays.copyOf(tree1arr, size);
//        tree1arr[n] = n;
//        for (int i = n+1; i < tree1arr.length; i++){
//            tree1arr[i] = tree2arr[i-n-1];
//        }
//        System.out.println(Arrays.equals(ret2, tree1arr));
    }

    static public class TestTree extends AVLTree {

        static private int get_left_diff(IAVLNode node) {
            return node.getHeight() - node.getLeft().getHeight();
        }

        static private int get_right_diff(IAVLNode node) {
            return node.getHeight() - node.getRight().getHeight();
        }

        static private int get_child_diff(IAVLNode node) {
            return get_left_diff(node) - get_right_diff(node);
        }

        public static void check_if_balanced(IAVLNode current) {
            if (current == null || !current.isRealNode())
                return;
            assert (!(get_child_diff(current) < -1 || get_child_diff(current) > 1)) : "Unbalanced tree after insert/delete";
            check_if_balanced(current.getRight());
            check_if_balanced(current.getLeft());
        }

        public int insert(int k, String i) {
            int retval = super.insert(k, i);

            check_if_balanced(this.getRoot());
            assert sanitizeTree(this.getRoot());
            return retval;
        }

        public int delete(int k) {
            int retval = super.delete(k);

            check_if_balanced(this.getRoot());
            assert sanitizeTree(this.getRoot());
            return retval;
        }

        public static int calcHeight(IAVLNode node) {
            if (node == null || !node.isRealNode())
                return -1;
            return Math.max(calcHeight(node.getLeft()), calcHeight(node.getRight())) + 1;
        }

        public static int calcSize(IAVLNode node) {
            if (node == null || !node.isRealNode())
                return 0;
            int sizeLeft = calcSize(node.getLeft());
            int sizeRight = calcSize(node.getRight());
            int size = sizeLeft + sizeRight + 1;
            if (size != ((AVLNode) node).getSize()) {
                assert false;
            }
            return size;
        }

        public static int calcSum(IAVLNode node) {
            if (node == null || !node.isRealNode())
                return 0;
            return calcSum(node.getLeft()) + calcSum(node.getRight()) + node.getKey();
        }

        public static boolean sanitizeTree(IAVLNode root) {
            if (root == null)
                return true;

            if (!root.isRealNode())
                return true;

            if (calcHeight(root) != root.getHeight()) {
                System.out.format("Height should be %d but is %d%n", calcHeight(root), root.getHeight());
                return false;
            }

            if (calcSize(root) != ((AVLNode) root).getSize()) {
                System.out.format("Size should be %d but is %d%n", calcSize(root), ((AVLNode) root).getSize());
                calcSize(root);
                return false;
            }

            int bf = calcHeight(root.getLeft()) - calcHeight(root.getRight());
            if (Math.abs(bf) >= 2) {
                System.out.format("Balance factor should be out of range %d%n", bf, ((AVLNode) root).getSize());
                return false;
            }

            return sanitizeTree(root.getLeft()) && sanitizeTree(root.getRight());
        }
    }

    public static void main(String[] args) {
        
    	
    	double[] arr = new double[3]; //TOTAL-MAX-AMOUNTofOPPERATIONS
    	int size;
    	//  int size = 10000;
		/*
		 * ArrayList<Integer> arr1 = new ArrayList<>(); for (int i=0; i < size; i++){
		 * arr1.add(i); } Collections.shuffle(arr1);
		 * 
		 * int[] ret = new int[size]; for (int i=0; i < ret.length; i++) { ret[i] =
		 * arr1.get(i); } double MaxJoinCost = 0; double AvgJoinCost = 0; AVLTree tree =
		 * arrayToTree(ret); //tree of 10000 different values Random rnd = new Random();
		 * 
		 * int RandomKey = rnd.nextInt(size); int MaxKeyLeftTree =
		 * Integer.parseInt(tree.min());
		 */
     
        double SumOfJoinCost = 0, NumOfJoin = 0;
        
        for(int k = 1; k <= 6; k++) {
        	 size = (10000) * k;
        	 ArrayList<Integer> arr1 = new ArrayList<>();
             for (int i=0; i < size; i++){
                 arr1.add(i);
             }
             Collections.shuffle(arr1);
     		
     		 int[] ret = new int[size];
     		 for (int i=0; i < ret.length; i++) { 
     			 ret[i] = arr1.get(i); 
     		 }
     		double MaxJoinCost = 0;
     		double AvgJoinCost = 0;
             AVLTree tree = arrayToTree(ret); //tree of 10000 different values
             Random rnd = new Random();
             
             int RandomKey = rnd.nextInt(size);
             int MaxKeyLeftTree = Integer.parseInt(tree.min());
             
             
             System.out.println("Expirement numner " + k + " for: " + size);
             System.out.println("------------------------");
             //tree.split(RandomKey, arr);
             SumOfJoinCost = arr[0];
             NumOfJoin = arr[2];
             AvgJoinCost = SumOfJoinCost / NumOfJoin;
             MaxJoinCost = arr[1];
             System.out.println("RandoM Expirement");
             System.out.println("AVGjoinCOST: " + AvgJoinCost);
             System.out.println("MaxJoinCost: " + MaxJoinCost);

             //Initialize
             arr = new double[3];
             System.out.println("------------------------");

            //SPLIT OMCE BY MaxKeyLeftTree
            tree = arrayToTree(ret);
            //tree.split( MaxKeyLeftTree, arr);
            SumOfJoinCost = arr[0];
            NumOfJoin = arr[2];
            AvgJoinCost = SumOfJoinCost / NumOfJoin;
            MaxJoinCost = arr[1];
            System.out.println("Determinist Expirement");

            System.out.println("AVGjoinCOST: " + AvgJoinCost);
            System.out.println("MaxJoinCost: " + MaxJoinCost);
        	
            System.out.println("////////////////////////////////////");
            System.out.println("////////////////////////////////////");
            System.out.println("////////////////////////////////////");
        	
        }
        
        
        
        //SPLIT ONCE BY RandomKey
		/*
		 * tree.split(RandomKey, arr); SumOfJoinCost = arr[0]; NumOfJoin = arr[2];
		 * AvgJoinCost = SumOfJoinCost / NumOfJoin; MaxJoinCost = arr[1];
		 * System.out.println("RandoM Expirement"); System.out.println("AVGjoinCOST: " +
		 * AvgJoinCost); System.out.println("MaxJoinCost: " + MaxJoinCost);
		 * 
		 * //Initialize arr = new double[3];
		 * System.out.println("////////////////////////////////////");
		 * 
		 * //SPLIT OMCE BY MaxKeyLeftTree tree = arrayToTree(ret); tree.split(
		 * MaxKeyLeftTree, arr); SumOfJoinCost = arr[0]; NumOfJoin = arr[2]; AvgJoinCost
		 * = SumOfJoinCost / NumOfJoin; MaxJoinCost = arr[1];
		 * System.out.println("Determinist Expirement");
		 * 
		 * System.out.println("AVGjoinCOST: " + AvgJoinCost);
		 * System.out.println("MaxJoinCost: " + MaxJoinCost);
		 */
        
		/*
		 * AVLTree tree1 = new AVLTree();
		 * 
		 * 
		 * tree1.insert(2, "2"); tree1.insert(1, "1"); tree1.insert(3, "3");
		 * 
		 * BTreePrinter.printNode(tree1.getRoot()); tree1.delete(2);
		 * BTreePrinter.printNode(tree1.getRoot());
		 * 
		 */    		
  //  		BTreePrinter.printNode(tree1.getRoot());
//    		BTreePrinter.printNode(tree1.getRoot());


     	
    	
    	
    	
        }
  
    }
		
    	
    	
    	/*
    	AVLTree[] result = null;
        int size = 100000;
        ArrayList<Integer> arr1 = new ArrayList<>();
        for (int i=0; i < size; i++){
            arr1.add(i);
        }
        Collections.shuffle(arr1);
        int[] ret = new int[size];
        for (int i=0; i < ret.length; i++)
        {
            ret[i] = arr1.get(i);
        }
        AVLTree tree1 = arrayToTree(ret);
        //BTreePrinter.printNode(tree1.getRoot());
        Random rnd = new Random();
        int n = rnd.nextInt(size);
        result = tree1.split(n);
        int n1 = result[0].size();
        int n2 = result[1].size();
        System.out.println("left size " + n1);
        System.out.println("right size " + n2);
        if (n1 + n2 != size - 1) {
            System.out.println("//////////////////////////////////////////////////////////");
            System.out.println("Splitting node " + n);
            BTreePrinter.printNode(result[0].getRoot());
            BTreePrinter.printNode(result[1].getRoot());
            //break;
        }
        int kk = result[0].join(new AVLTree(n, "" + n).getRoot(), result[1]);
        tree1 = result[0];
        if (tree1.size() != size) {
            System.out.println("//////////////////////////////////////////////////////////");
            System.out.println("Joining node " + n + " for size " + tree1.size());
        }
        //BTreePrinter.printNode(tree1.getRoot());
        }
        */
    