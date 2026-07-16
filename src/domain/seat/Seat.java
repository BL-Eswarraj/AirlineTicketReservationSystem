package domain.seat;

import java.util.ArrayList;
import java.util.List;

public class Seat {
    private final String seatNumber; // e.g., "12A"
    private final int rowNumber;
    private final char columnLetter;
    private SeatStatus status;
    private final SeatCategory category;
    private final SeatType type;
    private final List<String> amenities;
    private String assignedPassengerName;

    public Seat(int rowNumber, char columnLetter, SeatCategory category, SeatType type) {
        this.rowNumber = rowNumber;
        this.columnLetter = columnLetter;
        this.seatNumber = rowNumber + String.valueOf(columnLetter);
        this.category = category;
        this.type = type;
        this.status = SeatStatus.AVAILABLE;
        this.amenities = new ArrayList<>();

        // Populate amenities based on type
        if (type == SeatType.PREMIUM || type == SeatType.EMERGENCY_EXIT || type == SeatType.EXTRA_LEGROOM) {
            amenities.add("Extra Legroom");
        }
        if (type == SeatType.PREMIUM) {
            amenities.add("Power Outlet");
            amenities.add("Priority Service");
        }
    }

    public boolean isAvailable() {
        return status == SeatStatus.AVAILABLE;
    }

    public void assignTo(String passengerName) {
        this.status = SeatStatus.TEMPORARILY_LOCKED;
        this.assignedPassengerName = passengerName;
    }

    public void confirmBooking() {
        this.status = SeatStatus.BOOKED;
    }

    public void releaseSeat() {
        this.status = SeatStatus.AVAILABLE;
        this.assignedPassengerName = null;
    }

    public void blockSeat() {
        this.status = SeatStatus.BLOCKED;
    }

    // Getters
    public String getSeatNumber() { return seatNumber; }
    public int getRowNumber() { return rowNumber; }
    public char getColumnLetter() { return columnLetter; }
    public SeatStatus getStatus() { return status; }
    public SeatCategory getCategory() { return category; }
    public SeatType getType() { return type; }
    public double getSurcharge() { return type.getSurcharge(); }
    public List<String> getAmenities() { return amenities; }
    public String getAssignedPassengerName() { return assignedPassengerName; }

    @Override
    public String toString() {
        return String.format("Seat %s [%s | %s] - Surcharge: ₹%.2f | Amenities: %s",
                seatNumber, category, type.getDescription(), type.getSurcharge(), String.join(", ", amenities));
    }
}