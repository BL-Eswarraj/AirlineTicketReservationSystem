package service.airport;

import domain.airport.*;
import java.util.*;
import java.util.stream.Collectors;

public class AirportManagementService {
    // Master registry indexed by IATA code
    private final Map<String, Airport> airportRegistry = new LinkedHashMap<>();
    // Secondary index by ICAO code for fast O(1) air traffic control lookups
    private final Map<String, Airport> icaoIndex = new HashMap<>();

    public AirportManagementService() {
        seedDefaultAirports();
    }

    // ==========================================
    // 9.1 AIRPORT INFORMATION MANAGEMENT
    // ==========================================
    public Airport registerAirport(String iata, String icao, String name, String city, String country, String timezone) {
        if (airportRegistry.containsKey(iata.toUpperCase())) {
            throw new IllegalArgumentException("Airport with IATA code " + iata + " is already registered!");
        }

        Airport airport = new Airport(iata, icao, name, city, country, timezone);
        airportRegistry.put(airport.getIataCode(), airport);
        if (!airport.getIcaoCode().isEmpty()) {
            icaoIndex.put(airport.getIcaoCode(), airport);
        }

        System.out.printf("[AIRPORT SETUP] Registered new airport: %s (%s) in %s, %s.%n", name, iata, city, country);
        return airport;
    }

    public void updateAirportDetails(String iataCode, String newName, String newCity, String newCountry, String newTimezone) {
        Airport airport = getAirportOrThrow(iataCode);
        airport.updateDetails(newName, newCity, newCountry, newTimezone);
        System.out.printf("[AIRPORT UPDATE] Updated profile for %s (%s).%n", airport.getAirportName(), iataCode);
    }

    public void setAirportOperationalStatus(String iataCode, boolean isActive, String reason) {
        Airport airport = getAirportOrThrow(iataCode);
        airport.setActive(isActive);
        System.out.printf("[AIRPORT STATUS] Marked %s (%s) as %s. Reason: %s%n",
                airport.getAirportName(), iataCode, isActive ? "ACTIVE" : "INACTIVE", reason);
    }

    public void configureTerminal(String iataCode, Terminal terminal) {
        Airport airport = getAirportOrThrow(iataCode);
        airport.addTerminal(terminal);
        System.out.printf("[TERMINAL SETUP] Added %s (%s) to airport %s. Gates configured: %d.%n",
                terminal.getTerminalName(), terminal.getTerminalId(), iataCode, terminal.getTotalGates());
    }

    public void updateContactDirectory(String iataCode, AirportContact contact) {
        Airport airport = getAirportOrThrow(iataCode);
        airport.setContactDetails(contact);
        System.out.printf("[CONTACT SETUP] Updated directory contacts for %s.%n", iataCode);
    }

    // ==========================================
    // 9.2 AIRPORT SEARCH AND RETRIEVAL
    // ==========================================

    // Search by IATA or ICAO code
    public Optional<Airport> searchByCode(String code) {
        if (code == null) return Optional.empty();
        String cleanCode = code.trim().toUpperCase();

        if (cleanCode.length() == 3) {
            return Optional.ofNullable(airportRegistry.get(cleanCode));
        } else if (cleanCode.length() == 4) {
            return Optional.ofNullable(icaoIndex.get(cleanCode));
        }
        return Optional.empty();
    }

