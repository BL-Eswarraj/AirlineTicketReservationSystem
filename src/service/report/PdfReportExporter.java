package service.report;

import java.util.List;

public class PdfReportExporter implements ReportExporter {

    @Override
    public void exportOccupancyReport(String reportTitle, List<OccupancyMetrics> data) {
        System.out.println("\n📄 [PDF ENGINE] Rendering PDF Document...");
        System.out.println("=========================================================================");
        System.out.println("                        " + reportTitle.toUpperCase());
        System.out.println("=========================================================================");
        System.out.printf("%-10s | %-15s | %-10s | %-10s | %-10s | %-15s%n",
                "FLIGHT", "ROUTE", "CAPACITY", "BOOKED", "RATE (%)", "STATUS");
        System.out.println("-------------------------------------------------------------------------");

        for (OccupancyMetrics m : data) {
            System.out.printf("%-10s | %-15s | %-10d | %-10d | %-10.2f | %-15s%n",
                    m.flightNumber(), m.route(), m.totalCapacity(),
                    m.bookedSeats(), m.getOccupancyRate(), m.getPerformanceStatus());
        }
        System.out.println("=========================================================================");
        System.out.println("-> PDF Export Saved: " + reportTitle.replace(" ", "_") + ".pdf\n");
    }

    @Override
    public void exportRevenueReport(String reportTitle, List<RevenueMetrics> data) {
        System.out.println("\n📄 [PDF ENGINE] Rendering PDF Document...");
        System.out.println("=========================================================================================");
        System.out.println("                              " + reportTitle.toUpperCase());
        System.out.println("=========================================================================================");
        System.out.printf("%-20s | %-15s | %-15s | %-15s | %-15s%n",
                "SEGMENT", "GROSS REV (₹)", "TAXES (₹)", "REFUNDS (₹)", "NET REV (₹)");
        System.out.println("-----------------------------------------------------------------------------------------");

        double totalNet = 0.0;
        for (RevenueMetrics m : data) {
            totalNet += m.getNetRevenue();
            System.out.printf("%-20s | %-15.2f | %-15.2f | %-15.2f | %-15.2f%n",
                    m.periodOrRoute(), m.grossRevenue(), m.taxesCollected(),
                    m.refundsProcessed(), m.getNetRevenue());
        }
        System.out.println("-----------------------------------------------------------------------------------------");
        System.out.printf("TOTAL SYSTEM NET REVENUE: ₹%.2f%n", totalNet);
        System.out.println("=========================================================================================");
        System.out.println("-> PDF Export Saved: " + reportTitle.replace(" ", "_") + ".pdf\n");
    }

    @Override
    public String getFormatName() {
        return "PDF";
    }
}