package domain.payment;

public class EmiPayment implements PaymentMethod {
    private final CardPayment baseCard;
    private final int tenureMonths; // e.g., 3, 6, 9, 12
    private final double annualInterestRate; // e.g., 12.0 for 12% p.a.

    public EmiPayment(CardPayment baseCard, int tenureMonths, double annualInterestRate) {
        this.baseCard = baseCard;
        this.tenureMonths = tenureMonths;
        this.annualInterestRate = annualInterestRate;
    }

    @Override
    public boolean validate() {
        boolean validTenure = (tenureMonths == 3 || tenureMonths == 6 || tenureMonths == 9 || tenureMonths == 12);
        if (!validTenure) {
            System.out.println("[EMI Validation] FAILED: Unsupported EMI tenure of " + tenureMonths + " months.");
            return false;
        }
        return baseCard.validate();
    }

    @Override
    public boolean process(double amount) {
        if (!validate()) return false;

        double totalInterest = amount * (annualInterestRate / 100.0) * (tenureMonths / 12.0);
        double totalPayable = amount + totalInterest;
        double monthlyInstallment = totalPayable / tenureMonths;

        System.out.printf("[EMI Gateway] Converting transaction of ₹%.2f into %d months EMI at %.1f%% p.a.%n", amount, tenureMonths, annualInterestRate);
        System.out.printf("[EMI Gateway] Monthly Installment: ₹%.2f | Total with Interest: ₹%.2f%n", monthlyInstallment, totalPayable);
        return baseCard.process(amount); // Merchant gets full amount; bank handles EMI conversion
    }

    @Override
    public boolean refund(String transactionId, double amount) {
        System.out.println("[EMI Refund] Informing issuing bank to cancel EMI schedule and credit principal.");
        return baseCard.refund(transactionId, amount);
    }

    @Override
    public String getMethodName() { return "EMI (" + tenureMonths + " Months via Card)"; }
}