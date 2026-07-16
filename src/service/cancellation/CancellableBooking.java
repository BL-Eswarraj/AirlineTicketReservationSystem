package service.cancellation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class CancellableBooking {
    private final String pnr;
    private final String flightNumber;
    private final LocalDateTime departureTime;
    private final TicketType ticketType;
    private String bookingStatus;
    private String eTicketNumber;
    private final List<CancellablePassenger> passengers;

    public CancellableBooking(String pnr, String flightNumber, LocalDateTime departureTime, TicketType ticketType) {
        this.pnr = pnr;
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;
        this.ticketType = ticketType;
        this.bookingStatus = "CONFIRMED";
        this.eTicketNumber = "TKT-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.passengers = new ArrayList<>();
    }

    public void addPassenger(CancellablePassenger passenger) {
        this.passengers.add(passenger);
    }

    public List<CancellablePassenger> getActivePassengers() {
        return passengers.stream().filter(p -> !p.isCancelled()).collect(Collectors.toList());
    }

    public double getTotalActivePaidAmount() {
        return getActivePassengers().stream().mapToDouble(CancellablePassenger::getIndividualFarePaid).sum();
    }

    public double getTotalActiveTaxAmount() {
        return getActivePassengers().stream().mapToDouble(CancellablePassenger::getIndividualTaxPaid).sum();
    }

    public void updateStatus(String newStatus) {
        this.bookingStatus = newStatus;
    }

    public void regenerateETicket() {
        this.eTicketNumber = "TKT-REV-" + UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        System.out.println("[TICKET SYSTEM] Updated E-Ticket generated for remaining passengers: " + this.eTicketNumber);
    }

    // Getters
    public String getPnr() { return pnr; }
    public String getFlightNumber() { return flightNumber; }
    public LocalDateTime getDepartureTime() { return departureTime; }
    public TicketType getTicketType() { return ticketType; }
    public String getBookingStatus() { return bookingStatus; }
    public String getETicketNumber() { return eTicketNumber; }
    public List<CancellablePassenger> getAllPassengers() { return passengers; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("PNR: %s | Flight: %s | Dep: %s | Status: %s | Ticket Type: %s | E-Ticket: %s%n",
                pnr, flightNumber, departureTime.toLocalDate(), bookingStatus, ticketType, eTicketNumber));
        sb.append("Passengers:\n");
        passengers.forEach(p -> sb.append("  -> ").append(p).append("\n"));
        return sb.toString();
    }
}