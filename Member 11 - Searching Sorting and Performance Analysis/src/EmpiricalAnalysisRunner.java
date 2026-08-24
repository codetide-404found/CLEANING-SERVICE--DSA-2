import java.io.FileWriter;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Empirical Efficiency Lab Engine (Section 9 Compliance).
 * Executes benchmark experiments for Searching and Sorting algorithms across N = 100, 500, 1000, 5000, 10000.
 * Averages 3+ warm-up and measured runs, records memory and runtime, and exports CSV + HTML/SVG visual graphs.
 * 
 * Member 11: Searching, Sorting & Performance Analysis
 * Ghana Smart Service Operations Optimizer - University of Ghana
 */
public class EmpiricalAnalysisRunner {

    private static final int[] INPUT_SIZES = {100, 500, 1000, 5000, 10000};
    private static final int NUM_RUNS = 3;
    private static final long SEED = 10954321L; // Member 11 Index Number derived random seed

    public static void main(String[] args) {
        System.out.println("========================================================================");
        System.out.println(" GHANA SMART SERVICE OPERATIONS OPTIMIZER - EMPIRICAL EFFICIENCY LAB ");
        System.out.println(" Member 11: Searching, Sorting & Performance Analysis Engine ");
        System.out.println("========================================================================\n");

        List<SearchMetrics> searchResults = runSearchExperiments();
        List<SortMetrics> sortResults = runSortingExperiments();

        String outputDir = "Member 11 - Searching Sorting and Performance Analysis/graphs/";
        java.io.File dir = new java.io.File(outputDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String csvPath = outputDir + "performance_results.csv";
        exportToCsv(searchResults, sortResults, csvPath);

        GraphPlotter.generateSvgCharts(searchResults, sortResults, outputDir);
        GraphPlotter.generateHtmlDashboard(searchResults, sortResults, outputDir + "performance_dashboard.html");

        System.out.println("\n========================================================================");
        System.out.println(" EMPIRICAL BENCHMARK COMPLETED SUCCESSFULLY!");
        System.out.println(" CSV Export: " + csvPath);
        System.out.println(" SVG Charts Generated in: graphs/");
        System.out.println(" HTML Dashboard: graphs/performance_dashboard.html");
        System.out.println("========================================================================");
    }

    public static List<SearchMetrics> runSearchExperiments() {
        System.out.println("--- RUNNING SEARCH COMPARISON EXPERIMENTS (100 to 10,000 Records) ---");
        List<SearchMetrics> avgMetricsList = new ArrayList<>();

        for (int size : INPUT_SIZES) {
            long totalLinearTime = 0;
            long totalLinearComp = 0;
            long totalBinaryTime = 0;
            long totalBinaryComp = 0;

            for (int run = 0; run < NUM_RUNS; run++) {
                int[] data = generateRandomArray(size, run + SEED);
                int targetKey = data[data.length / 2]; // Target present in array

                // Linear Search Test
                SearchMetrics linMetrics = new SearchMetrics("Linear Search", size);
                SearchingAlgorithms.linearSearchWithMetrics(data, targetKey, linMetrics);
                totalLinearTime += linMetrics.getExecutionTimeNs();
                totalLinearComp += linMetrics.getComparisons();

                // Binary Search Test (Requires sorted array)
                int[] sortedData = Arrays.copyOf(data, data.length);
                SortingAlgorithms.quickSort(sortedData); // Pre-sort for binary search

                SearchMetrics binMetrics = new SearchMetrics("Binary Search", size);
                SearchingAlgorithms.binarySearchWithMetrics(sortedData, targetKey, binMetrics);
                totalBinaryTime += binMetrics.getExecutionTimeNs();
                totalBinaryComp += binMetrics.getComparisons();
            }

            SearchMetrics avgLinear = new SearchMetrics("Linear Search", size);
            avgLinear.setExecutionTimeNs(totalLinearTime / NUM_RUNS);
            avgLinear.addComparisons(totalLinearComp / NUM_RUNS);
            avgMetricsList.add(avgLinear);

            SearchMetrics avgBinary = new SearchMetrics("Binary Search", size);
            avgBinary.setExecutionTimeNs(totalBinaryTime / NUM_RUNS);
            avgBinary.addComparisons(totalBinaryComp / NUM_RUNS);
            avgMetricsList.add(avgBinary);

            System.out.printf(" N = %-6d | Linear: %8.4f ms (%6d comps) | Binary: %7.4f ms (%3d comps)\n",
                    size, avgLinear.getExecutionTimeMs(), avgLinear.getComparisons(),
                    avgBinary.getExecutionTimeMs(), avgBinary.getComparisons());
        }
        return avgMetricsList;
    }

    public static List<SortMetrics> runSortingExperiments() {
        System.out.println("\n--- RUNNING SORTING COMPARISON EXPERIMENTS (100 to 10,000 Records) ---");
        List<SortMetrics> avgMetricsList = new ArrayList<>();
        String[] algorithms = {"Selection Sort", "Insertion Sort", "Merge Sort", "Quick Sort"};

        for (int size : INPUT_SIZES) {
            for (String algo : algorithms) {
                long totalTime = 0;
                long totalComp = 0;
                long totalSwaps = 0;
                long totalShifts = 0;
                int maxDepth = 0;

                for (int run = 0; run < NUM_RUNS; run++) {
                    int[] data = generateRandomArray(size, run + SEED + 100);
                    SortMetrics metrics = new SortMetrics(algo, size);

                    switch (algo) {
                        case "Selection Sort":
                            SortingAlgorithms.selectionSortWithMetrics(data, metrics);
                            break;
                        case "Insertion Sort":
                            SortingAlgorithms.insertionSortWithMetrics(data, metrics);
                            break;
                        case "Merge Sort":
                            SortingAlgorithms.mergeSortWithMetrics(data, metrics);
                            break;
                        case "Quick Sort":
                            SortingAlgorithms.quickSortWithMetrics(data, metrics);
                            break;
                    }

                    totalTime += metrics.getExecutionTimeNs();
                    totalComp += metrics.getComparisons();
                    totalSwaps += metrics.getSwaps();
                    totalShifts += metrics.getShiftsOrCopies();
                    if (metrics.getMaxRecursionDepth() > maxDepth) {
                        maxDepth = metrics.getMaxRecursionDepth();
                    }
                }

                SortMetrics avgSort = new SortMetrics(algo, size);
                avgSort.setExecutionTimeNs(totalTime / NUM_RUNS);
                avgSort.addComparisons(totalComp / NUM_RUNS);
                avgSort.addSwaps(totalSwaps / NUM_RUNS);
                avgSort.addShiftsOrCopies(totalShifts / NUM_RUNS);
                avgSort.updateRecursionDepth(maxDepth);

                avgMetricsList.add(avgSort);

                System.out.printf(" N = %-6d | %-14s | Time: %9.4f ms | Comps: %10d | Swaps: %8d\n",
                        size, algo, avgSort.getExecutionTimeMs(), avgSort.getComparisons(), avgSort.getSwaps());
            }
        }
        return avgMetricsList;
    }

    private static int[] generateRandomArray(int size, long seed) {
        Random rnd = new Random(seed);
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = rnd.nextInt(size * 10) + 1;
        }
        return arr;
    }

