package domain.queue;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.UUID;

public class PriorityBookingRequest implements Comparable<PriorityBookingRequest> {
    private final String requestId;
    private final String pnr;
    private final String passengerName;
    private final String flightNumber;
    private final double totalFare;

    private BookingPriorityLevel priorityLevel;
    private final double expressFeePaid;
    private final LocalDateTime submissionTimestamp;
    private LocalDateTime processingStartTime;
    private LocalDateTime completionTimestamp;
    private boolean isStarvationElevated;

    public PriorityBookingRequest(String pnr, String passengerName, String flightNumber,
                                  double totalFare, BookingPriorityLevel priorityLevel) {
        this.requestId = "REQ-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.pnr = pnr;
        this.passengerName = passengerName;
        this.flightNumber = flightNumber;
        this.totalFare = totalFare;
        this.priorityLevel = priorityLevel;
        this.expressFeePaid = priorityLevel.getExpressFee();
        this.submissionTimestamp = LocalDateTime.now();
        this.isStarvationElevated = false;
    }

    // 10.1 Core DSA Sorting Logic: Priority Rank first, then FIFO Timestamp
    @Override
    public int compareTo(PriorityBookingRequest other) {
        // 1. Compare Priority Level Ranks (EXPRESS rank 1 vs REGULAR rank 2)
        int rankComparison = Integer.compare(this.priorityLevel.getRank(), other.priorityLevel.getRank());
        if (rankComparison != 0) {
            return rankComparison;
        }
        // 2. Tie-breaker: FIFO using submission timestamps
        return this.submissionTimestamp.compareTo(other.submissionTimestamp);
    }

    // 10.2 Starvation Protection Elevation
    public void elevatePriorityDueToStarvation() {
        if (this.priorityLevel == BookingPriorityLevel.REGULAR) {
            this.priorityLevel = BookingPriorityLevel.EXPRESS;
            this.isStarvationElevated = true;
            System.out.printf("[STARVATION PROTECTION] Request %s (%s) elevated to EXPRESS priority due to excessive queue waiting time!%n",
                    requestId, passengerName);
        }
    }

    public void markProcessingStarted() {
        this.processingStartTime = LocalDateTime.now();
    }

    public void markCompleted() {
        this.completionTimestamp = LocalDateTime.now();
    }

    public long getWaitTimeMillis() {
        LocalDateTime end = (processingStartTime != null) ? processingStartTime : LocalDateTime.now();
        return Duration.between(submissionTimestamp, end).toMillis();
    }

    public long getProcessingDurationMillis() {
        if (processingStartTime == null || completionTimestamp == null) return 0;
        return Duration.between(processingStartTime, completionTimestamp).toMillis();
    }

    // Getters
    public String getRequestId() { return requestId; }
    public String getPnr() { return pnr; }
    public String getPassengerName() { return passengerName; }
    public String getFlightNumber() { return flightNumber; }
    public double getTotalFare() { return totalFare; }
    public BookingPriorityLevel getPriorityLevel() { return priorityLevel; }
    public double getExpressFeePaid() { return expressFeePaid; }
    public LocalDateTime getSubmissionTimestamp() { return submissionTimestamp; }
    public boolean isStarvationElevated() { return isStarvationElevated; }

    @Override
    public String toString() {
        return String.format("[%s] ReqID: %s | PNR: %s | Passenger: %-18s | Fee Paid: ₹%-5.2f | Wait: %d ms %s",
                priorityLevel, requestId, pnr, passengerName, expressFeePaid, getWaitTimeMillis(),
                isStarvationElevated ? "(ELEVATED)" : "");
    }
}