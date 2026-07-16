package domain.user;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Passenger extends User {
    private String passportNumber;
    private String nationalId;
    private TravelPreferences preferences;
    private EmergencyContact emergencyContact;
    private List<CompanionProfile> savedCompanions;
    private List<String> bookingHistoryPnrs;
    private String frequentFlyerNumber;

    public Passenger(String userId, String name, String email, String phone, String passwordHash,
                     LocalDate dob, String passportNumber, String nationalId) {
        super(userId, name, email, phone, passwordHash, dob, UserRole.PASSENGER);
        this.passportNumber = passportNumber;
        this.nationalId = nationalId;
        this.preferences = new TravelPreferences();
        this.savedCompanions = new ArrayList<>();
        this.bookingHistoryPnrs = new ArrayList<>();
    }

    @Override
    public boolean hasPermission(String feature) {
        // Passengers can only manage their own bookings and profiles
        return switch (feature) {
            case "SEARCH_FLIGHTS", "BOOK_FLIGHT", "VIEW_OWN_BOOKINGS",
                 "CANCEL_OWN_BOOKING", "MODIFY_OWN_PROFILE", "SELECT_SEAT", "CHECK_IN" -> true;
            default -> false;
        };
    }

    public void updateIdentification(String passportNumber, String nationalId) {
        this.passportNumber = passportNumber;
        this.nationalId = nationalId;
    }

    public void setEmergencyContact(EmergencyContact contact) {
        this.emergencyContact = contact;
    }

    public void addCompanion(CompanionProfile companion) {
        this.savedCompanions.add(companion);
    }

    public void addBookingPnr(String pnr) {
        this.bookingHistoryPnrs.add(pnr);
    }

    public void setFrequentFlyerNumber(String ffNumber) {
        this.frequentFlyerNumber = ffNumber;
    }

    public void printFullProfile() {
        System.out.println("\n==========================================");
        System.out.println(" PASSENGER PROFILE: " + name);
        System.out.println("==========================================");
        System.out.printf("User ID    : %s | Role: %s | Status: %s%n", userId, role, isActive ? "ACTIVE" : "INACTIVE");
        System.out.printf("Email      : %s (Verified: %b)%n", email, isEmailVerified);
        System.out.printf("Phone      : %s (Verified: %b)%n", phone, isPhoneVerified);
        System.out.printf("DOB        : %s | Passport: %s | ID: %s%n", dateOfBirth, passportNumber, nationalId);
        System.out.printf("Freq Flyer : %s%n", frequentFlyerNumber != null ? frequentFlyerNumber : "N/A");
        System.out.println("Preferences: " + preferences);
        System.out.println("Emergency  : " + (emergencyContact != null ? emergencyContact : "Not set"));

        System.out.println("\nSaved Companions (" + savedCompanions.size() + "):");
        savedCompanions.forEach(c -> System.out.println(" - " + c));

        System.out.println("\nBooking History PNRs (" + bookingHistoryPnrs.size() + "):");
        bookingHistoryPnrs.forEach(pnr -> System.out.print("[" + pnr + "] "));
        System.out.println("\n==========================================");
    }

    // Getters
    public String getPassportNumber() { return passportNumber; }
    public String getNationalId() { return nationalId; }
    public TravelPreferences getPreferences() { return preferences; }
    public EmergencyContact getEmergencyContact() { return emergencyContact; }
    public List<CompanionProfile> getSavedCompanions() { return savedCompanions; }
    public List<String> getBookingHistoryPnrs() { return bookingHistoryPnrs; }
}