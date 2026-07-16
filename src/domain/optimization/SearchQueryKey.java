package domain.optimization;

import java.time.LocalDate;

// Immutable cache key representing a unique user search query
public record SearchQueryKey(String sourceAirport, String destinationAirport, LocalDate travelDate, int maxStops) {
    public SearchQueryKey {
        sourceAirport = sourceAirport != null ? sourceAirport.toUpperCase().trim() : "";
        destinationAirport = destinationAirport != null ? destinationAirport.toUpperCase().trim() : "";
    }

    @Override
    public String toString() {
        return String.format("KEY[%s->%s | Date: %s | MaxStops: %d]", sourceAirport, destinationAirport, travelDate, maxStops);
    }
}