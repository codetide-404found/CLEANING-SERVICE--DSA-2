import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class PriorityQueueManagerTest {

    private PriorityQueueManager manager;

    @BeforeEach
    void setUp() {
        manager = new PriorityQueueManager();
    }

    @Test
    void emptyManager_isEmptyAndSizeZero() {
        assertTrue(manager.isEmpty());
        assertEquals(0, manager.size());
    }

    @Test
    void addRequest_increasesSize() {
        CampusLocation loc = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc, "Sanitization", Priority.LOW, LocalDateTime.now()));
        assertFalse(manager.isEmpty());
        assertEquals(1, manager.size());
    }

    @Test
    void getNextRequest_returnsHighestPriority() {
        CampusLocation loc1 = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CampusLocation loc2 = new CampusLocation("LOC-2", "Engineering Lab", "East", 4, 45);

        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc1, "Sanitization", Priority.LOW, LocalDateTime.now()));
        manager.addRequest(new CleaningRequest("REQ-2", "Bob", loc2, "Lab", Priority.CRITICAL, LocalDateTime.now()));

        assertEquals("REQ-2", manager.getNextRequest().getRequestId());
    }

    @Test
    void getNextRequest_onEmpty_throwsException() {
        assertThrows(IllegalStateException.class, () -> manager.getNextRequest());
    }

    @Test
    void peekNextRequest_doesNotRemove() {
        CampusLocation loc = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc, "Sanitization", Priority.LOW, LocalDateTime.now()));
        assertEquals("REQ-1", manager.peekNextRequest().getRequestId());
        assertEquals(1, manager.size());
    }

    @Test
    void updateRequestPriority_changesOrder() {
        CampusLocation loc1 = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CampusLocation loc2 = new CampusLocation("LOC-2", "Engineering Lab", "East", 4, 45);

        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc1, "Sanitization", Priority.LOW, LocalDateTime.now()));
        manager.addRequest(new CleaningRequest("REQ-2", "Bob", loc2, "Lab", Priority.LOW, LocalDateTime.now()));

        assertTrue(manager.updateRequestPriority("REQ-1", Priority.CRITICAL));
        assertEquals("REQ-1", manager.peekNextRequest().getRequestId());
        assertTrue(manager.validateHeapProperty());
    }

    @Test
    void updateRequestPriority_nonexistent_returnsFalse() {
        assertFalse(manager.updateRequestPriority("REQ-999", Priority.CRITICAL));
    }

    @Test
    void addNullRequest_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> manager.addRequest(null));
    }

    @Test
    void tieBreaking_earlierTimeFirst() {
        CampusLocation loc1 = new CampusLocation("LOC-1", "Hostel B", "South", 3, 30);
        CampusLocation loc2 = new CampusLocation("LOC-2", "Admin Block", "Central", 3, 25);

        manager.addRequest(new CleaningRequest("REQ-1", "Alice", loc1, "Room", Priority.MEDIUM, LocalDateTime.of(2025, 1, 1, 10, 10)));
        manager.addRequest(new CleaningRequest("REQ-2", "Bob", loc2, "Office", Priority.MEDIUM, LocalDateTime.of(2025, 1, 1, 9, 0)));

        assertEquals("REQ-2", manager.getNextRequest().getRequestId());
    }

    @Test
    void fullDispatchOrder_matchesExpected() {
        CampusLocation medical = new CampusLocation("LOC-1", "Medical Centre", "North", 5, 60);
        CampusLocation engineering = new CampusLocation("LOC-2", "Engineering Lab", "East", 4, 45);
        CampusLocation hostel = new CampusLocation("LOC-3", "Hostel B", "South", 3, 30);
        CampusLocation admin = new CampusLocation("LOC-4", "Admin Block", "Central", 3, 25);
        CampusLocation library = new CampusLocation("LOC-5", "Library", "West", 2, 20);

        manager.addRequest(new CleaningRequest("R1", "Alice", medical, "Sanitization", Priority.CRITICAL, LocalDateTime.of(2025, 1, 1, 10, 0)));
        manager.addRequest(new CleaningRequest("R2", "Bob", engineering, "Lab", Priority.HIGH, LocalDateTime.of(2025, 1, 1, 10, 5)));
        manager.addRequest(new CleaningRequest("R3", "Carol", hostel, "Room", Priority.MEDIUM, LocalDateTime.of(2025, 1, 1, 10, 10)));
        manager.addRequest(new CleaningRequest("R4", "Dave", admin, "Office", Priority.MEDIUM, LocalDateTime.of(2025, 1, 1, 9, 0)));
        manager.addRequest(new CleaningRequest("R5", "Eve", library, "General", Priority.LOW, LocalDateTime.of(2025, 1, 1, 10, 15)));

        assertEquals("R1", manager.getNextRequest().getRequestId());
        assertEquals("R2", manager.getNextRequest().getRequestId());
        assertEquals("R4", manager.getNextRequest().getRequestId());
        assertEquals("R3", manager.getNextRequest().getRequestId());
        assertEquals("R5", manager.getNextRequest().getRequestId());
        assertTrue(manager.isEmpty());
    }
}
