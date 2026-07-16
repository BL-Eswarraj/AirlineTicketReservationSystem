package domain.rules;

import java.time.LocalDateTime;

// Immutable snapshot of travel input requirements
public record PassengerTravelDetails(
        String pnr,
        String passengerName,
        String flightNumber,
        String route,
        TravelClass travelClass,
        double initialBaseFare,
        double declaredBaggageWeightKg,
        LocalDateTime bookingTimestamp,
        LocalDateTime scheduledDeparture,
        boolean isPeakFestivalSeason
) {}