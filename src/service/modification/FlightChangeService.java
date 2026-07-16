package service.modification;

import java.time.LocalDateTime;
import java.util.List;

public class FlightChangeService {

    // 6.1 Flight Change / Modification Workflow
    public boolean processFlightChange(ModifiableBooking booking, String newFlightNumber,
                                       LocalDateTime newDepartureTime, double newFlightBaseFare,
                                       List<String> newSeats, double newSeatSurcharge) {

        System.out.println("\n--- INITIATING FLIGHT CHANGE FOR PNR: " + booking.getPnr() + " ---");

        // 1. Validate Eligibility
        if (!ModificationPolicy.isEligibleForModification(booking.getBookingTimestamp(), booking.getDepartureTime())) {
            return false;
        }

        // 2. Calculate Fare Difference
        double fareDifference = newFlightBaseFare - booking.getBaseFarePaid();
        double seatDifference = newSeatSurcharge - booking.getSeatSurchargesPaid();
        double netFareDifference = fareDifference + seatDifference;

        // 3. Calculate Modification Penalty
        double penalty = ModificationPolicy.calculateModificationFee(ModificationType.FLIGHT_CHANGE, booking.getBookingTimestamp());
        double totalPayableOrRefundable = netFareDifference + penalty;

        System.out.println("[FARE ANALYSIS] Current Base Fare Paid: ₹" + booking.getBaseFarePaid());
        System.out.println("[FARE ANALYSIS] New Flight Base Fare: ₹" + newFlightBaseFare);
        System.out.printf("[FARE ANALYSIS] Net Fare Difference: %s₹%.2f%n", (netFareDifference >= 0 ? "+" : ""), netFareDifference);
        System.out.println("[FARE ANALYSIS] Modification Penalty: ₹" + penalty);
        System.out.println("--------------------------------------------------");

        if (totalPayableOrRefundable > 0) {
            System.out.printf("[PAYMENT REQUIRED] Additional amount to pay: ₹%.2f. Simulating payment processing... SUCCESS.%n", totalPayableOrRefundable);
        } else if (totalPayableOrRefundable < 0) {
            System.out.printf("[REFUND ELIGIBLE] Net refund due to passenger: ₹%.2f. Simulating refund initiation... SUCCESS.%n", Math.abs(totalPayableOrRefundable));
        } else {
            System.out.println("[FINANCIALS] No fare difference or penalties applicable.");
        }

        // 4. Release Old Inventory
        System.out.printf("[INVENTORY] Releasing old seat allocation %s on flight %s back to available inventory.%n",
                booking.getAssignedSeats(), booking.getFlightNumber());

        // 5. Update Booking Record
        booking.updateFlight(newFlightNumber, newDepartureTime, newFlightBaseFare);
        booking.updateSeats(newSeats, newSeatSurcharge);
        booking.regenerateETicket();

        // 6. Send Notification
        sendModificationAlert(booking, "Flight successfully changed to " + newFlightNumber + " departing on " + newDepartureTime.toLocalDate());
        return true;
    }

    private void sendModificationAlert(ModifiableBooking booking, String message) {
        System.out.printf("[NOTIFICATION] Sent SMS/Email to %s (%s): %s%n",
                booking.getPassengerName(), booking.getContactEmail(), message);
    }
}