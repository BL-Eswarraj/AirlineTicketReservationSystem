package integration.adapter;

import integration.client.MockRestHttpClient;
import integration.dto.WeatherAtcRestResponse;

public class AirTrafficControlWeatherAdapter {
    private final MockRestHttpClient httpClient;

    public AirTrafficControlWeatherAdapter(MockRestHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    // Evaluates external weather telemetry before allowing a flight to depart
    public boolean verifyFlightDepartureClearance(String flightNumber, String airportIata) {
        System.out.printf("%n[ADAPTER: ATC TELEMETRY] Requesting live Air Traffic Control clearance for %s departing from %s...%n",
                flightNumber, airportIata);

        WeatherAtcRestResponse telemetry = httpClient.getAirportTelemetry(airportIata);

        System.out.println("+-----------------------------------------------------------------+");
        System.out.printf("| 🛰️ LIVE ATC & WEATHER TELEMETRY: %-30s |%n", airportIata.toUpperCase());
        System.out.println("+-----------------------------------------------------------------+");
        System.out.printf("| Weather Condition : %-43s |%n", telemetry.weatherCondition());
        System.out.printf("| Wind Speed        : %-43s |%n", telemetry.windSpeedKmh() + " km/h");
        System.out.printf("| Airspace Status   : %-43s |%n", telemetry.isAirspaceOpen() ? "OPEN & OPERATIONAL" : "CLOSED - DANGER");
        System.out.printf("| Advisory Notice   : %-43s |%n", telemetry.advisoryMessage());
        System.out.println("+-----------------------------------------------------------------+");

        if (telemetry.isSafeForDeparture()) {
            System.out.printf("✅ [ATC CLEARANCE GRANTED] %s is cleared for takeoff from %s.%n", flightNumber, airportIata);
            return true;
        } else {
            System.out.printf("🚫 [ATC CLEARANCE DENIED] Takeoff halted for %s! Telemetry safety thresholds violated.%n", flightNumber);
            return false;
        }
    }
}