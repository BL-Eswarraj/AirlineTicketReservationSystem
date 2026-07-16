package integration.client;

import integration.dto.*;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

// Simulates java.net.http.HttpClient communicating over external network boundaries
public class MockRestHttpClient {
    private static final Random random = new Random();
    private boolean simulateNetworkInstability = false;

    public void setSimulateNetworkInstability(boolean unstable) {
        this.simulateNetworkInstability = unstable;
    }

    // Simulate POST request to Bank Payment Gateway
    public BankPaymentRestResponse postPaymentTransaction(String url, BankPaymentRestRequest request) {
        System.out.printf("[HTTP CLIENT] POST %s%n[HTTP CLIENT] Payload:%n%s%n", url, request.toJsonPayload());
        simulateNetworkLatency(200);

        if (simulateNetworkInstability && random.nextInt(10) > 6) {
            System.out.println("[HTTP CLIENT] ⚠️ 503 Service Unavailable: Gateway connection dropped!");
            return new BankPaymentRestResponse(503, "NONE", "FAILED", "ERR_GATEWAY_TIMEOUT", LocalDateTime.now().toString());
        }

        // Simulate intentional rejection for test amounts equal to ₹13.00
        if (request.amountInInr() == 13.00) {
            return new BankPaymentRestResponse(400, "NONE", "FAILED", "ERR_INSUFFICIENT_FUNDS", LocalDateTime.now().toString());
        }

        String txId = "BANK-REST-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        return new BankPaymentRestResponse(200, txId, "SUCCESS", "NONE", LocalDateTime.now().toString());
    }

    // Simulate POST request to SMS/WhatsApp API
    public SmsGatewayRestResponse postSmsDispatch(String url, SmsGatewayRestRequest request) {
        System.out.printf("[HTTP CLIENT] POST %s%n[HTTP CLIENT] Payload:%n%s%n", url, request.toJsonPayload());
        simulateNetworkLatency(80);

        String msgId = "SMS-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        return new SmsGatewayRestResponse(200, msgId, "DELIVERED_TO_CARRIER", 0.25);
    }

    // Simulate GET request to Meteorological & Air Traffic Control API
    public WeatherAtcRestResponse getAirportTelemetry(String airportIata) {
        String url = "https://api.aviationweather.gov/v1/telemetry/" + airportIata.toUpperCase();
        System.out.printf("[HTTP CLIENT] GET %s%n", url);
        simulateNetworkLatency(120);

        return switch (airportIata.toUpperCase()) {
            case "MAA" -> new WeatherAtcRestResponse(200, "MAA", "CLEAR_SKIES", 18.5, true, "Airspace operational. Normal clearance.");
            case "DEL" -> new WeatherAtcRestResponse(200, "DEL", "DENSE_FOG", 22.0, true, "Low visibility operations (CAT-III) in effect.");
            case "COK" -> new WeatherAtcRestResponse(200, "COK", "HEAVY_MONSOON_SQUALL", 92.0, false, "CRITICAL: Airspace closed due to severe crosswinds!");
            default -> new WeatherAtcRestResponse(404, airportIata, "UNKNOWN", 0.0, false, "Airport IATA code not tracked by ATC.");
        };
    }

    private void simulateNetworkLatency(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}