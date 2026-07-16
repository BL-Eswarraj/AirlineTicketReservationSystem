package service.flightmanagement;

import domain.flightmanagement.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FlightManagementService {
    private final Map<String, ManagedFlight> flightFleet = new LinkedHashMap<>();
    private final DynamicPricingEngine pricingEngine = new DynamicPricingEngine();

    // 8.1 Flight Creation and Setup
    public ManagedFlight createNewFlight(String flightNumber, String airline, String aircraftType,
                                         String source, String destination,
                                         LocalDateTime depTime, LocalDateTime arrTime,
                                         ScheduleType scheduleType, String seatLayout) {

        if (flightFleet.containsKey(flightNumber.toUpperCase())) {
            throw new IllegalArgumentException("Flight number " + flightNumber + " already exists in system fleet!");
        }

        ManagedFlight flight = new ManagedFlight(flightNumber.toUpperCase(), airline, aircraftType, source, destination, depTime, arrTime, scheduleType, seatLayout);
        flightFleet.put(flight.getFlightNumber(), flight);
        System.out.printf("[FLIGHT SETUP] Successfully created flight %s (%s) on route %s -> %s.%n",
                flight.getFlightNumber(), airline, source, destination);
        return flight;
    }

    public void configureFlightClasses(String flightNumber, List<ClassConfiguration> classConfigs) {
        ManagedFlight flight = getFlightOrThrow(flightNumber);
        for (ClassConfiguration config : classConfigs) {
            flight.addClassConfiguration(config);
        }
        System.out.printf("[CLASS SETUP] Configured %d travel classes for flight %s. Total Fleet Capacity: %d seats.%n",
                classConfigs.size(), flightNumber, flight.getTotalFleetCapacity());
    }

    // 8.2 Flight Information Management
    public void updateFlightSchedule(String flightNumber, LocalDateTime newDep, LocalDateTime newArr) {
        ManagedFlight flight = getFlightOrThrow(flightNumber);
        flight.updateSchedule(newDep, newArr);
        System.out.printf("[SCHEDULE UPDATE] %s new timing updated -> Departs: %s | Arrives: %s%n",
                flightNumber, newDep.toLocalTime(), newArr.toLocalTime());
    }

    public void publishFlightDelay(String flightNumber, int delayMinutes, String delayReason) {
        ManagedFlight flight = getFlightOrThrow(flightNumber);
        flight.applyDelay(Duration.ofMinutes(delayMinutes), delayReason);
        broadcastNotification(flight, "FLIGHT DELAY ALERT: Your flight " + flightNumber + " is delayed by " + delayMinutes + " minutes due to " + delayReason + ".");
    }

    public void cancelFlight(String flightNumber, String cancellationReason) {
        ManagedFlight flight = getFlightOrThrow(flightNumber);
        flight.setStatus(FlightOperationalStatus.CANCELLED);
        System.out.printf("[FLIGHT CANCELLED] %s has been cancelled. Reason: %s%n", flightNumber, cancellationReason);
        broadcastNotification(flight, "CRITICAL ALERT: Flight " + flightNumber + " has been CANCELLED. Please visit the portal for full refund or re-routing options.");
    }

    public void setSeasonalPricingMultiplier(String flightNumber, double multiplier, String seasonName) {
        ManagedFlight flight = getFlightOrThrow(flightNumber);
        flight.setSeasonalMultiplier(multiplier);
        System.out.printf("[SEASONAL PRICING] Applied %.2fx %s seasonal pricing factor to flight %s.%n",
                multiplier, seasonName, flightNumber);
        pricingEngine.adjustFlightPricing(flight);
    }

    public void triggerDynamicPricingEvaluation(String flightNumber) {
        ManagedFlight flight = getFlightOrThrow(flightNumber);
        pricingEngine.adjustFlightPricing(flight);
    }

    // Simulate booking seats (to test dynamic occupancy changes)
    public void simulateSeatBooking(String flightNumber, String travelClass, int seatsBooked) {
        ManagedFlight flight = getFlightOrThrow(flightNumber);
        ClassConfiguration config = flight.getClassConfig(travelClass);
        if (config == null) throw new IllegalArgumentException("Travel class " + travelClass + " not configured for " + flightNumber);

        config.bookSeats(seatsBooked);
        System.out.printf("[SIMULATED BOOKING] Booked %d seats in %s for %s. New Occupancy: %.1f%%%n",
                seatsBooked, travelClass, flightNumber, config.getOccupancyRate());

        // Auto-evaluate dynamic pricing after booking surge
        pricingEngine.adjustFlightPricing(flight);
    }

    // 8.3 Flight Search and Filtering (Admin Views)
    public List<ManagedFlight> filterFlightsByRoute(String source, String destination) {
        return flightFleet.values().stream()
                .filter(f -> source == null || f.getSourceAirport().equalsIgnoreCase(source))
                .filter(f -> destination == null || f.getDestinationAirport().equalsIgnoreCase(destination))
                .collect(Collectors.toList());
    }

    public List<ManagedFlight> filterFlightsByStatus(FlightOperationalStatus status) {
        return flightFleet.values().stream()
                .filter(f -> f.getStatus() == status)
                .collect(Collectors.toList());
    }

    // 8.3 Generate Fleet Occupancy & Revenue Report
    public void generateFleetOccupancyReport() {
        System.out.println("\n=====================================================================================================");
        System.out.println("                         ADMIN FLEET OCCUPANCY & FINANCIAL REPORT                            ");
        System.out.println("=====================================================================================================");
        System.out.printf("%-8s | %-12s | %-10s | %-10s | %-9s | %-12s | %-14s%n",
                "FLIGHT", "ROUTE", "STATUS", "CAPACITY", "BOOKED", "OCCUPANCY", "PROJ. REVENUE");
        System.out.println("-----------------------------------------------------------------------------------------------------");

        double totalFleetRevenue = 0.0;
        int totalFleetSeats = 0;
        int totalBookedSeats = 0;

        for (ManagedFlight f : flightFleet.values()) {
            double projRev = f.calculateProjectedRevenue();
            totalFleetRevenue += projRev;
            totalFleetSeats += f.getTotalFleetCapacity();
            totalBookedSeats += f.getTotalBookedSeats();

            System.out.printf("%-8s | %s -> %s | %-10s | %-10d | %-9d | %6.1f%%     | ₹%-13.2f%n",
                    f.getFlightNumber(), f.getSourceAirport(), f.getDestinationAirport(), f.getStatus(),
                    f.getTotalFleetCapacity(), f.getTotalBookedSeats(), f.getOverallOccupancyRate(), projRev);

            // Print breakdown per class
            for (ClassConfiguration c : f.getAllClassConfigs()) {
                System.out.println("   * " + c);
            }
            System.out.println("-----------------------------------------------------------------------------------------------------");
        }

        double overallFleetOccupancy = totalFleetSeats == 0 ? 0.0 : ((double) totalBookedSeats / totalFleetSeats) * 100.0;
        System.out.printf("TOTAL FLEET SUMMARY: Capacity: %d | Booked: %d | Overall Occupancy: %.1f%% | TOTAL REVENUE: ₹%.2f%n",
                totalFleetSeats, totalBookedSeats, overallFleetOccupancy, totalFleetRevenue);
        System.out.println("=====================================================================================================\n");
    }

    public ManagedFlight getFlightOrThrow(String flightNumber) {
        ManagedFlight flight = flightFleet.get(flightNumber.toUpperCase());
        if (flight == null) throw new NoSuchElementException("Flight " + flightNumber + " not found in system fleet.");
        return flight;
    }

    private void broadcastNotification(ManagedFlight flight, String message) {
        System.out.printf("[BROADCAST SYSTEM] Transmitting alert to all %d passengers booked on %s -> \"%s\"%n",
                flight.getTotalBookedSeats(), flight.getFlightNumber(), message);
    }
}