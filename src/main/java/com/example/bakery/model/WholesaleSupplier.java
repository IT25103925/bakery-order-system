package com.example.bakery.model;

public class WholesaleSupplier extends Supplier {

    private int minimumOrderQuantity;

    public WholesaleSupplier() {
        super();
    }

    public WholesaleSupplier(int id, String name, String email, String phone, String address, int moq) {
        super(id, name, email, phone, address);
        this.minimumOrderQuantity = moq;
    }

    @Override
    public String getSupplierType() {
        return "WHOLESALE";
    }

    @Override
    public double getDiscountRate() {
        return 0.15;
    }  // 15% wholesale discount

    @Override
    public String toString() {
        return super.toString() + "," + minimumOrderQuantity;
    }

    public static WholesaleSupplier fromLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 6) return null;
        try {
            int moq = p.length > 6 ? Integer.parseInt(p[6].trim()) : 50;
            return new WholesaleSupplier(Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(),
                    p[3].trim(), p[4].trim(), moq);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public int getMinimumOrderQuantity() {
        return minimumOrderQuantity;
    }
    public void setMinimumOrderQuantity(int minimumOrderQuantity) {
        this.minimumOrderQuantity = minimumOrderQuantity;
    }
}
