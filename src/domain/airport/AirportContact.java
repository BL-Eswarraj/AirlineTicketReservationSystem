package domain.airport;

public record AirportContact(
        String helpdeskPhone,
        String emergencyPhone,
        String supportEmail,
        String officialWebsite
) {
    @Override
    public String toString() {
        return String.format("Helpdesk: %s | Emergency: %s | Email: %s | Web: %s",
                helpdeskPhone, emergencyPhone, supportEmail, officialWebsite);
    }
}