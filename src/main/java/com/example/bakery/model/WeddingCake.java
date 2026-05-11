package com.example.bakery.model;

public class WeddingCake extends CustomCake {

    private String couplesNames;
    private String weddingDate;
    private String frostingStyle;

    public WeddingCake() { super(); setCakeType("WEDDING"); }

    public WeddingCake(int id, int customerId, String customerName, String flavor,
                        int tiers, String message, String deliveryDate,
                        String couplesNames, String frostingStyle) {
        super(id, customerId, customerName, flavor, tiers, message, deliveryDate, "WEDDING");
        this.couplesNames = couplesNames;
        this.weddingDate = deliveryDate;
        this.frostingStyle = frostingStyle;
        setPrice(calculateBasePrice());
    }

    // Wedding cakes are premium - higher base price (Lecture 04 - Method Overriding)
    @Override
    public double calculateBasePrice() {
        return getTiers() * 3000.0 + 2000.0;
    }

    public String getCouplesNames() { return couplesNames; }
    public void setCouplesNames(String couplesNames) { this.couplesNames = couplesNames; }
    public String getWeddingDate() { return weddingDate; }
    public void setWeddingDate(String weddingDate) { this.weddingDate = weddingDate; }
    public String getFrostingStyle() { return frostingStyle; }
    public void setFrostingStyle(String frostingStyle) { this.frostingStyle = frostingStyle; }
}
