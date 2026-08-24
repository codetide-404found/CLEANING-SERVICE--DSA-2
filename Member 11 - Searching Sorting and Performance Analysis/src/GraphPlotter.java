import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.List;

/**
 * Graph Visualizer & Plotter Engine for Performance Analysis.
 * Generates standalone SVG line graphs and an interactive HTML dashboard.
 * 
 * Member 11: Searching, Sorting & Performance Analysis
 * Ghana Smart Service Operations Optimizer - University of Ghana
 */
public class GraphPlotter {

    public static void generateSvgCharts(List<SearchMetrics> searchList, List<SortMetrics> sortList, String outputDir) {
        generateSearchSvg(searchList, outputDir + "search_performance.svg");
        generateSortSvg(sortList, outputDir + "sorting_performance.svg");
    }

    private static void generateSearchSvg(List<SearchMetrics> searchList, String svgPath) {
        File file = new File(svgPath);
        if (file.getParentFile() != null) file.getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 800 500' width='800' height='500'>");
            pw.println("  <rect width='100%' height='100%' fill='#1e1e2e'/>");
            pw.println("  <text x='400' y='35' fill='#f5e0dc' font-family='sans-serif' font-size='20' font-weight='bold' text-anchor='middle'>Linear Search vs Binary Search Runtime (ms)</text>");
            
            // Draw grid & axes
            pw.println("  <line x1='80' y1='420' x2='750' y2='420' stroke='#6c7086' stroke-width='2'/>");
            pw.println("  <line x1='80' y1='60' x2='80' y2='420' stroke='#6c7086' stroke-width='2'/>");
            
            // Labels
            pw.println("  <text x='400' y='460' fill='#a6adc8' font-family='sans-serif' font-size='14' text-anchor='middle'>Input Size (N)</text>");
            pw.println("  <text x='30' y='240' fill='#a6adc8' font-family='sans-serif' font-size='14' text-anchor='middle' transform='rotate(-90 30,240)'>Time (ms)</text>");

            // Legend
            pw.println("  <rect x='580' y='70' width='150' height='60' fill='#313244' rx='6'/>");
            pw.println("  <line x1='595' y1='90' x2='625' y2='90' stroke='#f38ba8' stroke-width='3'/>");
            pw.println("  <text x='635' y='94' fill='#cdd6f4' font-family='sans-serif' font-size='12'>Linear Search</text>");
            pw.println("  <line x1='595' y1='115' x2='625' y2='115' stroke='#89b4fa' stroke-width='3'/>");
            pw.println("  <text x='635' y='119' fill='#cdd6f4' font-family='sans-serif' font-size='12'>Binary Search</text>");

            int[] xCoords = {100, 230, 360, 520, 700};
            int[] sizes = {100, 500, 1000, 5000, 10000};
            for (int i = 0; i < sizes.length; i++) {
                pw.printf("  <text x='%d' y='440' fill='#a6adc8' font-family='sans-serif' font-size='12' text-anchor='middle'>%d</text>\n", xCoords[i], sizes[i]);
                pw.printf("  <line x1='%d' y1='420' x2='%d' y2='425' stroke='#6c7086'/>\n", xCoords[i], xCoords[i]);
            }

            double maxTime = 0.001;
            for (SearchMetrics m : searchList) {
                if (m.getExecutionTimeMs() > maxTime) maxTime = m.getExecutionTimeMs();
            }

            StringBuilder linPoly = new StringBuilder();
            StringBuilder binPoly = new StringBuilder();

            int idx = 0;
            for (int i = 0; i < searchList.size(); i += 2) {
                SearchMetrics lin = searchList.get(i);
                SearchMetrics bin = searchList.get(i + 1);

                int x = xCoords[idx];
                int yLin = 420 - (int) ((lin.getExecutionTimeMs() / maxTime) * 330);
                int yBin = 420 - (int) ((bin.getExecutionTimeMs() / maxTime) * 330);

                if (yLin < 65) yLin = 65;
                if (yBin < 65) yBin = 65;

                linPoly.append(x).append(",").append(yLin).append(" ");
                binPoly.append(x).append(",").append(yBin).append(" ");

                pw.printf("  <circle cx='%d' cy='%d' r='5' fill='#f38ba8'/>\n", x, yLin);
                pw.printf("  <circle cx='%d' cy='%d' r='5' fill='#89b4fa'/>\n", x, yBin);

                idx++;
            }

            pw.printf("  <polyline points='%s' fill='none' stroke='#f38ba8' stroke-width='3'/>\n", linPoly.toString().trim());
            pw.printf("  <polyline points='%s' fill='none' stroke='#89b4fa' stroke-width='3'/>\n", binPoly.toString().trim());

            pw.println("</svg>");
        } catch (Exception e) {
            System.err.println("Error creating Search SVG graph: " + e.getMessage());
        }
    }

    private static void generateSortSvg(List<SortMetrics> sortList, String svgPath) {
        File file = new File(svgPath);
        if (file.getParentFile() != null) file.getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("<svg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 850 520' width='850' height='520'>");
            pw.println("  <rect width='100%' height='100%' fill='#181825'/>");
            pw.println("  <text x='425' y='35' fill='#cdd6f4' font-family='sans-serif' font-size='20' font-weight='bold' text-anchor='middle'>Sorting Algorithms Efficiency Benchmarks (Runtime ms)</text>");

            pw.println("  <line x1='80' y1='430' x2='780' y2='430' stroke='#585b70' stroke-width='2'/>");
            pw.println("  <line x1='80' y1='60' x2='80' y2='430' stroke='#585b70' stroke-width='2'/>");

            pw.println("  <text x='425' y='475' fill='#a6adc8' font-family='sans-serif' font-size='14' text-anchor='middle'>Input Size (N)</text>");
            pw.println("  <text x='30' y='245' fill='#a6adc8' font-family='sans-serif' font-size='14' text-anchor='middle' transform='rotate(-90 30,245)'>Time (ms)</text>");

            // Legend
            pw.println("  <rect x='610' y='70' width='160' height='110' fill='#1e1e2e' rx='6' stroke='#313244'/>");
            pw.println("  <line x1='625' y1='90' x2='655' y2='90' stroke='#f38ba8' stroke-width='3'/><text x='665' y='94' fill='#cdd6f4' font-family='sans-serif' font-size='12'>Selection Sort</text>");
            pw.println("  <line x1='625' y1='115' x2='655' y2='115' stroke='#fab387' stroke-width='3'/><text x='665' y='119' fill='#cdd6f4' font-family='sans-serif' font-size='12'>Insertion Sort</text>");
            pw.println("  <line x1='625' y1='140' x2='655' y2='140' stroke='#a6e3a1' stroke-width='3'/><text x='665' y='144' fill='#cdd6f4' font-family='sans-serif' font-size='12'>Merge Sort</text>");
            pw.println("  <line x1='625' y1='165' x2='655' y2='165' stroke='#89b4fa' stroke-width='3'/><text x='665' y='169' fill='#cdd6f4' font-family='sans-serif' font-size='12'>Quick Sort</text>");

            int[] xCoords = {110, 240, 380, 540, 720};
            int[] sizes = {100, 500, 1000, 5000, 10000};
            for (int i = 0; i < sizes.length; i++) {
                pw.printf("  <text x='%d' y='450' fill='#a6adc8' font-family='sans-serif' font-size='12' text-anchor='middle'>%d</text>\n", xCoords[i], sizes[i]);
                pw.printf("  <line x1='%d' y1='430' x2='%d' y2='435' stroke='#585b70'/>\n", xCoords[i], xCoords[i]);
            }

            double maxTime = 0.001;
            for (SortMetrics m : sortList) {
                if (m.getExecutionTimeMs() > maxTime) maxTime = m.getExecutionTimeMs();
            }

            StringBuilder selPoly = new StringBuilder();
            StringBuilder insPoly = new StringBuilder();
            StringBuilder merPoly = new StringBuilder();
            StringBuilder quiPoly = new StringBuilder();

            for (int sIdx = 0; sIdx < sizes.length; sIdx++) {
                int size = sizes[sIdx];
                int x = xCoords[sIdx];

                for (SortMetrics m : sortList) {
                    if (m.getInputSize() == size) {
                        int y = 430 - (int) ((m.getExecutionTimeMs() / maxTime) * 350);
                        if (y < 65) y = 65;

                        String color = "#ffffff";
                        if ("Selection Sort".equals(m.getAlgorithmName())) {
                            selPoly.append(x).append(",").append(y).append(" ");
                            color = "#f38ba8";
                        } else if ("Insertion Sort".equals(m.getAlgorithmName())) {
                            insPoly.append(x).append(",").append(y).append(" ");
                            color = "#fab387";
                        } else if ("Merge Sort".equals(m.getAlgorithmName())) {
                            merPoly.append(x).append(",").append(y).append(" ");
                            color = "#a6e3a1";
                        } else if ("Quick Sort".equals(m.getAlgorithmName())) {
                            quiPoly.append(x).append(",").append(y).append(" ");
                            color = "#89b4fa";
                        }
                        pw.printf("  <circle cx='%d' cy='%d' r='4' fill='%s'/>\n", x, y, color);
                    }
                }
            }

            pw.printf("  <polyline points='%s' fill='none' stroke='#f38ba8' stroke-width='3'/>\n", selPoly.toString().trim());
            pw.printf("  <polyline points='%s' fill='none' stroke='#fab387' stroke-width='3'/>\n", insPoly.toString().trim());
            pw.printf("  <polyline points='%s' fill='none' stroke='#a6e3a1' stroke-width='3'/>\n", merPoly.toString().trim());
            pw.printf("  <polyline points='%s' fill='none' stroke='#89b4fa' stroke-width='3'/>\n", quiPoly.toString().trim());

            pw.println("</svg>");
        } catch (Exception e) {
            System.err.println("Error writing Sorting SVG graph: " + e.getMessage());
        }
    }

    public static void generateHtmlDashboard(List<SearchMetrics> searchList, List<SortMetrics> sortList, String htmlPath) {
        File file = new File(htmlPath);
        if (file.getParentFile() != null) file.getParentFile().mkdirs();

        try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
            pw.println("<!DOCTYPE html>");
            pw.println("<html lang='en'>");
            pw.println("<head>");
            pw.println("<meta charset='UTF-8'>");
            pw.println("<title>Member 11 - Algorithm Efficiency Dashboard</title>");
            pw.println("<style>");
            pw.println("body { font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif; background-color: #0f0f17; color: #cdd6f4; margin: 0; padding: 30px; }");
            pw.println("h1, h2 { color: #89b4fa; }");
            pw.println(".card { background: #1e1e2e; border-radius: 12px; padding: 24px; margin-bottom: 30px; box-shadow: 0 4px 20px rgba(0,0,0,0.5); }");
            pw.println("table { width: 100%; border-collapse: collapse; margin-top: 15px; }");
            pw.println("th, td { padding: 12px 15px; text-align: left; border-bottom: 1px solid #313244; }");
            pw.println("th { background-color: #181825; color: #f5e0dc; }");
            pw.println("tr:hover { background-color: #313244; }");
            pw.println(".img-container { text-align: center; margin: 20px 0; }");
            pw.println("img { max-width: 100%; border-radius: 8px; border: 1px solid #45475a; }");
            pw.println("</style>");
            pw.println("</head>");
            pw.println("<body>");
            pw.println("<h1>Ghana Smart Service Operations Optimizer</h1>");
            pw.println("<h2>Member 11: Searching, Sorting & Empirical Performance Analysis</h2>");

            // Search section
            pw.println("<div class='card'>");
            pw.println("<h3>1. Linear Search vs Binary Search Empirical Comparison</h3>");
            pw.println("<div class='img-container'><img src='search_performance.svg' alt='Search Performance Chart'/></div>");
            pw.println("<table>");
            pw.println("<tr><th>Algorithm</th><th>Input Size (N)</th><th>Avg Comparisons</th><th>Avg Runtime (ms)</th><th>Memory (KB)</th></tr>");
            for (SearchMetrics m : searchList) {
                pw.printf("<tr><td>%s</td><td>%d</td><td>%d</td><td>%.4f</td><td>%d</td></tr>\n",
                        m.getAlgorithmName(), m.getInputSize(), m.getComparisons(), m.getExecutionTimeMs(), m.getMemoryUsedKb());
            }
            pw.println("</table>");
            pw.println("</div>");

            // Sorting section
            pw.println("<div class='card'>");
            pw.println("<h3>2. Selection, Insertion, Merge & Quick Sort Empirical Benchmarks</h3>");
            pw.println("<div class='img-container'><img src='sorting_performance.svg' alt='Sorting Performance Chart'/></div>");
            pw.println("<table>");
            pw.println("<tr><th>Algorithm</th><th>Input Size (N)</th><th>Comparisons</th><th>Swaps</th><th>Shifts/Copies</th><th>Max Depth</th><th>Runtime (ms)</th></tr>");
            for (SortMetrics m : sortList) {
                pw.printf("<tr><td>%s</td><td>%d</td><td>%d</td><td>%d</td><td>%d</td><td>%d</td><td>%.4f</td></tr>\n",
                        m.getAlgorithmName(), m.getInputSize(), m.getComparisons(), m.getSwaps(),
                        m.getShiftsOrCopies(), m.getMaxRecursionDepth(), m.getExecutionTimeMs());
            }
            pw.println("</table>");
            pw.println("</div>");

            pw.println("</body>");
            pw.println("</html>");
        } catch (Exception e) {
            System.err.println("Error writing HTML dashboard: " + e.getMessage());
        }
    }
}
