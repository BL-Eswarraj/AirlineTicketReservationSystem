package domain.flight;

import domain.flight.FlightEnums.TravelClass;
import java.time.LocalDate;

public record SearchCriteria(
        String source,
        String destination,
        LocalDate departureDate,
        LocalDate returnDate, // Nullable for one-way
        int passengersCount,
        TravelClass travelClass,
        Double maxPrice,
        Integer maxStops,
        String preferredAirline,
        SortBy sortBy
) {
    public enum SortBy { PRICE_LOW_TO_HIGH, PRICE_HIGH_TO_LOW, DURATION_SHORTEST, DEPARTURE_EARLIEST }
}