package domain.user;

import java.time.LocalDate;

public class Admin extends User {
    private String department;

    public Admin(String userId, String name, String email, String phone, String passwordHash, String department) {
        super(userId, name, email, phone, passwordHash, LocalDate.of(1990, 1, 1), UserRole.ADMIN);
        this.department = department;
    }

    @Override
    public boolean hasPermission(String feature) {
        // Admin has universal access to all system features
        return true;
    }

    public String getDepartment() { return department; }
}