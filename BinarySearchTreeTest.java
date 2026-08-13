import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

class BinarySearchTreeTest {

    private BinarySearchTree<Integer> bst;

    @BeforeEach
    void setUp() {
        bst = new BinarySearchTree<>();
    }

    // -------------------- NORMAL CASES --------------------

    @Test
    void insertAndSearch_findsInsertedKeys() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        bst.insert(20);
        bst.insert(40);

        assertTrue(bst.search(50));
        assertTrue(bst.search(20));
        assertTrue(bst.search(40));
        assertFalse(bst.search(99));
    }

    @Test
    void inOrderTraversal_returnsSortedOrder() {
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) bst.insert(v);

        List<Integer> result = bst.inOrder();
        List<Integer> expected = List.of(20, 30, 40, 50, 60, 70, 80);
        assertEquals(expected, result);
    }

    @Test
    void minAndMax_returnCorrectValues() {
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) bst.insert(v);

        assertEquals(20, bst.min());
        assertEquals(80, bst.max());
    }

    @Test
    void delete_leafNode_removesCorrectly() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);

        bst.delete(30);
        assertFalse(bst.search(30));
        assertEquals(2, bst.size());
    }

    @Test
    void delete_nodeWithTwoChildren_replacesWithSuccessor() {
        int[] values = {50, 30, 70, 20, 40, 60, 80};
        for (int v : values) bst.insert(v);

        bst.delete(30); // has two children (20, 40)

        assertFalse(bst.search(30));
        assertTrue(bst.search(20));
        assertTrue(bst.search(40));
        assertEquals(6, bst.size());
    }

    @Test
    void height_reflectsTreeShape() {
        bst.insert(50);
        bst.insert(30);
        bst.insert(70);
        assertEquals(1, bst.height());
    }

    // -------------------- BOUNDARY CASES --------------------

    @Test
    void emptyTree_searchReturnsFalse() {
        assertFalse(bst.search(10));
        assertTrue(bst.isEmpty());
        assertEquals(0, bst.size());
    }

    @Test
    void emptyTree_heightIsMinusOne() {
        assertEquals(-1, bst.height());
    }

    @Test
    void singleNodeTree_minEqualsMaxEqualsRoot() {
        bst.insert(42);
        assertEquals(42, bst.min());
        assertEquals(42, bst.max());
        assertEquals(0, bst.height());
    }

    @Test
    void insertDuplicateKey_isIgnored() {
        bst.insert(50);
        bst.insert(50);
        assertEquals(1, bst.size());
    }

    @Test
    void deleteFromEmptyTree_doesNothingAndDoesNotThrow() {
        assertDoesNotThrow(() -> bst.delete(10));
        assertEquals(0, bst.size());
    }

    // -------------------- INVALID INPUT CASES --------------------

    @Test
    void insertNullKey_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> bst.insert(null));
    }

    @Test
    void deleteNullKey_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> bst.delete(null));
    }

    @Test
    void deleteNonExistentKey_doesNotAlterTree() {
        bst.insert(50);
        bst.insert(30);
        bst.delete(999); // not in tree

        assertEquals(2, bst.size());
        assertTrue(bst.search(50));
        assertTrue(bst.search(30));
    }

    @Test
    void minOnEmptyTree_throwsException() {
        assertThrows(IllegalStateException.class, () -> bst.min());
    }

    @Test
    void maxOnEmptyTree_throwsException() {
        assertThrows(IllegalStateException.class, () -> bst.max());
    }
}
