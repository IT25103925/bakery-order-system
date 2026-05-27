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
        // Format: id, name, desc(may have commas), price, qty, category, TYPE, available, shelfLife, bulkDiscount
        // Read fixed fields from the END to handle commas in description
        if (p.length < 8) return null;
        try {
            int last = p.length - 1;
            boolean bulkDiscount = Boolean.parseBoolean(p[last].trim());
            int shelfLife = Integer.parseInt(p[last - 1].trim());
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
            NonPerishableProduct prod = new NonPerishableProduct(
                Integer.parseInt(p[0].trim()), p[1].trim(), desc.toString().trim(),
                price, qty, category, shelfLife
            );
            prod.setBulkDiscountEligible(bulkDiscount);
            return prod;
        } catch (Exception e) { return null; }
    }

    public int getShelfLifeDays() { return shelfLifeDays; }
    public void setShelfLifeDays(int shelfLifeDays) { this.shelfLifeDays = shelfLifeDays; }
    public boolean isBulkDiscountEligible() { return bulkDiscountEligible; }
    public void setBulkDiscountEligible(boolean bulkDiscountEligible) { this.bulkDiscountEligible = bulkDiscountEligible; }
}
