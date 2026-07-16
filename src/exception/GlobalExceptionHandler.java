package exception;

import java.time.LocalDateTime;

// Centralized exception dispatcher ensuring the application never crashes from unhandled errors
public class GlobalExceptionHandler {
    private static final GlobalExceptionHandler INSTANCE = new GlobalExceptionHandler();

    private GlobalExceptionHandler() {}

    public static GlobalExceptionHandler getInstance() {
        return INSTANCE;
    }

    public ErrorResponse handle(Throwable throwable) {
        // Log the exception securely to internal monitoring (Simulated)
        logToSecurityAudit(throwable);

        if (throwable instanceof AirlineException ex) {
            return mapDomainException(ex);
        } else if (throwable instanceof IllegalArgumentException ex) {
            return new ErrorResponse(
                    LocalDateTime.now(), 400, "ERR_BAD_REQUEST",
                    ex.getMessage(), "Please verify the syntax and format of your input arguments."
            );
        } else if (throwable instanceof IllegalStateException ex) {
            return new ErrorResponse(
                    LocalDateTime.now(), 409, "ERR_STATE_CONFLICT",
                    ex.getMessage(), "Ensure the system or booking is in the correct operational state before retrying."
            );
        } else {
            // Fallback for unexpected system exceptions (NullPointer, IndexOutOfBounds, etc.)
            return new ErrorResponse(
                    LocalDateTime.now(), 500, "ERR_INTERNAL_SERVER",
                    "An unexpected internal error occurred while processing your request.",
                    "Please contact SRM Global Airways technical support at support@srmairways.com."
            );
        }
    }

    private ErrorResponse mapDomainException(AirlineException ex) {
        String hint = switch (ex.getErrorCode()) {
            case "ERR_PNR_404" -> "Check your 6-character PNR reference on your e-ticket and try again.";
            case "ERR_SEAT_409" -> "Please view the live interactive seat map to select an alternative available seat.";
            case "ERR_PAYMENT_402" -> "Verify your UPI PIN, card balance, or switch to an alternative payment method.";
            case "ERR_RBAC_403" -> "Switch to an Admin or Airline Staff account to access this operational feature.";
            case "ERR_MOD_422" -> "Review the ticket fare rules regarding deadlines for online modifications.";
            default -> "Please review the system notice and retry.";
        };

        return new ErrorResponse(ex.getTimestamp(), ex.getStatusCode(), ex.getErrorCode(), ex.getMessage(), hint);
    }

    private void logToSecurityAudit(Throwable t) {
        // In production, this writes to Slf4j, CloudWatch, or ELK stack
        System.err.printf("[AUDIT LOG - %s] Intercepted Exception: %s (%s)%n",
                LocalDateTime.now().toLocalTime(), t.getClass().getSimpleName(), t.getMessage());
    }
}