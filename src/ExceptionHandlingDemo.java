import exception.*;

public class ExceptionHandlingDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC17: EXCEPTION HANDLING MODULE DEMO ");
        System.out.println("==================================================");

        GlobalExceptionHandler errorHandler = GlobalExceptionHandler.getInstance();

        // --- SCENARIO 1: INVALID PNR LOOKUP (404) ---
        System.out.println("\n--- 1. Simulating Booking Lookup with Invalid PNR ---");
        executeSafely(() -> {
            throw new InvalidPNRException("PNR-FAKE-99");
        }, errorHandler);

        // --- SCENARIO 2: SEAT BOOKING CONFLICT (409) ---
        System.out.println("\n--- 2. Simulating Seat Conflict (Attempting to book an already locked seat) ---");
        executeSafely(() -> {
            throw new SeatUnavailableException("12A", "AI-101", "Seat is already confirmed by another passenger.");
        }, errorHandler);

        // --- SCENARIO 3: PAYMENT GATEWAY TIMEOUT / FAILURE (402) ---
        System.out.println("\n--- 3. Simulating Payment Gateway Rejection ---");
        executeSafely(() -> {
            throw new PaymentFailedException("TXN-UPI-8899", 12500.00, "3D Secure OTP verification timed out.");
        }, errorHandler);

        // --- SCENARIO 4: UNAUTHORIZED ROLE ACCESS (403) ---
        System.out.println("\n--- 4. Simulating Role-Based Access Control Violation ---");
        executeSafely(() -> {
            throw new UnauthorizedRoleException("Annadurai Anbarasu", "PASSENGER", "CANCEL_FLIGHT_SCHEDULE");
        }, errorHandler);

        // --- SCENARIO 5: BOOKING MODIFICATION POLICY VIOLATION (422) ---
        System.out.println("\n--- 5. Simulating Late Booking Modification Attempt ---");
        executeSafely(() -> {
            throw new BookingModificationException("PNR-DELBOM-01", "Modifications are strictly prohibited within 4 hours of scheduled departure.");
        }, errorHandler);

        // --- SCENARIO 6: UNEXPECTED RUNTIME EXCEPTION (500) ---
        System.out.println("\n--- 6. Simulating Uncaught NullPointerException (Graceful Degradation) ---");
        executeSafely(() -> {
            String uninitializedData = null;
            uninitializedData.trim(); // Throws NullPointerException
        }, errorHandler);

        System.out.println("==================================================");
        System.out.println(" UC17 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }

    // Utility wrapper simulating a controller or CLI menu intercepting failures
    private static void executeSafely(Runnable action, GlobalExceptionHandler handler) {
        try {
            action.run();
        } catch (Throwable t) {
            ErrorResponse formattedResponse = handler.handle(t);
            System.out.println(formattedResponse);
        }
    }
}