package domain.rules;

import java.util.ArrayList;
import java.util.List;

// Mutable Context object modified sequentially by each Pricing Rule in the pipeline
public class FareCalculationContext {
    private final PassengerTravelDetails details;

    private double currentBaseFare;
    private double discountAmount;
    private double surchargeAmount;
    private double excessBaggageFee;
    private double gstAmount;
    private double airportServiceFee;
    private final List<String> auditLogs;

    public FareCalculationContext(PassengerTravelDetails details) {
        this.details = details;
        this.currentBaseFare = details.initialBaseFare();
        this.discountAmount = 0.0;
        this.surchargeAmount = 0.0;
        this.excessBaggageFee = 0.0;
        this.gstAmount = 0.0;
        this.airportServiceFee = 200.0; // Flat ₹200 mandatory PSF across Indian airports
        this.auditLogs = new ArrayList<>();

        logRuleExecution("Context initialized with Base Fare: ₹" + currentBaseFare);
    }

    public void addDiscount(double amount, String reason) {
        this.discountAmount += amount;
        logRuleExecution(String.format("DISCOUNT APPLIED [-₹%.2f]: %s", amount, reason));
    }

    public void addSurcharge(double amount, String reason) {
        this.surchargeAmount += amount;
        logRuleExecution(String.format("SURCHARGE APPLIED [+₹%.2f]: %s", amount, reason));
    }

    public void setExcessBaggageFee(double fee, String reason) {
        this.excessBaggageFee = fee;
        logRuleExecution(String.format("BAGGAGE PENALTY [+₹%.2f]: %s", fee, reason));
    }

    public void setGstAmount(double gst, String reason) {
        this.gstAmount = gst;
        logRuleExecution(String.format("TAX APPLIED [+₹%.2f]: %s", gst, reason));
    }

    public void logRuleExecution(String message) {
        this.auditLogs.add(message);
    }

    public double getTaxableBaseAmount() {
        return Math.max(0.0, currentBaseFare - discountAmount + surchargeAmount);
    }

    public FareBreakdown generateBreakdown() {
        double finalTotal = getTaxableBaseAmount() + excessBaggageFee + gstAmount + airportServiceFee;
        return new FareBreakdown(
                details.pnr(),
                details.passengerName(),
                details.flightNumber(),
                details.initialBaseFare(),
                discountAmount,
                surchargeAmount,
                excessBaggageFee,
                gstAmount,
                airportServiceFee,
                finalTotal,
                List.copyOf(auditLogs)
        );
    }

    // Getters
    public PassengerTravelDetails getDetails() { return details; }
    public double getCurrentBaseFare() { return currentBaseFare; }
    public double getDiscountAmount() { return discountAmount; }
    public double getSurchargeAmount() { return surchargeAmount; }
}