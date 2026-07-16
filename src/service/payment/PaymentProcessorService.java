package service.payment;

import domain.payment.*;
import java.util.*;

public class PaymentProcessorService {
    private final Map<String, PaymentTransaction> transactionDatabase = new HashMap<>();
    private final Set<String> activePaymentPnrs = new HashSet<>(); // For Idempotency check
    private final Map<String, PromoCode> availablePromos = new HashMap<>();

    public PaymentProcessorService() {
        seedPromoCodes();
    }

    // 5.2 & 5.3 Payment Processing Flow with Security and Idempotency
    public synchronized PaymentTransaction executePayment(String pnr, double originalFare, PaymentMethod method, String promoCodeStr) {
        System.out.println("\n--- INITIATING PAYMENT FOR PNR: " + pnr + " ---");

        // 1. Idempotency Check (Prevent duplicate submissions)
        if (activePaymentPnrs.contains(pnr)) {
            throw new IllegalStateException("[SECURITY ERROR] Duplicate payment request detected for PNR: " + pnr + ". Transaction aborted.");
        }
        activePaymentPnrs.add(pnr);

        try {
            // 2. Apply Promo Codes
            double discount = 0.0;
            if (promoCodeStr != null && !promoCodeStr.isBlank()) {
                PromoCode promo = availablePromos.get(promoCodeStr.toUpperCase());
                if (promo != null && promo.isValid()) {
                    discount = promo.calculateDiscount(originalFare);
                    System.out.printf("[PROMO] Applied %s: Saved ₹%.2f!%n", promo.code(), discount);
                } else {
                    System.out.println("[PROMO] Invalid or expired promo code: " + promoCodeStr);
                }
            }

            // 3. Create Transaction Record
            PaymentTransaction tx = new PaymentTransaction(pnr, originalFare, discount, method.getMethodName());
            transactionDatabase.put(tx.getTransactionId(), tx);

            System.out.printf("[FARE SUMMARY] Original: ₹%.2f | Discount: -₹%.2f | Final Payable: ₹%.2f%n",
                    originalFare, discount, tx.getFinalPaidAmount());

            // 4. Process via Gateway
            System.out.println("[GATEWAY] Connecting to secure payment gateway...");
            boolean success = method.process(tx.getFinalPaidAmount());

            if (success) {
                tx.markSuccess();
                generateReceipt(tx);
            } else {
                tx.markFailed();
                System.out.println("[PAYMENT FAILED] Gateway rejected the transaction.");
            }
            return tx;

        } finally {
            // Release lock regardless of outcome
            activePaymentPnrs.remove(pnr);
        }
    }

    // 5.2 Receipt Generation
    private void generateReceipt(PaymentTransaction tx) {
        System.out.println("\n+-------------------------------------------------+");
        System.out.println("|           OFFICIAL PAYMENT RECEIPT              |");
        System.out.println("+-------------------------------------------------+");
        System.out.printf("| Transaction ID : %-30s |%n", tx.getTransactionId());
        System.out.printf("| PNR Reference  : %-30s |%n", tx.getPnr());
        System.out.printf("| Date & Time    : %-30s |%n", tx.getTimestamp().withNano(0));
        System.out.printf("| Payment Method : %-30s |%n", tx.getPaymentMethodName());
        System.out.println("|-------------------------------------------------|");
        System.out.printf("| Base Fare      : ₹%-29.2f |%n", tx.getOriginalAmount());
        System.out.printf("| Promo Discount : -₹%-28.2f |%n", tx.getDiscountApplied());
        System.out.printf("| TOTAL PAID     : ₹%-29.2f |%n", tx.getFinalPaidAmount());
        System.out.println("| Status         : SUCCESS                        |");
        System.out.println("+-------------------------------------------------+\n");
    }

    public Optional<PaymentTransaction> getTransaction(String txId) {
        return Optional.ofNullable(transactionDatabase.get(txId));
    }

    private void seedPromoCodes() {
        availablePromos.put("FLYSRM500", new PromoCode("FLYSRM500", 10.0, 500.0, java.time.LocalDate.now().plusMonths(1)));
        availablePromos.put("FESTIVE20", new PromoCode("FESTIVE20", 20.0, 1500.0, java.time.LocalDate.now().plusMonths(2)));
    }
}