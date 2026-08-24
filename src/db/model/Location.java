package db.model;

public class Location {
    public String locationId;
    public String name;
    public String area;
    public String type;
    public double latitude;
    public double longitude;

    public Location() {}

    public Location(String locationId, String name, String area, String type, double latitude, double longitude) {
        this.locationId = locationId;
        this.name = name;
        this.area = area;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Location{" + locationId + ", " + name + ", " + area + ", " + type + "}";
    }
}
