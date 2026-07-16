package service.cancellation;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class CancellationPolicy {

    // 7.3 Cancellation Policy Management Engine
    public static RefundBreakdown calculateRefund(double paidAmount, double taxAmount, TicketType ticketType, LocalDateTime flightDepartureTime) {
        long hoursToDeparture = ChronoUnit.HOURS.between(LocalDateTime.now(), flightDepartureTime);

        // Case 1: Flight has already departed
        if (hoursToDeparture < 0) {
            return new RefundBreakdown(paidAmount, paidAmount, 100.0, 0.0, false, "Flight has already departed. No-show penalty applied.");
        }

        // Case 2: Non-Refundable Tickets (Only government taxes are returned)
        if (ticketType == TicketType.NON_REFUNDABLE) {
            double penalty = paidAmount - taxAmount;
            double percentage = (penalty / paidAmount) * 100.0;
            return new RefundBreakdown(paidAmount, penalty, percentage, taxAmount, true, "Non-Refundable Ticket: Only taxes (₹" + taxAmount + ") refunded.");
        }

        // Case 3: Flexible Tickets (0% fee up to 2 hours before departure)
        if (ticketType == TicketType.FLEXIBLE) {
            if (hoursToDeparture >= 2) {
                return new RefundBreakdown(paidAmount, 0.0, 0.0, paidAmount, true, "Flexible Ticket: 100% full refund granted.");
            } else {
                double penalty = paidAmount * 0.20; // 20% late cancellation fee within 2 hours
                return new RefundBreakdown(paidAmount, penalty, 20.0, paidAmount - penalty, true, "Flexible Ticket: 20% late cancellation fee applied (< 2 hours).");
            }
        }

        // Case 4: Standard Tickets (Tiered time-based penalties)
        if (hoursToDeparture >= 24) {
            // Full refund or minimal processing fee 24+ hours before departure (Section 7.3 requirement)
            double processingFee = 500.0;
            double penalty = Math.min(processingFee, paidAmount);
            double percentage = (penalty / paidAmount) * 100.0;
            return new RefundBreakdown(paidAmount, penalty, percentage, paidAmount - penalty, true, "Standard Ticket (> 24 hrs): Flat ₹500 processing fee deducted.");
        } else if (hoursToDeparture >= 4) {
            double penalty = paidAmount * 0.30; // 30% fee between 4 to 24 hours
            return new RefundBreakdown(paidAmount, penalty, 30.0, paidAmount - penalty, true, "Standard Ticket (4-24 hrs): 30% cancellation charge deducted.");
        } else {
            double penalty = paidAmount * 0.80; // 80% last-minute charge (< 4 hours)
            return new RefundBreakdown(paidAmount, penalty, 80.0, paidAmount - penalty, true, "Standard Ticket (< 4 hrs): 80% last-minute cancellation fee deducted.");
        }
    }
}