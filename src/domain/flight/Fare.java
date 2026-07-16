package domain.flight;

public record Fare(double baseFare, double taxes, double fees) {
    public double getTotalFare() {
        return baseFare + taxes + fees;
    }
}