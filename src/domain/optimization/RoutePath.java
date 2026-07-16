package domain.optimization;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

// Represents either a direct flight or a multi-leg connecting flight path
public record RoutePath(List<OptimizedFlight> flights, double totalFare, Duration totalDuration, int stops) {

    public static RoutePath ofDirect(OptimizedFlight flight) {
        return new RoutePath(List.of(flight), flight.fare(), flight.getDuration(), 0);
    }

    public static RoutePath ofConnecting(OptimizedFlight leg1, OptimizedFlight leg2) {
        double combinedFare = leg1.fare() + leg2.fare();
        Duration totalTime = Duration.between(leg1.departureTime(), leg2.arrivalTime());
        return new RoutePath(List.of(leg1, leg2), combinedFare, totalTime, 1);
    }

    public String getFormattedRoute() {
        return flights.stream()
                .map(f -> f.flightNumber() + "(" + f.source() + "->" + f.destination() + ")")
                .collect(Collectors.joining(" ==> "));
    }

    @Override
    public String toString() {
        return String.format("[%d-Stop] %s | Total Duration: %dh %dm | Total Fare: ₹%.2f",
                stops, getFormattedRoute(), totalDuration.toHours(), totalDuration.toMinutesPart(), totalFare);
    }
}