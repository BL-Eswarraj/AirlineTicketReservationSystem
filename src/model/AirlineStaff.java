package model;

public class AirlineStaff extends User {

    public AirlineStaff(String fullName,
                        String email,
                        String password) {

        super(fullName, email, password);
    }

    @Override
    public String getRole() {
        return "Airline Staff";
    }

    @Override
    public void showMenu() {

        System.out.println("\n===== Airline Staff Menu =====");
        System.out.println("1. Manage Flights");
        System.out.println("2. View Bookings");
    }

    @Override
    public boolean canManageUsers() {
        return false;
    }

    @Override
    public boolean canManageFlights() {
        return true;
    }

    @Override
    public boolean canManageAllBookings() {
        return false;
    }
}