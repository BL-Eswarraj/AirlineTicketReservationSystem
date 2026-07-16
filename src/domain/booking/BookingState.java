package domain.booking;

import java.util.List;

public interface BookingState {
    void addPassengers(BookingContext context, List<PassengerInfo> passengers);
    void selectSeats(BookingContext context, List<String> seats);
    void processPayment(BookingContext context, double amount);
    void confirmBooking(BookingContext context);
    void cancelBooking(BookingContext context);
    String getStateName();
}