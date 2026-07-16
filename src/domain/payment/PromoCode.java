package domain.payment;

import java.time.LocalDate;

public record PromoCode(String code, double discountPercentage, double maxDiscountAmount, LocalDate validUntil) {
    public boolean isValid() {
        return LocalDate.now().isBefore(validUntil) || LocalDate.now().isEqual(validUntil);
    }

    public double calculateDiscount(double totalAmount) {
        if (!isValid()) return 0.0;
        double calculated = (totalAmount * discountPercentage) / 100.0;
        return Math.min(calculated, maxDiscountAmount);
    }
}