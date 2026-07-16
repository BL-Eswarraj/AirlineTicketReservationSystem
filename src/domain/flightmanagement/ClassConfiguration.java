package domain.flightmanagement;

public class ClassConfiguration {
    private final String travelClass; // e.g., "ECONOMY", "BUSINESS", "FIRST"
    private int totalCapacity;
    private int bookedSeats;
    private double baseFare;
    private double currentDynamicFare;

    public ClassConfiguration(String travelClass, int totalCapacity, double baseFare) {
        this.travelClass = travelClass;
        this.totalCapacity = totalCapacity;
        this.bookedSeats = 0;
        this.baseFare = baseFare;
        this.currentDynamicFare = baseFare;
    }

    public int getAvailableSeats() {
        return totalCapacity - bookedSeats;
    }

    public double getOccupancyRate() {
        if (totalCapacity == 0) return 0.0;
        return ((double) bookedSeats / totalCapacity) * 100.0;
    }

    public void bookSeats(int count) {
        if (bookedSeats + count > totalCapacity) {
            throw new IllegalStateException("Cannot book more seats than available capacity in " + travelClass);
        }
        this.bookedSeats += count;
    }

    public void releaseSeats(int count) {
        this.bookedSeats = Math.max(0, this.bookedSeats - count);
    }

    // Getters and Setters
    public String getTravelClass() { return travelClass; }
    public int getTotalCapacity() { return totalCapacity; }
    public void setTotalCapacity(int totalCapacity) { this.totalCapacity = totalCapacity; }
    public int getBookedSeats() { return bookedSeats; }
    public double getBaseFare() { return baseFare; }
    public void setBaseFare(double baseFare) {
        this.baseFare = baseFare;
        this.currentDynamicFare = baseFare; // Reset dynamic fare when base changes
    }
    public double getCurrentDynamicFare() { return currentDynamicFare; }
    public void setCurrentDynamicFare(double currentDynamicFare) { this.currentDynamicFare = currentDynamicFare; }

    @Override
    public String toString() {
        return String.format("%-10s | Capacity: %3d | Booked: %3d | Occupancy: %5.1f%% | Base: ₹%-7.2f | Live Fare: ₹%-7.2f",
                travelClass, totalCapacity, bookedSeats, getOccupancyRate(), baseFare, currentDynamicFare);
    }
}