package exception;

public class PaymentFailedException extends AirlineException {
    public PaymentFailedException(String transactionId, double amount, String gatewayError) {
        super(String.format("Payment of ₹%.2f failed for Transaction [%s]. Gateway Response: %s", amount, transactionId, gatewayError),
                "ERR_PAYMENT_402", 402);
    }
}