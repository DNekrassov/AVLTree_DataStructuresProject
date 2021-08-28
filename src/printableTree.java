import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class printableTree extends AVLTree {
    //you should implement IAVLnode
    // you should write insert(k, info)

    public void printTree() {
        IAVLNode root = this.getRoot();
        List<List<String>> lines = new ArrayList<List<String>>();

        List<IAVLNode> level = new ArrayList<IAVLNode>();
        List<IAVLNode> next = new ArrayList<IAVLNode>();

        level.add(root);
        int nn = 1;

        int widest = 0;

        while (nn != 0) {
            List<String> line = new ArrayList<String>();

            nn = 0;

            for (IAVLNode n : level) {
                if (n == null) {
                    line.add(null);

                    next.add(null);
                    next.add(null);
                } else {
                    String aa = getText(n);
                    line.add(aa);
                    if (aa.length() > widest) widest = aa.length();

                    next.add(n.getLeft());
                    next.add(n.getRight());

                    if (n.getLeft() != null) nn++;
                    if (n.getRight() != null) nn++;
                }
            }

            if (widest % 8 == 1) widest++;

            lines.add(line);

            List<IAVLNode> tmp = level;
            level = next;
            next = tmp;
            next.clear();
        }

        int perpiece = lines.get(lines.size() - 2).size() * (widest);
        for (int i = 0; i < lines.size(); i++) {
            List<String> line = lines.get(i);
            int hpw = (int) Math.floor(perpiece / 2f) - 1;

            if (i > 0) {
                for (int j = 0; j < line.size(); j++) {

                    // split node
                    char c = ' ';
                    if (j % 2 == 1) {
                        if (line.get(j - 1) != null) {
                            c = (line.get(j) != null) ? '┴' : '┘';
                        } else {
                            if (j < line.size() && line.get(j) != null) c = '└';
                        }
                    }
                    System.out.print(c);

                    // lines and spaces
                    if (line.get(j) == null) {
                        for (int k = 0; k < perpiece - 1; k++) {
                            System.out.print(" ");
                        }
                    } else {

                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? " " : "─");
                        }
                        System.out.print(j % 2 == 0 ? "┌" : "┐");
                        for (int k = 0; k < hpw; k++) {
                            System.out.print(j % 2 == 0 ? "─" : " ");
                        }
                    }
                }
                System.out.println();
            }

            // print line of numbers
            for (int j = 0; j < line.size(); j++) {

                String f = line.get(j);
                if (f == null) f = "";
                int gap1 = (int) Math.ceil(perpiece / 2f - f.length() / 2f);
                int gap2 = (int) Math.floor(perpiece / 2f - f.length() / 2f);

                // a number
                for (int k = 0; k < gap1; k++) {
                    System.out.print(" ");
                }
                System.out.print(f);
                for (int k = 0; k < gap2; k++) {
                    System.out.print(" ");
                }
            }
            System.out.println();

            perpiece /= 2;
        }
    }

    public String getText(IAVLNode node){
        if (node.getKey() == -1){
            return "";
        }
        return "" + node.getKey();

    }

    public static void main(String[] args){
        printableTree tree = new printableTree();
        Tests test = new Tests();
        if (tree.empty()) {
            System.out.println("Success 1");
        }
        //int[] values = new int[]{16, 24, 36, 19, 44, 28, 61, 74, 83, 64, 52, 65, 86, 93, 88};
        int[] values = new int[]{0, 2, 4, 1, 5, 3, 7, 10, 11, 8, 6, 9, 12, 14, 13};
        //int[] values = new int[]{16, 24, 36, 19, 44, 28, 88, 20};
        for (int val : values) {
            tree.insert(val, "" + val);
            tree.printTree();
            System.out.println("///////////////////////////");
        }
        if (tree.min().equals("0")) {
            System.out.println("Success 2");
        }
        if (tree.max().equals("14")) {
            System.out.println("Success 3");
        }
        if (test.checkBalanceOfTree(tree.getRoot())) {
            System.out.println("Success 4");
        }
        if (test.checkOrderingOfTree(tree.getRoot())) {
            System.out.println("Success 5");
        }
        tree.delete(13);
        if (test.checkBalanceOfTree(tree.getRoot())) {
            System.out.println("Success 6");
        }
        if (test.checkOrderingOfTree(tree.getRoot())) {
            System.out.println("Success 7");
        }
        if (tree.search(13) == null) {
            System.out.println("Success 8");
        }

        tree.delete(1);
        if (test.checkBalanceOfTree(tree.getRoot())) {
            System.out.println("Success 9");
        }
        if (test.checkOrderingOfTree(tree.getRoot())) {
            System.out.println("Success 10");
        }
        if (tree.search(1) == null) {
            System.out.println("Success 11");
        }

        tree.delete(0);
        if (test.checkBalanceOfTree(tree.getRoot())) {
            System.out.println("Success 12");
        }
        if (test.checkOrderingOfTree(tree.getRoot())) {
            System.out.println("Success 13");
        }
        if (tree.search(0) == null) {
            System.out.println("Success 14");
        }

        tree.delete(3);
        if (test.checkBalanceOfTree(tree.getRoot())) {
            System.out.println("Success 15");
        }
        if (test.checkOrderingOfTree(tree.getRoot())) {
            System.out.println("Success 16");
        }
        if (tree.search(3) == null) {
            System.out.println("Success 16.5");
        }
        tree.delete(2);
        if (test.checkBalanceOfTree(tree.getRoot())) {
            System.out.println("Success 17");
        }
        if (test.checkOrderingOfTree(tree.getRoot())) {
            System.out.println("Success 18");
        }
        if (tree.search(2) == null) {
            System.out.println("Success 19");
        }

        tree.delete(4);
        if (test.checkBalanceOfTree(tree.getRoot())) {
            System.out.println("Success 20");
        }
        if (test.checkOrderingOfTree(tree.getRoot())) {
            System.out.println("Success 21");
        }
        if (tree.search(4) == null) {
            System.out.println("Success 22");
        }

        tree.delete(6);
        if (test.checkBalanceOfTree(tree.getRoot())) {
            System.out.println("Success 23");
        }
        if (test.checkOrderingOfTree(tree.getRoot())) {
            System.out.println("Success 24");
        }
        if (tree.search(6) == null) {
            System.out.println("Success 25");
        }

        tree.delete(14);
        if (test.checkBalanceOfTree(tree.getRoot())) {
            System.out.println("Success 26");
        }
        if (test.checkOrderingOfTree(tree.getRoot())) {
            System.out.println("Success 27");
        }
        if (tree.search(14) == null) {
            System.out.println("Success 28");
        }

        tree.delete(12);
        if (test.checkBalanceOfTree(tree.getRoot())) {
            System.out.println("Success 29");
        }
        if (test.checkOrderingOfTree(tree.getRoot())) {
            System.out.println("Success 30");
        }
        if (tree.search(12) == null) {
            System.out.println("Success 31");
        }

        tree.delete(11);
        if (test.checkBalanceOfTree(tree.getRoot())) {
            System.out.println("Success 32");
        }
        if (test.checkOrderingOfTree(tree.getRoot())) {
            System.out.println("Success 33");
        }
        if (tree.search(11) == null) {
            System.out.println("Success 34");
        }
    }
}
