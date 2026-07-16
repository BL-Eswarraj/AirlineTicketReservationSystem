import domain.flightmanagement.*;
import service.flightmanagement.FlightManagementService;
import java.time.LocalDateTime;
import java.util.List;

public class FlightManagementDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC8: FLIGHT MANAGEMENT MODULE (ADMIN/STAFF) DEMO ");
        System.out.println("==================================================");

        FlightManagementService adminService = new FlightManagementService();

        // --- STEP 1: CREATE NEW FLIGHTS & SETUP CLASSES (8.1) ---
        System.out.println("\n--- 1. Configuring New Flights & Seat Capacities ---");
        LocalDateTime tomorrowMorning = LocalDateTime.now().plusDays(1).withHour(6).withMinute(0);

        // Flight 1: Air India Express (MAA -> DEL)
        ManagedFlight ai101 = adminService.createNewFlight(
                "AI-101", "Air India", "Airbus A320", "MAA", "DEL",
                tomorrowMorning, tomorrowMorning.plusHours(2).plusMinutes(45),
                ScheduleType.DAILY, "ABC-DEF"
        );
        adminService.configureFlightClasses("AI-101", List.of(
                new ClassConfiguration("BUSINESS", 12, 18000.0),
                new ClassConfiguration("ECONOMY", 150, 5000.0)
        ));
        ai101.addAmenity("In-Flight Wi-Fi");
        ai101.addAmenity("Complimentary Meal");

        // Flight 2: Vistara Premium (DEL -> BOM)
        ManagedFlight uk808 = adminService.createNewFlight(
                "UK-808", "Vistara", "Boeing 737", "DEL", "BOM",
                tomorrowMorning.plusHours(4), tomorrowMorning.plusHours(6),
                ScheduleType.WEEKLY, "ABC-DEF"
        );
        adminService.configureFlightClasses("UK-808", List.of(
                new ClassConfiguration("BUSINESS", 16, 22000.0),
                new ClassConfiguration("PREMIUM", 30, 9500.0),
                new ClassConfiguration("ECONOMY", 120, 6000.0)
        ));

        // --- STEP 2: SIMULATE SEAT BOOKINGS & DYNAMIC PRICING (8.2) ---
        System.out.println("\n--- 2. Simulating Bookings & Demand Surge Pricing (8.2) ---");
        // Simulate heavy demand on AI-101 Economy (filling 135 out of 150 seats = 90% occupancy -> Surge!)
        adminService.simulateSeatBooking("AI-101", "ECONOMY", 135);

        // Simulate low occupancy on AI-101 Business (filling 2 out of 12 seats = 16.6% -> Discount incentive!)
        adminService.simulateSeatBooking("AI-101", "BUSINESS", 2);

        // --- STEP 3: APPLY SEASONAL PRICING MULTIPLIER (8.2) ---
        System.out.println("\n--- 3. Applying Diwali Festival Rush Seasonal Pricing ---");
        // Applying a 1.25x seasonal multiplier to UK-808
        adminService.setSeasonalPricingMultiplier("UK-808", 1.25, "Diwali Rush");

        // Simulate moderate bookings on UK-808
        adminService.simulateSeatBooking("UK-808", "PREMIUM", 22); // ~73% occupancy -> moderate surge + seasonal factor
        adminService.simulateSeatBooking("UK-808", "ECONOMY", 60);  // 50% occupancy -> normal demand + seasonal factor

        // --- STEP 4: FLIGHT OPERATIONS & DELAY BROADCASTING (8.2) ---
        System.out.println("\n--- 4. Managing Live Flight Operations & Delays ---");
        // Staff publishes a 45-minute weather delay for AI-101
        adminService.publishFlightDelay("AI-101", 45, "Severe Thunderstorms over Chennai Airspace");

        // --- STEP 5: ADMIN SEARCH & FILTERING (8.3) ---
        System.out.println("\n--- 5. Admin Search & Filtering ---");
        System.out.println(">> Filtering all flights departing from DEL:");
        List<ManagedFlight> delFlights = adminService.filterFlightsByRoute("DEL", null);
        delFlights.forEach(f -> System.out.printf("   Found: %s (%s -> %s) | Status: %s%n",
                f.getFlightNumber(), f.getSourceAirport(), f.getDestinationAirport(), f.getStatus()));

        System.out.println("\n>> Filtering all DELAYED flights in fleet:");
        List<ManagedFlight> delayedFlights = adminService.filterFlightsByStatus(FlightOperationalStatus.DELAYED);
        delayedFlights.forEach(f -> System.out.printf("   Delayed: %s | New Dep: %s%n",
                f.getFlightNumber(), f.getScheduledDepartureTime().toLocalTime()));

        // --- STEP 6: GENERATE EXECUTIVE FLEET REPORT (8.3) ---
        adminService.generateFleetOccupancyReport();

        System.out.println("\n==================================================");
        System.out.println(" UC8 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}