package com.example.bakery.model;

public class BirthdayCake extends CustomCake {

    private String ageDecoration;
    private String theme;

    public BirthdayCake() { super(); setCakeType("BIRTHDAY"); }

    public BirthdayCake(int id, int customerId, String customerName, String flavor,
                         int tiers, String message, String deliveryDate, String theme, String ageDecoration) {
        super(id, customerId, customerName, flavor, tiers, message, deliveryDate, "BIRTHDAY");
        this.theme = theme;
        this.ageDecoration = ageDecoration;
        setPrice(calculateBasePrice());
    }

    // Method Overriding - birthday cakes have character decorations (Lecture 04)
    @Override
    public double calculateBasePrice() {
        return getTiers() * 1500.0 + 800.0; // Slightly more for decorations
    }

    public String getAgeDecoration() { return ageDecoration; }
    public void setAgeDecoration(String ageDecoration) { this.ageDecoration = ageDecoration; }
    public String getTheme() { return theme; }
    public void setTheme(String theme) { this.theme = theme; }
}
