package service.booking;

import domain.booking.BookingContext;

import java.util.*;
import java.util.stream.Collectors;

public class BookingService {
    // In-memory Database for Bookings
    private final Map<String, BookingContext> bookingDatabase = new HashMap<>();

    public void saveBooking(BookingContext booking) {
        bookingDatabase.put(booking.getPnr(), booking);
        System.out.println("[DB] Booking " + booking.getPnr() + " saved to repository.");
    }

    // 4.4 Booking Retrieval
    public Optional<BookingContext> retrieveByPnr(String pnr) {
        return Optional.ofNullable(bookingDatabase.get(pnr.toUpperCase()));
    }

    public List<BookingContext> retrieveByUserEmail(String email) {
        return bookingDatabase.values().stream()
                .filter(b -> b.getUserEmail().equalsIgnoreCase(email))
                .collect(Collectors.toList());
    }

    // 4.5 Booking History Management
    public void displayBookingHistory(String email, String statusFilter) {
        List<BookingContext> history = retrieveByUserEmail(email).stream()
                .filter(b -> statusFilter == null || b.getCurrentState().getStateName().equalsIgnoreCase(statusFilter))
                .sorted(Comparator.comparing(BookingContext::getBookingDate).reversed()) // Newest first
                .toList();

        System.out.println("\n--- BOOKING HISTORY FOR: " + email + " ---");
        if (history.isEmpty()) {
            System.out.println("No bookings found.");
            return;
        }

        for (BookingContext b : history) {
            System.out.printf("[%s] PNR: %s | Flight: %s | Date: %s | Fare: ₹%.2f | E-Ticket: %s%n",
                    b.getCurrentState().getStateName(),
                    b.getPnr(),
                    b.getFlightNumber(),
                    b.getBookingDate().toLocalDate(),
                    b.getTotalFare(),
                    b.getETicketNumber() != null ? b.getETicketNumber() : "N/A"
            );
        }
    }

    // Simulate Exporting to PDF
    public void exportBookingToPDF(String pnr) {
        Optional<BookingContext> bookingOpt = retrieveByPnr(pnr);
        if (bookingOpt.isEmpty()) {
            System.out.println("[EXPORT ERROR] PNR not found.");
            return;
        }

        BookingContext b = bookingOpt.get();
        System.out.println("\n[PDF GENERATOR] Generating E-Ticket PDF for " + pnr + "...");
        System.out.println("--------------------------------------------------");
        System.out.println("                AIRLINE E-TICKET                  ");
        System.out.println("--------------------------------------------------");
        System.out.println("PNR: " + b.getPnr() + "      TICKET NO: " + b.getETicketNumber());
        System.out.println("FLIGHT: " + b.getFlightNumber() + "   STATUS: " + b.getCurrentState().getStateName());
        System.out.println("PASSENGERS: ");
        b.getPassengers().forEach(p -> System.out.println(" - " + p.fullName() + " (" + b.getSelectedSeats() + ")")); // Simplified print
        System.out.println("SEATS: " + String.join(", ", b.getSelectedSeats()));
        System.out.println("TOTAL PAID: ₹" + b.getTotalFare());
        System.out.println("--------------------------------------------------");
        System.out.println("-> Saved as: " + b.getPnr() + "_Ticket.pdf\n");
    }
}