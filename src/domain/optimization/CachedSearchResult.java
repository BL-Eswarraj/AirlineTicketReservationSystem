package domain.optimization;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

// Wrapper for cached results with automatic Time-To-Live (TTL) expiration tracking
public class CachedSearchResult {
    private final List<RoutePath> results;
    private final LocalDateTime creationTimestamp;
    private final long ttlSeconds;

    public CachedSearchResult(List<RoutePath> results, long ttlSeconds) {
        this.results = results;
        this.creationTimestamp = LocalDateTime.now();
        this.ttlSeconds = ttlSeconds;
    }

    public boolean isExpired() {
        long elapsed = ChronoUnit.SECONDS.between(creationTimestamp, LocalDateTime.now());
        return elapsed >= ttlSeconds;
    }

    public List<RoutePath> getResults() {
        return results;
    }
}