package service.notification;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

// Manages real-time event subscriptions per flight
public class FlightEventPublisher {
    // Maps a Flight Number to a List of Subscribers
    private final Map<String, List<FlightStatusSubscriber>> flightSubscribers = new ConcurrentHashMap<>();

    public void subscribe(String flightNumber, FlightStatusSubscriber passenger) {
        flightSubscribers.computeIfAbsent(flightNumber.toUpperCase(), k -> new ArrayList<>()).add(passenger);
        System.out.printf("[SUBSCRIPTION] Passenger %s successfully subscribed to live alerts for flight %s.%n",
                passenger.getPassengerId(), flightNumber.toUpperCase());
    }

    public void unsubscribe(String flightNumber, String passengerId) {
        List<FlightStatusSubscriber> subscribers = flightSubscribers.get(flightNumber.toUpperCase());
        if (subscribers != null) {
            subscribers.removeIf(p -> p.getPassengerId().equals(passengerId));
            System.out.printf("[SUBSCRIPTION] Passenger %s unsubscribed from flight %s.%n", passengerId, flightNumber.toUpperCase());
        }
    }

    // Triggers notification to all subscribed passengers (The Observer Pattern Magic)
    public void publishFlightEvent(String flightNumber, String subject, String message) {
        List<FlightStatusSubscriber> subscribers = flightSubscribers.get(flightNumber.toUpperCase());

        System.out.println("\n==================================================");
        System.out.printf("📡 [BROADCASTING] Flight Event for %s: %s%n", flightNumber.toUpperCase(), subject);
        System.out.println("==================================================");

        if (subscribers == null || subscribers.isEmpty()) {
            System.out.println("No passengers are currently subscribed to alerts for this flight.");
            return;
        }

        System.out.printf("Notifying %d subscribed passenger(s)...%n%n", subscribers.size());

        for (FlightStatusSubscriber passenger : subscribers) {
            passenger.update(flightNumber.toUpperCase(), subject, message);
        }
        System.out.println("✅ Broadcast Complete.\n");
    }
}