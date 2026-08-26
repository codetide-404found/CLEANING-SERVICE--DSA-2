import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class HeapTest {

    private Heap heap;

    @BeforeEach
    void setUp() {
        heap = new Heap();
    }

    @Test
    void emptyHeap_isEmptyAndSizeZero() {
        assertTrue(heap.isEmpty());
        assertEquals(0, heap.size());
    }

    @Test
    void peek_onEmptyHeap_throwsException() {
        assertThrows(IllegalStateException.class, () -> heap.peek());
    }

    @Test
    void extract_onEmptyHeap_throwsException() {
        assertThrows(IllegalStateException.class, () -> heap.extractHighestPriority());
    }

    @Test
    void insertSingleElement_peekReturnsIt() {
        CampusLocation loc = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CleaningRequest req = new CleaningRequest("REQ-1", "Alice", loc, "Sanitization", Priority.CRITICAL, LocalDateTime.of(2025, 1, 1, 10, 0));
        heap.insert(req);
        assertEquals(req, heap.peek());
        assertEquals(1, heap.size());
    }

    @Test
    void insertSingleElement_extractReturnsIt() {
        CampusLocation loc = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CleaningRequest req = new CleaningRequest("REQ-1", "Alice", loc, "Sanitization", Priority.CRITICAL, LocalDateTime.of(2025, 1, 1, 10, 0));
        heap.insert(req);
        assertEquals(req, heap.extractHighestPriority());
        assertTrue(heap.isEmpty());
    }

    @Test
    void insertMultiple_maintainsMaxHeapOrder() {
        CampusLocation loc1 = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CampusLocation loc2 = new CampusLocation("LOC-2", "Engineering Lab", "East", 4, 45);
        CampusLocation loc3 = new CampusLocation("LOC-3", "Hostel B", "South", 3, 30);

        heap.insert(new CleaningRequest("REQ-1", "Alice", loc1, "Sanitization", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 0)));
        heap.insert(new CleaningRequest("REQ-2", "Bob", loc2, "Lab", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 5)));
        heap.insert(new CleaningRequest("REQ-3", "Carol", loc3, "Room", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 10)));

        assertTrue(heap.validateHeapProperty());
    }

    @Test
    void extract_removesMaxAndRestoresHeap() {
        CampusLocation loc1 = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CampusLocation loc2 = new CampusLocation("LOC-2", "Engineering Lab", "East", 4, 45);
        CampusLocation loc3 = new CampusLocation("LOC-3", "Hostel B", "South", 3, 30);

        heap.insert(new CleaningRequest("REQ-1", "Alice", loc1, "Sanitization", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 0)));
        heap.insert(new CleaningRequest("REQ-2", "Bob", loc2, "Lab", Priority.CRITICAL, LocalDateTime.of(2025, 1, 1, 10, 5)));
        heap.insert(new CleaningRequest("REQ-3", "Carol", loc3, "Room", Priority.HIGH, LocalDateTime.of(2025, 1, 1, 10, 10)));

        assertEquals("REQ-2", heap.extractHighestPriority().getRequestId());
        assertTrue(heap.validateHeapProperty());
        assertEquals(2, heap.size());

        assertEquals("REQ-3", heap.extractHighestPriority().getRequestId());
        assertTrue(heap.validateHeapProperty());

        assertEquals("REQ-1", heap.extractHighestPriority().getRequestId());
    }

    @Test
    void insertNull_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> heap.insert(null));
    }

    @Test
    void insertDuplicateId_throwsException() {
        CampusLocation loc = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        heap.insert(new CleaningRequest("REQ-1", "Alice", loc, "Sanitization", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 0)));
        assertThrows(IllegalArgumentException.class, () ->
                heap.insert(new CleaningRequest("REQ-1", "Bob", loc, "Cleaning", Priority.HIGH, LocalDateTime.of(2025, 1, 1, 11, 0))));
    }

    @Test
    void updatePriority_upgradeMovesUp() {
        CampusLocation loc1 = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CampusLocation loc2 = new CampusLocation("LOC-2", "Engineering Lab", "East", 4, 45);

        heap.insert(new CleaningRequest("REQ-1", "Alice", loc1, "Sanitization", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 0)));
        heap.insert(new CleaningRequest("REQ-2", "Bob", loc2, "Lab", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 5)));

        assertTrue(heap.updatePriority("REQ-1", Priority.CRITICAL));
        assertTrue(heap.validateHeapProperty());
        assertEquals("REQ-1", heap.peek().getRequestId());
        assertEquals(Priority.CRITICAL, heap.peek().getPriority());
    }

    @Test
    void updatePriority_downgradeMovesDown() {
        CampusLocation loc1 = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CampusLocation loc2 = new CampusLocation("LOC-2", "Engineering Lab", "East", 4, 45);
        CampusLocation loc3 = new CampusLocation("LOC-3", "Hostel B", "South", 3, 30);

        heap.insert(new CleaningRequest("REQ-1", "Alice", loc1, "Sanitization", Priority.CRITICAL, LocalDateTime.of(2025, 1, 1, 10, 0)));
        heap.insert(new CleaningRequest("REQ-2", "Bob", loc2, "Lab", Priority.HIGH, LocalDateTime.of(2025, 1, 1, 10, 5)));
        heap.insert(new CleaningRequest("REQ-3", "Carol", loc3, "Room", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 10)));

        assertTrue(heap.updatePriority("REQ-1", Priority.LOW));
        assertTrue(heap.validateHeapProperty());
        assertEquals("REQ-2", heap.peek().getRequestId());
    }

    @Test
    void updatePriority_nonexistent_returnsFalse() {
        assertFalse(heap.updatePriority("REQ-999", Priority.CRITICAL));
    }

    @Test
    void updatePriority_null_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> heap.updatePriority("REQ-1", null));
    }

    @Test
    void contains_returnsCorrectStatus() {
        CampusLocation loc = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        heap.insert(new CleaningRequest("REQ-1", "Alice", loc, "Sanitization", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 0)));
        assertTrue(heap.contains("REQ-1"));
        assertFalse(heap.contains("REQ-2"));
        heap.extractHighestPriority();
        assertFalse(heap.contains("REQ-1"));
    }
}
