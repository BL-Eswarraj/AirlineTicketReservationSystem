package domain.payment;

public enum PaymentStatus {
    INITIATED,
    AUTHENTICATING,
    SUCCESS,
    FAILED,
    REFUND_INITIATED,
    REFUNDED
}