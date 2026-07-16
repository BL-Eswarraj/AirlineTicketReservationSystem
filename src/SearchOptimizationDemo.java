import domain.optimization.OptimizedFlight;
import domain.optimization.RoutePath;
import service.optimization.OptimizedFlightSearchFacade;

import java.time.LocalDate;
import java.util.List;

public class SearchOptimizationDemo {

    public static void main(String[] args) throws InterruptedException {
        System.out.println("==================================================");
        System.out.println(" UC15: SEARCH OPTIMIZATION MODULE DEMO ");
        System.out.println("==================================================");

        OptimizedFlightSearchFacade searchEngine = new OptimizedFlightSearchFacade();

        // --- STEP 1: SEED MOCK NETWORK INVENTORY (INCLUDING CONNECTING HUBS) ---
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<OptimizedFlight> mockInventory = List.of(
                // Direct Chennai (MAA) -> Delhi (DEL)
                new OptimizedFlight("AI-101", "Air India", "MAA", "DEL", tomorrow.atTime(8, 0), tomorrow.atTime(10, 45), 5500.0),
                new OptimizedFlight("UK-808", "Vistara", "MAA", "DEL", tomorrow.atTime(18, 0), tomorrow.atTime(20, 50), 6800.0),

                // Connecting Route MAA -> HYD -> DEL (Via Hyderabad transfer hub)
                new OptimizedFlight("6E-101", "IndiGo", "MAA", "HYD", tomorrow.atTime(9, 0), tomorrow.atTime(10, 15), 2500.0),
                new OptimizedFlight("6E-202", "IndiGo", "HYD", "DEL", tomorrow.atTime(11, 30), tomorrow.atTime(13, 45), 2800.0),

                // Connecting Route MAA -> BOM -> DEL (Via Mumbai transfer hub)
                new OptimizedFlight("AI-303", "Air India", "MAA", "BOM", tomorrow.atTime(7, 0), tomorrow.atTime(9, 0), 3200.0),
                new OptimizedFlight("AI-404", "Air India", "BOM", "DEL", tomorrow.atTime(10, 30), tomorrow.atTime(12, 30), 3500.0),

                // Unrelated direct flight
                new OptimizedFlight("SG-555", "SpiceJet", "BLR", "DEL", tomorrow.atTime(14, 0), tomorrow.atTime(16, 30), 4900.0)
        );

        searchEngine.initializeSystemInventory(mockInventory);

        // --- STEP 2: TEST GRAPH TRAVERSAL vs. CACHE HITS ---
        System.out.println("\n--- 1. First Query: MAA -> DEL (Max Stops: 1) [Expect CACHE MISS & Graph Computation] ---");
        List<RoutePath> results1 = searchEngine.executeSearch("MAA", "DEL", tomorrow, 1);
        results1.forEach(r -> System.out.println("   -> " + r));

        System.out.println("\n--- 2. Repeat Query: MAA -> DEL (Max Stops: 1) [Expect Instant CACHE HIT] ---");
        List<RoutePath> results2 = searchEngine.executeSearch("MAA", "DEL", tomorrow, 1);
        results2.forEach(r -> System.out.println("   -> " + r));

        // --- STEP 3: TEST DIRECT ONLY FILTER (Max Stops: 0) ---
        System.out.println("\n--- 3. Direct Only Query: MAA -> DEL (Max Stops: 0) [Expect New Cache Key] ---");
        List<RoutePath> directOnly = searchEngine.executeSearch("MAA", "DEL", tomorrow, 0);
        directOnly.forEach(r -> System.out.println("   -> " + r));

        // --- STEP 4: SIMULATE TIME-TO-LIVE (TTL) CACHE EXPIRATION ---
        System.out.println("\n--- 4. Testing Time-To-Live (TTL) Expiration ---");
        System.out.println("Sleeping for 6 seconds to allow cache entries (5s TTL) to expire...");
        Thread.sleep(6000);

        System.out.println("Re-querying MAA -> DEL after TTL expiration:");
        searchEngine.executeSearch("MAA", "DEL", tomorrow, 1); // Should trigger TTL expiration eviction and miss!

        // --- STEP 5: DISPLAY PERFORMANCE METRICS ---
        searchEngine.printSystemMetrics();

        System.out.println("==================================================");
        System.out.println(" UC15 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}