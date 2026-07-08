package model;

import java.util.ArrayList;

public class Passenger extends User {

    private String phoneNumber;
    private String dateOfBirth;
    private String passportNumber;

    private String mealType;
    private String seatPreference;
    private String specialAssistance;

    private boolean emailNotification;
    private boolean smsNotification;

    private String emergencyContactName;
    private String emergencyContactNumber;

    private ArrayList<Traveler> travelers = new ArrayList<>();

    public Passenger(String fullName,
                     String email,
                     String phoneNumber,
                     String dateOfBirth,
                     String passportNumber,
                     String password) {

        super(fullName, email, password);

        this.phoneNumber = phoneNumber;
        this.dateOfBirth = dateOfBirth;
        this.passportNumber = passportNumber;
    }

    // Getters
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPassportNumber() {
        return passportNumber;
    }

    public String getMealType() {
        return mealType;
    }

    public String getSeatPreference() {
        return seatPreference;
    }

    public String getSpecialAssistance() {
        return specialAssistance;
    }

    public boolean isEmailNotification() {
        return emailNotification;
    }

    public boolean isSmsNotification() {
        return smsNotification;
    }

    public String getEmergencyContactName() {
        return emergencyContactName;
    }

    public String getEmergencyContactNumber() {
        return emergencyContactNumber;
    }

    public ArrayList<Traveler> getTravelers() {
        return travelers;
    }

    // Setters
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setPassportNumber(String passportNumber) {
        this.passportNumber = passportNumber;
    }

    public void setMealType(String mealType) {
        this.mealType = mealType;
    }

    public void setSeatPreference(String seatPreference) {
        this.seatPreference = seatPreference;
    }

    public void setSpecialAssistance(String specialAssistance) {
        this.specialAssistance = specialAssistance;
    }

    public void setEmailNotification(boolean emailNotification) {
        this.emailNotification = emailNotification;
    }

    public void setSmsNotification(boolean smsNotification) {
        this.smsNotification = smsNotification;
    }

    public void setEmergencyContactName(String emergencyContactName) {
        this.emergencyContactName = emergencyContactName;
    }

    public void setEmergencyContactNumber(String emergencyContactNumber) {
        this.emergencyContactNumber = emergencyContactNumber;
    }

    // Traveler Management
    public void addTraveler(Traveler traveler) {
        travelers.add(traveler);
    }

    // ===== Role Methods =====

    @Override
    public String getRole() {
        return "Passenger";
    }

    @Override
    public void showMenu() {
        System.out.println("\nPassenger Menu");
        System.out.println("1. View Profile");
        System.out.println("2. Book Flight");
        System.out.println("3. View My Bookings");
        System.out.println("4. Cancel Booking");
    }

    @Override
    public boolean canManageUsers() {
        return false;
    }

    @Override
    public boolean canManageFlights() {
        return false;
    }

    @Override
    public boolean canManageAllBookings() {
        return false;
    }
}