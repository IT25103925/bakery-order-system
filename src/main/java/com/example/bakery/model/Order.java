package com.example.bakery.model;

import java.util.ArrayList;
import java.util.List;

public class Order extends BaseEntity {

    public enum Status { PENDING, CONFIRMED, READY, DELIVERED, CANCELLED }
    public enum PaymentMethod { CASH_ON_PICKUP, ONLINE_SIMULATION }

    private int customerId;
    private String customerName;
    private List<OrderItem> items; // Composition (Lecture 04)
    private double totalAmount;
    private Status status;
    private PaymentMethod paymentMethod;
    private String orderDate;
    private String notes;

    public Order() {
        super();
        this.items = new ArrayList<>();
        this.status = Status.PENDING;
    }

    public Order(int id, int customerId, String customerName, String orderDate, PaymentMethod paymentMethod) {
        super(id);
        this.customerId = customerId;
        this.customerName = customerName;
        this.items = new ArrayList<>();
        this.status = Status.PENDING;
        this.orderDate = orderDate;
        this.paymentMethod = paymentMethod;
        this.totalAmount = 0.0;
    }

    public void addItem(OrderItem item) {
        items.add(item);
        recalculateTotal();
    }

    public void recalculateTotal() {
        totalAmount = 0;
        for (OrderItem item : items) {
            totalAmount += item.getSubtotal();
        }
    }

    // toString for CSV file storage
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (OrderItem item : items) {
            sb.append(item.getProductId()).append(":").append(item.getQuantity()).append(":").append(item.getUnitPrice()).append("|");
        }
        String itemsStr = sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
        return getId() + "," + customerId + "," + customerName + "," + totalAmount + "," +
               status.name() + "," + (paymentMethod != null ? paymentMethod.name() : "CASH_ON_PICKUP") + "," +
               orderDate + "," + (notes != null ? notes : "") + "," + itemsStr;
    }

    public static Order fromLine(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 9) return null;
        try {
            Order order = new Order();
            order.setId(Integer.parseInt(parts[0].trim()));
            order.setCustomerId(Integer.parseInt(parts[1].trim()));
            order.setCustomerName(parts[2].trim());
            order.setTotalAmount(Double.parseDouble(parts[3].trim()));
            order.setStatus(Status.valueOf(parts[4].trim()));
            order.setPaymentMethod(PaymentMethod.valueOf(parts[5].trim()));
            order.setOrderDate(parts[6].trim());
            order.setNotes(parts[7].trim());
            // Parse items
            if (parts.length > 8 && !parts[8].trim().isEmpty()) {
                String[] itemTokens = parts[8].split("\\|");
                for (String token : itemTokens) {
                    String[] ip = token.split(":");
                    if (ip.length == 3) {
                        OrderItem oi = new OrderItem();
                        oi.setProductId(Integer.parseInt(ip[0]));
                        oi.setQuantity(Integer.parseInt(ip[1]));
                        oi.setUnitPrice(Double.parseDouble(ip[2]));
                        order.getItems().add(oi);
                    }
                }
            }
            return order;
        } catch (Exception e) { return null; }
    }

    // Getters and Setters

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }
    public String getOrderDate() { return orderDate; }
    public void setOrderDate(String orderDate) { this.orderDate = orderDate; }
    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }
}
