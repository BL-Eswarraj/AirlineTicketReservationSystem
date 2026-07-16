package service.optimization;

import domain.optimization.OptimizedFlight;
import domain.optimization.RoutePath;
import domain.optimization.SearchQueryKey;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Facade combining graph indexing and LRU caching for sub-millisecond searches
public class OptimizedFlightSearchFacade {
    private final RouteIndexingService indexer = new RouteIndexingService();
    private final FlightCacheEngine cacheEngine = new FlightCacheEngine(10, 5); // 10 items max, 5 sec TTL for demo

    public void initializeSystemInventory(List<OptimizedFlight> flights) {
        System.out.println("\n--- INITIALIZING SEARCH ENGINE INVENTORY ---");
        indexer.buildIndex(flights);
        cacheEngine.invalidateAll(); // Clear old cache when inventory changes
    }

    public List<RoutePath> executeSearch(String source, String destination, LocalDate date, int maxStops) {
        SearchQueryKey queryKey = new SearchQueryKey(source, destination, date, maxStops);
        System.out.println("\n>>> EXECUTING SEARCH: " + queryKey);

        long startTime = System.nanoTime();

        // 1. Try Cache First (O(1) lookup)
        Optional<List<RoutePath>> cachedResult = cacheEngine.get(queryKey);
        if (cachedResult.isPresent()) {
            long durationMicro = (System.nanoTime() - startTime) / 1000;
            System.out.printf("⚡ [SEARCH COMPLETED] Returned %d route(s) in %d µs (CACHE HIT)%n",
                    cachedResult.get().size(), durationMicro);
            return cachedResult.get();
        }

        // 2. Fallback to Graph Index Traversal if Cache Miss
        List<RoutePath> computedRoutes = indexer.findOptimizedRoutes(source, destination, date, maxStops);

        // 3. Store result in Cache for subsequent queries
        cacheEngine.put(queryKey, computedRoutes);

        long durationMicro = (System.nanoTime() - startTime) / 1000;
        System.out.printf("🔍 [SEARCH COMPLETED] Computed %d route(s) in %d µs (INDEX TRAVERSAL)%n",
                computedRoutes.size(), durationMicro);

        return computedRoutes;
    }

    public void printSystemMetrics() {
        cacheEngine.printPerformanceMetrics();
    }
}