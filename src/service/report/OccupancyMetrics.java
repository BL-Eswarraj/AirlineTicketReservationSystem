package service.report;

public record OccupancyMetrics(
        String flightNumber,
        String route,
        int totalCapacity,
        int bookedSeats
) {
    public double getOccupancyRate() {
        if (totalCapacity == 0) return 0.0;
        return ((double) bookedSeats / totalCapacity) * 100.0;
    }

    public String getPerformanceStatus() {
        double rate = getOccupancyRate();
        if (rate >= 85.0) return "EXCELLENT";
        if (rate >= 60.0) return "HEALTHY";
        if (rate >= 30.0) return "UNDERPERFORMING";
        return "CRITICAL - LOW OCCUPANCY";
    }
}