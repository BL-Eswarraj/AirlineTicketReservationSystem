import domain.checkin.*;
import service.checkin.WebCheckInService;

import java.time.LocalDateTime;
import java.util.Optional;

public class OnlineCheckInDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC14: ONLINE CHECK-IN MODULE DEMO ");
        System.out.println("==================================================");

        WebCheckInService checkInService = new WebCheckInService();

        // --- STEP 1: REGISTER MOCK BOOKINGS INTO REPOSITORY ---
        System.out.println("\n--- 1. Seeding Bookings into Check-In Repository ---");

        // 1. Eligible Booking (Departing in 12 hours - well within 48h to 1h window)
        PassengerCheckInRecord eligibleRecord = new PassengerCheckInRecord(
                "PNR-CHK-101", "TKT-998877", "Annadurai Anbarasu", "AI-101",
                "MAA -> DEL", LocalDateTime.now().plusHours(12), "ECONOMY", "14A"
        );

        // 2. Too Early Booking (Departing in 72 hours - window not open yet)
        PassengerCheckInRecord earlyRecord = new PassengerCheckInRecord(
                "PNR-EAR-202", "TKT-112233", "Ramesh Kumar", "UK-808",
                "DEL -> BOM", LocalDateTime.now().plusHours(72), "BUSINESS", "2F"
        );

        // 3. Too Late Booking (Departing in 30 minutes - web window closed)
        PassengerCheckInRecord lateRecord = new PassengerCheckInRecord(
                "PNR-LAT-303", "TKT-445566", "Late Traveler", "6E-303",
                "BLR -> HYD", LocalDateTime.now().plusMinutes(30), "ECONOMY", "22C"
        );

        checkInService.registerBookingForCheckIn(eligibleRecord);
        checkInService.registerBookingForCheckIn(earlyRecord);
        checkInService.registerBookingForCheckIn(lateRecord);

        // --- STEP 2: TEST TIME WINDOW RESTRICTIONS ---
        System.out.println("\n--- 2. Testing Check-In Time Window Enforcements ---");

        System.out.println(">> Attempting check-in 72 hours before departure:");
        checkInService.initiateCheckIn("PNR-EAR-202", "Ramesh Kumar");

        System.out.println("\n>> Attempting check-in 30 minutes before departure:");
        checkInService.initiateCheckIn("PNR-LAT-303", "Late Traveler");

        // --- STEP 3: TEST DANGEROUS GOODS SAFETY REJECTION ---
        System.out.println("\n--- 3. Testing Dangerous Goods Safety Enforcement ---");
        Optional<PassengerCheckInRecord> sessionOpt = checkInService.initiateCheckIn("PNR-CHK-101", "Annadurai Anbarasu");

        if (sessionOpt.isPresent()) {
            // Passenger tries to check in without agreeing to dangerous goods terms
            BaggageDeclaration nonCompliantBag = new BaggageDeclaration(1, 14.5, 1, false);
            checkInService.completeCheckIn("PNR-CHK-101", nonCompliantBag, "14A", "G12");
        }

        // --- STEP 4: SUCCESSFUL CHECK-IN & BOARDING PASS GENERATION ---
        System.out.println("\n--- 4. Completing Valid Check-In & Issuing Boarding Pass ---");
        if (sessionOpt.isPresent()) {
            // Valid declaration: 1 checked bag (14.5 kg), 1 cabin bag, dangerous goods accepted
            BaggageDeclaration validBag = new BaggageDeclaration(1, 14.5, 1, true);
            checkInService.completeCheckIn("PNR-CHK-101", validBag, "14A", "G12");
        }

        // --- STEP 5: TEST IDEMPOTENCY (DUPLICATE CHECK-IN ATTEMPTS) ---
        System.out.println("--- 5. Testing Idempotency (Attempting Check-In Again) ---");
        checkInService.initiateCheckIn("PNR-CHK-101", "Annadurai Anbarasu");

        // --- STEP 6: TEST GATE BOARDING SCAN ---
        System.out.println("--- 6. Simulating Airport Gate Boarding Scan ---");
        checkInService.getRecord("PNR-CHK-101").ifPresent(PassengerCheckInRecord::markBoarded);

        System.out.println("\n==================================================");
        System.out.println(" UC14 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}