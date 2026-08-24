import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ExperimentRunner {

    public static void main(String[] args) throws Exception {
        String dbFile = args.length > 0 ? args[0] : "ug.db";
        String locationsCsv = args.length > 1 ? args[1] : "seeds/locations.csv";
        DBLoader loader = new DBLoader(dbFile);

    List<String> ids = new ArrayList<>();
    List<String> names = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(locationsCsv))) {
        String header = br.readLine();
        String line;
        while ((line = br.readLine()) != null) {
            String[] p = line.split(",", -1);
            if (p.length >= 2) {
                ids.add(p[0].trim());
                names.add(p[1].trim());
            }
        }
    }

    BTree<String,String> btree = new BTree<>(3);
    HashTable<String,String> htChain = new HashTable<>(nextPrime(Math.max(11, ids.size()*2)), HashTable.Mode.CHAINING);
    HashTable<String,String> htProbe = new HashTable<>(nextPrime(Math.max(11, ids.size()*2)), HashTable.Mode.LINEAR_PROBING);

    long t0 = System.nanoTime();
    for (int i=0;i<ids.size();i++) btree.insert(ids.get(i), names.get(i));
    long t1 = System.nanoTime();
    loader.recordRun("BTree_insert_locations_t=3", ids.size(), (t1-t0), usedMemoryKb());

    t0 = System.nanoTime();
    for (int i=0;i<ids.size();i++) htChain.put(ids.get(i), names.get(i));
    t1 = System.nanoTime();
    loader.recordRun("Hash_CHAINING_insert_locations", ids.size(), (t1-t0), usedMemoryKb());

    t0 = System.nanoTime();
    for (int i=0;i<ids.size();i++) htProbe.put(ids.get(i), names.get(i));
    t1 = System.nanoTime();
    loader.recordRun("Hash_PROBING_insert_locations", ids.size(), (t1-t0), usedMemoryKb());

    Random rnd = new Random(42);
    int Q = Math.min(10_000, Math.max(1_000, ids.size()));
    String[] queryIds = new String[Q];
    for (int i=0;i<Q;i++) queryIds[i] = ids.get(rnd.nextInt(ids.size()));

    t0 = System.nanoTime();
    for (String k: queryIds) btree.search(k);
    t1 = System.nanoTime();
    loader.recordRun("BTree_search_locations_t=3", Q, (t1-t0), usedMemoryKb());

    t0 = System.nanoTime();
    for (String k: queryIds) htChain.get(k);
    t1 = System.nanoTime();
    loader.recordRun("Hash_CHAINING_get_locations", Q, (t1-t0), usedMemoryKb());

    t0 = System.nanoTime();
    for (String k: queryIds) htProbe.get(k);
    t1 = System.nanoTime();
    loader.recordRun("Hash_PROBING_get_locations", Q, (t1-t0), usedMemoryKb());

    double[] loadTargets = {0.1, 0.3, 0.5, 0.7, 0.9};
    int nKeys = Math.min(10000, ids.size());
    List<String> keys = ids.subList(0, nKeys);
    for (HashTable.Mode mode : new HashTable.Mode[]{HashTable.Mode.CHAINING, HashTable.Mode.LINEAR_PROBING}) {
        for (double lf : loadTargets) {
            int cap = Math.max(11, (int)Math.ceil(nKeys/lf));
            HashTable<String,String> ht = new HashTable<>(cap, mode);
            long start = System.nanoTime();
            for (int i=0;i<nKeys;i++) ht.put(keys.get(i), "N");
            long insertNs = System.nanoTime() - start;
            loader.recordRun("Hash_"+mode+"_insert_lf="+lf, nKeys, insertNs, usedMemoryKb());

            System.out.println(mode+" lf~"+lf+" cap="+cap+" n="+nKeys+" collisions="+ht.insertCollisions+" probes="+ht.totalProbes);
        }
    }
    System.out.println("Experiment runs recorded to algorithm_runs.");
}

private static long usedMemoryKb() {
    Runtime rt = Runtime.getRuntime();
    return (rt.totalMemory() - rt.freeMemory()) / 1024;
}
private static int nextPrime(int n){ while(true){ if(isPrime(n)) return n; n++; } }
private static boolean isPrime(int n){ if(n<2) return false; for(int i=2;i*i<=n;i++) if(n%i==0) return false; return true; }
}
