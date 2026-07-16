package service.report;

import java.util.List;

public class CsvReportExporter implements ReportExporter {

    @Override
    public void exportOccupancyReport(String reportTitle, List<OccupancyMetrics> data) {
        System.out.println("\n[SYSTEM] Generating " + reportTitle + " in " + getFormatName() + " format...");
        System.out.println("File: " + reportTitle.replace(" ", "_") + ".csv");
        System.out.println("Flight_Number,Route,Capacity,Booked,Occupancy_%,Status");

        for (OccupancyMetrics m : data) {
            System.out.printf("%s,%s,%d,%d,%.2f,%s%n",
                    m.flightNumber(), m.route(), m.totalCapacity(),
                    m.bookedSeats(), m.getOccupancyRate(), m.getPerformanceStatus());
        }
        System.out.println("-> CSV Export Complete.\n");
    }

    @Override
    public void exportRevenueReport(String reportTitle, List<RevenueMetrics> data) {
        System.out.println("\n[SYSTEM] Generating " + reportTitle + " in " + getFormatName() + " format...");
        System.out.println("File: " + reportTitle.replace(" ", "_") + ".csv");
        System.out.println("Segment,Gross_Revenue,Taxes,Refunds,Net_Revenue");

        for (RevenueMetrics m : data) {
            System.out.printf("%s,%.2f,%.2f,%.2f,%.2f%n",
                    m.periodOrRoute(), m.grossRevenue(), m.taxesCollected(),
                    m.refundsProcessed(), m.getNetRevenue());
        }
        System.out.println("-> CSV Export Complete.\n");
    }

    @Override
    public String getFormatName() {
        return "CSV";
    }
}