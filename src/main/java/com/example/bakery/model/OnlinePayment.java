package com.example.bakery.model;

public class OnlinePayment extends PaymentMethod {
    private String cardLastFour;

    public OnlinePayment(String paymentId, double amount, String cardLastFour) {
        super(paymentId, amount);
        this.cardLastFour = cardLastFour;
    }

    @Override
    public String processPayment() {
        // Simulate online payment processing
        return "PAID_ONLINE";
    }

    @Override
    public String getPaymentType() { return "ONLINE"; }

    public String getCardLastFour() { return cardLastFour; }
}
