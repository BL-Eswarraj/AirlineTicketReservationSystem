package exception;

public class BookingModificationException extends AirlineException {
    public BookingModificationException(String pnr, String policyViolation) {
        super(String.format("Modification rejected for PNR [%s]: %s", pnr, policyViolation),
                "ERR_MOD_422", 422);
    }
}