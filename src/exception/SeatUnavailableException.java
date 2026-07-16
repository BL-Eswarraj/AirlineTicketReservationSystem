package exception;

public class SeatUnavailableException extends AirlineException {
    public SeatUnavailableException(String seatNumber, String flightNumber, String reason) {
        super(String.format("Seat %s on flight %s cannot be selected. Reason: %s", seatNumber, flightNumber, reason),
                "ERR_SEAT_409", 409);
    }
}