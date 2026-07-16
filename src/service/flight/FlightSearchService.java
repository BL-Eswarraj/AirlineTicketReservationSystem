package service.flight;

import domain.flight.Flight;
import domain.flight.FlightEnums.*;
import domain.flight.RoundTripOption;
import domain.flight.SearchCriteria;

import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class FlightSearchService {

    // ==========================================
    // 2.1 FLIGHT SEARCH OPERATIONS
    // ==========================================
    public List<Flight> searchFlights(List<Flight> inventory, SearchCriteria criteria) {
        return inventory.stream()
                // Core Filters
                .filter(f -> criteria.source() == null || f.source().equalsIgnoreCase(criteria.source()))
                .filter(f -> criteria.destination() == null || f.destination().equalsIgnoreCase(criteria.destination()))
                .filter(f -> criteria.departureDate() == null || f.departureTime().toLocalDate().equals(criteria.departureDate()))

                // Passenger & Class Filters
                .filter(f -> criteria.passengersCount() <= f.availableSeats())
                .filter(f -> criteria.travelClass() == null || f.travelClass() == criteria.travelClass())

                // Advanced Filters
                .filter(f -> criteria.maxPrice() == null || f.getTotalPrice() <= criteria.maxPrice())
                .filter(f -> criteria.maxStops() == null || f.stops() <= criteria.maxStops())
                .filter(f -> criteria.preferredAirline() == null || f.airline().equalsIgnoreCase(criteria.preferredAirline()))

                // Sorting Logic
                .sorted(getComparator(criteria.sortBy()))
                .collect(toList());
    }

    private Comparator<Flight> getComparator(SearchCriteria.SortBy sortBy) {
        if (sortBy == null) return Comparator.comparingDouble(Flight::getTotalPrice); // Default

        return switch (sortBy) {
            case PRICE_LOW_TO_HIGH -> Comparator.comparingDouble(Flight::getTotalPrice);
            case PRICE_HIGH_TO_LOW -> Comparator.comparingDouble(Flight::getTotalPrice).reversed();
            case DURATION_SHORTEST -> Comparator.comparing(Flight::duration);
            case DEPARTURE_EARLIEST -> Comparator.comparing(Flight::departureTime);
        };
    }

    // ==========================================
    // 2.3 ADVANCED SEARCH FEATURES (Streams & groupingBy)
    // ==========================================

    // Group flights by airline
    public Map<String, List<Flight>> groupFlightsByAirline(List<Flight> flights) {
        return flights.stream().collect(groupingBy(Flight::airline));
    }

    // Group flights by price range categories
    public Map<PriceRange, List<Flight>> groupFlightsByPriceRange(List<Flight> flights) {
        return flights.stream().collect(groupingBy(f -> {
            double price = f.getTotalPrice();
            if (price < 4000) return PriceRange.BUDGET;
            if (price <= 10000) return PriceRange.MODERATE;
            return PriceRange.PREMIUM;
        }));
    }

    // Group flights by departure time slots
    public Map<TimeSlot, List<Flight>> groupFlightsByTimeSlot(List<Flight> flights) {
        return flights.stream().collect(groupingBy(f -> {
            int hour = f.departureTime().getHour();
            if (hour >= 5 && hour < 12) return TimeSlot.MORNING;
            if (hour >= 12 && hour < 17) return TimeSlot.AFTERNOON;
            if (hour >= 17 && hour < 21) return TimeSlot.EVENING;
            return TimeSlot.NIGHT;
        }));
    }

    // Calculate average fare by airline
    public Map<String, Double> calculateAverageFareByAirline(List<Flight> flights) {
        return flights.stream().collect(groupingBy(
                Flight::airline,
                averagingDouble(Flight::getTotalPrice)
        ));
    }

    // Find cheapest flights by route (source-destination combination)
    public Map<String, Optional<Flight>> findCheapestFlightsByRoute(List<Flight> flights) {
        return flights.stream().collect(groupingBy(
                f -> f.source() + "-" + f.destination(),
                minBy(Comparator.comparingDouble(Flight::getTotalPrice))
        ));
    }

    // Filter and group connecting flights by layover duration
    public Map<String, List<Flight>> groupConnectingFlightsByLayover(List<Flight> flights) {
        return flights.stream()
                .filter(f -> f.stops() > 0 && f.layoverDuration() != null)
                .collect(groupingBy(f ->
                        f.layoverDuration().toHours() < 3 ? "Short Layover (<3h)" : "Long Layover (>=3h)"
                ));
    }

    // Aggregate available seats across all classes
    public Map<TravelClass, Integer> aggregateAvailableSeatsByClass(List<Flight> flights) {
        return flights.stream().collect(groupingBy(
                Flight::travelClass,
                summingInt(Flight::availableSeats)
        ));
    }

    // Group round-trip options by total journey time
    public Map<String, List<RoundTripOption>> groupRoundTripsByDurationCategory(List<RoundTripOption> roundTrips) {
        return roundTrips.stream().collect(groupingBy(rt -> {
            long totalHours = rt.getTotalJourneyTime().toHours();
            if (totalHours < 6) return "Quick Round Trip (< 6 hrs flying)";
            if (totalHours <= 15) return "Standard Round Trip (6-15 hrs flying)";
            return "Long Haul Round Trip (> 15 hrs flying)";
        }));
    }
}