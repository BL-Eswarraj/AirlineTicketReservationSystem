import domain.payment.*;
import service.payment.PaymentProcessorService;
import service.payment.RefundService;

public class PaymentProcessingDemo {

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println(" UC5: PAYMENT PROCESSING MODULE DEMO ");
        System.out.println("==================================================");

        PaymentProcessorService paymentService = new PaymentProcessorService();
        RefundService refundService = new RefundService();

        // --- STEP 1: TEST UPI PAYMENT WITH PROMO CODE ---
        System.out.println("\n--- 1. Testing UPI Payment (Google Pay) + Promo Code ---");
        UpiPayment upiMethod = new UpiPayment("anbu@okhdfcbank", "Google Pay");

        PaymentTransaction upiTx = paymentService.executePayment(
                "PNR-DELBOM-01",
                6000.0,
                upiMethod,
                "FLYSRM500" // 10% discount up to ₹500
        );

        // --- STEP 2: TEST CARD PAYMENT WITH PCI-DSS MASKING ---
        System.out.println("\n--- 2. Testing Credit Card Payment ---");
        CardPayment cardMethod = new CardPayment("4532789012345678", "Annadurai Anbarasu", "08/29", "456");

        PaymentTransaction cardTx = paymentService.executePayment(
                "PNR-MAASIN-02",
                12500.0,
                cardMethod,
                "FESTIVE20" // 20% discount up to ₹1500
        );

        // --- STEP 3: TEST EMI PAYMENT METHOD ---
        System.out.println("\n--- 3. Testing EMI Option (6 Months at 12% p.a.) ---");
        CardPayment emiBaseCard = new CardPayment("5412753098765432", "Ramesh Kumar", "11/27", "890");
        EmiPayment emiMethod = new EmiPayment(emiBaseCard, 6, 12.0);

        paymentService.executePayment("PNR-BLRLON-03", 45000.0, emiMethod, null);

        // --- STEP 4: TEST VALIDATION FAILURE (INVALID UPI) ---
        System.out.println("\n--- 4. Testing Validation Failure Handling ---");
        UpiPayment invalidUpi = new UpiPayment("invalid-upi-without-at-sign", "PhonePe");
        paymentService.executePayment("PNR-FAIL-04", 3000.0, invalidUpi, null);

        // --- STEP 5: TEST REFUND PROCESSING (WITH CANCELLATION CHARGE) ---
        System.out.println("\n--- 5. Testing Refund Processing (With 15% Cancellation Fee) ---");
        // Refunding the successful Card transaction from Step 2
        if (cardTx.getStatus() == PaymentStatus.SUCCESS) {
            refundService.processRefund(cardTx, cardMethod, 15.0); // 15% cancellation fee
        }

        // Print final status of transaction
        System.out.println("\nFinal Transaction Record Status:");
        System.out.println(cardTx);

        System.out.println("\n==================================================");
        System.out.println(" UC5 MODULE EXECUTION COMPLETED SUCCESSFULLY! ");
        System.out.println("==================================================");
    }
}