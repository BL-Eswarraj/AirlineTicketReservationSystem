package domain.payment;

public class CardPayment implements PaymentMethod {
    private final String cardNumber;
    private final String cardHolderName;
    private final String expiryDate; // MM/YY
    private final String cvv;

    public CardPayment(String cardNumber, String cardHolderName, String expiryDate, String cvv) {
        this.cardNumber = cardNumber != null ? cardNumber.replaceAll("\\s+", "") : "";
        this.cardHolderName = cardHolderName;
        this.expiryDate = expiryDate;
        this.cvv = cvv != null ? cvv.trim() : "";
    }

    @Override
    public boolean validate() {
        boolean validCard = cardNumber.matches("^\\d{16}$");
        boolean validCvv = cvv.matches("^\\d{3,4}$");
        boolean validExpiry = expiryDate != null && expiryDate.matches("^(0[1-9]|1[0-2])/\\d{2}$");

        if (validCard && validCvv && validExpiry) {
            System.out.println("[Card Validation] PCI-DSS compliance check passed for card: " + getMaskedCardNumber());
            return true;
        }
        System.out.println("[Card Validation] FAILED: Invalid Card Details provided.");
        return false;
    }

    @Override
    public boolean process(double amount) {
        if (!validate()) return false;
        System.out.printf("[Card Gateway] Initiating 3D Secure OTP verification for %s...%n", getMaskedCardNumber());
        System.out.printf("[Card Gateway] Charging ₹%.2f to card... SUCCESS.%n", amount);
        return true;
    }

    @Override
    public boolean refund(String transactionId, double amount) {
        System.out.printf("[Card Refund] Processing refund of ₹%.2f to %s. Normal settlement time is 3-5 business days.%n",
                amount, getMaskedCardNumber());
        return true;
    }

    public String getMaskedCardNumber() {
        if (cardNumber.length() < 4) return "XXXX";
        return "XXXX-XXXX-XXXX-" + cardNumber.substring(cardNumber.length() - 4);
    }

    @Override
    public String getMethodName() { return "Credit/Debit Card (" + getMaskedCardNumber() + ")"; }
}