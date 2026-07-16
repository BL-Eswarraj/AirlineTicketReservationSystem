package domain.user;

import java.time.LocalDate;

public class AirlineStaff extends User {
    private String assignedAirlineCode; // e.g., "AI" for Air India, "UK" for Vistara
    private String staffBadgeNumber;

    public AirlineStaff(String userId, String name, String email, String phone, String passwordHash,
                        String assignedAirlineCode, String staffBadgeNumber) {
        super(userId, name, email, phone, passwordHash, LocalDate.of(1995, 5, 15), UserRole.AIRLINE_STAFF);
        this.assignedAirlineCode = assignedAirlineCode;
        this.staffBadgeNumber = staffBadgeNumber;
    }

    @Override
    public boolean hasPermission(String feature) {
        // Staff can manage flights, view bookings, and handle check-ins
        return switch (feature) {
            case "MANAGE_FLIGHTS", "VIEW_ALL_BOOKINGS", "UPDATE_FLIGHT_STATUS",
                 "PROCESS_CHECK_IN", "SEARCH_FLIGHTS", "VIEW_PASSENGER_LIST" -> true;
            default -> false;
        };
    }

    public String getAssignedAirlineCode() { return assignedAirlineCode; }
    public String getStaffBadgeNumber() { return staffBadgeNumber; }
}