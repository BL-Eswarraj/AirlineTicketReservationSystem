package service.queue;

import domain.queue.*;
import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

public class ExpressQueueManagerService {
    // Thread-safe PriorityQueue to handle concurrent booking requests efficiently (Section 10.1)
    private final PriorityBlockingQueue<PriorityBookingRequest> bookingQueue;
    private final List<PriorityBookingRequest> completedRequestsHistory;

    // Starvation threshold: If waiting more than 1500 milliseconds in demo, elevate priority
    private static final long STARVATION_THRESHOLD_MS = 1500;

    public ExpressQueueManagerService() {
        this.bookingQueue = new PriorityBlockingQueue<>();
        this.completedRequestsHistory = Collections.synchronizedList(new ArrayList<>());
    }

    // 10.1 & 10.2 Submit booking request into PriorityQueue
    public void submitBookingRequest(String pnr, String passengerName, String flightNumber,
                                     double baseFare, boolean optForExpress) {

        BookingPriorityLevel level = optForExpress ? BookingPriorityLevel.EXPRESS : BookingPriorityLevel.REGULAR;
        double totalPayable = baseFare + level.getExpressFee();

        PriorityBookingRequest request = new PriorityBookingRequest(pnr, passengerName, flightNumber, totalPayable, level);
        bookingQueue.offer(request);

        System.out.printf("[QUEUE ENQUEUE] Added %s for %s. Priority: %s | Total: ₹%.2f | Current Queue Size: %d%n",
                request.getRequestId(), passengerName, level, totalPayable, bookingQueue.size());
    }

    // 10.2 Extract and process highest priority booking
    public synchronized Optional<PriorityBookingRequest> processNextBooking() {
        if (bookingQueue.isEmpty()) {
            System.out.println("[QUEUE PROCESSOR] Queue is currently empty. No pending bookings.");
            return Optional.empty();
        }

        // Apply Starvation Protection Check before popping
        applyStarvationProtection();

        // Pop highest priority element (O(log n) time complexity)
        PriorityBookingRequest nextRequest = bookingQueue.poll();
        if (nextRequest == null) return Optional.empty();

        nextRequest.markProcessingStarted();
        System.out.println("\n--- EXTRACTED HIGHEST PRIORITY BOOKING FOR PROCESSING ---");
        System.out.println("Processing: " + nextRequest);

        // Simulate resource allocation and transaction latency
        try {
            long processingTime = (nextRequest.getPriorityLevel() == BookingPriorityLevel.EXPRESS) ? 150 : 350;
            Thread.sleep(processingTime); // Dedicated faster routing for express
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        nextRequest.markCompleted();
        completedRequestsHistory.add(nextRequest);

        System.out.printf("[PROCESSING SUCCESS] Booking %s confirmed! E-Ticket dispatched to %s. Processing duration: %d ms.%n",
                nextRequest.getPnr(), nextRequest.getPassengerName(), nextRequest.getProcessingDurationMillis());

        return Optional.of(nextRequest);
    }

    // Process all pending items in queue
    public void processAllPendingBookings() {
        System.out.println("\n>>> INITIATING BATCH PROCESSING OF ALL QUEUED BOOKINGS >>>");
        while (!bookingQueue.isEmpty()) {
            processNextBooking();
        }
        System.out.println(">>> BATCH PROCESSING COMPLETED. QUEUE IS CLEARED <<<\n");
    }

    // 10.2 Ensure Regular bookings are not starved
    private void applyStarvationProtection() {
        long nowMillis = System.currentTimeMillis();
        boolean elevationOccurred = false;

        // Iterate through queue snapshot to check for aging requests
        for (PriorityBookingRequest req : bookingQueue) {
            if (req.getPriorityLevel() == BookingPriorityLevel.REGULAR && req.getWaitTimeMillis() > STARVATION_THRESHOLD_MS) {
                req.elevatePriorityDueToStarvation();
                elevationOccurred = true;
            }
        }

        // If any priority changed, we must re-heapify the PriorityQueue by rebuilding it
        if (elevationOccurred) {
            List<PriorityBookingRequest> tempList = new ArrayList<>(bookingQueue);
            bookingQueue.clear();
            bookingQueue.addAll(tempList);
            System.out.println("[QUEUE RE-HEAPIFIED] Priority tree restructured after starvation elevation.");
        }
    }

    // 10.2 Generate priority processing reports
    public QueueProcessingReport generateOperationalReport() {
        if (completedRequestsHistory.isEmpty()) {
            return new QueueProcessingReport(0, 0, 0, 0, 0.0, 0.0, 0.0);
        }

        int total = completedRequestsHistory.size();
        int expressCount = 0;
        int regularCount = 0;
        int elevatedCount = 0;
        double totalRevenue = 0.0;
        long totalWait = 0;
        long totalProc = 0;

        for (PriorityBookingRequest req : completedRequestsHistory) {
            if (req.getPriorityLevel() == BookingPriorityLevel.EXPRESS) {
                expressCount++;
                totalRevenue += req.getExpressFeePaid();
            } else {
                regularCount++;
            }

            if (req.isStarvationElevated()) {
                elevatedCount++;
            }

            totalWait += req.getWaitTimeMillis();
            totalProc += req.getProcessingDurationMillis();
        }

        return new QueueProcessingReport(
                total, expressCount, regularCount, elevatedCount, totalRevenue,
                (double) totalWait / total, (double) totalProc / total
        );
    }

    public int getCurrentQueueSize() { return bookingQueue.size(); }
    public void peekNextInLine() {
        PriorityBookingRequest next = bookingQueue.peek();
        if (next != null) {
            System.out.println("[QUEUE PEEK] Next in line -> " + next);
        } else {
            System.out.println("[QUEUE PEEK] Queue is empty.");
        }
    }
}