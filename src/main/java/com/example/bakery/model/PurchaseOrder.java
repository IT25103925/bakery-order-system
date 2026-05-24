package com.example.bakery.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class PurchaseOrder extends BaseEntity {

    private int supplierId;
    private String supplierName;
    private String supplierType;
    private int ingredientId;
    private String ingredientName;
    private String unit;
    private double quantityOrdered;
    private double unitPrice;
    private double discountRate;
    private double grossTotal;
    private double discountAmount;
    private double netTotal;
    private String status;
    private String orderDate;

    private static final DateTimeFormatter FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public PurchaseOrder() {
        super();
    }

    public PurchaseOrder(int id, int supplierId, String supplierName, String supplierType,
                         int ingredientId, String ingredientName, String unit,
                         double quantityOrdered, double unitPrice, double discountRate) {
        super(id);
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.supplierType = supplierType;
        this.ingredientId = ingredientId;
        this.ingredientName = ingredientName;
        this.unit = unit;
        this.quantityOrdered = quantityOrdered;
        this.unitPrice = unitPrice;
        this.discountRate = discountRate;
        this.grossTotal = quantityOrdered * unitPrice;
        this.discountAmount = this.grossTotal * discountRate;
        this.netTotal = this.grossTotal - this.discountAmount;
        this.status = "PENDING";
        this.orderDate = LocalDate.now().format(FMT);
    }

    public void recalculate() {
        this.grossTotal = this.quantityOrdered * this.unitPrice;
        this.discountAmount = this.grossTotal * this.discountRate;
        this.netTotal = this.grossTotal - this.discountAmount;
    }

    @Override
    public String toString() {
        return getId() + "," + supplierId + "," + supplierName + "," + supplierType + ","
                + ingredientId + "," + ingredientName + "," + unit + ","
                + quantityOrdered + "," + unitPrice + "," + discountRate + ","
                + grossTotal + "," + discountAmount + "," + netTotal + ","
                + status + "," + orderDate;
    }

    public static PurchaseOrder fromLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 15) return null;
        try {
            PurchaseOrder po = new PurchaseOrder();
            po.setId(Integer.parseInt(p[0].trim()));
            po.setSupplierId(Integer.parseInt(p[1].trim()));
            po.setSupplierName(p[2].trim());
            po.setSupplierType(p[3].trim());
            po.setIngredientId(Integer.parseInt(p[4].trim()));
            po.setIngredientName(p[5].trim());
            po.setUnit(p[6].trim());
            po.setQuantityOrdered(Double.parseDouble(p[7].trim()));
            po.setUnitPrice(Double.parseDouble(p[8].trim()));
            po.setDiscountRate(Double.parseDouble(p[9].trim()));
            po.setGrossTotal(Double.parseDouble(p[10].trim()));
            po.setDiscountAmount(Double.parseDouble(p[11].trim()));
            po.setNetTotal(Double.parseDouble(p[12].trim()));
            po.setStatus(p[13].trim());
            po.setOrderDate(p[14].trim());
            return po;

        } catch (NumberFormatException e) {
            return null;
        }
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
    public String getSupplierType() {
        return supplierType;
    }
    public void setSupplierType(String supplierType) {
        this.supplierType = supplierType;
    }
    public int getIngredientId() {
        return ingredientId;
    }
    public void setIngredientId(int ingredientId) {
        this.ingredientId = ingredientId;
    }
    public String getIngredientName() {
        return ingredientName;
    }
    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    public double getQuantityOrdered() {
        return quantityOrdered;
    }
    public void setQuantityOrdered(double quantityOrdered) {
        this.quantityOrdered = quantityOrdered;
    }
    public double getUnitPrice() {
        return unitPrice;
    }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
    public double getDiscountRate() {
        return discountRate;
    }
    public void setDiscountRate(double discountRate) {
        this.discountRate = discountRate;
    }
    public double getGrossTotal() {
        return grossTotal;
    }
    public void setGrossTotal(double grossTotal) {
        this.grossTotal = grossTotal;
    }
    public double getDiscountAmount() {
        return discountAmount;
    }
    public void setDiscountAmount(double discountAmount) {
        this.discountAmount = discountAmount;
    }
    public double getNetTotal() {
        return netTotal;
    }
    public void setNetTotal(double netTotal) {
        this.netTotal = netTotal;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
    public String getOrderDate() {
        return orderDate;
    }
    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }
}