    // Search airport by city name (Case-insensitive partial matching)
    public List<Airport> searchByCity(String cityName) {
        if (cityName == null || cityName.isBlank()) return Collections.emptyList();
        String query = cityName.trim().toLowerCase();

        return airportRegistry.values().stream()
                .filter(a -> a.getCity().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    // Search airport by official name
    public List<Airport> searchByName(String airportName) {
        if (airportName == null || airportName.isBlank()) return Collections.emptyList();
        String query = airportName.trim().toLowerCase();

        return airportRegistry.values().stream()
                .filter(a -> a.getAirportName().toLowerCase().contains(query))
                .collect(Collectors.toList());
    }

    // List all airports by country
    public List<Airport> listAirportsByCountry(String country) {
        if (country == null || country.isBlank()) return Collections.emptyList();

        return airportRegistry.values().stream()
                .filter(a -> a.getCountry().equalsIgnoreCase(country.trim()))
                .sorted(Comparator.comparing(Airport::getCity))
                .collect(Collectors.toList());
    }

    // Auto-suggest airports during flight search (Matches prefix against Code, City, or Name)
    public List<Airport> autoSuggestAirports(String prefixInput) {
        if (prefixInput == null || prefixInput.trim().length() < 2) {
            return Collections.emptyList(); // Require at least 2 characters for meaningful suggestions
        }
        String prefix = prefixInput.trim().toLowerCase();

        return airportRegistry.values().stream()
                .filter(Airport::isActive) // Only suggest active airports for booking
                .filter(a -> a.getIataCode().toLowerCase().startsWith(prefix) ||
                        a.getCity().toLowerCase().startsWith(prefix) ||
                        a.getAirportName().toLowerCase().contains(prefix))
                .limit(5) // Top 5 relevant suggestions for UI dropdown
                .collect(Collectors.toList());
    }

    // 9.2 Passenger-Facing Detailed Display Rendering
    public void renderPassengerAirportGuide(String iataCode) {
        Optional<Airport> airportOpt = searchByCode(iataCode);
        if (airportOpt.isEmpty()) {
            System.out.println("[GUIDE ERROR] Airport code " + iataCode + " not found.");
            return;
        }

        Airport a = airportOpt.get();
        System.out.println("\n=========================================================================");
        System.out.printf(" PASSENGER GUIDE: %s (%s / %s)%n", a.getAirportName().toUpperCase(), a.getIataCode(), a.getIcaoCode());
        System.out.println("=========================================================================");
        System.out.printf("Location     : %s, %s | Timezone: %s%n", a.getCity(), a.getCountry(), a.getTimezone());
        System.out.printf("Status       : %s%n", a.isActive() ? "OPERATIONAL & OPEN" : "TEMPORARILY CLOSED");

        if (a.getContactDetails() != null) {
            System.out.println("Contact Info : " + a.getContactDetails());
        }

        System.out.println("\n--- Terminal Directory (" + a.getTerminals().size() + " Terminals) ---");
        for (Terminal t : a.getTerminals()) {
            System.out.println(" * " + t);
            if (!t.getAvailableFacilities().isEmpty()) {
                String facilityNames = t.getAvailableFacilities().stream()
                        .map(FacilityType::name)
                        .collect(Collectors.joining(", "));
                System.out.println("   -> Amenities: " + facilityNames);
            }
        }

        System.out.println("\n--- Overall Airport Facilities & Passenger Services ---");
        if (a.getAirportFacilities().isEmpty()) {
            System.out.println(" No specialized facilities listed.");
        } else {
            a.getAirportFacilities().forEach(f -> System.out.printf("  [✔] %-20s : %s%n", f.name(), f.getDescription()));
        }
        System.out.println("=========================================================================\n");
    }

    public Airport getAirportOrThrow(String iataCode) {
        Airport airport = airportRegistry.get(iataCode.toUpperCase());
        if (airport == null) {
            throw new NoSuchElementException("Airport with IATA code " + iataCode + " not found.");
        }
        return airport;
    }

    private void seedDefaultAirports() {
        // 1. Chennai International Airport (MAA)
        Airport maa = registerAirport("MAA", "VOMM", "Chennai International Airport", "Chennai", "India", "Asia/Kolkata");
        maa.setContactDetails(new AirportContact("+91-44-22560551", "+91-44-22560555", "support@aaichennai.in", "www.chennaiairport.com"));

        Terminal maaT1 = new Terminal("T1", "Kamaraj Domestic Terminal", true, false, 12);
        maaT1.addFacility(FacilityType.VIP_LOUNGE);
        maaT1.addFacility(FacilityType.FREE_WIFI);
        maaT1.addFacility(FacilityType.FOOD_COURT);

        Terminal maaT4 = new Terminal("T4", "Anna International Terminal", false, true, 18);
        maaT4.addFacility(FacilityType.VIP_LOUNGE);
        maaT4.addFacility(FacilityType.DUTY_FREE);
        maaT4.addFacility(FacilityType.CURRENCY_EXCHANGE);
        maaT4.addFacility(FacilityType.MEDICAL_CENTER);

        maa.addTerminal(maaT1);
        maa.addTerminal(maaT4);
        maa.addFacility(FacilityType.PARKING);
        maa.addFacility(FacilityType.SPECIAL_ASSISTANCE);

        // 2. Indira Gandhi International Airport (DEL)
        Airport del = registerAirport("DEL", "VIDP", "Indira Gandhi International Airport", "New Delhi", "India", "Asia/Kolkata");
        del.setContactDetails(new AirportContact("+91-124-4797300", "+91-11-25652011", "feedback@gmrgroup.in", "www.newdelhiairport.in"));

        Terminal delT3 = new Terminal("T3", "Terminal 3 Integrated", true, true, 45);
        delT3.addFacility(FacilityType.VIP_LOUNGE);
        delT3.addFacility(FacilityType.TRANSIT_HOTEL);
        delT3.addFacility(FacilityType.DUTY_FREE);
        delT3.addFacility(FacilityType.MEDICAL_CENTER);
        del.addTerminal(delT3);

        // 3. Chhatrapati Shivaji Maharaj International Airport (BOM)
        Airport bom = registerAirport("BOM", "VABB", "Chhatrapati Shivaji Maharaj International Airport", "Mumbai", "India", "Asia/Kolkata");
        bom.setContactDetails(new AirportContact("+91-22-66851010", "+91-22-66851000", "info@csia.in", "www.csmia.adaniairports.com"));

        // 4. Kempegowda International Airport (BLR)
        registerAirport("BLR", "VOBL", "Kempegowda International Airport", "Bengaluru", "India", "Asia/Kolkata");

        // 5. Singapore Changi Airport (SIN)
        registerAirport("SIN", "WSSS", "Singapore Changi Airport", "Singapore", "Singapore", "Asia/Singapore");
    }
}