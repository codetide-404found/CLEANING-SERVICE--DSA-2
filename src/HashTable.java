// HashTable.java
// University of Ghana campus - hash table for fast lookup
// Implements separate chaining and linear-probing open addressing
// Tracks collisions/probe lengths and supports resizing.

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class HashTable<K, V> {
    public enum Mode {CHAINING, LINEAR_PROBING}

    private final Mode mode;
    // chaining
    private List<LinkedList<Entry<K, V>>> tableChain;
    // linear probing
    private Entry<K, V>[] tableProbe;
    private int size = 0;
    private int capacity;
    private final double maxLoadFactor = 0.7;

    // stats
    public long insertCollisions = 0;
    public long totalProbes = 0;
    public long gets = 0;

    @SuppressWarnings("unchecked")
    public HashTable(int initialCapacity, Mode mode) {
        this.mode = mode;
        this.capacity = nextPrime(initialCapacity);
        if (mode == Mode.CHAINING) {
            tableChain = new ArrayList<>(capacity);
            for (int i = 0; i < capacity; i++) tableChain.add(new LinkedList<>());
        } else {
            tableProbe = (Entry<K, V>[]) new Entry[capacity];
        }
    }

    private static class Entry<K, V> {
        K key;
        V value;
        boolean deleted = false;
        Entry(K k, V v) { key = k; value = v; }
    }

    private int hash(Object key) {
        return (Objects.hashCode(key) & 0x7fffffff) % capacity;
    }

    // Separate chaining put/get/remove
    public void putChain(K key, V value) {
        int idx = hash(key);
        LinkedList<Entry<K, V>> bucket = tableChain.get(idx);
        if (!bucket.isEmpty()) insertCollisions++;
        for (Entry<K, V> e : bucket) {
            if (Objects.equals(e.key, key)) { e.value = value; return; }
        }
        bucket.add(new Entry<>(key, value));
        size++;
    }

    public V getChain(K key) {
        gets++;
        int idx = hash(key);
        LinkedList<Entry<K, V>> bucket = tableChain.get(idx);
        for (Entry<K, V> e : bucket) {
            if (Objects.equals(e.key, key)) return e.value;
        }
        return null;
    }

    public void removeChain(K key) {
        int idx = hash(key);
        LinkedList<Entry<K, V>> bucket = tableChain.get(idx);
        boolean removed = bucket.removeIf(e -> Objects.equals(e.key, key));
        if (removed) size--;
    }

    // Linear probing put/get/remove
    public void putProbe(K key, V value) {
        if (size + 1 > capacity * maxLoadFactor) resize();
        int idx = hash(key);
        int probes = 0;
        while (tableProbe[idx] != null && !tableProbe[idx].deleted && !Objects.equals(tableProbe[idx].key, key)) {
            idx = (idx + 1) % capacity;
            probes++;
        }
        totalProbes += probes;
        if (tableProbe[idx] == null || tableProbe[idx].deleted) {
            tableProbe[idx] = new Entry<>(key, value);
            size++;
            if (probes > 0) insertCollisions++;
        } else {
            tableProbe[idx].value = value; // replace
        }
    }

    public V getProbe(K key) {
        gets++;
        int idx = hash(key);
        int probes = 0;
        while (tableProbe[idx] != null) {
            if (!tableProbe[idx].deleted && Objects.equals(tableProbe[idx].key, key)) {
                totalProbes += probes;
                return tableProbe[idx].value;
            }
            idx = (idx + 1) % capacity;
            probes++;
            if (probes > capacity) break;
        }
        totalProbes += probes;
        return null;
    }

    public void removeProbe(K key) {
        int idx = hash(key);
        int probes = 0;
        while (tableProbe[idx] != null) {
            if (!tableProbe[idx].deleted && Objects.equals(tableProbe[idx].key, key)) {
                tableProbe[idx].deleted = true;
                size--;
                return;
            }
            idx = (idx + 1) % capacity;
            probes++;
            if (probes > capacity) break;
        }
    }

    public void put(K key, V value) {
        if (mode == Mode.CHAINING) putChain(key, value);
        else putProbe(key, value);
    }

    public V get(K key) {
        return mode == Mode.CHAINING ? getChain(key) : getProbe(key);
    }

    public void remove(K key) {
        if (mode == Mode.CHAINING) removeChain(key); else removeProbe(key);
    }

    public int size() { return size; }
    public double loadFactor() {
        return mode == Mode.CHAINING ? (double) size / capacity : (double) size / capacity;
    }

    @SuppressWarnings("unchecked")
    private void resize() {
        int newCap = nextPrime(capacity * 2);
        if (mode == Mode.CHAINING) {
            List<LinkedList<Entry<K, V>>> old = tableChain;
            tableChain = new ArrayList<>(newCap);
            for (int i = 0; i < newCap; i++) tableChain.add(new LinkedList<>());
            capacity = newCap;
            size = 0;
            for (LinkedList<Entry<K, V>> bucket : old) {
                for (Entry<K, V> e : bucket) putChain(e.key, e.value);
            }
        } else {
            Entry<K, V>[] old = tableProbe;
            tableProbe = (Entry<K, V>[]) new Entry[newCap];
            capacity = newCap;
            size = 0;
            for (Entry<K, V> e : old) {
                if (e != null && !e.deleted) putProbe(e.key, e.value);
            }
        }
    }

    private int nextPrime(int n) {
        while (true) {
            if (isPrime(n)) return n;
            n++;
        }
    }
    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; i * i <= n; i++) if (n % i == 0) return false;
        return true;
    }

    // simple CSV stats line for experiments
    public String csvStats(String name, int inputSize) {
        return name + "," + mode + "," + capacity + "," + inputSize + "," + size + "," + loadFactor() + "," +
                insertCollisions + "," + totalProbes + "," + gets;
    }
}
