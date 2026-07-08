package model;

public class Session {

    private Passenger loggedInPassenger;

    public Passenger getLoggedInPassenger() {
        return loggedInPassenger;
    }

    public void setLoggedInPassenger(Passenger passenger) {
        this.loggedInPassenger = passenger;
    }

    public void logout() {
        loggedInPassenger = null;
    }

    public boolean isLoggedIn() {
        return loggedInPassenger != null;
    }
}