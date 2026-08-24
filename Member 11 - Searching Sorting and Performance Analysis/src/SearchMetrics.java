/**
 * Operation metrics container for Searching Algorithms.
 * Tracks comparison count, target index found, execution time, and memory overhead.
 * 
 * Member 11: Searching, Sorting & Performance Analysis
 * Ghana Smart Service Operations Optimizer - University of Ghana
 */
public class SearchMetrics {
    private String algorithmName;
    private int inputSize;
    private long comparisons;
    private int targetIndex;
    private long executionTimeNs;
    private long memoryUsedKb;

    public SearchMetrics(String algorithmName, int inputSize) {
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.comparisons = 0;
        this.targetIndex = -1;
        this.executionTimeNs = 0;
        this.memoryUsedKb = 0;
    }

    public void incrementComparisons() {
        this.comparisons++;
    }

    public void addComparisons(long count) {
        this.comparisons += count;
    }

    public long getComparisons() {
        return comparisons;
    }

    public int getTargetIndex() {
        return targetIndex;
    }

    public void setTargetIndex(int targetIndex) {
        this.targetIndex = targetIndex;
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
        return String.format("%s [N=%d] -> Index: %d, Comparisons: %d, Time: %.4f ms (%d ns), Memory: %d KB",
                algorithmName, inputSize, targetIndex, comparisons, getExecutionTimeMs(), executionTimeNs, memoryUsedKb);
    }

    public String toCsvRow() {
        return String.format("%s,%d,%d,%d,%.4f,%d",
                algorithmName, inputSize, comparisons, targetIndex, getExecutionTimeMs(), memoryUsedKb);
    }
}
