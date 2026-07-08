package service;

import model.Passenger;

public class SessionService {

    private Passenger loggedInPassenger;

    public void login(Passenger passenger) {
        loggedInPassenger = passenger;
    }

    public void logout() {
        loggedInPassenger = null;
    }

    public Passenger getLoggedInPassenger() {
        return loggedInPassenger;
    }

    public boolean isLoggedIn() {
        return loggedInPassenger != null;
    }
}
