package exception;

import java.time.LocalDateTime;

// Immutable DTO for standardized error communication across API and UI layers
public record ErrorResponse(
        LocalDateTime timestamp,
        int statusCode,
        String errorCode,
        String message,
        String actionHint
) {
    @Override
    public String toString() {
        return String.format("""
                +-----------------------------------------------------------------------------+
                | ❌ SYSTEM ERROR RESPONSE [%d - %s]
                +-----------------------------------------------------------------------------+
                | Time        : %s
                | Error Code  : %s
                | Description : %s
                +-----------------------------------------------------------------------------+
                | 💡 Hint     : %s
                +-----------------------------------------------------------------------------+""",
                statusCode, errorCode, timestamp.withNano(0), errorCode, message, actionHint);
    }
}