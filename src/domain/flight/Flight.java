package domain.flight;

import domain.flight.FlightEnums.FlightStatus;
import domain.flight.FlightEnums.TravelClass;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

public record Flight(
        String flightNumber,
        String airline,
        String aircraftType,
        String source,
        String destination,
        LocalDateTime departureTime,
        LocalDateTime arrivalTime,
        Duration duration,
        int stops,
        Duration layoverDuration, // Nullable if non-stop
        Fare fare,
        int availableSeats,
        TravelClass travelClass,
        BaggagePolicy baggagePolicy,
        String cancellationPolicy,
        List<String> amenities,
        FlightStatus status
) {
    public double getTotalPrice() {
        return fare.getTotalFare();
    }

    // Helper to print flight duration in a readable format
    public String getFormattedDuration() {
        return duration.toHours() + "h " + duration.toMinutesPart() + "m";
    }
}