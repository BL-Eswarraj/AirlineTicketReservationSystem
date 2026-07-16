import domain.rules.FareBreakdown;
import domain.rules.PassengerTravelDetails;
import domain.rules.TravelClass;
import service.rules.FareCalculationEngine;

import java.time.LocalDateTime;

public class BusinessRulesDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC16: BUSINESS RULES & FARE CALCULATION DEMO ");
        System.out.println("==================================================");

        FareCalculationEngine engine = new FareCalculationEngine();

        // --- SCENARIO 1: EARLY BIRD BOOKING IN ECONOMY (NO EXCESS BAGGAGE) ---
        System.out.println("\n--- SCENARIO 1: Early Bird Reservation (Booked 35 Days in Advance, Economy) ---");
        PassengerTravelDetails earlyBird = new PassengerTravelDetails(
                "PNR-EARLY-01",
                "Annadurai Anbarasu",
                "AI-101",
                "MAA -> DEL",
                TravelClass.ECONOMY,
                6000.0,                    // ₹6000 initial base fare
                14.0,                      // 14kg baggage (within 15kg limit)
                LocalDateTime.now().minusDays(35), // Booked 35 days ago
                LocalDateTime.now().plusDays(1),   // Departing tomorrow
                false                      // Not peak season
        );

        FareBreakdown breakdown1 = engine.calculateFare(earlyBird);
        System.out.println(breakdown1);

        // --- SCENARIO 2: FESTIVAL RUSH WITH EXCESS BAGGAGE (BUSINESS CLASS) ---
        System.out.println("\n--- SCENARIO 2: Diwali Peak Rush + Excess Baggage (Business Class) ---");
        PassengerTravelDetails festivalSurge = new PassengerTravelDetails(
                "PNR-FEST-02",
                "Ramesh Kumar",
                "UK-808",
                "DEL -> BOM",
                TravelClass.BUSINESS,
                15000.0,                   // ₹15000 initial base fare
                36.5,                      // 36.5kg baggage (6.5kg over 30kg Business allowance!)
                LocalDateTime.now().minusDays(5),  // Booked only 5 days ago (No early bird discount)
                LocalDateTime.now().plusDays(2),   // Departing in 2 days
                true                       // IS Peak Festival Season
        );

        FareBreakdown breakdown2 = engine.calculateFare(festivalSurge);
        System.out.println(breakdown2);

        // --- SCENARIO 3: LAST MINUTE PREMIUM ECONOMY ---
        System.out.println("\n--- SCENARIO 3: Last Minute Premium Economy (20 Days Advance, Compliant Baggage) ---");
        PassengerTravelDetails midAdvance = new PassengerTravelDetails(
                "PNR-PREM-03",
                "John Doe",
                "6E-303",
                "BLR -> HYD",
                TravelClass.PREMIUM_ECONOMY,
                8500.0,
                18.0,                      // Within 20kg limit
                LocalDateTime.now().minusDays(20), // 20 days advance -> 5% discount
                LocalDateTime.now().plusDays(1),
                false
        );

        FareBreakdown breakdown3 = engine.calculateFare(midAdvance);
        System.out.println(breakdown3);

        System.out.println("==================================================");
        System.out.println(" UC16 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}