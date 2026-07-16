package domain.checkin;

import java.time.LocalDateTime;
import java.util.UUID;

public class PassengerCheckInRecord {
    private final String checkInId;
    private final String pnr;
    private final String eTicketNumber;
    private final String passengerName;
    private final String flightNumber;
    private final String route;
    private final LocalDateTime departureTime;
    private final String travelClass;
    private String assignedSeat;
    private CheckInStatus status;
    private BaggageDeclaration baggageDeclaration;
    private BoardingPass boardingPass;

    public PassengerCheckInRecord(String pnr, String eTicketNumber, String passengerName, String flightNumber,
                                  String route, LocalDateTime departureTime, String travelClass, String initialSeat) {
        this.checkInId = "CHK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.pnr = pnr;
        this.eTicketNumber = eTicketNumber;
        this.passengerName = passengerName;
        this.flightNumber = flightNumber;
        this.route = route;
        this.departureTime = departureTime;
        this.travelClass = travelClass;
        this.assignedSeat = initialSeat;
        this.status = CheckInStatus.NOT_CHECKED_IN;
    }

    public void completeCheckIn(BaggageDeclaration baggage, String confirmedSeat, BoardingPass boardingPass) {
        this.baggageDeclaration = baggage;
        this.assignedSeat = confirmedSeat;
        this.boardingPass = boardingPass;
        this.status = CheckInStatus.CHECKED_IN;
    }

    public void markBoarded() {
        if (this.status == CheckInStatus.CHECKED_IN) {
            this.status = CheckInStatus.BOARDED;
            System.out.printf("[GATE SCAN] Passenger %s (PNR: %s) has successfully boarded flight %s.%n",
                    passengerName, pnr, flightNumber);
        } else {
            throw new IllegalStateException("Cannot board passenger. Current status: " + status);
        }
    }

    // Getters and Setters
    public String getCheckInId() { return checkInId; }
    public String getPnr() { return pnr; }
    public String getETicketNumber() { return eTicketNumber; }
    public String getPassengerName() { return passengerName; }
    public String getFlightNumber() { return flightNumber; }
    public String getRoute() { return route; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public String getTravelClass() { return travelClass; }
    public String getAssignedSeat() { return assignedSeat; }
    public void setAssignedSeat(String seat) { this.assignedSeat = seat; }
    public CheckInStatus getStatus() { return status; }
    public void setStatus(CheckInStatus status) { this.status = status; }
    public BaggageDeclaration getBaggageDeclaration() { return baggageDeclaration; }
    public BoardingPass getBoardingPass() { return boardingPass; }

    @Override
    public String toString() {
        return String.format("PNR: %s | Passenger: %s | Flight: %s | Seat: %s | Status: %s",
                pnr, passengerName, flightNumber, assignedSeat, status);
    }
}