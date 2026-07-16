package service.checkin;

import domain.checkin.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class WebCheckInService {
    private static final int CHECK_IN_OPEN_HOURS = 48;
    private static final int CHECK_IN_CLOSE_HOURS = 1;

    private final Map<String, PassengerCheckInRecord> checkInDatabase = new HashMap<>();
    private final BoardingPassGenerator passGenerator = new BoardingPassGenerator();

    public void registerBookingForCheckIn(PassengerCheckInRecord record) {
        checkInDatabase.put(record.getPnr().toUpperCase(), record);
        System.out.printf("[SYSTEM] Registered PNR %s in check-in repository for flight %s.%n",
                record.getPnr(), record.getFlightNumber());
    }

    // Step 1: Validate PNR and Check-In Time Window
    public Optional<PassengerCheckInRecord> initiateCheckIn(String pnr, String passengerName) {
        System.out.println("\n--- INITIATING ONLINE CHECK-IN FOR PNR: " + pnr + " ---");

        PassengerCheckInRecord record = checkInDatabase.get(pnr.toUpperCase());
        if (record == null) {
            System.out.println("[CHECK-IN ERROR] PNR not found in system repository.");
            return Optional.empty();
        }

        if (!record.getPassengerName().equalsIgnoreCase(passengerName.trim())) {
            System.out.println("[CHECK-IN ERROR] Passenger name does not match PNR record.");
            return Optional.empty();
        }

        if (record.getStatus() == CheckInStatus.CHECKED_IN || record.getStatus() == CheckInStatus.BOARDED) {
            System.out.println("[CHECK-IN NOTICE] Passenger is already checked in!");
            if (record.getBoardingPass() != null) {
                passGenerator.renderBoardingPassAscii(record.getBoardingPass());
            }
            return Optional.of(record);
        }

        // Enforce time window (48 hours to 1 hour before departure)
        long hoursToDeparture = ChronoUnit.HOURS.between(LocalDateTime.now(), record.getDepartureTime());
        System.out.printf("[TIME CHECK] Current window: %d hours until departure scheduled for %s.%n",
                hoursToDeparture, record.getDepartureTime().toLocalDate());

        if (hoursToDeparture > CHECK_IN_OPEN_HOURS) {
            System.out.printf("[CHECK-IN DENIED] Web check-in opens %d hours before departure. Please return later.%n", CHECK_IN_OPEN_HOURS);
            return Optional.empty();
        }
        if (hoursToDeparture < CHECK_IN_CLOSE_HOURS) {
            System.out.printf("[CHECK-IN CLOSED] Web check-in closes %d hour(s) before departure. Please proceed to airport counter.%n", CHECK_IN_CLOSE_HOURS);
            return Optional.empty();
        }

        record.setStatus(CheckInStatus.ELIGIBLE);
        System.out.println("[ELIGIBILITY CONFIRMED] Passenger eligible for check-in. Proceeding to baggage declaration...");
        return Optional.of(record);
    }

    // Step 2: Complete Check-in with Baggage Declaration & Seat Confirmation
    public boolean completeCheckIn(String pnr, BaggageDeclaration baggage, String confirmedSeat, String assignedGate) {
        PassengerCheckInRecord record = checkInDatabase.get(pnr.toUpperCase());
        if (record == null || record.getStatus() != CheckInStatus.ELIGIBLE) {
            System.out.println("[CHECK-IN ERROR] Cannot complete check-in. Passenger must be validated and ELIGIBLE first.");
            return false;
        }

        // Enforce baggage allowances based on class (Example: Economy = 2 bags/20kg max)
        int maxBags = record.getTravelClass().equalsIgnoreCase("BUSINESS") ? 3 : 2;
        double maxWeight = record.getTravelClass().equalsIgnoreCase("BUSINESS") ? 35.0 : 20.0;

        System.out.println("[BAGGAGE VERIFICATION] Validating baggage declaration and dangerous goods compliance...");
        if (!baggage.isCompliant(maxBags, maxWeight)) {
            return false;
        }

        // Generate Boarding Pass
        BoardingPass boardingPass = passGenerator.generate(record, assignedGate);

        // Commit State
        record.completeCheckIn(baggage, confirmedSeat, boardingPass);

        System.out.println("[CHECK-IN SUCCESS] Online check-in completed successfully! Boarding pass generated.");
        passGenerator.renderBoardingPassAscii(boardingPass);
        return true;
    }

    public Optional<PassengerCheckInRecord> getRecord(String pnr) {
        return Optional.ofNullable(checkInDatabase.get(pnr.toUpperCase()));
    }
}