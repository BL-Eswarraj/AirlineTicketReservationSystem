import domain.flight.*;
import domain.flight.FlightEnums.*;
import service.flight.FlightSearchService;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FlightSearchDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC2: FLIGHT SEARCH & DISCOVERY DEMO ");
        System.out.println("==================================================");

        FlightSearchService searchService = new FlightSearchService();
        List<Flight> inventory = createMockInventory();

        // ---------------------------------------------------------
        // 2.1 & 2.2 FLIGHT SEARCH & INFORMATION DISPLAY
        // ---------------------------------------------------------
        System.out.println("\n--- 2.1 Searching: MAA to DEL, Date: Tomorrow, < ₹8000, Sort: Duration ---");
        SearchCriteria criteria = new SearchCriteria(
                "MAA", "DEL", LocalDate.now().plusDays(1), null, 1,
                null, 8000.0, null, null, SearchCriteria.SortBy.DURATION_SHORTEST
        );

        List<Flight> searchResults = searchService.searchFlights(inventory, criteria);

        for (Flight f : searchResults) {
            System.out.println("\n[Flight Detail Display - Section 2.2]");
            System.out.printf("Airline    : %s (%s) | Aircraft: %s%n", f.airline(), f.flightNumber(), f.aircraftType());
            System.out.printf("Route      : %s -> %s | Stops: %d%n", f.source(), f.destination(), f.stops());
            System.out.printf("Timings    : Departs %s | Arrives %s%n", f.departureTime().toLocalTime(), f.arrivalTime().toLocalTime());
            System.out.printf("Duration   : %s (Layover: %s)%n", f.getFormattedDuration(), f.layoverDuration() == null ? "None" : f.layoverDuration().toHours()+"h");
            System.out.printf("Fare Brkwn : Base ₹%.2f | Tax ₹%.2f | Total: ₹%.2f%n", f.fare().baseFare(), f.fare().taxes(), f.getTotalPrice());
            System.out.printf("Seats/Class: %d seats left in %s%n", f.availableSeats(), f.travelClass());
            System.out.printf("Baggage    : %dkg Cabin, %dkg Check-in%n", f.baggagePolicy().cabinKgs(), f.baggagePolicy().checkInKgs());
            System.out.printf("Amenities  : %s | Status: %s%n", String.join(", ", f.amenities()), f.status());
            System.out.printf("Policies   : %s%n", f.cancellationPolicy());
            System.out.println("--------------------------------------------------");
        }

        // ---------------------------------------------------------
        // 2.3 ADVANCED SEARCH FEATURES (Java Streams)
        // ---------------------------------------------------------
        System.out.println("\n--- 2.3 Advanced Stream Operations ---");

        System.out.println("\n>> Group By Price Range:");
        Map<PriceRange, List<Flight>> byPrice = searchService.groupFlightsByPriceRange(inventory);
        byPrice.forEach((range, flights) ->
                System.out.println(range + ": " + flights.stream().map(Flight::flightNumber).toList())
        );

        System.out.println("\n>> Group By Time Slot:");
        Map<TimeSlot, List<Flight>> byTimeSlot = searchService.groupFlightsByTimeSlot(inventory);
        byTimeSlot.forEach((slot, flights) ->
                System.out.println(slot + ": " + flights.stream().map(f -> f.flightNumber() + " @ " + f.departureTime().toLocalTime()).toList())
        );

        System.out.println("\n>> Average Fare By Airline:");
        Map<String, Double> avgFares = searchService.calculateAverageFareByAirline(inventory);
        avgFares.forEach((airline, avg) -> System.out.printf("%s: ₹%.2f%n", airline, avg));

        System.out.println("\n>> Cheapest Flight By Route (minBy):");
        Map<String, Optional<Flight>> cheapestByRoute = searchService.findCheapestFlightsByRoute(inventory);
        cheapestByRoute.forEach((route, optFlight) ->
                optFlight.ifPresent(f -> System.out.printf("%s -> %s (₹%.2f)%n", route, f.flightNumber(), f.getTotalPrice()))
        );

        System.out.println("\n>> Aggregated Available Seats Across Classes:");
        Map<TravelClass, Integer> seatsByClass = searchService.aggregateAvailableSeatsByClass(inventory);
        seatsByClass.forEach((tClass, totalSeats) -> System.out.println(tClass + ": " + totalSeats + " seats total"));

        System.out.println("\n>> Grouping Connecting Flights by Layover Duration:");
        Map<String, List<Flight>> groupedLayovers = searchService.groupConnectingFlightsByLayover(inventory);
        groupedLayovers.forEach((category, flights) ->
                System.out.println(category + ": " + flights.stream().map(Flight::flightNumber).toList())
        );

        System.out.println("\n==================================================");
        System.out.println(" UC2 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }

    private static List<Flight> createMockInventory() {
        BaggagePolicy standardBaggage = new BaggagePolicy(15, 7, "1 piece per passenger");
        String standardPolicy = "Full refund if cancelled before 24hrs";

        LocalDateTime tomorrow = LocalDate.now().plusDays(1).atStartOfDay();

        return List.of(
                new Flight("6E-101", "IndiGo", "Airbus A320", "MAA", "DEL",
                        tomorrow.plusHours(6), tomorrow.plusHours(8).plusMinutes(45), Duration.ofHours(2).plusMinutes(45),
                        0, null, new Fare(4000, 800, 200), 45, TravelClass.ECONOMY,
                        standardBaggage, standardPolicy, List.of("Snacks", "Water"), FlightStatus.ON_TIME),

                new Flight("UK-808", "Vistara", "Boeing 737", "MAA", "DEL",
                        tomorrow.plusHours(18), tomorrow.plusHours(21), Duration.ofHours(3),
                        0, null, new Fare(6500, 1000, 250), 20, TravelClass.PREMIUM_ECONOMY,
                        new BaggagePolicy(20, 7, "Priority Tag"), standardPolicy, List.of("Hot Meal", "WiFi"), FlightStatus.ON_TIME),

                new Flight("AI-202", "Air India", "Airbus A350", "MAA", "DEL",
                        tomorrow.plusHours(10), tomorrow.plusHours(15), Duration.ofHours(5),
                        1, Duration.ofHours(2), new Fare(3500, 600, 100), 100, TravelClass.ECONOMY,
                        new BaggagePolicy(25, 8, "Connecting flight rules apply"), "Non-refundable", List.of("Meal"), FlightStatus.DELAYED),

                new Flight("6E-555", "IndiGo", "Airbus A320", "BOM", "BLR",
                        tomorrow.plusHours(14), tomorrow.plusHours(15).plusMinutes(30), Duration.ofHours(1).plusMinutes(30),
                        0, null, new Fare(2500, 500, 100), 150, TravelClass.ECONOMY,
                        standardBaggage, standardPolicy, List.of(), FlightStatus.ON_TIME)
        );
    }
}