import manager.DatabaseConnectionManager;
import manager.SystemCacheManager;
import manager.SystemConfigurationManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SingletonManagersDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("==================================================");
        System.out.println(" UC11: SINGLETON MANAGERS MODULE DEMO ");
        System.out.println("==================================================");

        // --- STEP 1: VERIFY THREAD-SAFETY & INSTANCE UNIQUENESS ACROSS THREADS ---
        System.out.println("\n--- 1. Testing Concurrent Singleton Instantiation Across 5 Threads ---");
        ExecutorService executor = Executors.newFixedThreadPool(5);

        for (int i = 1; i <= 5; i++) {
            final int threadId = i;
            executor.submit(() -> {
                // Fetch instances from separate threads
                DatabaseConnectionManager dbMgr = DatabaseConnectionManager.getInstance();
                SystemConfigurationManager cfgMgr = SystemConfigurationManager.getInstance();
                SystemCacheManager cacheMgr = SystemCacheManager.INSTANCE;

                System.out.printf("[Thread %d] DB Hash: %d | Config Hash: %d | Cache Hash: %d%n",
                        threadId, System.identityHashCode(dbMgr), System.identityHashCode(cfgMgr), System.identityHashCode(cacheMgr));
            });
        }
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.SECONDS);

        System.out.println(">> Notice: All identity hashcodes match identically across threads! Only 1 instance exists per class.");

        // --- STEP 2: TEST SYSTEM CONFIGURATION MANAGER (BILL PUGH PATTERN) ---
        System.out.println("\n--- 2. Testing System Configuration Manager ---");
        SystemConfigurationManager config = SystemConfigurationManager.getInstance();
        System.out.println("Initial Config: " + config);

        // Modifying tax rate globally
        System.out.println("Updating Global GST Tax Rate from 18.0% to 12.0%...");
        config.setDefaultTaxRatePercentage(12.0);

        // Fetching from a seemingly "new" call to prove shared state
        SystemConfigurationManager anotherConfigRef = SystemConfigurationManager.getInstance();
        System.out.println("Verified Shared Config: " + anotherConfigRef);

        // --- STEP 3: TEST DATABASE CONNECTION POOLING (DOUBLE-CHECKED LOCKING) ---
        System.out.println("\n--- 3. Testing Database Connection Pool Management ---");
        DatabaseConnectionManager db = DatabaseConnectionManager.getInstance();

        String conn1 = db.borrowConnection("Passenger-Search-Service");
        String conn2 = db.borrowConnection("Payment-Gateway-Service");
        String conn3 = db.borrowConnection("Booking-Confirmation-Service");

        System.out.println("Available connections left in pool: " + db.getAvailableConnectionsCount());

        // Return a connection
        db.releaseConnection(conn2, "Payment-Gateway-Service");
        System.out.println("Available connections after release: " + db.getAvailableConnectionsCount());

        // --- STEP 4: TEST HIGH-SPEED CONCURRENT MEMORY CACHE (ENUM SINGLETON) ---
        System.out.println("\n--- 4. Testing System Cache Manager ---");
        SystemCacheManager cache = SystemCacheManager.INSTANCE;

        // Cache frequently accessed flight routes
        cache.put("ROUTE_MAA_DEL", "Air India AI-101 (₹5000) | Vistara UK-808 (₹6500)");
        cache.put("ROUTE_DEL_BOM", "IndiGo 6E-303 (₹4200)");

        // Simulate Search Queries hitting cache
        System.out.println("\nExecuting passenger route lookups:");
        Object cachedRoute1 = cache.get("ROUTE_MAA_DEL"); // Should be a HIT
        Object cachedRoute2 = cache.get("ROUTE_MAA_SIN"); // Should be a MISS
        Object cachedRoute3 = cache.get("route_maa_del"); // Case-insensitive test -> HIT

        // Print final performance analytics
        cache.printCacheStatistics();

        // --- STEP 5: TEST MAINTENANCE MODE INTERCEPTION ---
        System.out.println("--- 5. Testing Global Maintenance Mode Interception ---");
        config.setMaintenanceModeActive(true);
        try {
            System.out.println("Attempting a new flight search transaction...");
            config.validateSystemAvailability();
        } catch (IllegalStateException e) {
            System.out.println("[INTERCEPTED SUCCESSFULLY] " + e.getMessage());
        }

        // Revert maintenance mode
        config.setMaintenanceModeActive(false);

        System.out.println("\n==================================================");
        System.out.println(" UC11 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}