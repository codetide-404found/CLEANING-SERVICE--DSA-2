package db;

import db.model.Location;
import db.model.ServiceRequestRecord;

import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

public class DatabaseSmokeTest {

    private static int pass = 0, fail = 0;

    public static void main(String[] args) throws Exception {
        DatabaseConnection.setDbPath("database/smoke_test.db");
        Files.deleteIfExists(Path.of("database/smoke_test.db"));
        DatabaseInitializer.initialize();

        LocationDAO locationDAO = new LocationDAO();
        ServiceRequestDAO requestDAO = new ServiceRequestDAO();
        CsvImportExport csv = new CsvImportExport();

        try (var conn = DatabaseConnection.getConnection()) {
            check("connection opens", conn != null && !conn.isClosed());
        }

        try (var conn = DatabaseConnection.getConnection()) {
            var meta = conn.getMetaData();
            for (String t : new String[]{"locations", "roads", "service_requests", "resources", "algorithm_runs", "audit_events"}) {
                try (var rs = meta.getTables(null, null, t, null)) {
                    check("table exists: " + t, rs.next());
                }
            }
        }

        locationDAO.insert(new Location("L1", "Test Gate", "Test Area", "Gate", 5.0, -0.1));
        check("insert then findById returns row", locationDAO.findById("L1") != null);

        check("findAll returns >=1 row", locationDAO.findAll().size() >= 1);

        Location loc = locationDAO.findById("L1");
        loc.name = "Updated Gate Name";
        int updated = locationDAO.update(loc);
        check("update affects 1 row", updated == 1);
        check("update persisted", "Updated Gate Name".equals(locationDAO.findById("L1").name));

        locationDAO.insert(new Location("L_DEL", "Temp", "Test Area", "Temp", 0, 0));
        int deleted = locationDAO.delete("L_DEL");
        check("delete affects 1 row", deleted == 1);
        check("delete removes row", locationDAO.findById("L_DEL") == null);

        try {
            requestDAO.insert(new ServiceRequestRecord("BAD1", "L1", "L1", "Cleaning", 99, "2026-08-01T00:00:00", null, "open"));
            check("invalid urgency rejected", false);
        } catch (SQLException e) {
            check("invalid urgency rejected", e.getMessage().toLowerCase().contains("check"));
        }

        try {
            requestDAO.insert(new ServiceRequestRecord("BAD2", "NO_SUCH_LOC", "L1", "Cleaning", 3, "2026-08-01T00:00:00", null, "open"));
            check("invalid foreign key rejected", false);
        } catch (SQLException e) {
            check("invalid foreign key rejected", e.getMessage().toLowerCase().contains("foreign key"));
        }

        Path tempCsv = Path.of("database/temp_bad_locations_smoke.csv");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(tempCsv))) {
            pw.println("locationId,name,area,type,latitude,longitude");
            pw.println("L100,Good Row,Area,Type,5.0,-0.1");
            pw.println(",Missing Id,Area,Type,5.0,-0.1");
            pw.println("L101,Bad Lat,Area,Type,notanumber,-0.1");
        }
        CsvImportExport.ImportResult r = csv.importLocations(tempCsv.toString());
        check("csv import: 1 succeeded", r.succeeded == 1);
        check("csv import: 2 failed", r.failed == 2);
        Files.deleteIfExists(tempCsv);

        try {
            locationDAO.insert(new Location("L1", "Duplicate", "Test Area", "Gate", 5.0, -0.1));
            check("duplicate primary key rejected", false);
        } catch (SQLException e) {
            check("duplicate primary key rejected", true);
        }

        Location reread = locationDAO.findById("L1");
        check("data persists across new connection", reread != null && "Updated Gate Name".equals(reread.name));

        System.out.println("\n" + pass + " passed, " + fail + " failed");
        if (fail > 0) System.exit(1);
    }

    private static void check(String label, boolean condition) {
        if (condition) {
            pass++;
            System.out.println("PASS - " + label);
        } else {
            fail++;
            System.out.println("FAIL - " + label);
        }
    }
}
