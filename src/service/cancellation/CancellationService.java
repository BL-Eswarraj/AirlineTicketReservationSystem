package service.cancellation;

import java.util.List;
import java.util.Optional;

public class CancellationService {

    // 7.1 Full Booking Cancellation
    public RefundBreakdown processFullCancellation(CancellableBooking booking) {
        System.out.println("\n--- INITIATING FULL CANCELLATION FOR PNR: " + booking.getPnr() + " ---");

        if (booking.getBookingStatus().equals("CANCELLED")) {
            System.out.println("[CANCELLATION ERROR] Booking is already cancelled!");
            return null;
        }

        // 1. Calculate Refund based on policy
        double totalPaid = booking.getTotalActivePaidAmount();
        double totalTax = booking.getTotalActiveTaxAmount();
        RefundBreakdown breakdown = CancellationPolicy.calculateRefund(totalPaid, totalTax, booking.getTicketType(), booking.getDepartureTime());

        System.out.println("[CANCELLATION POLICY DISPLAY]");
        System.out.println("Rule Applied: " + breakdown.policyReason());
        System.out.println(breakdown);
        System.out.println("--------------------------------------------------");

        if (!breakdown.isEligibleForRefund()) {
            System.out.println("[CANCELLATION ABORTED] Booking is ineligible for cancellation/refund.");
            return breakdown;
        }

        // 2. Release allocated seats & cancel all active passengers
        List<CancellablePassenger> activeList = booking.getActivePassengers();
        for (CancellablePassenger p : activeList) {
            p.cancel();
            releaseSeatInventory(booking.getFlightNumber(), p.getAssignedSeat());
        }

        // 3. Update status & trigger refund
        booking.updateStatus("CANCELLED");
        triggerRefundGateway(booking.getPnr(), breakdown.netRefundAmount());
        sendNotification(booking.getPnr(), "Full booking cancelled. Refund of ₹" + breakdown.netRefundAmount() + " initiated to original payment source.");

        return breakdown;
    }

    // 7.2 Partial Booking Cancellation
    public RefundBreakdown processPartialCancellation(CancellableBooking booking, List<String> passengerIdsToCancel) {
        System.out.println("\n--- INITIATING PARTIAL CANCELLATION FOR PNR: " + booking.getPnr() + " ---");
        System.out.println("Target Passenger IDs to Cancel: " + passengerIdsToCancel);

        if (booking.getBookingStatus().equals("CANCELLED")) {
            System.out.println("[CANCELLATION ERROR] Cannot perform partial cancellation on a completely cancelled booking.");
            return null;
        }

        double totalCancelledPaid = 0.0;
        double totalCancelledTax = 0.0;
        int successfullyCancelledCount = 0;

        for (String targetId : passengerIdsToCancel) {
            Optional<CancellablePassenger> passOpt = booking.getAllPassengers().stream()
                    .filter(p -> p.getPassengerId().equalsIgnoreCase(targetId) && !p.isCancelled())
                    .findFirst();

            if (passOpt.isPresent()) {
                CancellablePassenger p = passOpt.get();
                totalCancelledPaid += p.getIndividualFarePaid();
                totalCancelledTax += p.getIndividualTaxPaid();
                p.cancel();
                releaseSeatInventory(booking.getFlightNumber(), p.getAssignedSeat());
                successfullyCancelledCount++;
            } else {
                System.out.println("[WARNING] Passenger ID " + targetId + " not found or already cancelled. Skipping.");
            }
        }

        if (successfullyCancelledCount == 0) {
            System.out.println("[PARTIAL CANCELLATION ABORTED] No valid active passengers found to cancel.");
            return null;
        }

        // Check if ALL active passengers ended up being cancelled
        if (booking.getActivePassengers().isEmpty()) {
            System.out.println("[STATE UPDATE] All passengers cancelled. Converting status to full CANCELLED.");
            booking.updateStatus("CANCELLED");
        } else {
            booking.updateStatus("PARTIALLY_CANCELLED");
            booking.regenerateETicket(); // 7.2 requirement: Update e-ticket for remaining passengers
        }

        // Calculate refund only for the cancelled passengers' share
        RefundBreakdown breakdown = CancellationPolicy.calculateRefund(totalCancelledPaid, totalCancelledTax, booking.getTicketType(), booking.getDepartureTime());
        System.out.println("[PARTIAL REFUND BREAKDOWN] For " + successfullyCancelledCount + " cancelled passenger(s):");
        System.out.println(breakdown);

        triggerRefundGateway(booking.getPnr() + "-PARTIAL", breakdown.netRefundAmount());
        sendNotification(booking.getPnr(), "Partial cancellation processed for " + successfullyCancelledCount + " passenger(s). Updated E-Ticket generated for remaining travelers.");

        return breakdown;
    }

    // 7.1 & 7.2 Simulated Inventory Release
    private void releaseSeatInventory(String flightNumber, String seatNumber) {
        System.out.printf("[INVENTORY UPDATE] Seat %s on flight %s released back to available flight seat inventory (+1 available seat).%n",
                seatNumber, flightNumber);
    }

    // Simulated Refund Processing
    private void triggerRefundGateway(String referenceId, double amount) {
        if (amount > 0) {
            System.out.printf("[REFUND GATEWAY] Processing refund of ₹%.2f for Reference: %s. Estimated credit: 3-5 business days.%n",
                    amount, referenceId);
        } else {
            System.out.println("[REFUND GATEWAY] Net refund amount is ₹0.00. No gateway transaction required.");
        }
    }

    private void sendNotification(String pnr, String message) {
        System.out.printf("[NOTIFICATION SERVICE] Alert sent to primary contact for PNR %s -> \"%s\"%n", pnr, message);
    }
}