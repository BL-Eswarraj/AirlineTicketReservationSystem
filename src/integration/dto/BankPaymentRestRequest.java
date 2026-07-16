package integration.dto;

// Represents the external JSON payload sent to third-party bank gateways (e.g., Razorpay / Stripe / NPCI)
public record BankPaymentRestRequest(
        String merchantId,
        String customerIdentifier, // UPI VPA or Masked Card
        double amountInInr,
        String currency,
        String orderRefId,
        String callbackUrl
) {
    public String toJsonPayload() {
        return """
                {
                  "merchant_id": "%s",
                  "customer_id": "%s",
                  "amount": %.2f,
                  "currency": "%s",
                  "order_ref": "%s",
                  "callback_url": "%s"
                }""".formatted(merchantId, customerIdentifier, amountInInr, currency, orderRefId, callbackUrl);
    }
}