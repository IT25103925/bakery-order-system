package com.example.bakery.model;

public class NonPerishableProduct extends Product {

    private int shelfLifeDays;
    private boolean bulkDiscountEligible;

    public NonPerishableProduct() { super(); }

    public NonPerishableProduct(int id, String name, String description, double price,
                                 int stockQuantity, String category, int shelfLifeDays) {
        super(id, name, description, price, stockQuantity, category);
        this.shelfLifeDays = shelfLifeDays;
        this.bulkDiscountEligible = false;
    }

    // Polymorphism - different price display for non-perishable (Lecture 04)
    @Override
    public String getDisplayPrice() {
        if (bulkDiscountEligible) {
            return String.format("Rs. %.2f (Bulk discount available)", getPrice());
        }
        return String.format("Rs. %.2f", getPrice());
    }

    @Override
    public String getType() { return "NON_PERISHABLE"; }

    @Override
    public String toString() {
        return super.toString() + "," + shelfLifeDays + "," + bulkDiscountEligible;
    }

    public static NonPerishableProduct fromLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 8) return null;
        try {
            NonPerishableProduct prod = new NonPerishableProduct(
                Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(),
                Double.parseDouble(p[3].trim()), Integer.parseInt(p[4].trim()),
                p[5].trim(), p.length > 8 ? Integer.parseInt(p[8].trim()) : 30
            );
            if (p.length > 9) prod.setBulkDiscountEligible(Boolean.parseBoolean(p[9].trim()));
            return prod;
        } catch (NumberFormatException e) { return null; }
    }

    public int getShelfLifeDays() { return shelfLifeDays; }
    public void setShelfLifeDays(int shelfLifeDays) { this.shelfLifeDays = shelfLifeDays; }
    public boolean isBulkDiscountEligible() { return bulkDiscountEligible; }
    public void setBulkDiscountEligible(boolean bulkDiscountEligible) { this.bulkDiscountEligible = bulkDiscountEligible; }
}
