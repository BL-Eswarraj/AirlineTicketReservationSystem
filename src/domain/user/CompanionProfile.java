package domain.user;

import java.time.LocalDate;

// Represents family members or frequent co-travelers saved under a primary profile
public record CompanionProfile(String fullName, String relationship, LocalDate dateOfBirth, String passportNumber) {
    @Override
    public String toString() {
        return String.format("%s (%s) - DOB: %s | Passport: %s", fullName, relationship, dateOfBirth, passportNumber);
    }
}