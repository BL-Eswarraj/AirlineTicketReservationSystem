import service.modification.*;
import java.time.LocalDateTime;
import java.util.List;

public class BookingModificationDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC6: BOOKING MODIFICATION MODULE DEMO ");
        System.out.println("==================================================");

        FlightChangeService flightChangeService = new FlightChangeService();
        PassengerModificationService passengerService = new PassengerModificationService();
        SeatChangeService seatChangeService = new SeatChangeService();

        // Initialize a mock confirmed booking
        ModifiableBooking booking = new ModifiableBooking(
                "PNR-MOD-889",
                "AI-101",
                "DEL -> BOM",
                LocalDateTime.now().plusDays(5), // Departing 5 days from now
                5000.0,  // Base fare paid
                0.0,     // No seat surcharge originally (Standard seat 14A)
                "Annadurai Anbarasu", // Intentional typo in first name
                "PASS-ID-778899",
                "anbu@srm.edu.in",
                "+919800000000"
        );

        System.out.println("\n[INITIAL BOOKING RECORD]");
        System.out.println(booking);

        // --- STEP 1: TEST NAME CORRECTION WITH DOCUMENT VALIDATION (6.2) ---
        System.out.println("\n--- 1. Testing Passenger Name Correction (6.2) ---");
        // Attempt 1: Fraudulent attempt using a different ID document
        passengerService.correctPassengerName(booking, "Ramesh Kumar", "FAKE-ID-000000");

        // Attempt 2: Valid typo correction matching original ID document
        passengerService.correctPassengerName(booking, "Annadurai Anbarasu", "PASS-ID-778899");

        // --- STEP 2: TEST FREE PREFERENCE & CONTACT UPDATES (6.2) ---
        System.out.println("\n--- 2. Testing Contact & Preference Updates (6.2) ---");
        passengerService.updateContactDetails(booking, "anbarasu.cs@srm.edu.in", "+919811111111");
        passengerService.updateTravelPreferences(booking, "SOUTH_INDIAN_VEG", "WHEELCHAIR_ASSIST");

        // --- STEP 3: TEST SEAT UPGRADE (6.3) ---
        System.out.println("\n--- 3. Testing Seat Upgrade (6.3) ---");
        // Upgrading from Standard (14A, ₹0) to Emergency Exit Row (12F, ₹1000 surcharge)
        seatChangeService.changeSeats(booking, List.of("12F"), 1000.0);

        // --- STEP 4: TEST FLIGHT CHANGE WITH FARE DIFFERENCE (6.1) ---
        System.out.println("\n--- 4. Testing Full Flight Change (6.1) ---");
        // Switching from AI-101 (₹5000 base) to UK-808 (₹6500 base) on a different date
        flightChangeService.processFlightChange(
                booking,
                "UK-808",
                LocalDateTime.now().plusDays(7),
                6500.0, // Higher base fare
                List.of("3A"), // New Premium Seat
                1500.0  // New seat surcharge
        );

        // --- STEP 5: FINAL RECORD SNAPSHOT ---
        System.out.println("\n[FINAL REVISED BOOKING RECORD]");
        System.out.println(booking);

        System.out.println("\n==================================================");
        System.out.println(" UC6 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}