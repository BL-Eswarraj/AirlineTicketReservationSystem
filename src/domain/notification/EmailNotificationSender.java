package domain.notification;

public class EmailNotificationSender implements NotificationSender {
    @Override
    public void send(String recipient, String subject, String message) {
        System.out.println("--------------------------------------------------");
        System.out.printf("📧 [EMAIL SENT TO]: %s%n", recipient);
        System.out.printf("Subject: %s%n", subject);
        System.out.printf("Message: %s%n", message);
        System.out.println("--------------------------------------------------");
    }

    @Override
    public NotificationType getType() { return NotificationType.EMAIL; }
}