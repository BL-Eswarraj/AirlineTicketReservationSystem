package service.modification;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class ModificationPolicy {
    private static final int FREE_MODIFICATION_WINDOW_HOURS = 24;
    private static final int MIN_HOURS_BEFORE_DEPARTURE = 4;

    public static boolean isEligibleForModification(LocalDateTime bookingTime, LocalDateTime flightDepartureTime) {
        long hoursToDeparture = ChronoUnit.HOURS.between(LocalDateTime.now(), flightDepartureTime);
        if (hoursToDeparture < MIN_HOURS_BEFORE_DEPARTURE) {
            System.out.println("[POLICY ERROR] Modifications are not permitted within " + MIN_HOURS_BEFORE_DEPARTURE + " hours of departure.");
            return false;
        }
        return true;
    }

    public static double calculateModificationFee(ModificationType type, LocalDateTime bookingTime) {
        long hoursSinceBooking = ChronoUnit.HOURS.between(bookingTime, LocalDateTime.now());
        if (hoursSinceBooking <= FREE_MODIFICATION_WINDOW_HOURS) {
            System.out.println("[POLICY BENEFIT] Modification requested within 24 hours of booking. Base penalty waived!");
            return 0.0;
        }
        return type.getBaseFee();
    }
}