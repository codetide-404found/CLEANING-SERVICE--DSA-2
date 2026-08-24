/**
 * Operation metrics container for Sorting Algorithms.
 * Tracks comparisons, swaps/shifts, recursion depth, runtime, and memory usage.
 * 
 * Member 11: Searching, Sorting & Performance Analysis
 * Ghana Smart Service Operations Optimizer - University of Ghana
 */
public class SortMetrics {
    private String algorithmName;
    private int inputSize;
    private long comparisons;
    private long swaps;
    private long shiftsOrCopies;
    private int maxRecursionDepth;
    private long executionTimeNs;
    private long memoryUsedKb;

    public SortMetrics(String algorithmName, int inputSize) {
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.comparisons = 0;
        this.swaps = 0;
        this.shiftsOrCopies = 0;
        this.maxRecursionDepth = 0;
        this.executionTimeNs = 0;
        this.memoryUsedKb = 0;
    }

    public void incrementComparisons() {
        this.comparisons++;
    }

    public void addComparisons(long count) {
        this.comparisons += count;
    }

    public void incrementSwaps() {
        this.swaps++;
    }

    public void addSwaps(long count) {
        this.swaps += count;
    }

    public void incrementShiftsOrCopies() {
        this.shiftsOrCopies++;
    }

    public void addShiftsOrCopies(long count) {
        this.shiftsOrCopies += count;
    }

    public void updateRecursionDepth(int depth) {
        if (depth > this.maxRecursionDepth) {
            this.maxRecursionDepth = depth;
        }
    }

    public long getComparisons() {
        return comparisons;
    }

    public long getSwaps() {
        return swaps;
    }

    public long getShiftsOrCopies() {
        return shiftsOrCopies;
    }

    public int getMaxRecursionDepth() {
        return maxRecursionDepth;
    }

    public long getExecutionTimeNs() {
        return executionTimeNs;
    }

    public double getExecutionTimeMs() {
        return executionTimeNs / 1_000_000.0;
    }

    public void setExecutionTimeNs(long executionTimeNs) {
        this.executionTimeNs = executionTimeNs;
    }

    public long getMemoryUsedKb() {
        return memoryUsedKb;
    }

    public void setMemoryUsedKb(long memoryUsedKb) {
        this.memoryUsedKb = memoryUsedKb;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public int getInputSize() {
        return inputSize;
    }

    @Override
    public String toString() {
        return String.format("%s [N=%d] -> Comparisons: %d, Swaps: %d, Shifts/Copies: %d, MaxDepth: %d, Time: %.4f ms (%d ns), Memory: %d KB",
                algorithmName, inputSize, comparisons, swaps, shiftsOrCopies, maxRecursionDepth, getExecutionTimeMs(), executionTimeNs, memoryUsedKb);
    }

    public String toCsvRow() {
        return String.format("%s,%d,%d,%d,%d,%d,%.4f,%d",
                algorithmName, inputSize, comparisons, swaps, shiftsOrCopies, maxRecursionDepth, getExecutionTimeMs(), memoryUsedKb);
    }
}
