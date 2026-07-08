package repository;

import java.util.ArrayList;
import model.AirlineStaff;

public class AirlineStaffRepository {

    private ArrayList<AirlineStaff> staffs = new ArrayList<>();

    public void save(AirlineStaff staff) {
        staffs.add(staff);
    }

    public AirlineStaff findByEmail(String email) {

        for (AirlineStaff staff : staffs) {

            if (staff.getEmail().equals(email)) {
                return staff;
            }

        }

        return null;
    }

    public AirlineStaff login(String email, String password) {

        AirlineStaff staff = findByEmail(email);

        if (staff != null && staff.getPassword().equals(password)) {
            return staff;
        }

        return null;
    }
}