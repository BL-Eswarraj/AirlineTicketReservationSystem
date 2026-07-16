package domain.rules;

public enum TravelClass {
    ECONOMY(15, 5.0),           // 15kg free baggage, 5% GST
    PREMIUM_ECONOMY(20, 12.0),  // 20kg free baggage, 12% GST
    BUSINESS(30, 12.0),         // 30kg free baggage, 12% GST
    FIRST(40, 12.0);            // 40kg free baggage, 12% GST

    private final int freeBaggageAllowanceKg;
    private final double gstPercentage;

    TravelClass(int freeBaggageAllowanceKg, double gstPercentage) {
        this.freeBaggageAllowanceKg = freeBaggageAllowanceKg;
        this.gstPercentage = gstPercentage;
    }

    public int getFreeBaggageAllowanceKg() { return freeBaggageAllowanceKg; }
    public double getGstPercentage() { return gstPercentage; }
}