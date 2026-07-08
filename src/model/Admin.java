package model;

public class Admin extends User {

    public Admin(String fullName,
                 String email,
                 String password) {

        super(fullName, email, password);
    }

    @Override
    public String getRole() {
        return "Admin";
    }

    @Override
    public void showMenu() {

        System.out.println("\n===== Admin Menu =====");
        System.out.println("1. Manage Users");
        System.out.println("2. Manage Flights");
        System.out.println("3. Manage All Bookings");
        System.out.println("4. View Reports");
    }

    @Override
    public boolean canManageUsers() {
        return true;
    }

    @Override
    public boolean canManageFlights() {
        return true;
    }

    @Override
    public boolean canManageAllBookings() {
        return true;
    }
}