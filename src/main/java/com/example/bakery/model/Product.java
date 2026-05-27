package com.example.bakery.model;


public abstract class Product extends BaseEntity {

    private String name;
    private String description;
    private double price;
    private int stockQuantity;
    private String category;
    private String imageUrl;
    private boolean available;

    public Product() {
        super();
    }

    public Product(int id, String name, String description, double price, int stockQuantity, String category) {
        super(id);
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.category = category;
        this.imageUrl = "";
        this.available = stockQuantity > 0;
    }

    // Abstract method - Polymorphism via method overriding (Lecture 04, 05)
    public abstract String getDisplayPrice();

    // Abstract method - each product type knows how to serialize
    public abstract String getType();

    @Override
    public String toString() {
        return getId() + "," + name + "," + description + "," + price + "," + stockQuantity + "," + category + "," + getType() + "," + available;
    }

    public boolean isAvailable() { return available && stockQuantity > 0; }
    public void setAvailable(boolean available) { this.available = available; }


    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public int getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
        this.available = stockQuantity > 0;
    }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}
