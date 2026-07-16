import domain.user.*;
import service.user.*;
import java.time.LocalDate;

public class UserManagementDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC1: USER MANAGEMENT & AUTHENTICATION DEMO ");
        System.out.println("==================================================");

        UserService userService = new UserService();
        UserSession session = UserSession.getInstance();

        // --- STEP 1: REGISTRATION & CONTACT VERIFICATION ---
        System.out.println("\n--- 1. Registering New Passenger ---");
        Passenger anbu = userService.registerPassenger(
                "Annadurai Anbarasu",
                "anbu@srm.edu.in",
                "+919800000000",
                "SecurePass@2026",
                LocalDate.of(2002, 9, 15),
                "Z9876543",
                "AADHAAR-1234-5678"
        );

        // Simulate OTP input from console/user (In demo we retrieve from active verification)
        // For testing, we verify with dummy values first, then correct them
        System.out.println("\n--- 2. Verifying Contact via OTP ---");
        // To simulate exact match, let's grab the generated OTP by re-sending
        String emailOtp = SecurityUtil.generateAndSendOtp("anbu@srm.edu.in", "Email Verification");
        String phoneOtp = SecurityUtil.generateAndSendOtp("+919800000000", "Phone Verification");
        userService.verifyUserContact("anbu@srm.edu.in", emailOtp, "+919800000000", phoneOtp);

        // --- STEP 2: PROFILE MANAGEMENT (1.2) ---
        System.out.println("\n--- 3. Enriching Passenger Profile ---");
        anbu.setFrequentFlyerNumber("AI-FREQ-8899");
        anbu.setEmergencyContact(new EmergencyContact("Mother", "Parent", "+919700000000"));

        // Add Travel Preferences
        anbu.getPreferences().updatePreferences("SOUTH_INDIAN_VEG", "WINDOW", "NONE", true, true);

        // Add Companion Profiles (Family members)
        anbu.addCompanion(new CompanionProfile("Sister Anbarasu", "Sister", LocalDate.of(2005, 4, 10), "K1122334"));
        anbu.addCompanion(new CompanionProfile("Mother Anbarasu", "Mother", LocalDate.of(1975, 8, 20), "M9988776"));

        // Add mock past booking PNRs
        anbu.addBookingPnr("PNR-DELBOM");
        anbu.addBookingPnr("PNR-MAASIN");

        // Display full profile
        anbu.printFullProfile();

        // --- STEP 3: LOGIN WITH MULTI-FACTOR AUTHENTICATION (MFA) ---
        System.out.println("\n--- 4. Testing Multi-Factor Authentication (MFA) Login ---");
        boolean credentialsOk = userService.login("anbu@srm.edu.in", "SecurePass@2026", true);

        if (credentialsOk) {
            // Generate MFA code and verify
            String mfaCode = SecurityUtil.generateAndSendOtp("anbu@srm.edu.in", "MFA Login Security Code");
            userService.verifyMfaLogin(mfaCode);
        }

        System.out.println("Is Session Authenticated? : " + session.isAuthenticated());
        System.out.println("Current Logged In User  : " + session.getLoggedInUser().getName());

        // --- STEP 4: POLYMORPHIC ROLE & PERMISSION CHECK (1.3) ---
        System.out.println("\n--- 5. Testing Role-Based Permission Hierarchy (1.3) ---");
        User passengerUser = session.getLoggedInUser();
        User adminUser = userService.getUserByEmail("admin@airline.com").get();
        User staffUser = userService.getUserByEmail("ramesh@airindia.in").get();

        System.out.println("\nFeature: [MANAGE_FLIGHTS] (Creating/Editing Schedules)");
        userService.checkUserPermission(passengerUser, "MANAGE_FLIGHTS"); // Denied for Passenger
        userService.checkUserPermission(staffUser, "MANAGE_FLIGHTS");     // Granted for Staff
        userService.checkUserPermission(adminUser, "MANAGE_FLIGHTS");     // Granted for Admin

        System.out.println("\nFeature: [BOOK_FLIGHT] (Making reservations)");
        userService.checkUserPermission(passengerUser, "BOOK_FLIGHT");    // Granted for Passenger
        userService.checkUserPermission(adminUser, "BOOK_FLIGHT");        // Granted for Admin

        // --- STEP 5: PASSWORD RESET FLOW ---
        System.out.println("\n--- 6. Testing Password Reset Flow ---");
        session.logout();
        userService.initiatePasswordReset("anbu@srm.edu.in");
        String resetOtp = SecurityUtil.generateAndSendOtp("anbu@srm.edu.in", "Password Reset Authorization");
        userService.completePasswordReset("anbu@srm.edu.in", resetOtp, "NewSecurePass@2027");

        // Try logging in with old vs new password
        System.out.println("Login with OLD password:");
        userService.login("anbu@srm.edu.in", "SecurePass@2026", false);

        System.out.println("Login with NEW password:");
        userService.login("anbu@srm.edu.in", "NewSecurePass@2027", false);

        System.out.println("\n==================================================");
        System.out.println(" UC1 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}