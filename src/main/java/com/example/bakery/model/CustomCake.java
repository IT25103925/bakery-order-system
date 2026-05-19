package com.example.bakery.model;

public class CustomCake {

    public enum BookingStatus { SUBMITTED, IN_PROGRESS, READY, DELIVERED, CANCELLED }

    private int id;
    private int customerId;
    private String customerName;
    private String flavor;
    private int tiers;
    private String message;
    private String deliveryDate;
    private double price;
    private BookingStatus status;
    private String specialNotes;
    private String cakeType; // "BIRTHDAY" or "WEDDING"

    public CustomCake() {
        this.status = BookingStatus.SUBMITTED;
    }

    public CustomCake(int id, int customerId, String customerName, String flavor,
                       int tiers, String message, String deliveryDate, String cakeType) {
        this.id = id;
        this.customerId = customerId;
        this.customerName = customerName;
        this.flavor = flavor;
        this.tiers = tiers;
        this.message = message;
        this.deliveryDate = deliveryDate;
        this.cakeType = cakeType;
        this.status = BookingStatus.SUBMITTED;
        this.price = calculateBasePrice();
    }

    // Overridable method - subclasses can override (Lecture 04)
    public double calculateBasePrice() {
        return tiers * 1500.0 + 500.0; // Base pricing
    }

    @Override
    public String toString() {
        return id + "," + customerId + "," + customerName + "," + flavor + "," + tiers + "," +
               message + "," + deliveryDate + "," + price + "," + status.name() + "," +
               cakeType + "," + (specialNotes != null ? specialNotes : "");
    }

    public static CustomCake fromLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 10) return null;
        try {
            CustomCake cake = new CustomCake();
            cake.setId(Integer.parseInt(p[0].trim()));
            cake.setCustomerId(Integer.parseInt(p[1].trim()));
            cake.setCustomerName(p[2].trim());
            cake.setFlavor(p[3].trim());
            cake.setTiers(Integer.parseInt(p[4].trim()));
            cake.setMessage(p[5].trim());
            cake.setDeliveryDate(p[6].trim());
            cake.setPrice(Double.parseDouble(p[7].trim()));
            cake.setStatus(BookingStatus.valueOf(p[8].trim()));
            cake.setCakeType(p[9].trim());
            if (p.length > 10) cake.setSpecialNotes(p[10].trim());
            return cake;
        } catch (Exception e) { return null; }
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getFlavor() { return flavor; }
    public void setFlavor(String flavor) { this.flavor = flavor; }
    public int getTiers() { return tiers; }
    public void setTiers(int tiers) { this.tiers = tiers; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    public String getSpecialNotes() { return specialNotes; }
    public void setSpecialNotes(String specialNotes) { this.specialNotes = specialNotes; }
    public String getCakeType() { return cakeType; }
    public void setCakeType(String cakeType) { this.cakeType = cakeType; }
}
