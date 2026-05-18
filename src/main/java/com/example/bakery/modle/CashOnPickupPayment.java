package com.example.bakery.model;

/**
 * Cash on Pickup payment — concrete implementation.
 * Lecture 04: Method Overriding / Polymorphism
 */
public class CashOnPickupPayment extends PaymentMethod {
    public CashOnPickupPayment(String paymentId, double amount) {
        super(paymentId, amount);
    }

    @Override
    public String processPayment() {
        return "PENDING_PICKUP";
    }

    @Override
    public String getPaymentType() { return "CASH_ON_PICKUP"; }
}
