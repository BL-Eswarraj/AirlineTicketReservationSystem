package model;

public class Traveler {

    private String fullName;
    private String dateOfBirth;
    private String passportNumber;

    public Traveler(String fullName,
                    String dateOfBirth,
                    String passportNumber) {

        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.passportNumber = passportNumber;
    }

    public String getFullName() {
        return fullName;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getPassportNumber() {
        return passportNumber;
    }
}