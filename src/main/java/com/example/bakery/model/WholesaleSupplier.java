package com.example.bakery.model;


public class WholesaleSupplier extends Supplier {

    private int minimumOrderQuantity;

    public WholesaleSupplier() { super(); }

    public WholesaleSupplier(int id, String name, String email, String phone, String address, int moq) {
        super(id, name, email, phone, address);
        this.minimumOrderQuantity = moq;
    }

    @Override
    public String getSupplierType() { return "WHOLESALE"; }

    @Override
    public double getDiscountRate() { return 0.15; }

    @Override
    public String toString() {
        return super.toString() + "," + minimumOrderQuantity;
    }

    public static WholesaleSupplier fromLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 6) return null;
        try {

            int last = p.length - 1;
            int moq = Integer.parseInt(p[last].trim());


            StringBuilder address = new StringBuilder();
            for (int i = 4; i <= last - 2; i++) {
                if (i > 4) address.append(",");
                address.append(p[i]);
            }
            return new WholesaleSupplier(
                    Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(),
                    p[3].trim(), address.toString().trim(), moq
            );
        } catch (Exception e) { return null; }
    }

    public int getMinimumOrderQuantity() {
        return minimumOrderQuantity;
    }
    public void setMinimumOrderQuantity(int moq) {
        this.minimumOrderQuantity = moq;
    }
}
