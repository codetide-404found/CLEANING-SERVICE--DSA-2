import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class RedBlackTreeTest {

    private RedBlackTree<Integer> rbt;

    @BeforeEach
    void setUp() {
        rbt = new RedBlackTree<>();
    }

    // -------------------- NORMAL CASES --------------------

    @Test
    void insertAndSearch_findsInsertedKeys() {
        int[] values = {10, 20, 30, 15, 25, 5};
        for (int v : values) rbt.insert(v);

        for (int v : values) {
            assertTrue(rbt.search(v));
        }
        assertFalse(rbt.search(999));
    }

    @Test
    void inOrderTraversal_returnsSortedOrder() {
        int[] values = {10, 20, 30, 15, 25, 5, 1};
        for (int v : values) rbt.insert(v);

        List<Integer> result = rbt.inOrder();
        List<Integer> expected = List.of(1, 5, 10, 15, 20, 25, 30);
        assertEquals(expected, result);
    }

    @Test
    void sequentialInsertions_remainBalanced() {
        // Sequential ascending inserts would skew a plain BST into a linked list.
        // A correct RBT should keep height close to O(log n).
        for (int i = 1; i <= 15; i++) {
            rbt.insert(i);
        }
        // 15 nodes: a skewed BST would have height 14.
        // A balanced RBT should have height no more than ~2*log2(16) ≈ 8.
        assertTrue(rbt.height() <= 8,
                "Tree height " + rbt.height() + " suggests balancing failed");
    }

    @Test
    void blackHeight_isConsistentAfterMultipleInserts() {
        int[] values = {50, 40, 60, 30, 45, 55, 70, 20};
        for (int v : values) rbt.insert(v);

        // Every root-to-leaf path must have the same black-height.
        // This test asserts it doesn't throw and returns a sane positive value.
        int bh = rbt.blackHeight();
        assertTrue(bh > 0);
    }

    @Test
    void size_tracksNumberOfInsertedKeys() {
        rbt.insert(1);
        rbt.insert(2);
        rbt.insert(3);
        assertEquals(3, rbt.size());
    }

    // -------------------- BOUNDARY CASES --------------------

    @Test
    void emptyTree_searchReturnsFalse() {
        assertFalse(rbt.search(10));
        assertTrue(rbt.isEmpty());
        assertEquals(0, rbt.size());
    }

    @Test
    void emptyTree_heightIsMinusOne() {
        assertEquals(-1, rbt.height());
    }

    @Test
    void singleNodeTree_isRoot() {
        rbt.insert(42);
        assertTrue(rbt.search(42));
        assertEquals(0, rbt.height());
        assertEquals(1, rbt.size());
    }

    @Test
    void insertDuplicateKey_isIgnored() {
        rbt.insert(50);
        rbt.insert(50);
        assertEquals(1, rbt.size());
    }

    // -------------------- INVALID INPUT CASES --------------------

    @Test
    void insertNullKey_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> rbt.insert(null));
    }

    @Test
    void searchNullKey_returnsFalseInsteadOfThrowing() {
        rbt.insert(10);
        assertFalse(rbt.search(null));
    }

    @Test
    void searchOnEmptyTree_returnsFalseNotException() {
        assertDoesNotThrow(() -> rbt.search(5));
        assertFalse(rbt.search(5));
    }
}
