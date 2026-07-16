import integration.adapter.*;
import integration.client.MockRestHttpClient;

public class ThirdPartyIntegrationDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC18: THIRD-PARTY INTEGRATION & REST APIS DEMO ");
        System.out.println("==================================================");

        MockRestHttpClient restClient = new MockRestHttpClient();

        ExternalPaymentGatewayAdapter paymentAdapter = new ExternalPaymentGatewayAdapter(restClient);
        ExternalSmsGatewayAdapter smsAdapter = new ExternalSmsGatewayAdapter(restClient);
        AirTrafficControlWeatherAdapter atcAdapter = new AirTrafficControlWeatherAdapter(restClient);

        // --- STEP 1: SUCCESSFUL EXTERNAL PAYMENT INTEGRATION ---
        System.out.println("\n--- 1. Testing Standard REST Payment Integration ---");
        paymentAdapter.executeExternalPaymentWithRetry("PNR-REST-101", "anbu@okhdfcbank", 5500.00, 2);

        // --- STEP 2: NETWORK INSTABILITY & AUTOMATIC RETRY SELF-HEALING ---
        System.out.println("\n--- 2. Simulating Gateway 503 Timeout & Automatic Retry ---");
        restClient.setSimulateNetworkInstability(true); // Turn on random 503 failures
        paymentAdapter.executeExternalPaymentWithRetry("PNR-RETRY-202", "ramesh@icici", 12000.00, 3);
        restClient.setSimulateNetworkInstability(false); // Turn off after test

        // --- STEP 3: NON-RETRIABLE GATEWAY REJECTION (400 BAD REQUEST) ---
        System.out.println("\n--- 3. Testing Non-Retriable Gateway Rejection (Insufficient Funds) ---");
        // Sending ₹13.00 triggers our mock client's intentional 400 Bad Request
        paymentAdapter.executeExternalPaymentWithRetry("PNR-FAIL-303", "test@sbi", 13.00, 2);

        // --- STEP 4: EXTERNAL SMS/WHATSAPP DISPATCH ---
        System.out.println("\n--- 4. Testing External SMS Gateway Dispatch ---");
        smsAdapter.dispatchTransactionalAlert("+919800000000", "Dear Annadurai Anbarasu, your e-ticket for AI-101 (MAA->DEL) has been generated. Happy flying!");

        // --- STEP 5: LIVE ATC TELEMETRY & WEATHER SAFETY CLEARANCES ---
        System.out.println("\n--- 5. Testing External Air Traffic Control & Weather API Integration ---");

        // Check 1: Normal operations at Chennai (MAA)
        atcAdapter.verifyFlightDepartureClearance("AI-101", "MAA");

        // Check 2: Low visibility operations at Delhi (DEL)
        atcAdapter.verifyFlightDepartureClearance("UK-808", "DEL");

        // Check 3: Airspace closure at Cochin (COK) due to severe storm -> Takeoff Denied!
        atcAdapter.verifyFlightDepartureClearance("6E-555", "COK");

        System.out.println("\n==================================================");
        System.out.println(" UC18 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}