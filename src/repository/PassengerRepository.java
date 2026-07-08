package repository;

import java.util.ArrayList;
import model.Passenger;

//Database operation Perform
public class PassengerRepository {

    private ArrayList<Passenger> passengers = new ArrayList<>();

    public void save(Passenger passenger) {
        passengers.add(passenger);

    }

    public Passenger findByEmail(String email) {
        for (Passenger passenger : passengers) {

            if (passenger.getEmail().equals(email)) {
                return passenger; // Since return object is Passenger
            }
        }
        return null;
    }

    //Update password
    public boolean updatePassword(String email, String newPassword) {

        Passenger passenger = findByEmail(email);

        if (passenger != null) {
            passenger.setPassword(newPassword);
            return true;
        }

        return false;
    }

    //Update Passenger details
    public boolean updatePassenger(String email,
            String fullName,
            String phoneNumber,
            String dateOfBirth,
            String passportNumber) {

        Passenger passenger = findByEmail(email);

        if (passenger != null) {

            passenger.setFullName(fullName);
            passenger.setPhoneNumber(phoneNumber);
            passenger.setDateOfBirth(dateOfBirth);
            passenger.setPassportNumber(passportNumber);

            return true;
        }

        return false;
    }

    public boolean deletePassenger(String email) {

        Passenger passenger = findByEmail(email);

        if (passenger != null) {
            passengers.remove(passenger);
            return true;
        }

        return false;
    }
}
