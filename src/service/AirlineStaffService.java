package service;

import model.AirlineStaff;
import repository.AirlineStaffRepository;

public class AirlineStaffService {

    private AirlineStaffRepository airlineStaffRepository =
            new AirlineStaffRepository();

    public void register(AirlineStaff staff) {
        airlineStaffRepository.save(staff);
    }

    public AirlineStaff login(String email, String password) {
        return airlineStaffRepository.login(email, password);
    }
}