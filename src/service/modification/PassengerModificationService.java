package service.modification;

public class PassengerModificationService {

    // 6.2 Allow name correction with supporting documentation validation
    public boolean correctPassengerName(ModifiableBooking booking, String correctedName, String supportingDocumentId) {
        System.out.println("\n--- INITIATING NAME CORRECTION FOR PNR: " + booking.getPnr() + " ---");

        if (!ModificationPolicy.isEligibleForModification(booking.getBookingTimestamp(), booking.getDepartureTime())) {
            return false;
        }

        // Validate that document ID matches stored passport/ID to prevent ticket transfer fraud
        if (supportingDocumentId == null || !supportingDocumentId.equalsIgnoreCase(booking.getPassportOrIdNumber())) {
            System.out.println("[SECURITY ERROR] Name correction rejected! Supporting ID document (" + supportingDocumentId +
                    ") does not match the original registered ID (" + booking.getPassportOrIdNumber() + "). Ticket transfers are strictly forbidden.");
            return false;
        }

        double fee = ModificationPolicy.calculateModificationFee(ModificationType.NAME_CORRECTION, booking.getBookingTimestamp());
        if (fee > 0) {
            System.out.printf("[PAYMENT REQUIRED] Name correction fee applied: ₹%.2f. Payment processed.%n", fee);
        }

        String oldName = booking.getPassengerName();
        booking.updatePassengerName(correctedName);
        booking.regenerateETicket(); // Mandatory new e-ticket for name changes

        System.out.printf("[SUCCESS] Passenger name updated from '%s' to '%s'.%n", oldName, correctedName);
        return true;
    }

    // 6.2 Contact Information & Preferences Update (Free)
    public void updateContactDetails(ModifiableBooking booking, String newEmail, String newPhone) {
        System.out.println("\n--- UPDATING CONTACT DETAILS ---");
        booking.updateContact(newEmail, newPhone);
        System.out.printf("[SUCCESS] Contact details updated to: Email=%s | Phone=%s%n", newEmail, newPhone);
    }

    public void updateTravelPreferences(ModifiableBooking booking, String mealPreference, String specialAssistance) {
        System.out.println("\n--- UPDATING TRAVEL PREFERENCES ---");
        booking.updatePreferences(mealPreference, specialAssistance);
        System.out.printf("[SUCCESS] Preferences updated: Meal=%s | Assistance=%s%n", mealPreference, specialAssistance);
    }
}