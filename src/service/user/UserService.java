package service.user;

import domain.user.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class UserService {
    private final Map<String, User> userDatabase = new HashMap<>();
    private final Map<String, String> rememberMeDatabase = new HashMap<>(); // Token -> Email

    public UserService() {
        // Seed default system users
        seedDefaultUsers();
    }

    // 1.1 User Registration
    public Passenger registerPassenger(String name, String email, String phone, String rawPassword,
                                       LocalDate dob, String passport, String nationalId) {
        if (userDatabase.containsKey(email.toLowerCase())) {
            throw new IllegalArgumentException("Error: Email already registered in system!");
        }

        String userId = "PASS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        String passwordHash = SecurityUtil.hashPassword(rawPassword);

        Passenger passenger = new Passenger(userId, name, email.toLowerCase(), phone, passwordHash, dob, passport, nationalId);
        userDatabase.put(email.toLowerCase(), passenger);

        System.out.println("[REGISTRATION] Passenger registered successfully: " + name);

        // Trigger OTP Verification for email and mobile
        SecurityUtil.generateAndSendOtp(email, "Email Account Verification");
        SecurityUtil.generateAndSendOtp(phone, "Mobile Number Verification");

        return passenger;
    }

    // Verify OTP for Registration
    public boolean verifyUserContact(String email, String emailOtp, String phone, String phoneOtp) {
        User user = userDatabase.get(email.toLowerCase());
        if (user == null) return false;

        boolean emailValid = SecurityUtil.verifyOtp(email, emailOtp);
        boolean phoneValid = SecurityUtil.verifyOtp(phone, phoneOtp);

        if (emailValid && phoneValid) {
            user.setVerified(true, true);
            System.out.println("[VERIFICATION] Email and Phone verified successfully for: " + user.getName());
            return true;
        }
        System.out.println("[VERIFICATION FAILURE] Incorrect OTP provided.");
        return false;
    }

    // 1.1 Login with MFA and Remember Me
    public boolean login(String email, String rawPassword, boolean rememberMe) {
        User user = userDatabase.get(email.toLowerCase());
        if (user == null || !user.isActive()) {
            System.out.println("[LOGIN FAILED] User not found or account deactivated.");
            return false;
        }

        String hashedInput = SecurityUtil.hashPassword(rawPassword);
        if (!user.getPasswordHash().equals(hashedInput)) {
            System.out.println("[LOGIN FAILED] Invalid password.");
            return false;
        }

        if (user.isMfaEnabled()) {
            // Initiate MFA flow
            System.out.println("[LOGIN] Credentials valid. Triggering Multi-Factor Authentication...");
            SecurityUtil.generateAndSendOtp(user.getEmail(), "MFA Login Security Code");
            UserSession.getInstance().startSession(user, false);
            return true;
        } else {
            UserSession.getInstance().startSession(user, true);
            handleRememberMe(user, rememberMe);
            return true;
        }
    }

    // Complete MFA Step
    public boolean verifyMfaLogin(String otp) {
        User user = UserSession.getInstance().getLoggedInUser();
        if (user != null && SecurityUtil.verifyOtp(user.getEmail(), otp)) {
            UserSession.getInstance().completeMfa();
            return true;
        }
        System.out.println("[MFA FAILED] Invalid MFA Code.");
        return false;
    }

    private void handleRememberMe(User user, boolean rememberMe) {
        if (rememberMe) {
            String token = SecurityUtil.generateRememberMeToken();
            user.setRememberMeToken(token);
            rememberMeDatabase.put(token, user.getEmail());
            System.out.println("[REMEMBER ME] Token generated: " + token);
        }
    }

    // Login via Remember Me Token
    public boolean loginWithRememberMeToken(String token) {
        String email = rememberMeDatabase.get(token);
        if (email != null && userDatabase.containsKey(email)) {
            User user = userDatabase.get(email);
            UserSession.getInstance().startSession(user, true);
            System.out.println("[REMEMBER ME LOGIN] Automatically logged in via stored token.");
            return true;
        }
        return false;
    }

    // 1.1 Password Reset via Email/SMS
    public void initiatePasswordReset(String email) {
        User user = userDatabase.get(email.toLowerCase());
        if (user != null) {
            SecurityUtil.generateAndSendOtp(email, "Password Reset Authorization");
        } else {
            System.out.println("[PASSWORD RESET] If account exists, OTP has been sent.");
        }
    }

    public boolean completePasswordReset(String email, String otp, String newRawPassword) {
        User user = userDatabase.get(email.toLowerCase());
        if (user != null && SecurityUtil.verifyOtp(email, otp)) {
            user.setPasswordHash(SecurityUtil.hashPassword(newRawPassword));
            System.out.println("[PASSWORD RESET] Password successfully updated for: " + user.getName());
            return true;
        }
        System.out.println("[PASSWORD RESET FAILED] Invalid OTP or user.");
        return false;
    }

    // 1.1 Deactivate Account
    public void deactivateAccount(String email) {
        User user = userDatabase.get(email.toLowerCase());
        if (user != null) {
            user.deactivateAccount();
            UserSession.getInstance().logout();
            System.out.println("[ACCOUNT STATUS] Account deactivated: " + email);
        }
    }

    // 1.3 Role-Based Permission Validation Service
    public boolean checkUserPermission(User user, String feature) {
        if (user == null || !user.isActive()) return false;
        boolean allowed = user.hasPermission(feature);
        System.out.printf("[PERMISSION CHECK] User: %s (%s) | Feature: %s | Access: %s%n",
                user.getName(), user.getRole(), feature, allowed ? "GRANTED" : "DENIED");
        return allowed;
    }

    public Optional<User> getUserByEmail(String email) {
        return Optional.ofNullable(userDatabase.get(email.toLowerCase()));
    }

    private void seedDefaultUsers() {
        Admin admin = new Admin("ADM-001", "System Admin", "admin@airline.com", "+919876543210", SecurityUtil.hashPassword("admin123"), "IT & Operations");
        AirlineStaff staff = new AirlineStaff("STF-001", "Ramesh Staff", "ramesh@airindia.in", "+919876543211", SecurityUtil.hashPassword("staff123"), "AI", "AI-B-909");
        userDatabase.put(admin.getEmail(), admin);
        userDatabase.put(staff.getEmail(), staff);
    }
}