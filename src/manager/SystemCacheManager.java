package manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// Enum Singleton: Best defense against serialization and reflection attacks
public enum SystemCacheManager {
    INSTANCE;

    // ConcurrentHashMap ensures thread-safe read/write operations across concurrent search requests
    private final Map<String, Object> globalMemoryCache = new ConcurrentHashMap<>();
    private long cacheHits = 0;
    private long cacheMisses = 0;

    SystemCacheManager() {
        System.out.println("[CACHE MANAGER INIT] Initializing High-Speed Concurrent In-Memory Cache...");
    }

    public synchronized void put(String cacheKey, Object data) {
        globalMemoryCache.put(cacheKey.toUpperCase(), data);
        System.out.printf("[CACHE PUT] Cached data under key [%s]. Total cached objects: %d%n", cacheKey.toUpperCase(), globalMemoryCache.size());
    }

    public synchronized Object get(String cacheKey) {
        Object data = globalMemoryCache.get(cacheKey.toUpperCase());
        if (data != null) {
            cacheHits++;
            System.out.printf("[CACHE HIT] Successfully retrieved data for key [%s]. (Total Hits: %d)%n", cacheKey.toUpperCase(), cacheHits);
        } else {
            cacheMisses++;
            System.out.printf("[CACHE MISS] No data found for key [%s]. (Total Misses: %d)%n", cacheKey.toUpperCase(), cacheMisses);
        }
        return data;
    }

    public synchronized void invalidate(String cacheKey) {
        if (globalMemoryCache.remove(cacheKey.toUpperCase()) != null) {
            System.out.println("[CACHE EVICT] Removed cached key: " + cacheKey.toUpperCase());
        }
    }

    public synchronized void clearAllCache() {
        globalMemoryCache.clear();
        System.out.println("[CACHE FLUSH] Global memory cache completely cleared.");
    }

    public void printCacheStatistics() {
        long totalRequests = cacheHits + cacheMisses;
        double hitRatio = (totalRequests == 0) ? 0.0 : ((double) cacheHits / totalRequests) * 100.0;

        System.out.println("\n==================================================");
        System.out.println("          GLOBAL CACHE PERFORMANCE STATS          ");
        System.out.println("==================================================");
        System.out.printf(" Active Cache Entries : %d%n", globalMemoryCache.size());
        System.out.printf(" Total Cache Hits     : %d%n", cacheHits);
        System.out.printf(" Total Cache Misses   : %d%n", cacheMisses);
        System.out.printf(" Cache Hit Efficiency : %.2f%%%n", hitRatio);
        System.out.println("==================================================\n");
    }
}