import db.CsvImportExport;
import db.DatabaseConnection;
import db.DatabaseInitializer;
import db.LocationDAO;
import db.ResourceDAO;
import db.RoadDAO;
import db.ServiceRequestDAO;
import db.model.Location;
import db.model.ServiceRequestRecord;

import java.util.List;

public class DatabaseLoader {

    private final LocationDAO locationDAO = new LocationDAO();
    private final RoadDAO roadDAO = new RoadDAO();
    private final ServiceRequestDAO requestDAO = new ServiceRequestDAO();
    private final ResourceDAO resourceDAO = new ResourceDAO();
    private final CsvImportExport csv = new CsvImportExport();

    public void initializeDatabase() throws Exception {
        DatabaseInitializer.initialize();
    }

    public void loadSeedDataIfEmpty(String seedsFolder) throws Exception {
        initializeDatabase();
        if (locationDAO.count() == 0) {
            System.out.println(csv.importLocations(seedsFolder + "/locations.csv"));
        }
        if (requestDAO.count() == 0) {
            System.out.println(csv.importServiceRequests(seedsFolder + "/service_requests.csv"));
        }

        if (roadDAO.count() == 0) {
            System.out.println(csv.importRoads(seedsFolder + "/roads.csv"));
        }
        if (resourceDAO.count() == 0) {
            System.out.println(csv.importResources(seedsFolder + "/resources.csv"));
        }
    }

    public void loadLocationsIntoStructures(BTree<String, String> btree, HashTable<String, String> hashTable) throws Exception {
        List<Location> locations = locationDAO.findAll();
        for (Location loc : locations) {
            btree.insert(loc.locationId, loc.name);
            hashTable.put(loc.locationId, loc.name);
        }
    }

    public List<ServiceRequestRecord> loadOpenServiceRequests() throws Exception {
        return requestDAO.findByStatus("open");
    }

    public static void main(String[] args) throws Exception {
        String seeds = args.length > 0 ? args[0] : "seeds";
        DatabaseLoader loader = new DatabaseLoader();
        loader.loadSeedDataIfEmpty(seeds);

        BTree<String, String> btree = new BTree<>(3);
        HashTable<String, String> hashTable = new HashTable<>(101, HashTable.Mode.CHAINING);
        loader.loadLocationsIntoStructures(btree, hashTable);

        System.out.println("Locations loaded into B-tree, height=" + btree.height());
        System.out.println("Open service requests ready for dispatch: " + loader.loadOpenServiceRequests().size());
        System.out.println("Database file: " + DatabaseConnection.getDbPath());
    }
}
