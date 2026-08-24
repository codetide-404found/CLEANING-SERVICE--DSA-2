package db.model;

public class ServiceRequestRecord {
    public String requestId;
    public String source;
    public String destination;
    public String category;
    public int urgency;
    public String timeSubmitted;
    public String deadline;
    public String status;

    public ServiceRequestRecord() {}

    public ServiceRequestRecord(String requestId, String source, String destination, String category,
                                 int urgency, String timeSubmitted, String deadline, String status) {
        this.requestId = requestId;
        this.source = source;
        this.destination = destination;
        this.category = category;
        this.urgency = urgency;
        this.timeSubmitted = timeSubmitted;
        this.deadline = deadline;
        this.status = status;
    }

    @Override
    public String toString() {
        return "ServiceRequestRecord{" + requestId + ", " + category + ", urgency=" + urgency + ", " + status + "}";
    }
}
