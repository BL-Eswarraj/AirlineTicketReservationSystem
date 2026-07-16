package domain.notification;

public interface NotificationSender {
    void send(String recipient, String subject, String message);
    NotificationType getType();
}