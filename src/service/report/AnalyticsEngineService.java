package service.report;

import java.util.List;

public class AnalyticsEngineService {

    private ReportExporter exporter; // The active export strategy

    // Default exporter
    public AnalyticsEngineService() {
        this.exporter = new PdfReportExporter();
    }

    // Change export strategy at runtime
    public void setExporter(ReportExporter exporter) {
        this.exporter = exporter;
        System.out.println("[SYSTEM] Report export format switched to: " + exporter.getFormatName());
    }

    // Facade method to generate and export Occupancy Reports
    public void generateOccupancyReport(String title) {
        // In a real app, this would query the DB. We generate mock aggregated data for UC13.
        List<OccupancyMetrics> data = fetchMockOccupancyData();

        System.out.println("\n[ANALYTICS ENGINE] Compiling flight occupancy data...");
        exporter.exportOccupancyReport(title, data);
    }

    // Facade method to generate and export Financial Reports
    public void generateFinancialReport(String title) {
        List<RevenueMetrics> data = fetchMockRevenueData();

        System.out.println("\n[ANALYTICS ENGINE] Compiling financial transactions and tax aggregations...");
        exporter.exportRevenueReport(title, data);
    }

    // Mock Database Fetches for Analytics Output
    private List<OccupancyMetrics> fetchMockOccupancyData() {
        return List.of(
                new OccupancyMetrics("AI-101", "DEL -> BOM", 180, 175),
                new OccupancyMetrics("UK-808", "MAA -> DEL", 150, 110),
                new OccupancyMetrics("6E-303", "BLR -> HYD", 180, 45),
                new OccupancyMetrics("SG-444", "CCU -> DEL", 200, 130)
        );
    }

    private List<RevenueMetrics> fetchMockRevenueData() {
        return List.of(
                new RevenueMetrics("Q1 - Domestic", 12500000.0, 2250000.0, 450000.0),
                new RevenueMetrics("Q1 - International", 34500000.0, 6210000.0, 1200000.0),
                new RevenueMetrics("Q2 - Domestic", 15800000.0, 2844000.0, 300000.0)
        );
    }
}