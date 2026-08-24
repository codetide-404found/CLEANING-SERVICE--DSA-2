package db.model;

public class AlgorithmRun {
    public int runId;
    public String algorithmName;
    public int inputSize;
    public long timeNs;
    public long memoryKb;
    public String dateRun;

    public AlgorithmRun() {}

    public AlgorithmRun(String algorithmName, int inputSize, long timeNs, long memoryKb, String dateRun) {
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.timeNs = timeNs;
        this.memoryKb = memoryKb;
        this.dateRun = dateRun;
    }

    @Override
    public String toString() {
        return "AlgorithmRun{" + algorithmName + ", n=" + inputSize + ", " + timeNs + "ns}";
    }
}
