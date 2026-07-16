package domain.rules;

import java.util.List;

// Immutable output record representing the final audited financial summary
public record FareBreakdown(
        String pnr,
        String passengerName,
        String flightNumber,
        double initialBaseFare,
        double totalDiscounts,
        double totalSurcharges,
        double excessBaggageFee,
        double gstTaxAmount,
        double airportServiceFee,
        double finalPayableAmount,
        List<String> appliedRuleLogs
) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n+=============================================================================+\n");
        sb.append("                       OFFICIAL FARE CALCULATION AUDIT                         \n");
        sb.append("+=============================================================================+\n");
        sb.append(String.format(" PNR Reference : %-15s | Passenger : %-25s%n", pnr, passengerName));
        sb.append(String.format(" Flight        : %-15s | Base Fare : ₹%-10.2f%n", flightNumber, initialBaseFare));
        sb.append("-------------------------------------------------------------------------------\n");
        sb.append(String.format(" (-) Advance/Promo Discounts    : -₹%-10.2f%n", totalDiscounts));
        sb.append(String.format(" (+) Seasonal / Surge Charges   : +₹%-10.2f%n", totalSurcharges));
        sb.append(String.format(" (+) Excess Baggage Penalty     : +₹%-10.2f%n", excessBaggageFee));
        sb.append(String.format(" (+) GST Tax Liability          : +₹%-10.2f%n", gstTaxAmount));
        sb.append(String.format(" (+) Airport Passenger Fee (PSF): +₹%-10.2f%n", airportServiceFee));
        sb.append("-------------------------------------------------------------------------------\n");
        sb.append(String.format(" TOTAL FINAL PAYABLE AMOUNT     : ₹%-10.2f%n", finalPayableAmount));
        sb.append("+-----------------------------------------------------------------------------+\n");
        sb.append(" Rule Execution Audit Trail:\n");
        appliedRuleLogs.forEach(log -> sb.append("  * ").append(log).append("\n"));
        sb.append("+=============================================================================+\n");
        return sb.toString();
    }
}