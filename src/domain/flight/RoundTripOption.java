package domain.flight;

import java.time.Duration;

// Represents a matched pair of outbound and return flights
public record RoundTripOption(Flight outboundFlight, Flight returnFlight) {
    public Duration getTotalJourneyTime() {
        return outboundFlight.duration().plus(returnFlight.duration());
    }

    public double getTotalPrice() {
        return outboundFlight.getTotalPrice() + returnFlight.getTotalPrice();
    }
}