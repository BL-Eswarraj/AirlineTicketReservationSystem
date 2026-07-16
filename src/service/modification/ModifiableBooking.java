package service.modification;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ModifiableBooking {
    private final String pnr;
    private String flightNumber;
    private String route;
    private LocalDateTime departureTime;
    private double baseFarePaid;
    private double seatSurchargesPaid;
    private double totalAmountPaid;
    private final LocalDateTime bookingTimestamp;
    private String eTicketNumber;

    // Passenger Details
    private String passengerName;
    private String passportOrIdNumber;
    private String contactEmail;
    private String contactPhone;
    private String mealPreference;
    private String specialAssistance;
    private final List<String> assignedSeats;

    public ModifiableBooking(String pnr, String flightNumber, String route, LocalDateTime departureTime,
                             double baseFarePaid, double seatSurchargesPaid, String passengerName,
                             String passportOrIdNumber, String contactEmail, String contactPhone) {
        this.pnr = pnr;
        this.flightNumber = flightNumber;
        this.route = route;
        this.departureTime = departureTime;
        this.baseFarePaid = baseFarePaid;
        this.seatSurchargesPaid = seatSurchargesPaid;
        this.totalAmountPaid = baseFarePaid + seatSurchargesPaid;
        this.bookingTimestamp = LocalDateTime.now().minusDays(2); // Simulated older booking
        this.eTicketNumber = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

        this.passengerName = passengerName;
        this.passportOrIdNumber = passportOrIdNumber;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.mealPreference = "STANDARD";
        this.specialAssistance = "NONE";
        this.assignedSeats = new ArrayList<>(List.of("14A"));
    }

    public void updateFlight(String newFlightNumber, LocalDateTime newDepartureTime, double newBaseFare) {
        this.flightNumber = newFlightNumber;
        this.departureTime = newDepartureTime;
        this.baseFarePaid = newBaseFare;
        recalculateTotal();
    }

    public void updatePassengerName(String newName) {
        this.passengerName = newName;
    }

    public void updateContact(String newEmail, String newPhone) {
        this.contactEmail = newEmail;
        this.contactPhone = newPhone;
    }

    public void updatePreferences(String meal, String assistance) {
        this.mealPreference = meal;
        this.specialAssistance = assistance;
    }

    public void updateSeats(List<String> newSeats, double newSeatSurcharge) {
        this.assignedSeats.clear();
        this.assignedSeats.addAll(newSeats);
        this.seatSurchargesPaid = newSeatSurcharge;
        recalculateTotal();
    }

    public void regenerateETicket() {
        this.eTicketNumber = "TKT-REV-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        System.out.println("[TICKET SYSTEM] Revised E-Ticket generated: " + this.eTicketNumber);
    }

    private void recalculateTotal() {
        this.totalAmountPaid = this.baseFarePaid + this.seatSurchargesPaid;
    }

    // Getters
    public String getPnr() { return pnr; }
    public String getFlightNumber() { return flightNumber; }
    public String getRoute() { return route; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public double getBaseFarePaid() { return baseFarePaid; }
    public double getSeatSurchargesPaid() { return seatSurchargesPaid; }
    public double getTotalAmountPaid() { return totalAmountPaid; }
    public LocalDateTime getBookingTimestamp() { return bookingTimestamp; }
    public String getETicketNumber() { return eTicketNumber; }
    public String getPassengerName() { return passengerName; }
    public String getPassportOrIdNumber() { return passportOrIdNumber; }
    public String getContactEmail() { return contactEmail; }
    public String getContactPhone() { return contactPhone; }
    public String getMealPreference() { return mealPreference; }
    public String getSpecialAssistance() { return specialAssistance; }
    public List<String> getAssignedSeats() { return assignedSeats; }

    @Override
    public String toString() {
        return String.format("PNR: %s | Flight: %s (%s) | Dep: %s | Passenger: %s | Seats: %s | Total Paid: ₹%.2f | E-Ticket: %s",
                pnr, flightNumber, route, departureTime.toLocalDate(), passengerName, assignedSeats, totalAmountPaid, eTicketNumber);
    }
}