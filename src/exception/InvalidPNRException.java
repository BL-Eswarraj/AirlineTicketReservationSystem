package exception;

public class InvalidPNRException extends AirlineException {
    public InvalidPNRException(String pnr) {
        super("Booking with PNR reference '" + pnr.toUpperCase() + "' was not found in the system repository.",
                "ERR_PNR_404", 404);
    }
}