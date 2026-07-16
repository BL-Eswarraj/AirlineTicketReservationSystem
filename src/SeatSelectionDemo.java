import domain.seat.*;
import service.seat.SeatSelectionService;
import java.util.List;

public class SeatSelectionDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC3: SEAT SELECTION & MAP VISUALIZATION DEMO ");
        System.out.println("==================================================");

        SeatSelectionService seatService = new SeatSelectionService();

        // Initialize a mock Airbus A320 Seat Map (15 rows, 3-3 layout: ABC-DEF)
        SeatMap a320Map = new SeatMap("Airbus A320", 15, "ABC-DEF");

        // Pre-book some seats to make the map look realistic
        a320Map.getSeat("1A").confirmBooking();
        a320Map.getSeat("1B").confirmBooking();
        a320Map.getSeat("5C").confirmBooking();
        a320Map.getSeat("5D").confirmBooking();
        a320Map.getSeat("12A").confirmBooking();
        a320Map.getSeat("12F").blockSeat(); // Blocked by crew

        // --- STEP 1: RENDER SEAT MAP (3.1) ---
        System.out.println("\n--- 1. Initial Interactive Seat Map ---");
        seatService.renderSeatMap(a320Map);

        // --- STEP 2: STANDARD & PREMIUM SEAT SELECTION ---
        System.out.println("--- 2. Selecting Individual Seats ---");
        // Select a Premium front row seat
        seatService.selectSeat(a320Map, "2A", "Annadurai Anbarasu", 23, true);
        // Select a Standard seat
        seatService.selectSeat(a320Map, "8C", "Ramesh Kumar", 45, true);

        // --- STEP 3: CONFLICT VALIDATION ---
        System.out.println("\n--- 3. Testing Seat Availability Conflict ---");
        // Try selecting an already booked seat (1A)
        seatService.selectSeat(a320Map, "1A", "Late Comer", 30, true);

        // --- STEP 4: EMERGENCY EXIT ROW RESTRICTION VALIDATION ---
        System.out.println("\n--- 4. Testing Emergency Exit Row Restrictions ---");
        // Attempt 1: Minor passenger (16 years old) trying to book Row 12
        seatService.selectSeat(a320Map, "12B", "Young Student", 16, true);

        // Attempt 2: Elderly/Non-able-bodied passenger
        seatService.selectSeat(a320Map, "12C", "Senior Citizen", 72, false);

        // Attempt 3: Valid passenger meeting safety criteria
        seatService.selectSeat(a320Map, "12D", "Fit Traveler", 28, true);

        // --- STEP 5: FAMILY / GROUP AUTO-ASSIGNMENT ---
        System.out.println("\n--- 5. Testing Group Auto-Assignment (Family of 4) ---");
        List<String> familyMembers = List.of("John Doe", "Jane Doe", "Kid One", "Kid Two");
        List<Seat> assignedGroup = seatService.autoAssignGroupSeats(a320Map, familyMembers);

        // --- STEP 6: RENDER UPDATED MAP & CALCULATE SURCHARGE ---
        System.out.println("\n--- 6. Updated Seat Map After Selections ---");
        seatService.renderSeatMap(a320Map);

        List<String> selectedSeats = List.of("2A", "8C", "12D");
        double totalSurcharge = seatService.calculateTotalSurcharge(a320Map, selectedSeats);
        System.out.printf("Total Seat Selection Surcharge for %s: ₹%.2f%n", selectedSeats, totalSurcharge);

        // --- STEP 7: RELEASE SEAT BEFORE PAYMENT ---
        System.out.println("\n--- 7. Releasing a Seat Before Payment ---");
        seatService.releaseSeats(a320Map, List.of("8C"));
        System.out.println("Status of 8C after release: " + a320Map.getSeat("8C").getStatus());

        System.out.println("\n==================================================");
        System.out.println(" UC3 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}