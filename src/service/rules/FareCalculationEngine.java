package service.rules;

import domain.rules.FareBreakdown;
import domain.rules.FareCalculationContext;
import domain.rules.PassengerTravelDetails;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class FareCalculationEngine {
    private final List<PricingRule> rulePipeline;

    public FareCalculationEngine() {
        this.rulePipeline = new ArrayList<>();

        // Register business rules into the engine pipeline
        registerRule(new AdvanceBookingDiscountRule());
        registerRule(new FestivalSurgeRule());
        registerRule(new ExcessBaggageRule());
        registerRule(new TaxCalculationRule());

        // Ensure execution order based on priority
        rulePipeline.sort(Comparator.comparingInt(PricingRule::getPriorityOrder));
    }

    public void registerRule(PricingRule rule) {
        rulePipeline.add(rule);
    }

    // Execute the Rule Engine pipeline over the passenger's context
    public FareBreakdown calculateFare(PassengerTravelDetails travelDetails) {
        System.out.printf("%n>>> [RULE ENGINE] Executing Fare Pipeline for PNR: %s (%s) >>>%n",
                travelDetails.pnr(), travelDetails.passengerName());

        FareCalculationContext context = new FareCalculationContext(travelDetails);

        for (PricingRule rule : rulePipeline) {
            System.out.printf(" -> Executing: %-30s [Priority: %3d]%n", rule.getRuleName(), rule.getPriorityOrder());
            rule.apply(context);
        }

        System.out.println(">>> [RULE ENGINE] Pipeline execution complete. Generating final audit breakdown... <<<");
        return context.generateBreakdown();
    }
}