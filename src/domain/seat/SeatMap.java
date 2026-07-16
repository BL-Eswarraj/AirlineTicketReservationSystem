package domain.seat;

import java.util.*;

public class SeatMap {
    private final String aircraftType;
    private final int totalRows;
    private final String layoutString; // e.g., "ABC-DEF" for 3-3 layout
    private final Map<String, Seat> seatGrid;

    public SeatMap(String aircraftType, int totalRows, String layoutString) {
        this.aircraftType = aircraftType;
        this.totalRows = totalRows;
        this.layoutString = layoutString;
        this.seatGrid = new LinkedHashMap<>();
        initializeSeatGrid();
    }

    private void initializeSeatGrid() {
        String columns = layoutString.replace("-", "");
        for (int row = 1; row <= totalRows; row++) {
            for (int colIdx = 0; colIdx < columns.length(); colIdx++) {
                char col = columns.charAt(colIdx);

                // Determine Category
                SeatCategory category;
                if (colIdx == 0 || colIdx == columns.length() - 1) {
                    category = SeatCategory.WINDOW;
                } else if (colIdx == 2 || colIdx == 3) {
                    category = SeatCategory.AISLE; // Assuming 3-3 layout (C and D are aisle)
                } else {
                    category = SeatCategory.MIDDLE;
                }

                // Determine Seat Type
                SeatType type = SeatType.STANDARD;
                if (row <= 2) {
                    type = SeatType.PREMIUM;
                } else if (row == 12 || row == 13) {
                    type = SeatType.EMERGENCY_EXIT; // Standard over-wing exit rows
                } else if (row <= 5) {
                    type = SeatType.EXTRA_LEGROOM;
                }

                Seat seat = new Seat(row, col, category, type);
                seatGrid.put(seat.getSeatNumber(), seat);
            }
        }
    }

    public Seat getSeat(String seatNumber) {
        return seatGrid.get(seatNumber.toUpperCase());
    }

    public Collection<Seat> getAllSeats() {
        return seatGrid.values();
    }

    public String getAircraftType() { return aircraftType; }
    public int getTotalRows() { return totalRows; }
    public String getLayoutString() { return layoutString; }
}