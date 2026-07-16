package domain.notification;

public class SmsNotificationSender implements NotificationSender {
    @Override
    public void send(String recipient, String subject, String message) {
        System.out.println("--------------------------------------------------");
        System.out.printf("📱 [SMS SENT TO]: %s%n", recipient);
        // SMS doesn't typically have subjects, so we append it for context
        System.out.printf("Payload: %s - %s%n", subject, message);
        System.out.println("--------------------------------------------------");
    }

    @Override
    public NotificationType getType() { return NotificationType.SMS; }
}