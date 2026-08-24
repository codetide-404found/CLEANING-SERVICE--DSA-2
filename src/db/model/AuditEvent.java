package db.model;

public class AuditEvent {
    public int eventId;
    public String eventType;
    public String description;
    public String relatedEntityId;
    public String performedBy;
    public String eventTime;

    public AuditEvent() {}

    public AuditEvent(String eventType, String description, String relatedEntityId, String performedBy, String eventTime) {
        this.eventType = eventType;
        this.description = description;
        this.relatedEntityId = relatedEntityId;
        this.performedBy = performedBy;
        this.eventTime = eventTime;
    }

    @Override
    public String toString() {
        return "AuditEvent{" + eventType + ", " + description + "}";
    }
}
