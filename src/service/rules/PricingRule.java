package service.rules;

import domain.rules.FareCalculationContext;

public interface PricingRule {
    void apply(FareCalculationContext context);
    String getRuleName();
    int getPriorityOrder(); // Lower number executes first
}