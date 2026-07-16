import service.report.AnalyticsEngineService;
import service.report.CsvReportExporter;

public class ReportingAnalyticsDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC13: REPORTING & ANALYTICS MODULE DEMO ");
        System.out.println("==================================================");

        // Initialize Analytics Engine
        AnalyticsEngineService analyticsService = new AnalyticsEngineService();

        // --- STEP 1: GENERATE PDF REPORTS (Default Strategy) ---
        System.out.println("\n--- 1. Generating Executive Reports in PDF Format ---");

        analyticsService.generateOccupancyReport("Daily Fleet Occupancy & Performance Report");
        analyticsService.generateFinancialReport("Quarterly Revenue & Tax Liability Summary");

        // --- STEP 2: SWITCH STRATEGY TO CSV AT RUNTIME ---
        System.out.println("--- 2. Switching Export Strategy to CSV for Data Science Team ---");

        // Applying the Strategy Pattern
        analyticsService.setExporter(new CsvReportExporter());

        // --- STEP 3: GENERATE CSV REPORTS ---
        System.out.println("\n--- 3. Generating Data Dump in CSV Format ---");

        analyticsService.generateOccupancyReport("Raw_Occupancy_Dump_July");
        analyticsService.generateFinancialReport("Raw_Financial_Dump_Q1Q2");

        System.out.println("==================================================");
        System.out.println(" UC13 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}