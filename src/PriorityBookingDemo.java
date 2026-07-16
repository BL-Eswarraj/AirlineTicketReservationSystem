import domain.queue.QueueProcessingReport;
import service.queue.ExpressQueueManagerService;

public class PriorityBookingDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("==================================================");
        System.out.println(" UC10: PRIORITY BOOKING QUEUE (DSA MODULE) DEMO ");
        System.out.println("==================================================");

        ExpressQueueManagerService queueService = new ExpressQueueManagerService();

        // --- STEP 1: SUBMIT MIXED BOOKING REQUESTS (10.1) ---
        System.out.println("\n--- 1. Submitting Booking Requests into PriorityQueue ---");

        // 1. Regular booking arrives first
        queueService.submitBookingRequest("PNR-REG-001", "John Regular", "AI-101", 5000.0, false);
        Thread.sleep(50); // Small delay to separate timestamps

        // 2. Another Regular booking arrives second
        queueService.submitBookingRequest("PNR-REG-002", "Alice Standard", "AI-101", 5000.0, false);
        Thread.sleep(50);

        // 3. Express booking arrives third (Should jump ahead of both regular bookings!)
        queueService.submitBookingRequest("PNR-EXP-888", "Annadurai Anbarasu", "AI-101", 5000.0, true);
        Thread.sleep(50);

        // 4. Second Express booking arrives fourth (Should be second in line behind Annadurai)
        queueService.submitBookingRequest("PNR-EXP-999", "VIP Ramesh", "AI-101", 8000.0, true);

        // --- STEP 2: PEEK NEXT ITEM IN QUEUE ---
        System.out.println("\n--- 2. Inspecting Head of Queue (peek) ---");
        queueService.peekNextInLine(); // Expecting PNR-EXP-888 (Annadurai)

        // --- STEP 3: PROCESS HIGHEST PRIORITY ITEMS ---
        System.out.println("\n--- 3. Processing First Two Requests in Queue ---");
        queueService.processNextBooking(); // Processes PNR-EXP-888 (Express 1)
        queueService.processNextBooking(); // Processes PNR-EXP-999 (Express 2)

        // --- STEP 4: SIMULATE STARVATION PROTECTION AGING (10.2) ---
        System.out.println("\n--- 4. Simulating Starvation Protection Aging ---");
        System.out.println("Simulating heavy server load... Waiting 1.6 seconds to let remaining regular requests age...");
        Thread.sleep(1600); // Exceeds STARVATION_THRESHOLD_MS (1500 ms)

        // Add a brand new Express request
        System.out.println("A new Express request suddenly arrives:");
        queueService.submitBookingRequest("PNR-EXP-777", "Late VIP Traveler", "AI-101", 6000.0, true);

        // --- STEP 5: PROCESS REMAINING QUEUE (OBSERVE STARVATION ELEVATION) ---
        System.out.println("\n--- 5. Processing Remaining Queue (Watch Starvation Elevation!) ---");
        // Because John Regular and Alice Standard waited > 1500ms, they should be elevated to EXPRESS
        // and processed before or alongside the newly arrived Express request!
        queueService.processAllPendingBookings();

        // --- STEP 6: GENERATE PRIORITY ANALYTICS REPORT (10.2) ---
        QueueProcessingReport report = queueService.generateOperationalReport();
        System.out.println(report);

        System.out.println("==================================================");
        System.out.println(" UC10 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}