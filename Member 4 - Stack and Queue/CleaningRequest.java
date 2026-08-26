public class CleaningRequest {

    private final int requestId;
    private final String locationName;
    private final String category;
    private final int urgencyLevel;
    private final String timeSubmitted;
    private String status;

    public CleaningRequest(int requestId, String locationName, String category,
                            int urgencyLevel, String timeSubmitted) {
        this.requestId = requestId;
        this.locationName = locationName;
        this.category = category;
        this.urgencyLevel = urgencyLevel;
        this.timeSubmitted = timeSubmitted;
        this.status = "PENDING";
    }

    public int getRequestId() {
        return requestId;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getCategory() {
        return category;
    }

    public int getUrgencyLevel() {
        return urgencyLevel;
    }

    public String getTimeSubmitted() {
        return timeSubmitted;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Req#" + requestId + " [" + category + " @ " + locationName
                + ", urgency=" + urgencyLevel + ", submitted=" + timeSubmitted
                + ", status=" + status + "]";
    }
}
