package service.notification;

public interface FlightStatusSubscriber {
    void update(String flightNumber, String eventSubject, String eventMessage);
    String getPassengerId();
}