import java.time.LocalDateTime;

public class CleaningRequest implements Comparable<CleaningRequest> {

    private final String requestId;
    private final String customerName;
    private final CampusLocation campusLocation;
    private final String cleaningCategory;
    private Priority priority;
    private final LocalDateTime requestTime;
    private RequestStatus status;

    public CleaningRequest(String requestId, String customerName, CampusLocation campusLocation,
                            String cleaningCategory, Priority priority, LocalDateTime requestTime) {
        if (requestId == null || requestId.isBlank()) {
            throw new IllegalArgumentException("Request ID cannot be null or blank");
        }
        if (customerName == null || customerName.isBlank()) {
            throw new IllegalArgumentException("Customer name cannot be null or blank");
        }
        if (campusLocation == null) {
            throw new IllegalArgumentException("Campus location cannot be null");
        }
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        if (requestTime == null) {
            throw new IllegalArgumentException("Request time cannot be null");
        }

        this.requestId = requestId;
        this.customerName = customerName;
        this.campusLocation = campusLocation;
        this.cleaningCategory = cleaningCategory;
        this.priority = priority;
        this.requestTime = requestTime;
        this.status = RequestStatus.PENDING;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public CampusLocation getCampusLocation() {
        return campusLocation;
    }

    public String getCleaningCategory() {
        return cleaningCategory;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        if (priority == null) {
            throw new IllegalArgumentException("Priority cannot be null");
        }
        this.priority = priority;
    }

    public LocalDateTime getRequestTime() {
        return requestTime;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    @Override
    public int compareTo(CleaningRequest other) {
        int priorityCompare = Integer.compare(this.priority.getLevel(), other.priority.getLevel());
        if (priorityCompare != 0) {
            return priorityCompare;
        }
        int timeCompare = other.requestTime.compareTo(this.requestTime);
        if (timeCompare != 0) {
            return timeCompare;
        }
        return other.requestId.compareTo(this.requestId);
    }

    @Override
    public String toString() {
        return "Request{" +
                "id='" + requestId + '\'' +
                ", customer='" + customerName + '\'' +
                ", location=" + campusLocation.getName() +
                ", category='" + cleaningCategory + '\'' +
                ", priority=" + priority +
                ", time=" + requestTime +
                ", status=" + status +
                '}';
    }
}
