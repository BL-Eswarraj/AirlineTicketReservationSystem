package service.checkin;

import domain.checkin.BoardingPass;
import domain.checkin.PassengerCheckInRecord;

import java.time.LocalDateTime;
import java.util.UUID;

public class BoardingPassGenerator {

    public BoardingPass generate(PassengerCheckInRecord record, String gate) {
        // Boarding typically begins 45 minutes prior to scheduled departure
        LocalDateTime boardingTime = record.getDepartureTime().minusMinutes(45);

        // Determine Boarding Zone based on Travel Class and Seat Row
        String zone = determineBoardingZone(record.getTravelClass(), record.getAssignedSeat());

        // Generate simulated cryptographic barcode/QR string
        String barcode = String.format("BC-%s-%s-%s",
                record.getPnr(), record.getAssignedSeat(), UUID.randomUUID().toString().substring(0, 4).toUpperCase());

        String passId = "BP-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();

        return new BoardingPass(
                passId,
                record.getPnr(),
                record.getETicketNumber(),
                record.getPassengerName(),
                record.getFlightNumber(),
                record.getRoute(),
                record.getAssignedSeat(),
                record.getTravelClass(),
                gate,
                boardingTime,
                zone,
                barcode
        );
    }

    private String determineBoardingZone(String travelClass, String seatNumber) {
        if (travelClass.equalsIgnoreCase("FIRST") || travelClass.equalsIgnoreCase("BUSINESS")) {
            return "Zone 1 (Priority)";
        }
        if (travelClass.equalsIgnoreCase("PREMIUM_ECONOMY")) {
            return "Zone 2 (Premium)";
        }
        // Extract row number from seat (e.g., "14A" -> 14)
        try {
            int row = Integer.parseInt(seatNumber.replaceAll("\\D", ""));
            if (row > 20) return "Zone 3 (Rear Economy)";
            return "Zone 4 (Front Economy)";
        } catch (NumberFormatException e) {
            return "Zone 4 (General)";
        }
    }

    // Render ASCII Digital Boarding Pass
    public void renderBoardingPassAscii(BoardingPass bp) {
        System.out.println("\n+=============================================================================+");
        System.out.println("|                     SRM GLOBAL AIRWAYS - DIGITAL BOARDING PASS              |");
        System.out.println("+=============================================================================+");
        System.out.printf("| PASSENGER: %-25s | PNR: %-12s | E-TICKET: %-10s |%n",
                bp.passengerName().toUpperCase(), bp.pnr(), bp.eTicketNumber());
        System.out.printf("| FLIGHT   : %-25s | ROUTE: %-10s   | CLASS   : %-10s |%n",
                bp.flightNumber(), bp.route(), bp.travelClass());
        System.out.println("+-----------------------------------------------------------------------------+");
        System.out.printf("| GATE     : %-8s | SEAT: %-8s | BOARDING TIME: %-16s |%n",
                bp.gate(), bp.seatNumber(), bp.boardingTime().toLocalTime() + " IST");
        System.out.printf("| ZONE     : %-20s | PASS ID      : %-16s |%n",
                bp.boardingZone(), bp.boardingPassId());
        System.out.println("+-----------------------------------------------------------------------------+");
        System.out.printf("| SECURITY BARCODE : ||| | |||| || ||| | |||| || [%-22s]  |%n", bp.securityBarcode());
        System.out.println("+=============================================================================+\n");
    }
}