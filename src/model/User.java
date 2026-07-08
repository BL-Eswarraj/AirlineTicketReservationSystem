package model;

public abstract class User {

    protected String fullName;
    protected String email;
    protected String password;

    public User(String fullName,
                String email,
                String password) {

        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public abstract String getRole();

    public abstract void showMenu();

    public abstract boolean canManageUsers();

    public abstract boolean canManageFlights();

    public abstract boolean canManageAllBookings();
}