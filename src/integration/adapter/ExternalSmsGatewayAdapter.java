package integration.adapter;

import integration.client.MockRestHttpClient;
import integration.dto.SmsGatewayRestRequest;
import integration.dto.SmsGatewayRestResponse;

public class ExternalSmsGatewayAdapter {
    private final MockRestHttpClient httpClient;
    private final String smsEndpoint = "https://api.msg91.com/api/v5/flow/";

    public ExternalSmsGatewayAdapter(MockRestHttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public void dispatchTransactionalAlert(String phoneNumber, String message) {
        System.out.println("\n[ADAPTER: SMS] Formulating external messaging payload...");
        SmsGatewayRestRequest request = new SmsGatewayRestRequest(
                "KEY_TWILIO_SECRET_887", phoneNumber, message, "SRMAIR"
        );

        SmsGatewayRestResponse response = httpClient.postSmsDispatch(smsEndpoint, request);

        if (response.httpStatusCode() == 200) {
            System.out.printf("✅ [SMS DISPATCHED] Carrier confirmed delivery! Message ID: %s | Cost: ₹%.2f%n",
                    response.messageId(), response.costInr());
        } else {
            System.out.printf("❌ [SMS FAILED] HTTP Status: %d%n", response.httpStatusCode());
        }
    }
}