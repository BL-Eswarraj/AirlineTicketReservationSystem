package exception;

import java.time.LocalDateTime;

public abstract class AirlineException extends RuntimeException {
    private final String errorCode;
    private final int statusCode;
    private final LocalDateTime timestamp;

    public AirlineException(String message, String errorCode, int statusCode) {
        super(message);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
    }

    public AirlineException(String message, String errorCode, int statusCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.statusCode = statusCode;
        this.timestamp = LocalDateTime.now();
    }

    public String getErrorCode() { return errorCode; }
    public int getStatusCode() { return statusCode; }
    public LocalDateTime getTimestamp() { return timestamp; }
}