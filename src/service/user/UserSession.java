package service.user;

import domain.user.User;

public class UserSession {
    private static UserSession instance;
    private User loggedInUser;
    private String currentSessionToken;
    private boolean isMfaVerified;

    private UserSession() {}

    public static synchronized UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    public void startSession(User user, boolean mfaVerified) {
        this.loggedInUser = user;
        this.isMfaVerified = mfaVerified;
        this.currentSessionToken = "SESS-" + System.currentTimeMillis();
        System.out.printf("[SESSION] User %s logged in successfully. Session: %s%n", user.getName(), currentSessionToken);
    }

    public void completeMfa() {
        this.isMfaVerified = true;
        System.out.println("[SESSION] MFA Verification complete. Full access granted.");
    }

    public void logout() {
        if (loggedInUser != null) {
            System.out.println("[SESSION] Logging out user: " + loggedInUser.getName());
            this.loggedInUser = null;
            this.currentSessionToken = null;
            this.isMfaVerified = false;
        }
    }

    public User getLoggedInUser() { return loggedInUser; }
    public boolean isAuthenticated() { return loggedInUser != null && (!loggedInUser.isMfaEnabled() || isMfaVerified); }
    public String getCurrentSessionToken() { return currentSessionToken; }
}