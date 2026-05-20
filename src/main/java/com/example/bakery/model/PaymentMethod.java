package com.example.bakery.model;

public abstract class PaymentMethod {
    private String paymentId;
    private double amount;

    public PaymentMethod(String paymentId, double amount) {
        this.paymentId = paymentId;
        this.amount = amount;
    }

    /** Each payment type processes differently */
    public abstract String processPayment();

    /** Returns a display label for the payment type */
    public abstract String getPaymentType();

    public String getPaymentId() { return paymentId; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
}
