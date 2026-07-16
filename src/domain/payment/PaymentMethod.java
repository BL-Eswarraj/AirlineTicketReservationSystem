package domain.payment;

public interface PaymentMethod {
    boolean validate();
    boolean process(double amount);
    boolean refund(String transactionId, double amount);
    String getMethodName();
}