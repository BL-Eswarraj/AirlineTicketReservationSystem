package domain.notification;

public class WhatsAppNotificationSender implements NotificationSender {
    @Override
    public void send(String recipient, String subject, String message) {
        System.out.println("--------------------------------------------------");
        System.out.printf("💬 [WHATSAPP SENT TO]: %s%n", recipient);
        System.out.printf("Business Alert: *%s*\n%s%n", subject, message);
        System.out.println("--------------------------------------------------");
    }

    @Override
    public NotificationType getType() { return NotificationType.WHATSAPP; }
}