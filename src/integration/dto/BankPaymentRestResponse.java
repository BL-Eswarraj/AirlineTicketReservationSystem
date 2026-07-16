package integration.dto;

public record BankPaymentRestResponse(
        int httpStatusCode,
        String gatewayTransactionId,
        String paymentStatus, // "SUCCESS", "FAILED", "PENDING"
        String errorCode,
        String timestamp
) {
    public boolean isSuccess() {
        return httpStatusCode == 200 && "SUCCESS".equalsIgnoreCase(paymentStatus);
    }
}