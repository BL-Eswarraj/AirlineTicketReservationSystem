package service.optimization;

import domain.optimization.CachedSearchResult;
import domain.optimization.RoutePath;
import domain.optimization.SearchQueryKey;

import java.util.*;

public class FlightCacheEngine {
    private final int maxCapacity;
    private final long defaultTtlSeconds;

    // LRU Cache implementation using LinkedHashMap with access-order set to true
    private final Map<SearchQueryKey, CachedSearchResult> lruCache;

    private long cacheHits = 0;
    private long cacheMisses = 0;
    private long evictions = 0;

    public FlightCacheEngine(int maxCapacity, long defaultTtlSeconds) {
        this.maxCapacity = maxCapacity;
        this.defaultTtlSeconds = defaultTtlSeconds;

        // When size exceeds maxCapacity, the eldest entry (least recently accessed) is automatically evicted
        this.lruCache = Collections.synchronizedMap(new LinkedHashMap<>(maxCapacity, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<SearchQueryKey, CachedSearchResult> eldest) {
                boolean shouldEvict = size() > FlightCacheEngine.this.maxCapacity;
                if (shouldEvict) {
                    evictions++;
                    System.out.println("[CACHE LRU EVICTION] Cache full! Evicted least recently used key: " + eldest.getKey());
                }
                return shouldEvict;
            }
        });
    }

    public synchronized Optional<List<RoutePath>> get(SearchQueryKey key) {
        CachedSearchResult cached = lruCache.get(key);
        if (cached != null) {
            if (cached.isExpired()) {
                lruCache.remove(key);
                evictions++;
                cacheMisses++;
                System.out.println("[CACHE TTL EXPIRED] Stale cache evicted for key: " + key);
                return Optional.empty();
            }
            cacheHits++;
            System.out.println("[CACHE HIT] High-speed retrieval from memory for key: " + key);
            return Optional.of(cached.getResults());
        }
        cacheMisses++;
        System.out.println("[CACHE MISS] Query not in cache: " + key);
        return Optional.empty();
    }

    public synchronized void put(SearchQueryKey key, List<RoutePath> results) {
        lruCache.put(key, new CachedSearchResult(results, defaultTtlSeconds));
        System.out.printf("[CACHE STORE] Stored %d route(s) under key %s (TTL: %d sec)%n",
                results.size(), key, defaultTtlSeconds);
    }

    public synchronized void invalidateAll() {
        lruCache.clear();
        System.out.println("[CACHE FLUSH] All cache entries cleared due to inventory/price update.");
    }

    public void printPerformanceMetrics() {
        long total = cacheHits + cacheMisses;
        double hitRate = (total == 0) ? 0.0 : ((double) cacheHits / total) * 100.0;

        System.out.println("\n+-------------------------------------------------+");
        System.out.println("|        SEARCH ENGINE CACHE ANALYTICS            |");
        System.out.println("+-------------------------------------------------+");
        System.out.printf("| Active Cache Size : %-27d |%n", lruCache.size());
        System.out.printf("| Cache Hits        : %-27d |%n", cacheHits);
        System.out.printf("| Cache Misses      : %-27d |%n", cacheMisses);
        System.out.printf("| Total Evictions   : %-27d |%n", evictions);
        System.out.printf("| Cache Hit Rate    : %-26.2f%% |%n", hitRate);
        System.out.println("+-------------------------------------------------+\n");
    }
}