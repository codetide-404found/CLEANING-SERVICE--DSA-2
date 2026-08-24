package db.model;

public class Resource {
    public String resourceId;
    public String type;
    public String homeLocation;
    public int capacity;
    public String availabilityStatus;

    public Resource() {}

    public Resource(String resourceId, String type, String homeLocation, int capacity, String availabilityStatus) {
        this.resourceId = resourceId;
        this.type = type;
        this.homeLocation = homeLocation;
        this.capacity = capacity;
        this.availabilityStatus = availabilityStatus;
    }

    @Override
    public String toString() {
        return "Resource{" + resourceId + ", " + type + ", " + availabilityStatus + "}";
    }
}
