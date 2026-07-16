package service.rules;

import domain.rules.FareCalculationContext;
import domain.rules.TravelClass;

public class ExcessBaggageRule implements PricingRule {
    private static final double EXCESS_FEE_PER_KG = 500.0; // ₹500 per extra kg

    @Override
    public void apply(FareCalculationContext context) {
        double declaredWeight = context.getDetails().declaredBaggageWeightKg();
        TravelClass tClass = context.getDetails().travelClass();
        int freeAllowance = tClass.getFreeBaggageAllowanceKg();

        if (declaredWeight > freeAllowance) {
            double excessWeight = declaredWeight - freeAllowance;
            double penalty = excessWeight * EXCESS_FEE_PER_KG;
            context.setExcessBaggageFee(penalty,
                    String.format("Excess Baggage: Declared %.1f kg exceeds %s free allowance of %d kg by %.1f kg (@ ₹%.2f/kg)",
                            declaredWeight, tClass, freeAllowance, excessWeight, EXCESS_FEE_PER_KG));
        } else {
            context.logRuleExecution(String.format("Baggage Compliant: Declared %.1f kg is within %s allowance of %d kg.",
                    declaredWeight, tClass, freeAllowance));
        }
    }

    @Override
    public String getRuleName() { return "Excess Baggage Rule"; }
    @Override
    public int getPriorityOrder() { return 30; }
}