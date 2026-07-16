package domain.user;

public record EmergencyContact(String name, String relationship, String phoneNumber) {
    @Override
    public String toString() {
        return name + " (" + relationship + ") - " + phoneNumber;
    }
}