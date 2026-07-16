package domain.payment;

public class UpiPayment implements PaymentMethod {
    private final String upiId;
    private final String provider; // e.g., Google Pay, PhonePe, Paytm, BHIM

    public UpiPayment(String upiId, String provider) {
        this.upiId = upiId != null ? upiId.trim() : "";
        this.provider = provider;
    }

    @Override
    public boolean validate() {
        // Indian UPI regex: username@bankname or mobile@upi
        boolean isValid = upiId.matches("^[a-zA-Z0-9.\\-_]{2,256}@[a-zA-Z]{2,64}$");
        if (isValid) {
            System.out.println("[UPI Validation] Valid format for VPA: " + upiId + " (" + provider + ")");
            return true;
        }
        System.out.println("[UPI Validation] FAILED: Invalid UPI ID format -> " + upiId);
        return false;
    }

    @Override
    public boolean process(double amount) {
        if (!validate()) return false;
        System.out.printf("[UPI Gateway] Sending push notification of ₹%.2f to %s via %s...%n", amount, upiId, provider);
        System.out.println("[UPI Gateway] User authorized via UPI PIN. Payment SUCCESS.");
        return true;
    }

    @Override
    public boolean refund(String transactionId, double amount) {
        System.out.printf("[UPI Refund] Instant credit of ₹%.2f initiated to VPA %s for original Tx: %s.%n", amount, upiId, transactionId);
        return true;
    }

    @Override
    public String getMethodName() { return "UPI (" + provider + ")"; }
}