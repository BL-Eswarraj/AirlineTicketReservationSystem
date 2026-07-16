package service.cancellation;

public record RefundBreakdown(
        double originalPaidAmount,
        double cancellationFeeAmount,
        double penaltyPercentage,
        double netRefundAmount,
        boolean isEligibleForRefund,
        String policyReason
) {
    @Override
    public String toString() {
        return String.format("Original Paid: ₹%.2f | Penalty (%.1f%%): -₹%.2f | NET REFUND: ₹%.2f (%s)",
                originalPaidAmount, penaltyPercentage, cancellationFeeAmount, netRefundAmount, policyReason);
    }
}