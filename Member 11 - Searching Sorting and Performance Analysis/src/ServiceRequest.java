/**
 * Domain model representing a Ghana Campus Service Request.
 * Implements Comparable for sorting by composite priority score, urgency, or request ID.
 * Incorporates AI-resistance index-number derived parameters for custom score weighting.
 * 
 * Member 11: Searching, Sorting & Performance Analysis
 * Ghana Smart Service Operations Optimizer - University of Ghana
 */
public class ServiceRequest implements Comparable<ServiceRequest> {
    
    // Index number parameter derived from team index numbers (e.g., Member 11 Index parameter)
    public static final int INDEX_PARAM_WEIGHT = 21; 

    private String requestId;
    private int sourceLocationId;
    private int destinationLocationId;
    private String category;
    private int urgency; // 1 (Low) to 5 (Critical)
    private String timeSubmitted;
    private String deadline;
    private String status;
    private double priorityScore;

    public ServiceRequest(String requestId, int sourceLocationId, int destinationLocationId, 
                          String category, int urgency, String timeSubmitted, String deadline, String status) {
        this.requestId = requestId;
        this.sourceLocationId = sourceLocationId;
        this.destinationLocationId = destinationLocationId;
        this.category = category;
        this.urgency = urgency;
        this.timeSubmitted = timeSubmitted;
        this.deadline = deadline;
        this.status = status;
        this.priorityScore = calculatePriorityScore();
    }

    /**
     * Calculates custom priority score derived from urgency and index parameter.
     */
    private double calculatePriorityScore() {
        return (this.urgency * 100.0) + (INDEX_PARAM_WEIGHT * 1.5);
    }

    public String getRequestId() {
        return requestId;
    }

    public int getSourceLocationId() {
        return sourceLocationId;
    }

    public int getDestinationLocationId() {
        return destinationLocationId;
    }

    public String getCategory() {
        return category;
    }

    public int getUrgency() {
        return urgency;
    }

    public String getTimeSubmitted() {
        return timeSubmitted;
    }

    public String getDeadline() {
        return deadline;
    }

    public String getStatus() {
        return status;
    }

    public double getPriorityScore() {
        return priorityScore;
    }

    /**
     * Default comparison orders by urgency descending (highest priority first), then by requestId ascending.
     */
    @Override
    public int compareTo(ServiceRequest other) {
        if (other == null) return 1;
        int cmp = Integer.compare(other.urgency, this.urgency); // High urgency first
        if (cmp != 0) return cmp;
        return this.requestId.compareTo(other.requestId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ServiceRequest other = (ServiceRequest) obj;
        return requestId.equals(other.requestId);
    }

    @Override
    public int hashCode() {
        return requestId.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Request[%s | Urgency:%d | Src:%d | Category:%s | Score:%.1f]",
                requestId, urgency, sourceLocationId, category, priorityScore);
    }

    /**
     * CSV Representation matching project seed files.
     */
    public String toCsvRow() {
        return String.format("%s,%d,%d,%s,%d,%s,%s,%s",
                requestId, sourceLocationId, destinationLocationId, category, urgency, timeSubmitted, deadline, status);
    }
}
