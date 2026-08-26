import java.util.ArrayList;
import java.util.List;

/**
 * Custom Binary Search Tree (BST).
 * Built from scratch — does NOT use java.util.TreeMap or any built-in tree class,
 * per project constraint (Section 8 of project brief).
 *
 * Supports: insert, delete, search, min, max, height, size,
 * and in-order / pre-order / post-order traversal.
 *
 * @param <T> a Comparable type (e.g., Integer, String, or a custom Record key)
 */
public class BinarySearchTree<T extends Comparable<T>> {

    /** Internal node class. */
    private class Node {
        T key;
        Node left, right;

        Node(T key) {
            this.key = key;
        }
    }

    private Node root;
    private int size;

    public BinarySearchTree() {
        this.root = null;
        this.size = 0;
    }

    // ---------------------------------------------------------
    // INSERT
    // ---------------------------------------------------------

    /** Inserts a key into the tree. Duplicate keys are ignored. */
    public void insert(T key) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot insert null key");
        }
        root = insertRecursive(root, key);
    }

    private Node insertRecursive(Node node, T key) {
        if (node == null) {
            size++;
            return new Node(key);
        }
        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = insertRecursive(node.left, key);
        } else if (cmp > 0) {
            node.right = insertRecursive(node.right, key);
        }
        // cmp == 0: duplicate, do nothing
        return node;
    }

    // ---------------------------------------------------------
    // UPDATE
    // ---------------------------------------------------------

    /**
     * Updates the stored value for a key that already exists in the tree
     * (e.g., a location's cleaning priority or estimated time changed, but
     * its locationId — the field used for comparison/ordering — stays the same).
     * Does NOT restructure the tree, since ordering is unaffected.
     *
     * @param newValue an object that compareTo()-equals an existing key,
     *                  carrying the new data to store
     * @return true if a matching node was found and updated, false if no match exists
     */
    public boolean update(T newValue) {
        if (newValue == null) {
            throw new IllegalArgumentException("Cannot update with null value");
        }
        return updateRecursive(root, newValue);
    }

    private boolean updateRecursive(Node node, T newValue) {
        if (node == null) return false;
        int cmp = newValue.compareTo(node.key);
        if (cmp == 0) {
            node.key = newValue; // same ordering key, refreshed data
            return true;
        }
        return cmp < 0 ? updateRecursive(node.left, newValue) : updateRecursive(node.right, newValue);
    }

    // ---------------------------------------------------------
    // SEARCH
    // ---------------------------------------------------------

    /** Returns true if key exists in the tree. */
    public boolean search(T key) {
        if (key == null) return false;
        return searchRecursive(root, key);
    }

    private boolean searchRecursive(Node node, T key) {
        if (node == null) return false;
        int cmp = key.compareTo(node.key);
        if (cmp == 0) return true;
        return cmp < 0 ? searchRecursive(node.left, key) : searchRecursive(node.right, key);
    }

    // ---------------------------------------------------------
    // DELETE
    // ---------------------------------------------------------

    /** Deletes a key from the tree, if present. */
    public void delete(T key) {
        if (key == null) {
            throw new IllegalArgumentException("Cannot delete null key");
        }
        root = deleteRecursive(root, key);
    }

    private Node deleteRecursive(Node node, T key) {
        if (node == null) {
            return null; // key not found, nothing to do
        }

        int cmp = key.compareTo(node.key);
        if (cmp < 0) {
            node.left = deleteRecursive(node.left, key);
        } else if (cmp > 0) {
            node.right = deleteRecursive(node.right, key);
        } else {
            // Found the node to delete
            size--;

            // Case 1: leaf node
            if (node.left == null && node.right == null) {
                return null;
            }
            // Case 2: one child
            if (node.left == null) {
                return node.right;
            }
            if (node.right == null) {
                return node.left;
            }
            // Case 3: two children -> replace with in-order successor
            Node successor = findMinNode(node.right);
            node.key = successor.key;
            size++; // undo the decrement since we're not actually removing this node yet
            node.right = deleteRecursive(node.right, successor.key);
        }
        return node;
    }

    // ---------------------------------------------------------
    // MIN / MAX
    // ---------------------------------------------------------

    public T min() {
        if (root == null) throw new IllegalStateException("Tree is empty");
        return findMinNode(root).key;
    }

    public T max() {
        if (root == null) throw new IllegalStateException("Tree is empty");
        Node current = root;
        while (current.right != null) current = current.right;
        return current.key;
    }

    private Node findMinNode(Node node) {
        Node current = node;
        while (current.left != null) current = current.left;
        return current;
    }

    // ---------------------------------------------------------
    // TRAVERSALS
    // ---------------------------------------------------------

    public List<T> inOrder() {
        List<T> result = new ArrayList<>();
        inOrderRecursive(root, result);
        return result;
    }

    private void inOrderRecursive(Node node, List<T> result) {
        if (node == null) return;
        inOrderRecursive(node.left, result);
        result.add(node.key);
        inOrderRecursive(node.right, result);
    }

    public List<T> preOrder() {
        List<T> result = new ArrayList<>();
        preOrderRecursive(root, result);
        return result;
    }

    private void preOrderRecursive(Node node, List<T> result) {
        if (node == null) return;
        result.add(node.key);
        preOrderRecursive(node.left, result);
        preOrderRecursive(node.right, result);
    }

    public List<T> postOrder() {
        List<T> result = new ArrayList<>();
        postOrderRecursive(root, result);
        return result;
    }

    private void postOrderRecursive(Node node, List<T> result) {
        if (node == null) return;
        postOrderRecursive(node.left, result);
        postOrderRecursive(node.right, result);
        result.add(node.key);
    }

    // ---------------------------------------------------------
    // UTILITY
    // ---------------------------------------------------------

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int height() {
        return heightRecursive(root);
    }

    private int heightRecursive(Node node) {
        if (node == null) return -1; // empty tree/subtree has height -1
        return 1 + Math.max(heightRecursive(node.left), heightRecursive(node.right));
    }

    /** Prints the tree structure sideways for quick visual debugging / trace tables. */
    public void printTree() {
        printTreeRecursive(root, 0);
    }

    private void printTreeRecursive(Node node, int depth) {
        if (node == null) return;
        printTreeRecursive(node.right, depth + 1);
        System.out.println("    ".repeat(depth) + node.key);
        printTreeRecursive(node.left, depth + 1);
    }
}
