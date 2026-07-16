package domain.airport;

import java.util.ArrayList;
import java.util.List;

public class Terminal {
    private final String terminalId; // e.g., "T1", "T2", "International Terminal"
    private final String terminalName;
    private final boolean isInternational;
    private final boolean isDomestic;
    private final int totalGates;
    private final List<String> gateNumbers;
    private final List<FacilityType> availableFacilities;

    public Terminal(String terminalId, String terminalName, boolean isDomestic, boolean isInternational, int totalGates) {
        this.terminalId = terminalId;
        this.terminalName = terminalName;
        this.isDomestic = isDomestic;
        this.isInternational = isInternational;
        this.totalGates = totalGates;
        this.gateNumbers = new ArrayList<>();
        this.availableFacilities = new ArrayList<>();

        // Auto-generate gate identifiers
        for (int i = 1; i <= totalGates; i++) {
            gateNumbers.add(terminalId + "-G" + i);
        }
    }

    public void addFacility(FacilityType facility) {
        if (!availableFacilities.contains(facility)) {
            availableFacilities.add(facility);
        }
    }

    public String getTerminalTypeString() {
        if (isDomestic && isInternational) return "Integrated (Domestic & International)";
        if (isInternational) return "International Only";
        return "Domestic Only";
    }

    // Getters
    public String getTerminalId() { return terminalId; }
    public String getTerminalName() { return terminalName; }
    public boolean isInternational() { return isInternational; }
    public boolean isDomestic() { return isDomestic; }
    public int getTotalGates() { return totalGates; }
    public List<String> getGateNumbers() { return gateNumbers; }
    public List<FacilityType> getAvailableFacilities() { return availableFacilities; }

    @Override
    public String toString() {
        return String.format("[%s] %s | Type: %s | Gates (%d): %s to %s | Facilities: %d available",
                terminalId, terminalName, getTerminalTypeString(), totalGates,
                gateNumbers.isEmpty() ? "N/A" : gateNumbers.get(0),
                gateNumbers.isEmpty() ? "N/A" : gateNumbers.get(gateNumbers.size() - 1),
                availableFacilities.size());
    }
}