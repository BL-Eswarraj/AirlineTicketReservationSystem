package service.rules;

import domain.rules.FareCalculationContext;
import java.time.temporal.ChronoUnit;

public class AdvanceBookingDiscountRule implements PricingRule {

    @Override
    public void apply(FareCalculationContext context) {
        long daysInAdvance = ChronoUnit.DAYS.between(
                context.getDetails().bookingTimestamp(),
                context.getDetails().scheduledDeparture()
        );

        if (daysInAdvance >= 30) {
            double discount = context.getCurrentBaseFare() * 0.10; // 10% discount
            context.addDiscount(discount, "Early Bird Discount (Booked " + daysInAdvance + " days in advance -> 10% off base)");
        } else if (daysInAdvance >= 15) {
            double discount = context.getCurrentBaseFare() * 0.05; // 5% discount
            context.addDiscount(discount, "Advance Reservation Discount (Booked " + daysInAdvance + " days in advance -> 5% off base)");
        } else {
            context.logRuleExecution("No Advance Booking Discount applicable (Booked " + daysInAdvance + " days prior).");
        }
    }

    @Override
    public String getRuleName() { return "Advance Booking Discount Rule"; }
    @Override
    public int getPriorityOrder() { return 10; } // Run first
}