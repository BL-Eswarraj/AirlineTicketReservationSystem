package service.rules;

import domain.rules.FareCalculationContext;

public class FestivalSurgeRule implements PricingRule {

    @Override
    public void apply(FareCalculationContext context) {
        if (context.getDetails().isPeakFestivalSeason()) {
            double surcharge = context.getCurrentBaseFare() * 0.15; // 15% peak demand surge
            context.addSurcharge(surcharge, "Peak Festival Season Surge (High Demand Period -> 15% surcharge applied)");
        } else {
            context.logRuleExecution("Standard Season: No holiday surge pricing applied.");
        }
    }

    @Override
    public String getRuleName() { return "Festival Season Surge Rule"; }
    @Override
    public int getPriorityOrder() { return 20; }
}