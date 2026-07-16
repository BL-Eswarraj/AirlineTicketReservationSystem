package service.modification;

import java.util.List;

public class SeatChangeService {

    // 6.3 Seat Change / Upgrade Workflow
    public boolean changeSeats(ModifiableBooking booking, List<String> newSeatNumbers, double newTotalSeatSurcharge) {
        System.out.println("\n--- INITIATING SEAT CHANGE FOR PNR: " + booking.getPnr() + " ---");

        if (!ModificationPolicy.isEligibleForModification(booking.getBookingTimestamp(), booking.getDepartureTime())) {
            return false;
        }

        System.out.println("[CURRENT SEAT ASSIGNMENT] " + booking.getAssignedSeats() + " (Surcharge paid: ₹" + booking.getSeatSurchargesPaid() + ")");
        System.out.println("[NEW SEAT SELECTION] " + newSeatNumbers + " (New Surcharge: ₹" + newTotalSeatSurcharge + ")");

        double surchargeDifference = newTotalSeatSurcharge - booking.getSeatSurchargesPaid();
        double fee = ModificationPolicy.calculateModificationFee(ModificationType.SEAT_CHANGE, booking.getBookingTimestamp());
        double totalToPay = surchargeDifference + fee;

        if (totalToPay > 0) {
            System.out.printf("[PAYMENT REQUIRED] Upgrade & change charges total: ₹%.2f. Simulating payment... SUCCESS.%n", totalToPay);
        } else if (totalToPay < 0) {
            System.out.printf("[REFUND NOTICE] Seat downgrade refund due: ₹%.2f. Crediting original payment source... SUCCESS.%n", Math.abs(totalToPay));
        } else {
            System.out.println("[FINANCIALS] Even exchange. No payment required.");
        }

        // Release old seat and bind new seat
        System.out.println("[INVENTORY] Releasing old seat " + booking.getAssignedSeats() + " back to seat map.");
        booking.updateSeats(newSeatNumbers, newTotalSeatSurcharge);

        System.out.println("[SUCCESS] Seat change confirmed. New seat assignment: " + booking.getAssignedSeats());
        return true;
    }
}