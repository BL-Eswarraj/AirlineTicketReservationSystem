package domain.booking;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

class InitiatedState implements BookingState {
    @Override
    public void addPassengers(BookingContext context, List<PassengerInfo> passengers) {
        if (passengers.stream().anyMatch(p -> !p.isValid())) {
            System.out.println("[ERROR] Invalid passenger details. Ensure name and ID are provided.");
            return;
        }
        context.setPassengers(passengers);
        System.out.println("[STATE: INITIATED] " + passengers.size() + " passenger(s) added. Transitioning to PASSENGER_DETAILS.");
        context.setState(new PassengerDetailsState());
    }
    @Override
    public void selectSeats(BookingContext context, List<String> seats) { System.out.println("[ERROR] Add passengers before selecting seats."); }
    @Override
    public void processPayment(BookingContext context, double amount) { System.out.println("[ERROR] Cannot pay yet."); }
    @Override
    public void confirmBooking(BookingContext context) { System.out.println("[ERROR] Cannot confirm yet."); }
    @Override
    public void cancelBooking(BookingContext context) {
        System.out.println("[STATE: INITIATED] Booking cancelled.");
        context.setState(new CancelledState());
    }
    @Override
    public String getStateName() { return "INITIATED"; }
}

class PassengerDetailsState implements BookingState {
    @Override
    public void addPassengers(BookingContext context, List<PassengerInfo> passengers) {
        context.setPassengers(passengers);
        System.out.println("[STATE: PASSENGER_DETAILS] Passenger list updated.");
    }
    @Override
    public void selectSeats(BookingContext context, List<String> seats) {
        if (seats.size() != context.getPassengers().size()) {
            System.out.println("[ERROR] Number of seats must match number of passengers.");
            return;
        }
        context.setSelectedSeats(seats);
        System.out.println("[STATE: PASSENGER_DETAILS] Seats " + seats + " locked. Transitioning to SEAT_SELECTED.");
        context.setState(new SeatSelectedState());
    }
    @Override
    public void processPayment(BookingContext context, double amount) { System.out.println("[ERROR] Select seats before payment."); }
    @Override
    public void confirmBooking(BookingContext context) { System.out.println("[ERROR] Cannot confirm yet."); }
    @Override
    public void cancelBooking(BookingContext context) {
        System.out.println("[STATE: PASSENGER_DETAILS] Booking cancelled.");
        context.setState(new CancelledState());
    }
    @Override
    public String getStateName() { return "PASSENGER_DETAILS"; }
}

class SeatSelectedState implements BookingState {
    @Override
    public void addPassengers(BookingContext context, List<PassengerInfo> passengers) { System.out.println("[ERROR] Cannot change passengers after seat selection. Go back."); }
    @Override
    public void selectSeats(BookingContext context, List<String> seats) {
        context.setSelectedSeats(seats);
        System.out.println("[STATE: SEAT_SELECTED] Seats updated to " + seats);
    }
    @Override
    public void processPayment(BookingContext context, double amount) {
        context.setPaymentExpiryTime(LocalDateTime.now().plusMinutes(15)); // Lock for 15 mins
        System.out.println("[STATE: SEAT_SELECTED] Initiating payment gateway. Expiry set to 15 mins. Transitioning to PAYMENT_PENDING.");
        context.setState(new PaymentPendingState());
    }
    @Override
    public void confirmBooking(BookingContext context) { System.out.println("[ERROR] Process payment first."); }
    @Override
    public void cancelBooking(BookingContext context) {
        System.out.println("[STATE: SEAT_SELECTED] Seats released. Booking cancelled.");
        context.setSelectedSeats(List.of()); // Release
        context.setState(new CancelledState());
    }
    @Override
    public String getStateName() { return "SEAT_SELECTED"; }
}

class PaymentPendingState implements BookingState {
    @Override
    public void addPassengers(BookingContext context, List<PassengerInfo> passengers) { System.out.println("[ERROR] Locked during payment."); }
    @Override
    public void selectSeats(BookingContext context, List<String> seats) { System.out.println("[ERROR] Locked during payment."); }
    @Override
    public void processPayment(BookingContext context, double amount) {
        if (context.isPaymentExpired()) {
            System.out.println("[ERROR] Payment window expired. Booking cancelled.");
            cancelBooking(context);
            return;
        }
        if (amount == context.getTotalFare()) {
            System.out.println("[STATE: PAYMENT_PENDING] Payment of ₹" + amount + " successful!");
            confirmBooking(context);
        } else {
            System.out.println("[ERROR] Amount mismatch. Required: ₹" + context.getTotalFare());
        }
    }
    @Override
    public void confirmBooking(BookingContext context) {
        String ticketNo = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        context.setETicketNumber(ticketNo);
        System.out.println("[STATE: PAYMENT_PENDING] E-Ticket generated: " + ticketNo + ". Transitioning to CONFIRMED.");
        context.setState(new ConfirmedState());
    }
    @Override
    public void cancelBooking(BookingContext context) {
        System.out.println("[STATE: PAYMENT_PENDING] Payment aborted. Seats released. Booking cancelled.");
        context.setSelectedSeats(List.of());
        context.setState(new CancelledState());
    }
    @Override
    public String getStateName() { return "PAYMENT_PENDING"; }
}

class ConfirmedState implements BookingState {
    @Override
    public void addPassengers(BookingContext context, List<PassengerInfo> passengers) { System.out.println("[ERROR] Booking is already confirmed."); }
    @Override
    public void selectSeats(BookingContext context, List<String> seats) { System.out.println("[ERROR] Use the modification module to change seats."); }
    @Override
    public void processPayment(BookingContext context, double amount) { System.out.println("[ERROR] Already paid."); }
    @Override
    public void confirmBooking(BookingContext context) { System.out.println("[ERROR] Already confirmed."); }
    @Override
    public void cancelBooking(BookingContext context) {
        System.out.println("[STATE: CONFIRMED] Initiating cancellation policy and refund rules... Transitioning to CANCELLED.");
        context.setState(new CancelledState());
    }
    @Override
    public String getStateName() { return "CONFIRMED"; }
}

class CancelledState implements BookingState {
    @Override
    public void addPassengers(BookingContext context, List<PassengerInfo> p) { System.out.println("[ERROR] Booking is cancelled."); }
    @Override
    public void selectSeats(BookingContext context, List<String> s) { System.out.println("[ERROR] Booking is cancelled."); }
    @Override
    public void processPayment(BookingContext context, double a) { System.out.println("[ERROR] Booking is cancelled."); }
    @Override
    public void confirmBooking(BookingContext context) { System.out.println("[ERROR] Booking is cancelled."); }
    @Override
    public void cancelBooking(BookingContext context) { System.out.println("[ERROR] Already cancelled."); }
    @Override
    public String getStateName() { return "CANCELLED"; }
}