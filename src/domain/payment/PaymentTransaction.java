package domain.payment;

import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentTransaction {
    private final String transactionId;
    private final String pnr;
    private final double originalAmount;
    private double discountApplied;
    private double finalPaidAmount;
    private final String paymentMethodName;
    private PaymentStatus status;
    private final LocalDateTime timestamp;
    private String refundTransactionId;
    private double refundedAmount;

    public PaymentTransaction(String pnr, double originalAmount, double discountApplied, String paymentMethodName) {
        this.transactionId = "TXN-" + UUID.randomUUID().toString().substring(0, 10).toUpperCase();
        this.pnr = pnr;
        this.originalAmount = originalAmount;
        this.discountApplied = discountApplied;
        this.finalPaidAmount = originalAmount - discountApplied;
        this.paymentMethodName = paymentMethodName;
        this.status = PaymentStatus.INITIATED;
        this.timestamp = LocalDateTime.now();
    }

    public void markSuccess() { this.status = PaymentStatus.SUCCESS; }
    public void markFailed() { this.status = PaymentStatus.FAILED; }

    public void setRefundDetails(String refundTxId, double refundAmount) {
        this.refundTransactionId = refundTxId;
        this.refundedAmount = refundAmount;
        this.status = PaymentStatus.REFUNDED;
    }

    // Getters
    public String getTransactionId() { return transactionId; }
    public String getPnr() { return pnr; }
    public double getOriginalAmount() { return originalAmount; }
    public double getDiscountApplied() { return discountApplied; }
    public double getFinalPaidAmount() { return finalPaidAmount; }
    public String getPaymentMethodName() { return paymentMethodName; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getRefundTransactionId() { return refundTransactionId; }
    public double getRefundedAmount() { return refundedAmount; }

    @Override
    public String toString() {
        return String.format("[%s] TxID: %s | PNR: %s | Paid: ₹%.2f (%s) | Date: %s",
                status, transactionId, pnr, finalPaidAmount, paymentMethodName, timestamp.toLocalTime());
    }
}