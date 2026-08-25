import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

class DynamicArrayTest {

    private DynamicArray<Integer> arr;

    @BeforeEach
    void setUp() {
        arr = new DynamicArray<>();
    }

    // -------------------- NORMAL CASES --------------------

    @Test
    void insertAppendsValueAndIncreasesSize() {
        arr.insert(10);
        arr.insert(20);
        assertEquals(2, arr.size());
        assertEquals(10, arr.get(0));
        assertEquals(20, arr.get(1));
    }

    @Test
    void insertAtIndexShiftsSubsequentElements() {
        DynamicArray<String> letters = new DynamicArray<>();
        letters.insert("A");
        letters.insert("C");
        letters.insert(1, "B");
        assertEquals("[A, B, C]", letters.toString());
    }

    @Test
    void setOverwritesValueAtIndex() {
        arr.insert(1);
        arr.set(0, 99);
        assertEquals(99, arr.get(0));
    }

    @Test
    void removeShiftsElementsLeftAndReturnsRemovedValue() {
        arr.insert(1);
        arr.insert(2);
        arr.insert(3);
        int removed = arr.remove(1);
        assertEquals(2, removed);
        assertEquals("[1, 3]", arr.toString());
    }

    // -------------------- BOUNDARY CASES --------------------

    @Test
    void resizeDoublesCapacityWhenFull() {
        DynamicArray<Integer> small = new DynamicArray<>(2);
        assertEquals(2, small.capacity());
        small.insert(1);
        small.insert(2);
        assertEquals(2, small.capacity());
        small.insert(3);
        assertEquals(4, small.capacity());
    }

    @Test
    void singleElementArrayBehavesCorrectly() {
        DynamicArray<String> single = new DynamicArray<>();
        single.insert("only");
        assertEquals(1, single.size());
        assertEquals("only", single.remove(0));
        assertTrue(single.isEmpty());
    }

    @Test
    void duplicateValuesAreAllowed() {
        arr.insert(5);
        arr.insert(5);
        arr.insert(5);
        assertEquals(3, arr.size());
        assertEquals("[5, 5, 5]", arr.toString());
    }

    // -------------------- INVALID INPUT CASES --------------------

    @Test
    void getWithNegativeIndexThrows() {
        arr.insert(1);
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(-1));
    }

    @Test
    void getWithIndexEqualToSizeThrows() {
        arr.insert(1);
        assertThrows(IndexOutOfBoundsException.class, () -> arr.get(1));
    }

    @Test
    void removeFromEmptyArrayThrows() {
        assertThrows(IndexOutOfBoundsException.class, () -> arr.remove(0));
    }

    @Test
    void constructorWithZeroOrNegativeCapacityThrows() {
        assertThrows(IllegalArgumentException.class, () -> new DynamicArray<Integer>(0));
        assertThrows(IllegalArgumentException.class, () -> new DynamicArray<Integer>(-5));
    }
}
