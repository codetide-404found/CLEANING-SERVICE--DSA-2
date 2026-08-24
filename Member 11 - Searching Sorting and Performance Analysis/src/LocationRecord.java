/**
 * Domain model representing a Ghanaian Campus Location entity.
 * 
 * Member 11: Searching, Sorting & Performance Analysis
 * Ghana Smart Service Operations Optimizer - University of Ghana
 */
public class LocationRecord implements Comparable<LocationRecord> {
    private int locationId;
    private String name;
    private String area;
    private String type;
    private double latitude;
    private double longitude;

    public LocationRecord(int locationId, String name, String area, String type, double latitude, double longitude) {
        this.locationId = locationId;
        this.name = name;
        this.area = area;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public int getLocationId() {
        return locationId;
    }

    public String getName() {
        return name;
    }

    public String getArea() {
        return area;
    }

    public String getType() {
        return type;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public int compareTo(LocationRecord other) {
        if (other == null) return 1;
        return Integer.compare(this.locationId, other.locationId);
    }

    @Override
    public String toString() {
        return String.format("Location[%d: %s (%s)]", locationId, name, area);
    }
}
