
import java.util.Scanner;
import model.Passenger;
import service.PassengerService;
import service.SessionService;
import util.OTPUtil;
import util.ValidationUtil;

public class Main {

    private static final int REGISTER = 1;
    private static final int LOGIN = 2;
    private static final int RESET_PASSWORD = 3;
    private static final int UPDATE_PROFILE = 4;
    private static final int DELETE_ACCOUNT = 5;
    private static final int VIEW_PROFILE = 6;
    private static final int EXIT = 7;

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        PassengerService passengerService = new PassengerService();
        SessionService sessionService = new SessionService();

        while (true) {

            System.out.println("\n=====================================");
            System.out.println("Welcome to Airline Resrvation System");
            System.out.println("=======================================");
            System.out.println();
            System.out.println("1. REGISTER");
            System.out.println("2. LOGIN");
            System.out.println("3. RESET PASSWORD");
            System.out.println("4. UPDATE PROFILE");
            System.out.println("5. DELETE ACCOUNT");
            System.out.println("6. VIEW PROFILE");
            System.out.println("7. LOGOUT");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {

                case REGISTER:
                    System.out.println("\n Patient Registration");

                    String name = ValidationUtil.readName(scanner);

                    String email = ValidationUtil.readEmail(scanner);

                    if (passengerService.isEmailExists(email)) {
                        System.out.println("Email Already Registered");
                        break;
                    }

                    String phone = ValidationUtil.readPhoneNumber(scanner);

                    String dob = ValidationUtil.readDateOfBirth(scanner);

                    String passport = ValidationUtil.readPassportNumber(scanner);

                    String password = ValidationUtil.readPassword(scanner);

                    Passenger passenger = new Passenger(name, email, phone, dob, passport, password);

                    passengerService.registerPassenger(passenger);

                    System.out.println("\n Patient registered Sucessfully");

                    break;
                case LOGIN:
                    System.out.println("\n Patient Login");

                    String loginemail = ValidationUtil.readEmail(scanner);

                    System.out.print("Enter the password: ");
                    String loginPassword = scanner.nextLine();

                    passenger = passengerService.loginPassenger(loginemail, loginPassword);

                    if (passenger != null) {

                        String otp = OTPUtil.generateOTP();

                        System.out.println("OTP : " + otp);

                        System.out.print("Enter OTP : ");
                        String enteredOTP = scanner.nextLine();

                        if (otp.equals(enteredOTP)) {

                            sessionService.login(passenger);

                            System.out.println("Login Successful");
                            System.out.println("Welcome " + passenger.getFullName());

                        } else {

                            System.out.println("Invalid OTP");

                        }

                    } else {

                        System.out.println("Invalid Email or Password");
                    }

                    break;
                case RESET_PASSWORD:

                    System.out.println("\nReset Password");

                    String resetEmail = ValidationUtil.readEmail(scanner);

                    String newPassword = ValidationUtil.readPassword(scanner);

                    boolean updated = passengerService.resetPassword(resetEmail, newPassword);

                    if (updated) {
                        System.out.println("Password updated successfully.");
                    } else {
                        System.out.println("Email not registered.");
                    }

                    break;
                case UPDATE_PROFILE:

                    System.out.println("\nUpdate Passenger Profile");

                    System.out.println("Registered Email ");
                    String updateEmail = ValidationUtil.readEmail(scanner);

                    String updatedName = ValidationUtil.readName(scanner);

                    String updatedPhone = ValidationUtil.readPhoneNumber(scanner);

                    String updatedDOB = ValidationUtil.readDateOfBirth(scanner);

                    String updatedPassport = ValidationUtil.readPassportNumber(scanner);

                    boolean isUpdated = passengerService.updatePassenger(
                            updateEmail,
                            updatedName,
                            updatedPhone,
                            updatedDOB,
                            updatedPassport
                    );

                    if (isUpdated) {
                        System.out.println("\nProfile Updated Successfully.");
                    } else {
                        System.out.println("\nEmail Not Found.");
                    }

                    break;
                case DELETE_ACCOUNT:

                    System.out.println("\nDelete Passenger Account");

                    String deleteEmail = ValidationUtil.readEmail(scanner);

                    boolean isDeleted = passengerService.deletePassenger(deleteEmail);

                    if (isDeleted) {
                        System.out.println("Account Deleted Successfully.");
                    } else {
                        System.out.println("Email Not Found.");
                    }

                    break;
                case VIEW_PROFILE:
                    String profileEmail = ValidationUtil.readEmail(scanner);

                    Passenger profile = passengerService.viewProfile(profileEmail);

                    if (profile != null) {

                        System.out.println("\n===== Passenger Profile =====");
                        System.out.println("Name          : " + profile.getFullName());
                        System.out.println("Email         : " + profile.getEmail());
                        System.out.println("Phone Number  : " + profile.getPhoneNumber());
                        System.out.println("Date of Birth : " + profile.getDateOfBirth());
                        System.out.println("Passport No   : " + profile.getPassportNumber());

                    } else {

                        System.out.println("Passenger Not Found.");

                    }

                    break;

                case EXIT:
                    System.out.println("Thank you for using AirLine Reservation System.");
                    return;
                default:
                    System.out.println("Invalid Choice Choose Valid option");

            }
        }
    }
}
