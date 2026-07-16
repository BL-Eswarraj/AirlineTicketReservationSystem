package integration.dto;

// Telemetry retrieved from external Civil Aviation Authority / Meteorological APIs
public record WeatherAtcRestResponse(
        int httpStatusCode,
        String airportIataCode,
        String weatherCondition,
        double windSpeedKmh,
        boolean isAirspaceOpen,
        String advisoryMessage
) {
    public boolean isSafeForDeparture() {
        return httpStatusCode == 200 && isAirspaceOpen && windSpeedKmh < 85.0;
    }
}