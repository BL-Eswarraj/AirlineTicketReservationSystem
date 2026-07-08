package service;

import model.Admin;
import repository.AdminRepository;

public class AdminService {

    private AdminRepository adminRepository = new AdminRepository();

    public void register(Admin admin) {
        adminRepository.save(admin);
    }

    public Admin login(String email, String password) {
        return adminRepository.login(email, password);
    }
}