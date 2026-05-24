package com.example.bakery.model;

public class LocalSupplier extends Supplier {

    private String locality;

    public LocalSupplier() { super(); }

    public LocalSupplier(int id, String name, String email, String phone, String address, String locality) {
        super(id, name, email, phone, address);
        this.locality = locality;
    }

    @Override
    public String getSupplierType() { return "LOCAL"; }

    @Override
    public double getDiscountRate() { return 0.05; }

    @Override
    public String toString() {
        return super.toString() + "," + locality;
    }

    public static LocalSupplier fromLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 6) return null;
        try {

            int last = p.length - 1;
            String locality = p[last].trim();

            StringBuilder address = new StringBuilder();
            for (int i = 4; i <= last - 2; i++) {
                if (i > 4) address.append(",");
                address.append(p[i]);
            }
            return new LocalSupplier(
                    Integer.parseInt(p[0].trim()), p[1].trim(), p[2].trim(),
                    p[3].trim(), address.toString().trim(), locality
            );
        } catch (Exception e) { return null; }
    }

    public String getLocality() {
        return locality;
    }
    public void setLocality(String locality) {
        this.locality = locality;
    }
}
