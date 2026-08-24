package db.model;

public class Road {
    public int roadId;
    public String fromLocationId;
    public String toLocationId;
    public double distance;
    public double travelTime;
    public double roadConditionWeight;

    public Road() {}

    public Road(String fromLocationId, String toLocationId, double distance, double travelTime, double roadConditionWeight) {
        this.fromLocationId = fromLocationId;
        this.toLocationId = toLocationId;
        this.distance = distance;
        this.travelTime = travelTime;
        this.roadConditionWeight = roadConditionWeight;
    }

    @Override
    public String toString() {
        return "Road{" + fromLocationId + "->" + toLocationId + ", dist=" + distance + "}";
    }
}
