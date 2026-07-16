package service.report;

import java.util.List;

public interface ReportExporter {
    void exportOccupancyReport(String reportTitle, List<OccupancyMetrics> data);

    void exportRevenueReport(String reportTitle, List<RevenueMetrics> data);

    String getFormatName();
}