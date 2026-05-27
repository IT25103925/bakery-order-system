package com.example.bakery.model;

public abstract class Supplier extends BaseEntity {

    private String name;
    private String contactEmail;
    private String phone;
    private String address;

    public Supplier() { super(); }

    public Supplier(int id, String name, String contactEmail, String phone, String address) {
        super(id);
        this.name = name;
        this.contactEmail = contactEmail;
        this.phone = phone;
        this.address = address;
    }

    public abstract String getSupplierType();

    public abstract double getDiscountRate();

    @Override
    public String toString() {
        return getId() + "," + name + "," + contactEmail + "," + phone + "," + address + "," + getSupplierType();
    }

    public static Supplier fromLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 6) return null;

        String secondLast = p[p.length - 2].trim();
        if ("LOCAL".equals(secondLast)) return LocalSupplier.fromLine(line);
        if ("WHOLESALE".equals(secondLast)) return WholesaleSupplier.fromLine(line);


        String last = p[p.length - 1].trim();
        if ("LOCAL".equals(last)) return LocalSupplier.fromLine(line);
        return WholesaleSupplier.fromLine(line);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getContactEmail() {
        return contactEmail;
    }
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}
