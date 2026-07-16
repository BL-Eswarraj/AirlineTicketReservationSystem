package domain.optimization;

import java.time.Duration;
import java.time.LocalDateTime;

// Simplified immutable flight representation for high-speed indexing
public record OptimizedFlight(
        String flightNumber,
        String airline,
        String source,
        String destination,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        double fare
) {
    public Duration getDuration() {
        return Duration.between(departureTime, arrivalTime);
    }

    @Override
    public String toString() {
        return String.format("%-6s (%s) | %s -> %s | Dep: %s | Arr: %s | ₹%.2f",
                flightNumber, airline, source, destination,
                departureTime.toLocalTime(), arrivalTime.toLocalTime(), fare);
    }
}