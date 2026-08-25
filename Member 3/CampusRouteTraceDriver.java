/**
 * Runnable demo using real campus cleaning-service location data plus an
 * index-number-derived parameter. Reuses the exact same sample dataset as
 * CampusTraceDriver.java (BST/RBT module) so both demos tell one consistent
 * story when presented together.
 *
 * <p>Here, DynamicArray holds the full location catalog (the flat roster
 * that Member 6's BST/RBT indexes by locationId), and MyLinkedList models
 * a single cleaning crew's ordered route for the day — stops can be
 * appended, inserted mid-route for a rush job, or removed for a
 * cancellation, all in O(1) once the target node is found.</p>
 */
public class CampusRouteTraceDriver {

    // Derived initial DynamicArray capacity — a required index-derived
    // parameter (Section 2.iii), distinct in purpose from Member 6's
    // priorityThreshold parameter in CampusTraceDriver.java.
    private static final int INDEX_NUMBER = 22241883;

    public static void main(String[] args) {
        int derivedInitialCapacity = 4 + (INDEX_NUMBER % 5);
        System.out.println("Derived initial DynamicArray capacity from index number "
                + INDEX_NUMBER + ": 4 + (" + INDEX_NUMBER + " % 5) = " + derivedInitialCapacity);
        System.out.println();

        // Same 8-location sample used in CampusTraceDriver.java (BST/RBT module)
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

        System.out.println("=== DYNAMIC ARRAY: full location catalog ===");
        DynamicArray<CampusLocation> catalog = new DynamicArray<>(derivedInitialCapacity);
        for (CampusLocation loc : locations) {
            int before = catalog.capacity();
            catalog.insert(loc);
            int after = catalog.capacity();
            String note = (before != after) ? "  <-- RESIZED (" + before + " -> " + after + ")" : "";
            System.out.println("Inserted " + loc.getLocationId() + " -> size=" + catalog.size()
                    + ", capacity=" + after + note);
        }

        System.out.println("\nCatalog by index (insertion order, not sorted):");
        for (int i = 0; i < catalog.size(); i++) {
            System.out.println("  [" + i + "] " + catalog.get(i));
        }

        System.out.println("\n=== MYLINKEDLIST: today's cleaning crew route ===");
        MyLinkedList<CampusLocation> route = new MyLinkedList<>();

        route.addLast(locations[0]); // Balme Library
        route.addLast(locations[2]); // Legon Hall
        route.addLast(locations[3]); // Computer Science Dept.
        System.out.println("Initial route: " + route);

        route.addFirst(locations[7]); // Volta Hall - urgent priority 5, add to front
        System.out.println("After addFirst(Volta Hall) [urgent, priority 5]: " + route);

        route.insertAfter(locations[2], locations[4]); // insert JQB after Legon Hall — rush job
        System.out.println("After insertAfter(Legon Hall, JQB) [rush job]: " + route);

        route.remove(locations[3]); // Computer Science Dept. cancelled for today
        System.out.println("After remove(Computer Science Dept.) [cancelled]: " + route);

        System.out.print("\nFinal route via iterator: ");
        StringBuilder sb = new StringBuilder();
        int totalMinutes = 0;
        for (CampusLocation stop : route) {
            sb.append(stop.getLocationId()).append(" -> ");
            totalMinutes += stop.getEstimatedMinutes();
        }
        if (sb.length() > 4) sb.setLength(sb.length() - 4);
        System.out.println(sb);
        System.out.println("Total estimated cleaning time for this route: " + totalMinutes + " minutes");
    }
}
