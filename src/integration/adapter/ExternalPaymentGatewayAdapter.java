package integration.adapter;

import integration.client.MockRestHttpClient;
import integration.dto.BankPaymentRestRequest;
import integration.dto.BankPaymentRestResponse;

public class ExternalPaymentGatewayAdapter {
    private final MockRestHttpClient httpClient;
    private final String merchantApiKey = "SRM_AIRWAYS_PROD_KEY_9988";
    private final String gatewayEndpoint = "https://api.npci.org.in/v2/upi/merchant/pay";

    public ExternalPaymentGatewayAdapter(MockRestHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    // Translates internal domain call to external REST payload with Retry Logic
    public boolean executeExternalPaymentWithRetry(String pnr, String customerVpa, double amountInr, int maxRetries) {
        System.out.printf("%n[ADAPTER: PAYMENT] Initiating external gateway integration for PNR: %s | Amount: ₹%.2f%n", pnr, amountInr);

        BankPaymentRestRequest requestPayload = new BankPaymentRestRequest(
                merchantApiKey, customerVpa, amountInr, "INR", pnr, "https://srmairways.com/api/callback"
        );

        int attempt = 0;
        while (attempt <= maxRetries) {
            attempt++;
            System.out.printf("[ADAPTER: PAYMENT] Transmission Attempt %d of %d...%n", attempt, maxRetries + 1);

            BankPaymentRestResponse response = httpClient.postPaymentTransaction(gatewayEndpoint, requestPayload);

            if (response.isSuccess()) {
                System.out.printf("✅ [GATEWAY SUCCESS] Transaction confirmed! Gateway ID: %s | Timestamp: %s%n",
                        response.gatewayTransactionId(), response.timestamp());
                return true;
            } else if (response.httpStatusCode() == 503) {
                System.out.printf("⚠️ [GATEWAY ERROR 503] Transient network drop detected. Executing exponential backoff retry...%n");
                try {
                    Thread.sleep(300L * attempt); // Backoff wait
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            } else {
                // Non-retriable error (400 Bad Request, Insufficient Funds, etc.)
                System.out.printf("❌ [GATEWAY REJECTED %d] Payment failed! Error Code: %s%n",
                        response.httpStatusCode(), response.errorCode());
                return false;
            }
        }

        System.out.println("❌ [ADAPTER: PAYMENT] All retry attempts exhausted. Transaction aborted.");
        return false;
    }
}