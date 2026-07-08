package util;
import java.util.Scanner;

public class ValidationUtil {
    
    //Name Validation
    public static boolean isValidname(String name){

        return name.matches( "^[A-Za-z ]{3,70}$");

    }
    public static  String readName(Scanner scanner){
        String name;

        while(true){
            System.out.print("Enter Patient Name: ");
            name = scanner.nextLine();

            if(isValidname(name)){
                return name;
            }
            System.out.println("Enter the name matching the requirements");
        }
    }
    
    //Email Validation
    public static boolean isValidEmail(String email){
        //Email regex values
        return email.matches("^[A-Za-z0-9+_.-]+@(.+)$");

    }

    public static String readEmail(Scanner scanner){
        
        String email;

        while(true){
            System.out.print("Enter email Address: ");
            email = scanner.nextLine();
            if(isValidEmail(email)){
                return email;
            }
            System.out.println("Invalid email Address, Please try again..");
        }
    }
    //Phone Number
    public static boolean isValidPhoneNumber(String phone){
        return phone.matches("^[6-9][0-9]{9}$");
    }

    public static String readPhoneNumber(Scanner scanner){

        String phno;

        while(true){
            System.out.print("Enter Contact Number: ");
            phno = scanner.nextLine();
            if(isValidPhoneNumber(phno)){
                return phno;
            }
            System.out.println("Enter the valid phone number");
        }
    }

    //Date of birth validation
    public static boolean isValidDob(String dob){
        return dob.matches("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$");
    }

    public static String readDateOfBirth(Scanner scanner){

        String dob;

        while (true){
            System.out.print("Enter Date of birth(dd/mm/yyyy): ");
            dob = scanner.nextLine();

            if(isValidDob(dob)){
                return dob;
            }
            System.out.println("Enter the Date of Bith in valid format");
        }
    }
    //Passport Number
    public static boolean isValidPassport(String passport){
        return passport.matches("^[A-Z][0-9]{7}$");
    }
    public static String readPassportNumber(Scanner scanner){

        String PassPortNo;

        while(true){
            System.out.print("Enter Passport Number: ");
            PassPortNo = scanner.nextLine();

            if(isValidPassport(PassPortNo)){
                return PassPortNo;
            }
            System.out.println("Enter Valid Passport Number, Please try again");
        }
    }

    //Password
    public static boolean isValidPassword(String password){
        return password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=!]).{8,}$");
    }

    public static String readPassword(Scanner scanner){
        String password;

        while (true) { 
            System.out.print("Enter Password: ");
            password = scanner.nextLine();

            if(isValidPassword(password)){

                return password;

            }
            System.out.println("Password does not meet requirements, Please try again");
        }
    }
}