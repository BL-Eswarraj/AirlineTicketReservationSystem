package service.optimization;

import domain.optimization.OptimizedFlight;
import domain.optimization.RoutePath;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

public class RouteIndexingService {
    // Adjacency List Graph: SourceAirport -> List of departing flights
    private final Map<String, List<OptimizedFlight>> adjacencyList = new HashMap<>();

    // Minimum layover time required to safely make a connection (e.g., 45 minutes)
    private static final Duration MIN_LAYOVER = Duration.ofMinutes(45);
    // Maximum layover time before a connection is deemed too long (e.g., 8 hours)
    private static final Duration MAX_LAYOVER = Duration.ofHours(8);

    // Build/Rebuild Graph Index O(N) time complexity
    public synchronized void buildIndex(List<OptimizedFlight> masterInventory) {
        adjacencyList.clear();
        for (OptimizedFlight flight : masterInventory) {
            adjacencyList.computeIfAbsent(flight.source().toUpperCase(), k -> new ArrayList<>()).add(flight);
        }
        System.out.printf("[INDEXER] Network graph index successfully built across %d departing airport hubs.%n", adjacencyList.size());
    }

    // High-Speed O(1) direct flight lookup + O(K) 1-stop connection graph traversal
    public List<RoutePath> findOptimizedRoutes(String source, String destination, LocalDate date, int maxStops) {
        List<RoutePath> validPaths = new ArrayList<>();
        String src = source.toUpperCase().trim();
        String dst = destination.toUpperCase().trim();

        List<OptimizedFlight> departingFromSource = adjacencyList.getOrDefault(src, Collections.emptyList());

        // 1. Scan direct flights departing from Source
        for (OptimizedFlight leg1 : departingFromSource) {
            if (!leg1.departureTime().toLocalDate().equals(date)) continue;

            if (leg1.destination().equalsIgnoreCase(dst)) {
                validPaths.add(RoutePath.ofDirect(leg1));
            }
            // 2. If maxStops >= 1, traverse adjacency list of the connecting hub (Leg 1 Destination)
            else if (maxStops >= 1) {
                String transferHub = leg1.destination().toUpperCase();
                List<OptimizedFlight> departingFromHub = adjacencyList.getOrDefault(transferHub, Collections.emptyList());

                for (OptimizedFlight leg2 : departingFromHub) {
                    if (leg2.destination().equalsIgnoreCase(dst)) {
                        // Validate temporal connection window (Layover rules)
                        Duration layover = Duration.between(leg1.arrivalTime(), leg2.departureTime());
                        if (layover.compareTo(MIN_LAYOVER) >= 0 && layover.compareTo(MAX_LAYOVER) <= 0) {
                            validPaths.add(RoutePath.ofConnecting(leg1, leg2));
                        }
                    }
                }
            }
        }

        // Sort by total fare ascending by default
        validPaths.sort(Comparator.comparingDouble(RoutePath::totalFare));
        return validPaths;
    }

    public int getIndexedHubsCount() { return adjacencyList.size(); }
}