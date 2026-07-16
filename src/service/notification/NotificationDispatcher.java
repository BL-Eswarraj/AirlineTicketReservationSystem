package service.notification;

import domain.notification.*;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

// Singleton Dispatcher to route messages to the correct Strategy
public class NotificationDispatcher {
    private static final NotificationDispatcher INSTANCE = new NotificationDispatcher();
    private final Map<NotificationType, NotificationSender> senders = new EnumMap<>(NotificationType.class);

    private NotificationDispatcher() {
        // Register available strategies
        senders.put(NotificationType.EMAIL, new EmailNotificationSender());
        senders.put(NotificationType.SMS, new SmsNotificationSender());
        senders.put(NotificationType.WHATSAPP, new WhatsAppNotificationSender());
    }

    public static NotificationDispatcher getInstance() {
        return INSTANCE;
    }

    public void dispatch(String recipient, String subject, String message, Set<NotificationType> preferredMethods) {
        if (preferredMethods == null || preferredMethods.isEmpty()) {
            System.out.println("[DISPATCHER ERROR] No notification preference set for " + recipient);
            return;
        }

        for (NotificationType type : preferredMethods) {
            NotificationSender sender = senders.get(type);
            if (sender != null) {
                sender.send(recipient, subject, message);
            }
        }
    }
}