package domain.booking;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BookingContext {
    private final String pnr;
    private final String flightNumber;
    private final String userEmail;
    private final double baseFare;
    private double totalFare;

    private List<PassengerInfo> passengers;
    private List<String> selectedSeats;
    private String eTicketNumber;
    private LocalDateTime bookingDate;
    private LocalDateTime paymentExpiryTime;

    private BookingState currentState;

    public BookingContext(String flightNumber, String userEmail, double baseFare) {
        this.pnr = "PNR" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.flightNumber = flightNumber;
        this.userEmail = userEmail;
        this.baseFare = baseFare;
        this.totalFare = baseFare;
        this.passengers = new ArrayList<>();
        this.selectedSeats = new ArrayList<>();
        this.bookingDate = LocalDateTime.now();
        this.currentState = new InitiatedState(); // Initial State

        System.out.println("[BOOKING] Initiated new booking. PNR: " + this.pnr);
    }

    // State Pattern Delegation Methods
    public void addPassengers(List<PassengerInfo> passengers) {
        currentState.addPassengers(this, passengers);
    }

    public void selectSeats(List<String> seats, double seatSurcharges) {
        this.totalFare = this.baseFare * this.passengers.size() + seatSurcharges; // Recalculate fare
        currentState.selectSeats(this, seats);
    }

    public void processPayment(double amount) {
        currentState.processPayment(this, amount);
    }

    public void confirmBooking() {
        currentState.confirmBooking(this);
    }

    public void cancelBooking() {
        currentState.cancelBooking(this);
    }

    // State Transition Helper
    protected void setState(BookingState state) {
        this.currentState = state;
    }

    // Getters and Setters used by States
    public String getPnr() { return pnr; }
    public String getFlightNumber() { return flightNumber; }
    public String getUserEmail() { return userEmail; }
    public List<PassengerInfo> getPassengers() { return passengers; }
    public void setPassengers(List<PassengerInfo> passengers) { this.passengers = passengers; }
    public List<String> getSelectedSeats() { return selectedSeats; }
    public void setSelectedSeats(List<String> selectedSeats) { this.selectedSeats = selectedSeats; }
    public double getTotalFare() { return totalFare; }
    public LocalDateTime getBookingDate() { return bookingDate; }
    public BookingState getCurrentState() { return currentState; }
    public String getETicketNumber() { return eTicketNumber; }

    protected void setETicketNumber(String eTicketNumber) { this.eTicketNumber = eTicketNumber; }
    protected void setPaymentExpiryTime(LocalDateTime time) { this.paymentExpiryTime = time; }

    public boolean isPaymentExpired() {
        return paymentExpiryTime != null && LocalDateTime.now().isAfter(paymentExpiryTime);
    }
}