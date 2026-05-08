package com.example.bakery.model;

public class PerishableProduct extends Product {

    private String expiryDate;
    private double discountRate; // e.g. 0.2 = 20% off near expiry

    public PerishableProduct() { super(); }

    public PerishableProduct(int id, String name, String description, double price,
                              int stockQuantity, String category, String expiryDate) {
        super(id, name, description, price, stockQuantity, category);
        this.expiryDate = expiryDate;
        this.discountRate = 0.0;
    }

    // Polymorphism - runtime method overriding (Lecture 04)
    @Override
    public String getDisplayPrice() {
        if (discountRate > 0) {
            double discounted = getPrice() * (1 - discountRate);
            return String.format("Rs. %.2f (%.0f%% off - Near Expiry!)", discounted, discountRate * 100);
        }
        return String.format("Rs. %.2f", getPrice());
    }

    @Override
    public String getType() { return "PERISHABLE"; }

    @Override
    public String toString() {
        return super.toString() + "," + expiryDate + "," + discountRate;
    }

    public static PerishableProduct fromLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 9) return null;
        try {
            PerishableProduct prod = new PerishableProduct(
                Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(),
                Double.parseDouble(p[3].trim()), Integer.parseInt(p[4].trim()),
                p[5].trim(), p.length > 8 ? p[8].trim() : ""
            );
            if (p.length > 9) prod.setDiscountRate(Double.parseDouble(p[9].trim()));
            return prod;
        } catch (NumberFormatException e) { return null; }
    }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public double getDiscountRate() { return discountRate; }
    public void setDiscountRate(double discountRate) { this.discountRate = discountRate; }
}
