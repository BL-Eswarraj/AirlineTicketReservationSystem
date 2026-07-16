package domain.booking;

// Represents a snapshot of passenger details for a specific booking
public record PassengerInfo(
        String fullName,
        int age,
        String gender,
        String idProofDocument,
        String mealPreference,
        String specialAssistance
) {
    public boolean isValid() {
        return fullName != null && !fullName.isBlank() && idProofDocument != null && !idProofDocument.isBlank();
    }
}