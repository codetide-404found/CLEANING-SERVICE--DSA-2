/**
 * Represents a single campus location in the cleaning service system
 * (e.g., a hall, lecture theatre, hostel block, or office building).
 *
 * This is the key type stored in Member 6's BST and Red-Black Tree,
 * indexed by locationId so the dispatch/routing system (Member 9) and
 * the priority scheduler (Member 5) can look up a location in
 * O(log n) instead of scanning a list of 50+ locations.
 */
public class CampusLocation implements Comparable<CampusLocation> {

    private final String locationId;   // e.g., "UG-CS-01" — unique, sortable code
    private final String name;         // e.g., "Department of Computer Science"
    private final String zone;         // e.g., "North Campus"
    private final int cleaningPriority; // 1 (low) - 5 (high), e.g., toilets/labs > offices
    private final int estimatedMinutes; // time to clean, used by scheduling algorithms

    public CampusLocation(String locationId, String name, String zone,
                           int cleaningPriority, int estimatedMinutes) {
        this.locationId = locationId;
        this.name = name;
        this.zone = zone;
        this.cleaningPriority = cleaningPriority;
        this.estimatedMinutes = estimatedMinutes;
    }

    public String getLocationId() { return locationId; }
    public String getName() { return name; }
    public String getZone() { return zone; }
    public int getCleaningPriority() { return cleaningPriority; }
    public int getEstimatedMinutes() { return estimatedMinutes; }

    /**
     * Locations are ordered by locationId. This makes the BST/RBT effectively
     * an alphanumeric index over campus locations — the exact structure the
     * routing module queries to resolve a stop into full location details.
     */
    @Override
    public int compareTo(CampusLocation other) {
        return this.locationId.compareTo(other.locationId);
    }

    @Override
    public String toString() {
        return locationId + " (" + name + ", " + zone + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CampusLocation other)) return false;
        return locationId.equals(other.locationId);
    }

    @Override
    public int hashCode() {
        return locationId.hashCode();
    }
}
