import service.cancellation.*;
import java.time.LocalDateTime;
import java.util.List;

public class BookingCancellationDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC7: BOOKING CANCELLATION MODULE DEMO ");
        System.out.println("==================================================");

        CancellationService cancellationService = new CancellationService();

        // --- STEP 1: TEST PARTIAL CANCELLATION ON STANDARD TICKET (> 24 HRS BEFORE DEPARTURE) ---
        System.out.println("\n--- 1. Testing Partial Cancellation (Standard Ticket, > 24 Hrs to Departure) ---");
        CancellableBooking groupBooking = new CancellableBooking(
                "PNR-GRP-101",
                "AI-303",
                LocalDateTime.now().plusDays(5), // Departing 5 days from now
                TicketType.STANDARD
        );
        // Adding a family of 3 (Each paid ₹5000 fare, including ₹800 tax)
        groupBooking.addPassenger(new CancellablePassenger("P1", "Annadurai Anbarasu", "12A", 5000.0, 800.0));
        groupBooking.addPassenger(new CancellablePassenger("P2", "Sister Anbarasu", "12B", 5000.0, 800.0));
        groupBooking.addPassenger(new CancellablePassenger("P3", "Mother Anbarasu", "12C", 5000.0, 800.0));

        System.out.println("[INITIAL GROUP BOOKING RECORD]");
        System.out.println(groupBooking);

        // Cancel only Sister (P2) from the booking
        cancellationService.processPartialCancellation(groupBooking, List.of("P2"));

        System.out.println("\n[UPDATED GROUP BOOKING RECORD AFTER PARTIAL CANCELLATION]");
        System.out.println(groupBooking);

        // --- STEP 2: TEST FULL CANCELLATION ON FLEXIBLE TICKET (< 2 HRS TO DEPARTURE) ---
        System.out.println("\n--- 2. Testing Full Cancellation (Flexible Ticket, < 2 Hrs to Departure) ---");
        CancellableBooking flexBooking = new CancellableBooking(
                "PNR-FLX-202",
                "UK-808",
                LocalDateTime.now().plusHours(1), // Departing in 1 hour
                TicketType.FLEXIBLE
        );
        flexBooking.addPassenger(new CancellablePassenger("P1", "Ramesh Kumar", "4D", 8000.0, 1200.0));

        cancellationService.processFullCancellation(flexBooking);

        // --- STEP 3: TEST NON-REFUNDABLE TICKET CANCELLATION ---
        System.out.println("\n--- 3. Testing Non-Refundable Ticket (Only Taxes Refunded) ---");
        CancellableBooking nonRefBooking = new CancellableBooking(
                "PNR-NOR-303",
                "6E-101",
                LocalDateTime.now().plusDays(3),
                TicketType.NON_REFUNDABLE
        );
        nonRefBooking.addPassenger(new CancellablePassenger("P1", "John Doe", "18F", 3500.0, 600.0));

        cancellationService.processFullCancellation(nonRefBooking);

        // --- STEP 4: TEST LAST-MINUTE STANDARD TICKET (< 4 HRS TO DEPARTURE) ---
        System.out.println("\n--- 4. Testing Last-Minute Standard Ticket Cancellation (< 4 Hrs) ---");
        CancellableBooking lastMinBooking = new CancellableBooking(
                "PNR-LST-404",
                "AI-555",
                LocalDateTime.now().plusHours(3), // 3 hours to departure -> 80% penalty
                TicketType.STANDARD
        );
        lastMinBooking.addPassenger(new CancellablePassenger("P1", "Late Traveler", "22C", 10000.0, 1500.0));

        cancellationService.processFullCancellation(lastMinBooking);

        System.out.println("\n==================================================");
        System.out.println(" UC7 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}