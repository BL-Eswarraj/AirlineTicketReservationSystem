package service.report;

public record RevenueMetrics(
        String periodOrRoute,
        double grossRevenue,
        double taxesCollected,
        double refundsProcessed
) {
    public double getNetRevenue() {
        return grossRevenue - taxesCollected - refundsProcessed;
    }
}