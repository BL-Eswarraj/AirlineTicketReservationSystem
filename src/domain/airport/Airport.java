package domain.airport;

import java.util.*;

public class Airport {
    private final String iataCode; // 3-letter code (e.g., MAA, DEL, BOM)
    private final String icaoCode; // 4-letter code (e.g., VOMM, VIDP, VABB)
    private String airportName;
    private String city;
    private String country;
    private String timezone; // e.g., "Asia/Kolkata", "UTC+05:30"
    private boolean isActive;

    private final Map<String, Terminal> terminals;
    private final Set<FacilityType> airportFacilities;
    private AirportContact contactDetails;

    public Airport(String iataCode, String icaoCode, String airportName, String city, String country, String timezone) {
        this.iataCode = iataCode != null ? iataCode.toUpperCase().trim() : "";
        this.icaoCode = icaoCode != null ? icaoCode.toUpperCase().trim() : "";
        this.airportName = airportName;
        this.city = city;
        this.country = country;
        this.timezone = timezone;
        this.isActive = true; // Active by default upon creation
        this.terminals = new LinkedHashMap<>();
        this.airportFacilities = new HashSet<>();
    }

    public void addTerminal(Terminal terminal) {
        terminals.put(terminal.getTerminalId().toUpperCase(), terminal);
        // Add terminal facilities to overall airport amenities
        airportFacilities.addAll(terminal.getAvailableFacilities());
    }

    public void addFacility(FacilityType facility) {
        airportFacilities.add(facility);
    }

    public void updateDetails(String newName, String newCity, String newCountry, String newTimezone) {
        if (newName != null && !newName.isBlank()) this.airportName = newName;
        if (newCity != null && !newCity.isBlank()) this.city = newCity;
        if (newCountry != null && !newCountry.isBlank()) this.country = newCountry;
        if (newTimezone != null && !newTimezone.isBlank()) this.timezone = newTimezone;
    }

    public void setActive(boolean active) {
        this.isActive = active;
    }

    public void setContactDetails(AirportContact contactDetails) {
        this.contactDetails = contactDetails;
    }

    // Getters
    public String getIataCode() { return iataCode; }
    public String getIcaoCode() { return icaoCode; }
    public String getAirportName() { return airportName; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public String getTimezone() { return timezone; }
    public boolean isActive() { return isActive; }
    public Collection<Terminal> getTerminals() { return terminals.values(); }
    public Set<FacilityType> getAirportFacilities() { return airportFacilities; }
    public AirportContact getContactDetails() { return contactDetails; }

    @Override
    public String toString() {
        return String.format("%s/%s - %s (%s, %s) | TZ: %s | Status: %s | Terminals: %d",
                iataCode, icaoCode, airportName, city, country, timezone, isActive ? "ACTIVE" : "INACTIVE", terminals.size());
    }
}