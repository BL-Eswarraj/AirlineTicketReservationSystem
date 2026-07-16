package domain.user;

public class TravelPreferences {
    private String mealPreference; // e.g., VEGETARIEN, HALAL, STANDARD, KOSHER
    private String seatPreference; // e.g., WINDOW, AISLE, MIDDLE
    private String specialAssistance; // e.g., WHEELCHAIR, ELDERLY_CARE, NONE
    private boolean emailNotifications;
    private boolean smsNotifications;

    public TravelPreferences() {
        this.mealPreference = "STANDARD";
        this.seatPreference = "ANY";
        this.specialAssistance = "NONE";
        this.emailNotifications = true;
        this.smsNotifications = true;
    }

    public void updatePreferences(String meal, String seat, String assistance, boolean emailNotif, boolean smsNotif) {
        this.mealPreference = meal;
        this.seatPreference = seat;
        this.specialAssistance = assistance;
        this.emailNotifications = emailNotif;
        this.smsNotifications = smsNotif;
    }

    // Getters
    public String getMealPreference() { return mealPreference; }
    public String getSeatPreference() { return seatPreference; }
    public String getSpecialAssistance() { return specialAssistance; }
    public boolean isEmailNotifications() { return emailNotifications; }
    public boolean isSmsNotifications() { return smsNotifications; }

    @Override
    public String toString() {
        return String.format("Meal: %s | Seat: %s | Assistance: %s | Alerts (Email/SMS): %b/%b",
                mealPreference, seatPreference, specialAssistance, emailNotifications, smsNotifications);
    }
}