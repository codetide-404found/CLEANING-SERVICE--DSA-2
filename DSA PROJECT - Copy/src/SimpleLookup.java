import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class SimpleLookup {
    private final BTree<String, String> locationIndex;   // locationId -> name
    private final HashTable<String, String> nameToId;    // name -> locationId
    private final HashTable<String, String> requestIndex; // requestId -> "src->dst|urg|deadline|status"
    private final HashTable<String, String> resourceIndex; // resourceId -> summary

    public SimpleLookup(String dbFile) throws Exception {
        Class.forName("org.sqlite.JDBC");
        String dbUrl = "jdbc:sqlite:" + dbFile;
        locationIndex = new BTree<>(3);
        nameToId = new HashTable<>(503, HashTable.Mode.CHAINING);
        requestIndex = new HashTable<>(4093, HashTable.Mode.LINEAR_PROBING);
        resourceIndex = new HashTable<>(1021, HashTable.Mode.CHAINING);

        try (Connection conn = DriverManager.getConnection(dbUrl)) {
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT locationId, name FROM locations")) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    String name = rs.getString(2);
                    locationIndex.insert(id, name);
                    nameToId.put(name, id);
                }
            }
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT requestId, source, destination, urgency, deadline, status FROM service_requests WHERE category='Cleaning'")) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    String summary = rs.getString(2) + "->" + rs.getString(3)
                            + "|urg=" + rs.getInt(4) + "|deadline=" + rs.getString(5)
                            + "|" + rs.getString(6);
                    requestIndex.put(id, summary);
                }
            }
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("SELECT resourceId, type, homeLocation, capacity, availabilityStatus FROM resources")) {
                while (rs.next()) {
                    String id = rs.getString(1);
                    String label = rs.getString(2) + "@" + rs.getString(3)
                            + " cap=" + rs.getInt(4) + " " + rs.getString(5);
                    resourceIndex.put(id, label);
                }
            }
        }
    }

    public String getLocationIdByName(String name) {
        return nameToId.get(name);
    }

    public String getLocationNameById(String id) {
        return locationIndex.search(id);
    }

    public String getRequestById(String requestId) {
        return requestIndex.get(requestId);
    }

    public String getResourceById(String resourceId) {
        return resourceIndex.get(resourceId);
    }

    // Optional helper to resolve a cleaning job’s endpoints for routing:
    public Map<String, String> getEndpointsForRequest(String requestId) {
        String s = requestIndex.get(requestId);
        if (s == null) {
            return null;
        }
        String pair = s.split("\\|", 2)[0];
        String[] parts = pair.split("->");
        Map<String, String> m = new HashMap<>();
        m.put("source", parts[0]);
        m.put("destination", parts[1]);
        return m;
    }
}
