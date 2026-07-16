import domain.notification.NotificationType;
import service.notification.*;

import java.util.Set;

public class NotificationSystemDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC12: NOTIFICATION & ALERT SYSTEM DEMO ");
        System.out.println("==================================================");

        // --- STEP 1: TEST DIRECT NOTIFICATION DISPATCH (STRATEGY PATTERN) ---
        System.out.println("\n--- 1. Testing Direct Transactional Notifications (Booking Success) ---");
        NotificationDispatcher dispatcher = NotificationDispatcher.getInstance();

        // Simulating sending a Booking Confirmation
        dispatcher.dispatch(
                "anbu@srm.edu.in",
                "Booking Confirmed: PNR-889900",
                "Your flight AI-101 is confirmed. Total Paid: ₹5000.",
                Set.of(NotificationType.EMAIL, NotificationType.SMS) // Multi-channel dispatch
        );

        // --- STEP 2: SETUP FLIGHT ALERT SUBSCRIPTIONS (OBSERVER PATTERN) ---
        System.out.println("\n--- 2. Setting Up Live Flight Alert Subscriptions ---");
        FlightEventPublisher flightPublisher = new FlightEventPublisher();

        String flightNo = "UK-808";

        // Passenger 1: Prefers Email only
        PassengerAlertObserver pass1 = new PassengerAlertObserver(
                "PASS-101", "John Doe", "john.doe@email.com", "+919000000001",
                Set.of(NotificationType.EMAIL)
        );

        // Passenger 2: Prefers SMS and WhatsApp (Mobile heavy)
        PassengerAlertObserver pass2 = new PassengerAlertObserver(
                "PASS-102", "Annadurai Anbarasu", "anbu@srm.edu.in", "+919800000000",
                Set.of(NotificationType.SMS, NotificationType.WHATSAPP)
        );

        // Passenger 3: All channels
        PassengerAlertObserver pass3 = new PassengerAlertObserver(
                "PASS-103", "Ramesh Kumar", "ramesh@email.com", "+919999999999",
                Set.of(NotificationType.EMAIL, NotificationType.WHATSAPP)
        );

        // Subscribe them to UK-808
        flightPublisher.subscribe(flightNo, pass1);
        flightPublisher.subscribe(flightNo, pass2);
        flightPublisher.subscribe(flightNo, pass3);

        // --- STEP 3: PUBLISH A REAL-TIME DELAY EVENT ---
        System.out.println("\n--- 3. Triggering a Real-Time Delay Event Broadcast ---");
        flightPublisher.publishFlightEvent(
                flightNo,
                "URGENT: Flight Delay Notification",
                "Flight UK-808 has been delayed by 45 minutes due to heavy rain in Chennai. Revised departure is 18:45 IST."
        );

        // --- STEP 4: PASSENGER UNSUBSCRIBES ---
        System.out.println("--- 4. Unsubscribing a Passenger ---");
        flightPublisher.unsubscribe(flightNo, "PASS-101");

        // --- STEP 5: PUBLISH BOARDING ALERT ---
        System.out.println("\n--- 5. Triggering Final Boarding Broadcast ---");
        flightPublisher.publishFlightEvent(
                flightNo,
                "Gate Open: Boarding Commenced",
                "Gate 4 is now open. Please proceed to boarding for UK-808."
        );

        System.out.println("==================================================");
        System.out.println(" UC12 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}