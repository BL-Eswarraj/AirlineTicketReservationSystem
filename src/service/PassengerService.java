package service;

import model.Passenger;
import repository.PassengerRepository;

public class PassengerService {

    private PassengerRepository passengerRepository = new PassengerRepository();

    public void registerPassenger(Passenger passenger) {

        passengerRepository.save(passenger);

    }

    public boolean isEmailExists(String email) {

        return passengerRepository.findByEmail(email) != null;
    }

    public Passenger loginPassenger(String email, String password) {
        Passenger passenger = passengerRepository.findByEmail(email);

        if (passenger == null) {
            return null;
        }
        if (passenger.getPassword().equals(password)) {
            return passenger;
        }

        return null;
    }

    //Reset password
    public boolean resetPassword(String email, String newPassword) {

        Passenger passenger = passengerRepository.findByEmail(email);

        if (passenger == null) {
            return false;
        }

        return passengerRepository.updatePassword(email, newPassword);
    }

    //Update Passenger
    public boolean updatePassenger(String email,
            String fullName,
            String phoneNumber,
            String dateOfBirth,
            String passportNumber) {

        return passengerRepository.updatePassenger(
                email,
                fullName,
                phoneNumber,
                dateOfBirth,
                passportNumber);
    }

    //Remove a passenger
    public boolean deletePassenger(String email) {

        return passengerRepository.deletePassenger(email);

    }

    //Get Profile details
    public Passenger viewProfile(String email) {

        return passengerRepository.findByEmail(email);

    }
}
