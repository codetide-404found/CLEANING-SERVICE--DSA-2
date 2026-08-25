import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Iterator;
import java.util.NoSuchElementException;

class MyLinkedListTest {

    private MyLinkedList<Integer> list;

    @BeforeEach
    void setUp() {
        list = new MyLinkedList<>();
    }

    // -------------------- NORMAL CASES --------------------

    @Test
    void addFirstInsertsAtHead() {
        list.addLast(2);
        list.addFirst(1);
        assertEquals("[1, 2]", list.toString());
    }

    @Test
    void addLastInsertsAtTail() {
        list.addLast(1);
        list.addLast(2);
        assertEquals("[1, 2]", list.toString());
    }

    @Test
    void insertAfterPlacesValueCorrectly() {
        MyLinkedList<String> route = new MyLinkedList<>();
        route.addLast("A");
        route.addLast("C");
        route.insertAfter("A", "B");
        assertEquals("[A, B, C]", route.toString());
    }

    @Test
    void removeDeletesFirstMatchingValue() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        assertTrue(list.remove(2));
        assertEquals("[1, 3]", list.toString());
    }

    @Test
    void iteratorTraversesInInsertionOrder() {
        list.addLast(1);
        list.addLast(2);
        list.addLast(3);
        StringBuilder sb = new StringBuilder();
        for (int value : list) {
            sb.append(value);
        }
        assertEquals("123", sb.toString());
    }

    // -------------------- BOUNDARY CASES --------------------

    @Test
    void emptyListIteratorHasNoNext() {
        Iterator<Integer> it = list.iterator();
        assertFalse(it.hasNext());
        assertTrue(list.isEmpty());
    }

    @Test
    void singleElementListAddAndRemove() {
        MyLinkedList<String> single = new MyLinkedList<>();
        single.addFirst("only");
        assertEquals(1, single.size());
        assertTrue(single.remove("only"));
        assertTrue(single.isEmpty());
    }

    @Test
    void insertAfterLastElementUpdatesTail() {
        list.addLast(1);
        list.insertAfter(1, 2);
        list.addLast(3);
        assertEquals("[1, 2, 3]", list.toString());
    }

    // -------------------- INVALID INPUT CASES --------------------

    @Test
    void iteratorNextOnEmptyListThrows() {
        Iterator<Integer> it = list.iterator();
        assertThrows(NoSuchElementException.class, it::next);
    }

    @Test
    void insertAfterNonexistentTargetThrows() {
        list.addLast(1);
        assertThrows(NoSuchElementException.class, () -> list.insertAfter(99, 2));
    }

    @Test
    void removeNonexistentValueReturnsFalse() {
        list.addLast(1);
        assertFalse(list.remove(99));
    }
}
