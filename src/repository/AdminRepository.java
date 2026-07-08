package repository;

import java.util.ArrayList;
import model.Admin;

public class AdminRepository {

    private ArrayList<Admin> admins = new ArrayList<>();

    public void save(Admin admin) {
        admins.add(admin);
    }

    public Admin findByEmail(String email) {

        for (Admin admin : admins) {

            if (admin.getEmail().equals(email)) {
                return admin;
            }

        }

        return null;
    }

    public Admin login(String email, String password) {

        Admin admin = findByEmail(email);

        if (admin != null && admin.getPassword().equals(password)) {
            return admin;
        }

        return null;
    }
}