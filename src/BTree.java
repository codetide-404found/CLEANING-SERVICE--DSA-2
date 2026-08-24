import java.util.ArrayList;
import java.util.List;

public class BTree<K extends Comparable<K>, V> {
    private final int t;
    private BNode root;

    public long nodeAccesses = 0;
    public long keyComparisons = 0;
    public List<String> trace = new ArrayList<>();

    public BTree(int t) {
        if (t < 2) {
            throw new IllegalArgumentException("t must be >= 2");
        }
        this.t = t;
        root = new BNode(true);
    }

    private class BNode {
        int n;
        List<K> keys = new ArrayList<>();
        List<V> values = new ArrayList<>();
        List<BNode> children = new ArrayList<>();
        boolean leaf;

        BNode(boolean leaf) {
            this.leaf = leaf;
        }
    }

    public V search(K key) {
        trace.clear();
        return searchNode(root, key);
    }

    private V searchNode(BNode x, K key) {
        nodeAccesses++;
        int i = 0;
        while (i < x.n) {
            keyComparisons++;
            int cmp = key.compareTo(x.keys.get(i));
            if (cmp == 0) {
                trace.add("Found key in node: " + key);
                return x.values.get(i);
            } else if (cmp > 0) {
                i++;
            } else {
                break;
            }
        }
        if (x.leaf) {
            trace.add("Reached leaf; key not found: " + key);
            return null;
        } else {
            trace.add("Descend to child index " + i + " from node with keys " + x.keys);
            return searchNode(x.children.get(i), key);
        }
    }

    private void splitChild(BNode parent, int childIndex) {
        nodeAccesses++;
        BNode y = parent.children.get(childIndex);
        BNode z = new BNode(y.leaf);
        int mid = t - 1;

        K midKey = y.keys.get(mid);
        V midValue = y.values.get(mid);

        List<K> newYKeys = new ArrayList<>();
        List<V> newYValues = new ArrayList<>();
        List<K> newZKeys = new ArrayList<>();
        List<V> newZValues = new ArrayList<>();

        for (int j = 0; j < mid; j++) {
            newYKeys.add(y.keys.get(j));
            newYValues.add(y.values.get(j));
        }
        for (int j = mid + 1; j < y.keys.size(); j++) {
            newZKeys.add(y.keys.get(j));
            newZValues.add(y.values.get(j));
        }

        y.keys = newYKeys;
        y.values = newYValues;
        z.keys = newZKeys;
        z.values = newZValues;

        if (!y.leaf) {
            List<BNode> newYChildren = new ArrayList<>();
            List<BNode> newZChildren = new ArrayList<>();
            for (int j = 0; j <= mid; j++) {
                newYChildren.add(y.children.get(j));
            }
            for (int j = mid + 1; j < y.children.size(); j++) {
                newZChildren.add(y.children.get(j));
            }
            y.children = newYChildren;
            z.children = newZChildren;
        }

        y.n = y.keys.size();
        z.n = z.keys.size();

        parent.children.add(childIndex + 1, z);
        parent.keys.add(childIndex, midKey);
        parent.values.add(childIndex, midValue);
        parent.n = parent.keys.size();

        trace.add("Split child at index " + childIndex + ". Promoted key: " + parent.keys.get(childIndex));
    }

    public void insert(K key, V value) {
        BNode r = root;
        if (r.n == 2 * t - 1) {
            BNode s = new BNode(false);
            s.children.add(r);
            root = s;
            splitChild(s, 0);
            insertNonFull(s, key, value);
        } else {
            insertNonFull(r, key, value);
        }
    }

    private void insertNonFull(BNode x, K key, V value) {
        int i = x.n - 1;
        if (x.leaf) {
            while (i >= 0) {
                keyComparisons++;
                if (key.compareTo(x.keys.get(i)) < 0) {
                    i--;
                } else {
                    break;
                }
            }
            x.keys.add(i + 1, key);
            x.values.add(i + 1, value);
            x.n = x.keys.size();
            trace.add("Inserted key " + key + " into leaf node. Node keys: " + x.keys);
        } else {
            while (i >= 0) {
                keyComparisons++;
                if (key.compareTo(x.keys.get(i)) < 0) {
                    i--;
                } else {
                    break;
                }
            }
            int childIdx = i + 1;
            BNode child = x.children.get(childIdx);
            if (child.n == 2 * t - 1) {
                splitChild(x, childIdx);
                if (key.compareTo(x.keys.get(childIdx)) > 0) {
                    childIdx++;
                }
            }
            insertNonFull(x.children.get(childIdx), key, value);
        }
    }

    public int height() {
        int h = 0;
        BNode x = root;
        while (!x.leaf) {
            h++;
            x = x.children.get(0);
        }
        return h;
    }

    public String dumpRootKeys() {
        return root.keys.toString();
    }
}
