package service.modification;

public enum ModificationType {
    FLIGHT_CHANGE(1500.0, "Standard Flight Change Fee"),
    NAME_CORRECTION(500.0, "Name Correction Fee"),
    SEAT_CHANGE(300.0, "Seat Re-assignment Fee"),
    CONTACT_UPDATE(0.0, "Contact Details Update (Free)"),
    MEAL_PREFERENCE_UPDATE(0.0, "Meal Preference Update (Free)");

    private final double baseFee;
    private final String description;

    ModificationType(double baseFee, String description) {
        this.baseFee = baseFee;
        this.description = description;
    }

    public double getBaseFee() { return baseFee; }
    public String getDescription() { return description; }
}