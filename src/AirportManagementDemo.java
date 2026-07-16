import domain.airport.*;
import service.airport.AirportManagementService;
import java.util.List;
import java.util.Optional;

public class AirportManagementDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC9: AIRPORT MANAGEMENT MODULE DEMO ");
        System.out.println("==================================================");

        AirportManagementService airportService = new AirportManagementService();

        // --- STEP 1: REGISTER NEW AIRPORT & CONFIGURE TERMINALS (9.1) ---
        System.out.println("\n--- 1. Registering New Airport & Terminals ---");
        Airport cochin = airportService.registerAirport(
                "COK", "VOCI", "Cochin International Airport", "Kochi", "India", "Asia/Kolkata"
        );

        Terminal cokT3 = new Terminal("T3", "Solar Powered International Terminal", false, true, 15);
        cokT3.addFacility(FacilityType.DUTY_FREE);
        cokT3.addFacility(FacilityType.VIP_LOUNGE);
        airportService.configureTerminal("COK", cokT3);

        airportService.updateContactDirectory("COK", new AirportContact(
                "+91-484-2610115", "+91-484-2610000", "pro@cial.aero", "www.cial.aero"
        ));

        // --- STEP 2: OPERATIONAL STATUS TOGGLING (9.1) ---
        System.out.println("\n--- 2. Toggling Airport Operational Status ---");
        // Simulate temporarily disabling an airport for runway resurfacing
        airportService.setAirportOperationalStatus("BLR", false, "Annual Runway Resurfacing Maintenance");

        // --- STEP 3: MULTI-ATTRIBUTE SEARCH & RETRIEVAL (9.2) ---
        System.out.println("\n--- 3. Testing Airport Search & Retrieval ---");

        System.out.println(">> Search by IATA Code ('DEL'):");
        Optional<Airport> delOpt = airportService.searchByCode("DEL");
        delOpt.ifPresent(a -> System.out.println("   Found: " + a));

        System.out.println("\n>> Search by ICAO Code ('VOMM'):");
        Optional<Airport> maaOpt = airportService.searchByCode("VOMM");
        maaOpt.ifPresent(a -> System.out.println("   Found: " + a));

        System.out.println("\n>> Search by City Keyword ('mumb'):");
        List<Airport> cityResults = airportService.searchByCity("mumb");
        cityResults.forEach(a -> System.out.println("   Match: " + a));

        System.out.println("\n>> List All Airports in 'India':");
        List<Airport> indianAirports = airportService.listAirportsByCountry("India");
        indianAirports.forEach(a -> System.out.printf("   [%s] %s (%s)%n", a.getIataCode(), a.getAirportName(), a.getCity()));

        // --- STEP 4: FLIGHT SEARCH AUTO-SUGGEST ENGINE (9.2) ---
        System.out.println("\n--- 4. Testing Live Search Auto-Suggestion Engine ---");

        System.out.println(">> User types prefix: 'Che'");
        List<Airport> suggestions1 = airportService.autoSuggestAirports("Che");
        suggestions1.forEach(a -> System.out.printf("   Suggestion -> %s (%s - %s)%n", a.getCity(), a.getIataCode(), a.getAirportName()));

        System.out.println("\n>> User types prefix: 'Sin'");
        List<Airport> suggestions2 = airportService.autoSuggestAirports("Sin");
        suggestions2.forEach(a -> System.out.printf("   Suggestion -> %s (%s - %s)%n", a.getCity(), a.getIataCode(), a.getAirportName()));

        System.out.println("\n>> User types prefix: 'Ben' (Testing inactive filter on Bengaluru BLR):");
        List<Airport> suggestions3 = airportService.autoSuggestAirports("Ben");
        if (suggestions3.isEmpty()) {
            System.out.println("   No active airport suggestions found (BLR is currently marked INACTIVE for maintenance).");
        }

        // --- STEP 5: RENDER PASSENGER AIRPORT GUIDE (9.2) ---
        System.out.println("\n--- 5. Rendering Passenger Airport Guide ---");
        airportService.renderPassengerAirportGuide("MAA");

        System.out.println("==================================================");
        System.out.println(" UC9 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}