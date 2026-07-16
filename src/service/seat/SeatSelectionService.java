package service.seat;

import domain.seat.*;
import java.util.*;

public class SeatSelectionService {

    // ANSI Color Codes for Terminal Rendering
    private static final String RESET = "\u001B[0m";
    private static final String GREEN = "\u001B[32m";  // Available
    private static final String RED = "\u001B[31m";    // Booked
    private static final String YELLOW = "\u001B[33m"; // Locked / Reserved
    private static final String GRAY = "\u001B[90m";   // Blocked
    private static final String CYAN = "\u001B[36m";   // Exit Row / Premium highlight

    // 3.1 Interactive Seat Map Visualization
    public void renderSeatMap(SeatMap seatMap) {
        System.out.println("\n==================================================");
        System.out.println(" AIRCRAFT SEAT MAP: " + seatMap.getAircraftType());
        System.out.println(" [" + GREEN + "O" + RESET + "] Available  [" + RED + "X" + RESET + "] Booked  [" + YELLOW + "L" + RESET + "] Selected  [" + GRAY + "B" + RESET + "] Blocked");
        System.out.println(" " + CYAN + "* Premium Row (₹1500)    ! Emergency Exit (₹1000)" + RESET);
        System.out.println("==================================================");

        String layout = seatMap.getLayoutString();
        String columns = layout.replace("-", "");

        // Print Header Lettering
        System.out.print("      ");
        for (char ch : layout.toCharArray()) {
            if (ch == '-') System.out.print("   ");
            else System.out.printf("  %c ", ch);
        }
        System.out.println("\n    +------------------------------------+");

        for (int row = 1; row <= seatMap.getTotalRows(); row++) {
            // Row identifier indicator
            char rowIndicator = ' ';
            if (row <= 2) rowIndicator = '*';
            else if (row == 12 || row == 13) rowIndicator = '!';

            System.out.printf(" %2d%c | ", row, rowIndicator);

            for (char ch : layout.toCharArray()) {
                if (ch == '-') {
                    System.out.print("   "); // Aisle corridor
                    continue;
                }

                Seat seat = seatMap.getSeat(row + String.valueOf(ch));
                String marker = getSeatMarker(seat);
                System.out.print(marker);
            }
            System.out.println(" |");
        }
        System.out.println("    +------------------------------------+");
        System.out.println("                 [ FRONT OF PLANE ]               \n");
    }

    private String getSeatMarker(Seat seat) {
        if (seat == null) return " [ ] ";
        return switch (seat.getStatus()) {
            case AVAILABLE -> GREEN + "[O]" + RESET + " ";
            case BOOKED -> RED + "[X]" + RESET + " ";
            case TEMPORARILY_LOCKED -> YELLOW + "[L]" + RESET + " ";
            case BLOCKED -> GRAY + "[B]" + RESET + " ";
        };
    }

    // 3.2 Seat Selection with Validation & Restrictions
    public boolean selectSeat(SeatMap seatMap, String seatNumber, String passengerName, int passengerAge, boolean isAbleBodied) {
        Seat seat = seatMap.getSeat(seatNumber);

        if (seat == null) {
            System.out.println("[SEAT ERROR] Seat " + seatNumber + " does not exist on this aircraft.");
            return false;
        }

        // Validate Availability
        if (!seat.isAvailable()) {
            System.out.println("[SEAT ERROR] Seat " + seatNumber + " is currently " + seat.getStatus() + ". Please choose another.");
            return false;
        }

        // Validate Emergency Exit Row Eligibility
        if (seat.getType() == SeatType.EMERGENCY_EXIT) {
            System.out.println("[EXIT ROW CHECK] Validating restrictions for seat " + seatNumber + "...");
            if (passengerAge < 18 || !isAbleBodied) {
                System.out.println("[SEAT RESTRICTION DENIED] Passenger " + passengerName +
                        " does not meet safety criteria for Emergency Exit row (Must be 18+ and able-bodied).");
                return false;
            }
            System.out.println("[EXIT ROW APPROVED] Safety criteria verified.");
        }

        // Lock Seat
        seat.assignTo(passengerName);
        System.out.printf("[SEAT ASSIGNED] %s successfully locked for %s. Surcharge applied: ₹%.2f%n",
                seat.getSeatNumber(), passengerName, seat.getSurcharge());
        return true;
    }

    // 3.2 Auto-assign adjacent seats for family/group bookings
    public List<Seat> autoAssignGroupSeats(SeatMap seatMap, List<String> passengerNames) {
        int groupSize = passengerNames.size();
        System.out.println("\n[GROUP ASSIGNMENT] Searching for " + groupSize + " adjacent available seats...");

        String columns = seatMap.getLayoutString().replace("-", "");
        List<Seat> assignedSeats = new ArrayList<>();

        // Scan row by row for consecutive available seats
        for (int row = 1; row <= seatMap.getTotalRows(); row++) {
            List<Seat> currentConsecutive = new ArrayList<>();

            for (int i = 0; i < columns.length(); i++) {
                Seat seat = seatMap.getSeat(row + String.valueOf(columns.charAt(i)));

                // Don't auto-assign emergency exit rows to random groups without manual verification
                if (seat != null && seat.isAvailable() && seat.getType() != SeatType.EMERGENCY_EXIT) {
                    currentConsecutive.add(seat);
                    if (currentConsecutive.size() == groupSize) {
                        // Found a valid block! Assign them.
                        for (int j = 0; j < groupSize; j++) {
                            Seat s = currentConsecutive.get(j);
                            s.assignTo(passengerNames.get(j));
                            assignedSeats.add(s);
                        }
                        System.out.println("[GROUP ASSIGNMENT SUCCESS] Assigned adjacent seats: " +
                                assignedSeats.stream().map(Seat::getSeatNumber).toList());
                        return assignedSeats;
                    }
                } else {
                    currentConsecutive.clear(); // Reset if sequence breaks
                }
            }
        }

        System.out.println("[GROUP ASSIGNMENT FAILED] Could not find " + groupSize + " consecutive seats together. Falling back to scattered assignment.");
        return assignedSeats;
    }

    // Calculate total surcharge for selected seats
    public double calculateTotalSurcharge(SeatMap seatMap, List<String> seatNumbers) {
        double total = 0;
        for (String seatNo : seatNumbers) {
            Seat s = seatMap.getSeat(seatNo);
            if (s != null) total += s.getSurcharge();
        }
        return total;
    }

    // Release seats before payment if passenger changes mind or session times out
    public void releaseSeats(SeatMap seatMap, List<String> seatNumbers) {
        for (String seatNo : seatNumbers) {
            Seat s = seatMap.getSeat(seatNo);
            if (s != null) {
                s.releaseSeat();
                System.out.println("[SEAT RELEASED] Seat " + seatNo + " is now available.");
            }
        }
    }
}