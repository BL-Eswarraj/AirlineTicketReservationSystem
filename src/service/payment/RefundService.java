package service.payment;

import domain.payment.PaymentMethod;
import domain.payment.PaymentStatus;
import domain.payment.PaymentTransaction;
import java.util.UUID;

public class RefundService {

    // 5.4 Refund Processing Engine
    public boolean processRefund(PaymentTransaction tx, PaymentMethod method, double cancellationFeePercentage) {
        System.out.println("\n--- INITIATING REFUND FOR TXN: " + tx.getTransactionId() + " ---");

        if (tx.getStatus() != PaymentStatus.SUCCESS) {
            System.out.println("[REFUND ERROR] Only successful transactions can be refunded. Current status: " + tx.getStatus());
            return false;
        }

        double paidAmount = tx.getFinalPaidAmount();
        double cancellationDeduction = (paidAmount * cancellationFeePercentage) / 100.0;
        double netRefundAmount = paidAmount - cancellationDeduction;

        System.out.printf("[REFUND CALCULATION] Total Paid: ₹%.2f | Cancellation Fee (%.1f%%): -₹%.2f | Net Refund: ₹%.2f%n",
                paidAmount, cancellationFeePercentage, cancellationDeduction, netRefundAmount);

        String refundTxId = "REF-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        System.out.println("[REFUND GATEWAY] Instructing payment gateway to route refund to original source...");
        boolean gatewaySuccess = method.refund(tx.getTransactionId(), netRefundAmount);

        if (gatewaySuccess) {
            tx.setRefundDetails(refundTxId, netRefundAmount);
            System.out.println("[REFUND SUCCESS] Refund Transaction ID: " + refundTxId + ". Booking status updated to REFUNDED.");
            return true;
        } else {
            System.out.println("[REFUND FAILED] Gateway could not process refund.");
            return false;
        }
    }
}