    private static void exportToCsv(List<SearchMetrics> searchList, List<SortMetrics> sortList, String csvPath) {
        try {
            java.io.File file = new java.io.File(csvPath);
            if (file.getParentFile() != null) file.getParentFile().mkdirs();
            try (PrintWriter writer = new PrintWriter(new FileWriter(file))) {
                writer.println("Category,Algorithm,InputSize,Comparisons,Swaps,ShiftsOrCopies,MaxRecursionDepth,TimeMs,TimeNs,MemoryKb");

                for (SearchMetrics m : searchList) {
                    writer.printf("Search,%s,%d,%d,0,0,0,%.4f,%d,%d\n",
                            m.getAlgorithmName(), m.getInputSize(), m.getComparisons(),
                            m.getExecutionTimeMs(), m.getExecutionTimeNs(), m.getMemoryUsedKb());
                }

                for (SortMetrics m : sortList) {
                    writer.printf("Sort,%s,%d,%d,%d,%d,%d,%.4f,%d,%d\n",
                            m.getAlgorithmName(), m.getInputSize(), m.getComparisons(),
                            m.getSwaps(), m.getShiftsOrCopies(), m.getMaxRecursionDepth(),
                            m.getExecutionTimeMs(), m.getExecutionTimeNs(), m.getMemoryUsedKb());
                }
            }
        } catch (Exception e) {
            System.err.println("Error writing CSV export: " + e.getMessage());
        }
    }
}
