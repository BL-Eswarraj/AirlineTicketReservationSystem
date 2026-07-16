package service.cancellation;

public enum TicketType {
    NON_REFUNDABLE("Non-Refundable (Only Taxes Refunded)"),
    STANDARD("Standard Fare (Time-Based Cancellation Fees)"),
    FLEXIBLE("Flexible Fare (Free Cancellation Up to 2 Hours Before Departure)");

    private final String description;

    TicketType(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
}