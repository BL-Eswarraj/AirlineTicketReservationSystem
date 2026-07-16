import domain.booking.BookingContext;
import domain.booking.PassengerInfo;
import service.booking.BookingService;

import java.util.List;

public class BookingManagementDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC4: BOOKING MANAGEMENT & STATE PATTERN DEMO ");
        System.out.println("==================================================");

        BookingService bookingService = new BookingService();

        // ---------------------------------------------------------
        // 4.1 & 4.2 INITIATE BOOKING AND PASSENGER DETAILS
        // ---------------------------------------------------------
        System.out.println("\n--- Step 1: Initiating Booking ---");
        // Base fare is ₹4500.00
        BookingContext myBooking = new BookingContext("AI-101", "anbu@srm.edu.in", 4500.0);

        System.out.println("\n--- Step 2: Adding Passengers ---");
        List<PassengerInfo> passengers = List.of(
                new PassengerInfo("Annadurai Anbarasu", 23, "Male", "AADHAAR-1234", "Veg", "None"),
                new PassengerInfo("Sister Anbarasu", 20, "Female", "AADHAAR-5678", "Veg", "None")
        );
        myBooking.addPassengers(passengers);

        // ---------------------------------------------------------
        // 4.1 SEAT SELECTION & STATE TRANSITIONS
        // ---------------------------------------------------------
        System.out.println("\n--- Step 3: Selecting Seats (Requires 2 seats) ---");
        // We select two premium seats with a total surcharge of ₹3000
        myBooking.selectSeats(List.of("2A", "2B"), 3000.0);

        // ---------------------------------------------------------
        // 4.3 PAYMENT & BOOKING CONFIRMATION
        // ---------------------------------------------------------
        System.out.println("\n--- Step 4: Initiating Payment ---");
        myBooking.processPayment(0); // Transition to PAYMENT_PENDING

        System.out.println("\n--- Step 5: Processing Actual Payment ---");
        // Total should be: (Base 4500 * 2 passengers) + 3000 Surcharge = 12000
        System.out.println("Required Amount: ₹" + myBooking.getTotalFare());

        // Simulating a failed payment amount
        myBooking.processPayment(10000.0);

        // Simulating the correct payment amount
        myBooking.processPayment(12000.0);

        // Save to Database
        bookingService.saveBooking(myBooking);

        // ---------------------------------------------------------
        // 4.4 & 4.5 BOOKING RETRIEVAL & EXPORT
        // ---------------------------------------------------------
        System.out.println("\n--- Step 6: Booking History & Retrieval ---");

        // Create a cancelled booking just to show history filtering
        BookingContext cancelledBooking = new BookingContext("6E-555", "anbu@srm.edu.in", 3000.0);
        cancelledBooking.cancelBooking();
        bookingService.saveBooking(cancelledBooking);

        // Display all history
        bookingService.displayBookingHistory("anbu@srm.edu.in", null);

        // Display only confirmed history
        System.out.println("\n[Filtered by CONFIRMED only]");
        bookingService.displayBookingHistory("anbu@srm.edu.in", "CONFIRMED");

        // Simulate PDF generation for the confirmed ticket
        bookingService.exportBookingToPDF(myBooking.getPnr());

        // ---------------------------------------------------------
        // 7.1 CANCELLATION CAPABILITY
        // ---------------------------------------------------------
        System.out.println("--- Step 7: Cancelling the Confirmed Ticket ---");
        myBooking.cancelBooking();

        System.out.println("\n==================================================");
        System.out.println(" UC4 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}