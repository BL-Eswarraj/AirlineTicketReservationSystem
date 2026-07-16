package domain.seat;

public enum SeatType {
    STANDARD(0.0, "Standard Seat"),
    PREMIUM(1500.0, "Premium Front Row - Extra Comfort"),
    EMERGENCY_EXIT(1000.0, "Emergency Exit - Extra Legroom"),
    EXTRA_LEGROOM(800.0, "Standard Row - Extra Legroom");

    private final double surcharge;
    private final String description;

    SeatType(double surcharge, String description) {
        this.surcharge = surcharge;
        this.description = description;
    }

    public double getSurcharge() { return surcharge; }
    public String getDescription() { return description; }
}