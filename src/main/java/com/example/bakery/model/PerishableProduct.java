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
        // Format: id, name, desc(may have commas), price, qty, category, TYPE, available, expiryDate, discountRate
        if (p.length < 8) return null;
        try {
            int last = p.length - 1;
            double discountRate = Double.parseDouble(p[last].trim());
            String expiry = p[last - 1].trim();
            // available = p[last-2], TYPE = p[last-3], category = p[last-4]
            String category = p[last - 4].trim();
            int qty = Integer.parseInt(p[last - 5].trim());
            double price = Double.parseDouble(p[last - 6].trim());
            // description = everything between index 2 and last-7 (inclusive)
            StringBuilder desc = new StringBuilder();
            for (int i = 2; i <= last - 7; i++) {
                if (i > 2) desc.append(",");
                desc.append(p[i]);
            }
            PerishableProduct prod = new PerishableProduct(
                Integer.parseInt(p[0].trim()), p[1].trim(), desc.toString().trim(),
                price, qty, category, expiry
            );
            prod.setDiscountRate(discountRate);
            return prod;
        } catch (Exception e) { return null; }
    }

    public String getExpiryDate() { return expiryDate; }
    public void setExpiryDate(String expiryDate) { this.expiryDate = expiryDate; }
    public double getDiscountRate() { return discountRate; }
    public void setDiscountRate(double discountRate) { this.discountRate = discountRate; }
}
