public class TraceDriver {
    public static void main(String[] args) {
        System.out.println("=== BST TRACE ===");
        BinarySearchTree<Integer> bst = new BinarySearchTree<>();
        int[] bstValues = {50, 30, 70, 20, 40, 60, 80};
        for (int v : bstValues) {
            bst.insert(v);
            System.out.println("Inserted " + v + " -> in-order: " + bst.inOrder());
        }
        System.out.println("Height: " + bst.height());
        System.out.println("Min: " + bst.min() + ", Max: " + bst.max());
        bst.delete(30);
        System.out.println("After deleting 30 -> in-order: " + bst.inOrder());

        System.out.println();
        System.out.println("=== RBT TRACE (sequential ascending insert 1..15) ===");
        RedBlackTree<Integer> rbt = new RedBlackTree<>();
        for (int i = 1; i <= 15; i++) {
            rbt.insert(i);
        }
        System.out.println("In-order: " + rbt.inOrder());
        System.out.println("Height: " + rbt.height() + " (plain BST would have height 14)");
        System.out.println();
        rbt.printTree();
    }
}
