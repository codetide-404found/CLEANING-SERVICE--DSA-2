import java.util.List;

/**
 * Trace driver for the campus cleaning service context.
 * Populates the BST and RBT with sample campus locations and demonstrates
 * search, traversal, and balancing on realistic data — not generic integers.
 *
 * INDEX-NUMBER PARAMETER (Section 2 requirement):
 * Replace INDEX_NUMBER below with your actual student index number.
 * We derive the "high cleaning priority threshold" from it, e.g.:
 *   threshold = (last two digits of index number % 3) + 3   -> gives a value 3,4,5
 * This ties the priority cutoff used in queries below to YOUR index number,
 * which the brief requires and which the examiner may ask you to justify.
 */
public class CampusTraceDriver {

    // TODO: replace with your real index number, e.g. 10987654
    private static final int INDEX_NUMBER = 10987654;

    public static void main(String[] args) {
        int priorityThreshold = (INDEX_NUMBER % 100) % 3 + 3;
        System.out.println("Derived priority threshold from index number "
                + INDEX_NUMBER + ": " + priorityThreshold);
        System.out.println();

        // Sample of 8 campus locations out of your team's full 50+ dataset
        CampusLocation[] locations = {
            new CampusLocation("UG-N-01", "Balme Library", "North Campus", 4, 45),
            new CampusLocation("UG-N-02", "Great Hall", "North Campus", 3, 60),
            new CampusLocation("UG-S-01", "Legon Hall", "South Campus", 5, 90),
            new CampusLocation("UG-C-01", "Computer Science Dept.", "Central Campus", 4, 30),
            new CampusLocation("UG-C-02", "JQB Lecture Theatre", "Central Campus", 3, 40),
            new CampusLocation("UG-E-01", "Engineering Block", "East Campus", 4, 50),
            new CampusLocation("UG-W-01", "Sports Complex", "West Campus", 2, 70),
            new CampusLocation("UG-S-02", "Volta Hall", "South Campus", 5, 90),
        };

        // ---------------- BST TRACE ----------------
        System.out.println("=== BST TRACE (campus location index, keyed by locationId) ===");
        BinarySearchTree<CampusLocation> bst = new BinarySearchTree<>();
        for (CampusLocation loc : locations) {
            bst.insert(loc);
            System.out.println("Inserted " + loc.getLocationId());
        }
        System.out.println("In-order (sorted by locationId): ");
        for (CampusLocation loc : bst.inOrder()) {
            System.out.println("  " + loc);
        }
        System.out.println("Tree height: " + bst.height());

        // Simulate a lookup the routing module would perform
        CampusLocation query = new CampusLocation("UG-C-01", "", "", 0, 0);
        System.out.println("\nLookup UG-C-01 found: " + bst.search(query));

        // ---------------- RBT TRACE ----------------
        System.out.println("\n=== RBT TRACE (same dataset, balancing demonstrated) ===");
        RedBlackTree<CampusLocation> rbt = new RedBlackTree<>();
        for (CampusLocation loc : locations) {
            rbt.insert(loc);
        }
        System.out.println("In-order: ");
        for (CampusLocation loc : rbt.inOrder()) {
            System.out.println("  " + loc);
        }
        System.out.println("Tree height: " + rbt.height());

        // ---------------- DOMAIN QUERY USING THE INDEX-NUMBER PARAMETER ----------------
        System.out.println("\n=== High-priority locations (priority >= " + priorityThreshold + ") ===");
        List<CampusLocation> sorted = bst.inOrder();
        for (CampusLocation loc : sorted) {
            if (loc.getCleaningPriority() >= priorityThreshold) {
                System.out.println("  " + loc + " -> priority " + loc.getCleaningPriority());
            }
        }
    }
}
