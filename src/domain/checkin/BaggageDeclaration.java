package domain.checkin;

public record BaggageDeclaration(
        int checkedBagsCount,
        double totalWeightKg,
        int cabinBagsCount,
        boolean dangerousGoodsAccepted
) {
    public boolean isCompliant(int maxCheckedBags, double maxWeightKg) {
        if (!dangerousGoodsAccepted) {
            System.out.println("[SAFETY ERROR] Check-in denied: Passenger must acknowledge and agree to dangerous goods regulations.");
            return false;
        }
        if (checkedBagsCount > maxCheckedBags || totalWeightKg > maxWeightKg) {
            System.out.printf("[BAGGAGE LIMIT EXCEEDED] Declared: %d bags / %.1f kg | Allowed: %d bags / %.1f kg.%n",
                    checkedBagsCount, totalWeightKg, maxCheckedBags, maxWeightKg);
            return false;
        }
        return true;
    }
}