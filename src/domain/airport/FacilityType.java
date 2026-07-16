package domain.airport;

public enum FacilityType {
    VIP_LOUNGE("Premium VIP & Business Lounge"),
    MEDICAL_CENTER("24/7 Emergency Medical & Pharmacy"),
    FREE_WIFI("High-Speed Complimentary Wi-Fi"),
    CURRENCY_EXCHANGE("Forex & Currency Exchange Counter"),
    DUTY_FREE("Duty-Free Shopping Plaza"),
    FOOD_COURT("Multi-Cuisine Food Court"),
    PARKING("Multi-Level Car & Two-Wheeler Parking"),
    TRANSIT_HOTEL("In-Terminal Transit Hotel"),
    SPECIAL_ASSISTANCE("Wheelchair & Elderly Assistance Desk");

    private final String description;

    FacilityType(String description) {
        this.description = description;
    }

    public String getDescription() { return description; }
}