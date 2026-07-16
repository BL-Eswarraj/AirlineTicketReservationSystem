package domain.flightmanagement;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class ManagedFlight {
    private final String flightNumber;
    private final String airlineName;
    private String aircraftType;
    private final String sourceAirport;
    private final String destinationAirport;
    private LocalDateTime scheduledDepartureTime;
    private LocalDateTime scheduledArrivalTime;
    private ScheduleType scheduleType;
    private FlightOperationalStatus status;
    private String seatLayoutString; // e.g., "ABC-DEF"

    private final Map<String, ClassConfiguration> classConfigs;
    private final List<String> amenities;
    private String baggagePolicyInfo;
    private String cancellationPolicyInfo;
    private double seasonalMultiplier;

    public ManagedFlight(String flightNumber, String airlineName, String aircraftType,
                         String sourceAirport, String destinationAirport,
                         LocalDateTime scheduledDepartureTime, LocalDateTime scheduledArrivalTime,
                         ScheduleType scheduleType, String seatLayoutString) {
        this.flightNumber = flightNumber;
        this.airlineName = airlineName;
        this.aircraftType = aircraftType;
        this.sourceAirport = sourceAirport;
        this.destinationAirport = destinationAirport;
        this.scheduledDepartureTime = scheduledDepartureTime;
        this.scheduledArrivalTime = scheduledArrivalTime;
        this.scheduleType = scheduleType;
        this.seatLayoutString = seatLayoutString;
        this.status = FlightOperationalStatus.SCHEDULED;
        this.classConfigs = new LinkedHashMap<>();
        this.amenities = new ArrayList<>();
        this.seasonalMultiplier = 1.0; // Default: normal pricing
        this.baggagePolicyInfo = "Standard: 15kg check-in, 7kg cabin";
        this.cancellationPolicyInfo = "Standard airline tiered cancellation policy";
    }

    public void addClassConfiguration(ClassConfiguration config) {
        classConfigs.put(config.getTravelClass().toUpperCase(), config);
    }

    public ClassConfiguration getClassConfig(String travelClass) {
        return classConfigs.get(travelClass.toUpperCase());
    }

    public int getTotalFleetCapacity() {
        return classConfigs.values().stream().mapToInt(ClassConfiguration::getTotalCapacity).sum();
    }

    public int getTotalBookedSeats() {
        return classConfigs.values().stream().mapToInt(ClassConfiguration::getBookedSeats).sum();
    }

    public double getOverallOccupancyRate() {
        int totalCap = getTotalFleetCapacity();
        if (totalCap == 0) return 0.0;
        return ((double) getTotalBookedSeats() / totalCap) * 100.0;
    }

    public double calculateProjectedRevenue() {
        return classConfigs.values().stream()
                .mapToDouble(c -> c.getBookedSeats() * c.getCurrentDynamicFare())
                .sum();
    }

    // Timings and Delay Management
    public void updateSchedule(LocalDateTime newDeparture, LocalDateTime newArrival) {
        this.scheduledDepartureTime = newDeparture;
        this.scheduledArrivalTime = newArrival;
    }

    public void applyDelay(Duration delayDuration, String reason) {
        this.scheduledDepartureTime = this.scheduledDepartureTime.plus(delayDuration);
        this.scheduledArrivalTime = this.scheduledArrivalTime.plus(delayDuration);
        this.status = FlightOperationalStatus.DELAYED;
        System.out.printf("[DELAY ALERT] %s (%s -> %s) delayed by %d minutes. Reason: %s. New Departure: %s%n",
                flightNumber, sourceAirport, destinationAirport, delayDuration.toMinutes(), reason, scheduledDepartureTime.toLocalTime());
    }

    // Getters and Setters
    public String getFlightNumber() {
        return flightNumber;
    }

    public String getAirlineName() {
        return airlineName;
    }

    public String getAircraftType() {
        return aircraftType;
    }

    public void setAircraftType(String aircraftType) {
        this.aircraftType = aircraftType;
    }

    public String getSourceAirport() {
        return sourceAirport;
    }

    public String getDestinationAirport() {
        return destinationAirport;
    }

    public LocalDateTime getScheduledDepartureTime() {
        return scheduledDepartureTime;
    }

    public LocalDateTime getScheduledArrivalTime() {
        return scheduledArrivalTime;
    }

    public ScheduleType getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(ScheduleType scheduleType) {
        this.scheduleType = scheduleType;
    }

    public FlightOperationalStatus getStatus() {
        return status;
    }

    public void setStatus(FlightOperationalStatus status) {
        this.status = status;
    }

    public String getSeatLayoutString() {
        return seatLayoutString;
    }

    public void setSeatLayoutString(String layout) {
        this.seatLayoutString = layout;
    }

    public Collection<ClassConfiguration> getAllClassConfigs() {
        return classConfigs.values();
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public void addAmenity(String amenity) {
        this.amenities.add(amenity);
    }

    public String getBaggagePolicyInfo() {
        return baggagePolicyInfo;
    }

    public void setBaggagePolicyInfo(String baggagePolicyInfo) {
        this.baggagePolicyInfo = baggagePolicyInfo;
    }

    public String getCancellationPolicyInfo() {
        return cancellationPolicyInfo;
    }

    public void setCancellationPolicyInfo(String cancellationPolicyInfo) {
        this.cancellationPolicyInfo = cancellationPolicyInfo;
    }

    public double getSeasonalMultiplier() {
        return seasonalMultiplier;
    }

    public void setSeasonalMultiplier(double seasonalMultiplier) {
        this.seasonalMultiplier = seasonalMultiplier;
    }
}