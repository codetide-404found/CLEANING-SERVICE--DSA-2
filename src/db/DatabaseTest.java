package db;

import db.model.Location;
import db.model.Resource;
import db.model.ServiceRequestRecord;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class DatabaseTest {

    private static final String TEST_DB = "database/test_campus_operations.db";
    private final LocationDAO locationDAO = new LocationDAO();
    private final ServiceRequestDAO requestDAO = new ServiceRequestDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final CsvImportExport csv = new CsvImportExport();

    @BeforeClass
    public static void setupDatabaseFile() throws Exception {
        Files.deleteIfExists(Path.of(TEST_DB));
        DatabaseConnection.setDbPath(TEST_DB);
        DatabaseInitializer.initialize();
    }

    @Before
    public void addBaseLocation() throws SQLException {

        if (locationDAO.findById("L1") == null) {
            locationDAO.insert(new Location("L1", "Test Gate", "Test Area", "Gate", 5.0, -0.1));
        }
    }

    @Test
    public void test01_connectionOpensSuccessfully() throws SQLException {
        try (var conn = DatabaseConnection.getConnection()) {
            assertNotNull(conn);
            assertFalse(conn.isClosed());
        }

    }

    @Test
    public void test02_allSixTablesExist() throws SQLException {
        String[] expected = {"locations", "roads", "service_requests", "resources", "algorithm_runs", "audit_events"};
        try (var conn = DatabaseConnection.getConnection()) {
            var meta = conn.getMetaData();
            for (String table : expected) {
                try (var rs = meta.getTables(null, null, table, null)) {
                    assertTrue("Expected table " + table + " to exist", rs.next());
                }
            }
        }

    }

    @Test
    public void test03_insertLocationSucceeds() throws SQLException {
        locationDAO.insert(new Location("L2", "Test Hostel", "Test Area", "Hostel", 5.01, -0.11));
        Location found = locationDAO.findById("L2");
        assertNotNull(found);
        assertEquals("Test Hostel", found.name);

    }

    @Test
    public void test04_findAllReturnsInsertedRows() throws SQLException {
        List<Location> all = locationDAO.findAll();
        assertTrue(all.size() >= 2);

    }

    @Test
    public void test05_updateLocationChangesName() throws SQLException {
        Location loc = locationDAO.findById("L1");
        loc.name = "Updated Gate Name";
        int rowsAffected = locationDAO.update(loc);
        assertEquals(1, rowsAffected);
        assertEquals("Updated Gate Name", locationDAO.findById("L1").name);

    }

    @Test
    public void test06_deleteLocationRemovesRow() throws SQLException {
        locationDAO.insert(new Location("L_DELETE_ME", "Temp", "Test Area", "Temp", 0, 0));
        assertNotNull(locationDAO.findById("L_DELETE_ME"));
        int rowsAffected = locationDAO.delete("L_DELETE_ME");
        assertEquals(1, rowsAffected);
        assertNull(locationDAO.findById("L_DELETE_ME"));

    }

    @Test
    public void test07_invalidUrgencyIsRejectedByCheckConstraint() throws SQLException {
        ServiceRequestRecord bad = new ServiceRequestRecord("BADREQ1", "L1", "L1", "Cleaning", 99, "2026-08-01T00:00:00", null, "open");
        try {
            requestDAO.insert(bad);
            fail("Expected a SQLException because urgency=99 violates the CHECK(urgency BETWEEN 1 AND 5) constraint");
        } catch (SQLException expected) {
            assertTrue(expected.getMessage().toLowerCase().contains("check"));
        }

    }

    @Test
    public void test07b_unknownForeignKeyIsRejected() throws SQLException {
        ServiceRequestRecord bad = new ServiceRequestRecord("BADREQ2", "NO_SUCH_LOCATION", "L1", "Cleaning", 3, "2026-08-01T00:00:00", null, "open");
        try {
            requestDAO.insert(bad);
            fail("Expected a SQLException because 'source' references a locationId that does not exist");
        } catch (SQLException expected) {
            assertTrue(expected.getMessage().toLowerCase().contains("foreign key"));
        }

    }

    @Test
    public void test08_csvImportReportsInvalidRowsWithoutCrashing() throws IOException {
        Path tempCsv = Path.of("database/temp_bad_locations.csv");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(tempCsv))) {
            pw.println("locationId,name,area,type,latitude,longitude");
            pw.println("L100,Good Row,Area,Type,5.0,-0.1");
            pw.println(",Missing Id,Area,Type,5.0,-0.1");
            pw.println("L101,Bad Lat,Area,Type,notanumber,-0.1");
        }
        CsvImportExport.ImportResult result = csv.importLocations(tempCsv.toString());
        assertEquals(1, result.succeeded);
        assertEquals(2, result.failed);
        Files.deleteIfExists(tempCsv);

    }

    @Test
    public void test09_duplicateLocationIdIsSkippedNotCrashed() throws SQLException {
        Location dup = new Location("L1", "Duplicate Gate", "Test Area", "Gate", 5.0, -0.1);
        try {
            locationDAO.insert(dup);
            fail("Expected a SQLException because locationId L1 already exists (PRIMARY KEY)");
        } catch (SQLException expected) {
            assertTrue(expected.getMessage().toLowerCase().contains("unique") || expected.getMessage().toLowerCase().contains("constraint"));
        }

    }

    @Test
    public void test09b_csvImportSkipsDuplicateIdsCleanly() throws IOException {
        Path tempCsv = Path.of("database/temp_dup_resources.csv");
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(tempCsv))) {
            pw.println("resourceId,type,homeLocation,capacity,availabilityStatus");
            pw.println("RESC_TEST1,Cleaner,L1,1,available");
            pw.println("RESC_TEST1,Cleaner,L1,1,available");
        }
        CsvImportExport.ImportResult result = csv.importResources(tempCsv.toString());
        assertEquals(1, result.succeeded);
        assertEquals(1, result.failed);
        Files.deleteIfExists(tempCsv);
    }

    @Test
    public void test10_dataSurvivesNewConnection() throws SQLException {

        Location loc = locationDAO.findById("L1");
        assertNotNull("Expected L1 to still exist after being committed in an earlier test", loc);
        assertEquals("Updated Gate Name", loc.name);

    }
}
