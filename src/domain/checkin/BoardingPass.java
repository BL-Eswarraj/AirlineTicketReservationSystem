package domain.checkin;

import java.time.LocalDateTime;

public record BoardingPass(
        String boardingPassId,
        String pnr,
        String eTicketNumber,
        String passengerName,
        String flightNumber,
        String route,
        String seatNumber,
        String travelClass,
        String gate,
        LocalDateTime boardingTime,
        String boardingZone,
        String securityBarcode
) {
    @Override
    public String toString() {
        return String.format("Boarding Pass [%s] | PNR: %s | Passenger: %s | Flight: %s | Seat: %s | Gate: %s | Zone: %s",
                securityBarcode, pnr, passengerName, flightNumber, seatNumber, gate, boardingZone);
    }
}