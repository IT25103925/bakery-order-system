package com.example.bakery.model;


public class LocalSupplier extends Supplier {

    private String locality;

    public LocalSupplier() {
        super();
    }

    public LocalSupplier(int id, String name, String email, String phone, String address, String locality) {
        super(id, name, email, phone, address);
        this.locality = locality;
    }

    @Override
    public String getSupplierType() {
        return "LOCAL";
    }

    @Override
    public double getDiscountRate() {
        return 0.05;
    }

    @Override
    public String toString() {
        return super.toString() + "," + locality;
    }

    public static LocalSupplier fromLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 6) return null;
        try {
            return new LocalSupplier(Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(),
                    p[3].trim(), p[4].trim(), p.length > 6 ? p[6].trim() : "");

        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getLocality() {
        return locality;
    }
    public void setLocality(String locality) {
        this.locality = locality;
    }
}
