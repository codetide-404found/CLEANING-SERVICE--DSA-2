`import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    // CSV columns: locationId,name,area,type,latitude,longitude
    static class Loc {
        final String id, name, area, type;
        final double lat, lon;
        Loc(String id, String name, String area, String type, double lat, double lon) {
            this.id=id; this.name=name; this.area=area; this.type=type; this.lat=lat; this.lon=lon;
        }
    }
    public static void main(String[] args) throws Exception {
    String csv = (args.length > 0) ? args[0] : "seeds/locations.csv";
    List<Loc> locs = loadLocations(csv);

    // Indexes
    BTree<String,String> btreeById = new BTree<>(3);              // id -> name
    HashTable<String,String> hashIdChain = new HashTable<>(nextPrime(locs.size()*2), HashTable.Mode.CHAINING);
    HashTable<String,String> hashIdProbe = new HashTable<>(nextPrime(locs.size()*2), HashTable.Mode.LINEAR_PROBING);
    HashTable<String,String> hashNameToId = new HashTable<>(nextPrime(locs.size()*2), HashTable.Mode.CHAINING); // name -> id

    // Build
    for (Loc L : locs) {
        btreeById.insert(L.id, L.name);
        hashIdChain.put(L.id, L.name);
        hashIdProbe.put(L.id, L.name);
        hashNameToId.put(L.name, L.id);
    }

    // Demo searches (by ID via B-tree, by name via hash)
    String demoId = locs.get(Math.min(10, locs.size()-1)).id;
    String demoName = locs.get(Math.min(15, locs.size()-1)).name;

    String r1 = btreeById.search(demoId);
    System.out.println("[B-Tree] search id=" + demoId + " -> " + r1);
    for (String s : btreeById.trace) System.out.println("  " + s);
    System.out.println("B-Tree height=" + btreeById.height() +
            " nodeAccesses=" + btreeById.nodeAccesses + " keyComparisons=" + btreeById.keyComparisons);

    System.out.println("\n[Hash CHAINING] get id=" + demoId + " -> " + hashIdChain.get(demoId));
    System.out.println("Stats: " + hashIdChain.csvStats("UG_Locations", locs.size()));
    System.out.println("[Hash PROBING]  get id=" + demoId + " -> " + hashIdProbe.get(demoId));
    System.out.println("Stats: " + hashIdProbe.csvStats("UG_Locations", locs.size()));
    System.out.println("[Hash name->id] get name=\"" + demoName + "\" -> " + hashNameToId.get(demoName));

    // Performance: random search benchmarking
    int[] sizes = {100, 500, 1000, Math.min(5000, locs.size())};
    benchmarkSearches(locs, btreeById, hashIdChain, hashIdProbe, sizes);

    // Collision behavior vs load factor (rebuild with different capacities)
    collisionStudy(locs, new double[]{0.1,0.3,0.5,0.7,0.9});
    }

static List<Loc> loadLocations(String csvPath) throws Exception {
    List<Loc> out = new ArrayList<>();
    try (BufferedReader br = Files.newBufferedReader(Paths.get(csvPath))) {
        String header = br.readLine(); // skip
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split(",", -1);
            if (p.length < 6) continue;
            out.add(new Loc(p[0].trim(), p[1].trim(), p[2].trim(), p[3].trim(),
                    safeD(p[4]), safeD(p[5])));
        }
    }
    return out;
}

static double safeD(String s){ try { return Double.parseDouble(s); } catch(Exception e){ return 0.0; } }

static void benchmarkSearches(List<Loc> locs,
                              BTree<String,String> btree,
                              HashTable<String,String> chain,
                              HashTable<String,String> probe,
                              int[] sizes) {
    Random rnd = new Random(42);
    System.out.println("\n=== Search performance (avg ns over 10k gets) ===");
    for (int N : sizes) {
        if (N > locs.size()) continue;
        // sample 10k queries from first N
        int Q = 10_000;
        String[] keys = new String[Q];
        for (int i=0;i<Q;i++) keys[i] = locs.get(rnd.nextInt(N)).id;

        long t0 = System.nanoTime();
        for (String k: keys) btree.search(k);
        long t1 = System.nanoTime();
        long btNs = t1 - t0;

        t0 = System.nanoTime();
        for (String k: keys) chain.get(k);
        t1 = System.nanoTime();
        long chNs = t1 - t0;

        t0 = System.nanoTime();
        for (String k: keys) probe.get(k);
        t1 = System.nanoTime();
        long prNs = t1 - t0;

        System.out.printf("N=%d | BTree avg=%.1f ns | Chain avg=%.1f ns | Probe avg=%.1f ns%n",
                N, btNs/(double)Q, chNs/(double)Q, prNs/(double)Q);
    }
}

static void collisionStudy(List<Loc> locs, double[] loadTargets) {
    System.out.println("\n=== Collision study (insert " + Math.min(10000, locs.size()) + " ids) ===");
    int nKeys = Math.min(10000, locs.size());
    List<String> keys = new ArrayList<>(nKeys);
    for (int i=0;i<nKeys;i++) keys.add(locs.get(i).id);

    for (HashTable.Mode mode : new HashTable.Mode[]{HashTable.Mode.CHAINING, HashTable.Mode.LINEAR_PROBING}) {
        for (double lf : loadTargets) {
            int cap = Math.max(11, (int)Math.ceil(nKeys/lf));
            HashTable<String,String> ht = new HashTable<>(cap, mode);
            for (int i=0;i<nKeys;i++) ht.put(keys.get(i), "N");
            System.out.printf("%s lf≈%.1f cap=%d n=%d | collisions=%d | totalProbes=%d | load=%.3f%n",
                    mode, lf, cap, nKeys, ht.insertCollisions, ht.totalProbes, ht.loadFactor());
        }
    }
}

static int nextPrime(int n) { while (true) { if (isPrime(n)) return n; n++; } }
static boolean isPrime(int n) { if (n < 2) return false; for (int i = 2; i * i <= n; i++) if (n % i == 0) return false; return true; }
}
