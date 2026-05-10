package com.example.bakery.model;

/**
 * Customer - Inherits from Person.
 * OOP: Inheritance (extends Person), Method Overriding (getDashboardPath).
 */
public class Customer extends Person {

    private String address;
    private int loyaltyPoints;

    public Customer() {
        super();
    }

    public Customer(int id, String username, String password, String email, String phone, String address) {
        super(id, username, password, email, phone, "CUSTOMER");
        this.address = address;
        this.loyaltyPoints = 0;
    }

    public Customer(int id, String username, String password, String email, String phone, String address, int loyaltyPoints) {
        super(id, username, password, email, phone, "CUSTOMER");
        this.address = address;
        this.loyaltyPoints = loyaltyPoints;
    }

    @Override
    public String getDashboardPath() {
        return "/customer/dashboard";
    }

    @Override
    public String toString() {
        return super.toString() + "," + address + "," + loyaltyPoints;
    }

    // Static factory method to build Customer from CSV line
    public static Customer fromLine(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 8) return null;
        try {
            return new Customer(
                Integer.parseInt(parts[0].trim()),
                parts[1].trim(), parts[2].trim(), parts[3].trim(),
                parts[4].trim(), parts[6].trim(),
                Integer.parseInt(parts[7].trim())
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public int getLoyaltyPoints() { return loyaltyPoints; }
    public void setLoyaltyPoints(int loyaltyPoints) { this.loyaltyPoints = loyaltyPoints; }
}
