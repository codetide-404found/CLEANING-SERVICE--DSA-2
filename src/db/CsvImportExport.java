package db;

import db.model.Location;
import db.model.Resource;
import db.model.Road;
import db.model.ServiceRequestRecord;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CsvImportExport {

    private final LocationDAO locationDAO = new LocationDAO();
    private final RoadDAO roadDAO = new RoadDAO();
    private final ServiceRequestDAO requestDAO = new ServiceRequestDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();

    public static class ImportResult {
        public int succeeded = 0;
        public int failed = 0;
        public List<String> errors = new ArrayList<>();

        @Override
        public String toString() {
            return "Imported " + succeeded + " row(s), " + failed + " failed" +
                    (errors.isEmpty() ? "" : ("\n  - " + String.join("\n  - ", errors)));
        }
    }

    public ImportResult importLocations(String csvPath) throws IOException {
        ImportResult result = new ImportResult();
        List<String[]> rows = readCsv(csvPath);
        for (String[] row : rows) {
            try {
                if (row.length < 6) throw new IllegalArgumentException("expected 6 columns, got " + row.length);
                String id = require(row[0], "locationId");
                String name = require(row[1], "name");
                String area = require(row[2], "area");
                String type = require(row[3], "type");
                double lat = parseDouble(row[4], "latitude");
                double lon = parseDouble(row[5], "longitude");

                if (locationDAO.findById(id) != null) {
                    result.failed++;
                    result.errors.add("duplicate locationId " + id + " - skipped");
                    continue;
                }
                locationDAO.insert(new Location(id, name, area, type, lat, lon));
                result.succeeded++;
            } catch (Exception e) {
                result.failed++;
                result.errors.add(rowLabel(row) + ": " + e.getMessage());
            }
        }
        return result;
    }

    public ImportResult importRoads(String csvPath) throws IOException {
        ImportResult result = new ImportResult();
        List<String[]> rows = readCsv(csvPath);
        for (String[] row : rows) {
            try {
                if (row.length < 5) throw new IllegalArgumentException("expected 5 columns, got " + row.length);
                String from = require(row[0], "fromLocationId");
                String to = require(row[1], "toLocationId");
                double distance = parseDouble(row[2], "distance");
                double travelTime = parseDouble(row[3], "travelTime");
                double weight = parseDouble(row[4], "roadConditionWeight");

                if (locationDAO.findById(from) == null || locationDAO.findById(to) == null) {
                    throw new IllegalArgumentException("references a locationId that does not exist (import locations first)");
                }
                roadDAO.insert(new Road(from, to, distance, travelTime, weight));
                result.succeeded++;
            } catch (Exception e) {
                result.failed++;
                result.errors.add(rowLabel(row) + ": " + e.getMessage());
            }
        }
        return result;
    }

    public ImportResult importServiceRequests(String csvPath) throws IOException {
        ImportResult result = new ImportResult();
        List<String[]> rows = readCsv(csvPath);
        for (String[] row : rows) {
            try {
                if (row.length < 8) throw new IllegalArgumentException("expected 8 columns, got " + row.length);
                String id = require(row[0], "requestId");
                String source = require(row[1], "source");
                String destination = require(row[2], "destination");
                String category = require(row[3], "category");
                int urgency = parseInt(row[4], "urgency");
                if (urgency < 1 || urgency > 5) throw new IllegalArgumentException("urgency must be 1-5, got " + urgency);
                String timeSubmitted = require(row[5], "timeSubmitted");
                String deadline = row[6] == null || row[6].isBlank() ? null : row[6].trim();
                String status = row[7] == null || row[7].isBlank() ? "open" : row[7].trim();

                if (requestDAO.findById(id) != null) {
                    result.failed++;
                    result.errors.add("duplicate requestId " + id + " - skipped");
                    continue;
                }
                requestDAO.insert(new ServiceRequestRecord(id, source, destination, category, urgency, timeSubmitted, deadline, status));
                result.succeeded++;
            } catch (Exception e) {
                result.failed++;
                result.errors.add(rowLabel(row) + ": " + e.getMessage());
            }
        }
        return result;
    }

    public ImportResult importResources(String csvPath) throws IOException {
        ImportResult result = new ImportResult();
        List<String[]> rows = readCsv(csvPath);
        for (String[] row : rows) {
            try {
                if (row.length < 5) throw new IllegalArgumentException("expected 5 columns, got " + row.length);
                String id = require(row[0], "resourceId");
                String type = require(row[1], "type");
                String homeLocation = require(row[2], "homeLocation");
                int capacity = parseInt(row[3], "capacity");
                String status = require(row[4], "availabilityStatus");

                if (locationDAO.findById(homeLocation) == null) {
                    throw new IllegalArgumentException("homeLocation " + homeLocation + " does not exist (import locations first)");
                }
                if (resourceDAO.findById(id) != null) {
                    result.failed++;
                    result.errors.add("duplicate resourceId " + id + " - skipped");
                    continue;
                }
                resourceDAO.insert(new Resource(id, type, homeLocation, capacity, status));
                result.succeeded++;
            } catch (Exception e) {
                result.failed++;
                result.errors.add(rowLabel(row) + ": " + e.getMessage());
            }
        }
        return result;
    }

    public void exportLocations(String outPath) throws IOException, SQLException {
        List<Location> all = locationDAO.findAll();
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Path.of(outPath)))) {
            pw.println("locationId,name,area,type,latitude,longitude");
            for (Location l : all) {
                pw.printf("%s,%s,%s,%s,%f,%f%n", l.locationId, l.name, l.area, l.type, l.latitude, l.longitude);
            }
        }
    }

    public void exportServiceRequests(String outPath) throws IOException, SQLException {
        List<ServiceRequestRecord> all = requestDAO.findAll();
        try (PrintWriter pw = new PrintWriter(Files.newBufferedWriter(Path.of(outPath)))) {
            pw.println("requestId,source,destination,category,urgency,timeSubmitted,deadline,status");
            for (ServiceRequestRecord r : all) {
                pw.printf("%s,%s,%s,%s,%d,%s,%s,%s%n", r.requestId, r.source, r.destination, r.category,
                        r.urgency, r.timeSubmitted, r.deadline == null ? "" : r.deadline, r.status);
            }
        }
    }

    private List<String[]> readCsv(String csvPath) throws IOException {
        List<String[]> rows = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Path.of(csvPath))) {
            String header = br.readLine();
            String line;
            while ((line = br.readLine()) != null) {
                if (line.isBlank()) continue;
                rows.add(line.split(",", -1));
            }
        }
        return rows;
    }

    private String require(String value, String field) {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException("missing required field '" + field + "'");
        }
        return value.trim();
    }

    private double parseDouble(String value, String field) {
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid number for '" + field + "': '" + value + "'");
        }
    }

    private int parseInt(String value, String field) {
        try {
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid integer for '" + field + "': '" + value + "'");
        }
    }

    private String rowLabel(String[] row) {
        return "row[" + String.join(",", row) + "]";
    }
}
