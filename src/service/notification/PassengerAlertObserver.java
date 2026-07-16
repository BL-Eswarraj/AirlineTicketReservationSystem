package service.notification;

import domain.notification.NotificationType;
import java.util.Set;

public class PassengerAlertObserver implements FlightStatusSubscriber {
    private final String passengerId;
    private final String contactName;
    private final String emailAddress;
    private final String phoneNumber;
    private final Set<NotificationType> preferences;

    public PassengerAlertObserver(String passengerId, String contactName, String emailAddress, String phoneNumber, Set<NotificationType> preferences) {
        this.passengerId = passengerId;
        this.contactName = contactName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.preferences = preferences;
    }

    @Override
    public void update(String flightNumber, String eventSubject, String eventMessage) {
        NotificationDispatcher dispatcher = NotificationDispatcher.getInstance();
        String personalizedMessage = String.format("Dear %s, %s", contactName, eventMessage);

        if (preferences.contains(NotificationType.EMAIL)) {
            dispatcher.dispatch(emailAddress, eventSubject, personalizedMessage, Set.of(NotificationType.EMAIL));
        }
        if (preferences.contains(NotificationType.SMS) || preferences.contains(NotificationType.WHATSAPP)) {
            // Group mobile notifications
            dispatcher.dispatch(phoneNumber, eventSubject, personalizedMessage,
                    Set.copyOf(preferences).stream().filter(p -> p != NotificationType.EMAIL).collect(java.util.stream.Collectors.toSet()));
        }
    }

    @Override
    public String getPassengerId() { return passengerId; }
}