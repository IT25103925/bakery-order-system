package com.example.bakery.model;

public class Ingredient extends BaseEntity {
    private String name;
    private double quantityInStock;
    private String unit;
    private double reorderLevel;
    private int supplierId;
    private String supplierName;

    public Ingredient() {
        super();
    }

    public Ingredient(int id, String name, double quantityInStock, String unit, double reorderLevel, int supplierId) {
        super(id);
        this.name = name;
        this.quantityInStock = quantityInStock;
        this.unit = unit;
        this.reorderLevel = reorderLevel;
        this.supplierId = supplierId;
    }

    public boolean isLowStock() {
        return quantityInStock <= reorderLevel;
    }

    @Override
    public String toString() {
        return getId() + "," + name + "," + quantityInStock + "," + unit + "," + reorderLevel + "," + supplierId + "," + supplierName;
    }

    public static Ingredient fromLine(String line) {
        String[] p = line.split(",", -1);

        if (p.length < 6) return null;
        try {
            Ingredient ing = new Ingredient(
                    Integer.parseInt(p[0].trim()), p[1].trim(),
                    Double.parseDouble(p[2].trim()), p[3].trim(),
                    Double.parseDouble(p[4].trim()), Integer.parseInt(p[5].trim())
            );
            if (p.length > 6) ing.setSupplierName(p[6].trim());
            return ing;
        } catch (NumberFormatException e) { return null; }
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public double getQuantityInStock() {
        return quantityInStock;
    }
    public void setQuantityInStock(double quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public double getReorderLevel() {
        return reorderLevel;
    }
    public void setReorderLevel(double reorderLevel) {
        this.reorderLevel = reorderLevel;
    }
    public int getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(int supplierId) {
        this.supplierId = supplierId;
    }
    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
