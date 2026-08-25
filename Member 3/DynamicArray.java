import java.util.NoSuchElementException;

/**
 * A generic, resizable array-backed list implemented from scratch
 * (no java.util.ArrayList used internally).
 *
 * <p>Growth strategy: capacity doubles whenever the array becomes full,
 * giving amortized O(1) insertion at the end.</p>
 *
 * <p>Shrink strategy: capacity halves once usage falls to a quarter of
 * capacity, but never below {@link #DEFAULT_CAPACITY}, avoiding
 * "thrashing" from alternating insert/remove.</p>
 */
public class DynamicArray<T> {

    private static final int DEFAULT_CAPACITY = 4;

    private Object[] data;
    private int size;
    private int capacity;

    public DynamicArray() {
        this(DEFAULT_CAPACITY);
    }

    public DynamicArray(int initialCapacity) {
        if (initialCapacity <= 0) {
            throw new IllegalArgumentException("Initial capacity must be positive");
        }
        this.capacity = initialCapacity;
        this.data = new Object[capacity];
        this.size = 0;
    }

    public int size() {
        return size;
    }

    public int capacity() {
        return capacity;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    /** Appends value to the end of the array, resizing if necessary. */
    public void insert(T value) {
        ensureCapacityForInsert();
        data[size] = value;
        size++;
    }

    /** Inserts value at the given index, shifting subsequent elements right. */
    public void insert(int index, T value) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        ensureCapacityForInsert();
        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];
        }
        data[index] = value;
        size++;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        checkIndex(index);
        return (T) data[index];
    }

    public void set(int index, T value) {
        checkIndex(index);
        data[index] = value;
    }

    /** Removes and returns the element at index, shifting subsequent elements left. */
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        checkIndex(index);
        T removed = (T) data[index];
        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];
        }
        data[size - 1] = null;
        size--;
        shrinkIfNeeded();
        return removed;
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
    }

    private void ensureCapacityForInsert() {
        if (size == capacity) {
            resize(capacity * 2);
        }
    }

    private void shrinkIfNeeded() {
        if (capacity > DEFAULT_CAPACITY && size <= capacity / 4) {
            resize(Math.max(DEFAULT_CAPACITY, capacity / 2));
        }
    }

    private void resize(int newCapacity) {
        Object[] newData = new Object[newCapacity];
        System.arraycopy(data, 0, newData, 0, size);
        data = newData;
        capacity = newCapacity;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (int i = 0; i < size; i++) {
            sb.append(data[i]);
            if (i < size - 1) sb.append(", ");
        }
        return sb.append("]").toString();
    }
}
