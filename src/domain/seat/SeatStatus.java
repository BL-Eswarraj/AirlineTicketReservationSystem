package domain.seat;

public enum SeatStatus {
    AVAILABLE,          // Green
    BOOKED,             // Red
    BLOCKED,            // Gray (e.g., reserved for crew or balance)
    TEMPORARILY_LOCKED  // Yellow (selected during active booking flow)
}