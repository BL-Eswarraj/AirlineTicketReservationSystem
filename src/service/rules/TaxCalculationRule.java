package service.rules;

import domain.rules.FareCalculationContext;
import domain.rules.TravelClass;

public class TaxCalculationRule implements PricingRule {

    @Override
    public void apply(FareCalculationContext context) {
        // GST is calculated on the net taxable base fare (Base - Discounts + Surcharges)
        double taxableAmount = context.getTaxableBaseAmount();
        TravelClass tClass = context.getDetails().travelClass();
        double gstRate = tClass.getGstPercentage();

        double gstAmount = (taxableAmount * gstRate) / 100.0;
        context.setGstAmount(gstAmount, String.format("Indian Civil Aviation GST @ %.1f%% on taxable base ₹%.2f (%s Class)",
                gstRate, taxableAmount, tClass));
    }

    @Override
    public String getRuleName() { return "Tax Calculation Rule"; }
    @Override
    public int getPriorityOrder() { return 100; } // Run last after all discounts and surcharges
}