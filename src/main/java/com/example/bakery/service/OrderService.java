package com.example.bakery.service;

import com.example.bakery.model.*;
import com.example.bakery.util.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderService - Business logic for Member 03: Order Processing System.
 * OOP: Uses Abstraction (PaymentMethod enum abstracts payment types).
 * ArrayList with Generics (Lecture 08), Exception Handling (Lecture 06)
 */
@Service
public class OrderService {

    private static final String FILE = "orders.txt";

    @Autowired
    private FileStorage fileStorage;
    @Autowired
    private ProductService productService;

    public List<Order> getAllOrders() {
        List<String> lines = fileStorage.readAll(FILE);
        List<Order> orders = new ArrayList<>();
        for (String line : lines) {
            Order o = Order.fromLine(line);
            if (o != null) orders.add(o);
        }
        return orders;
    }

    public List<Order> getOrdersByCustomer(int customerId) {
        List<Order> all = getAllOrders();
        List<Order> result = new ArrayList<>();
        for (Order o : all) {
            if (o.getCustomerId() == customerId) result.add(o);
        }
        return result;
    }

    public Order findById(int id) {
        for (Order o : getAllOrders()) {
            if (o.getId() == id) return o;
        }
        return null;
    }

    public Order createOrder(int customerId, String customerName,
                              List<OrderItem> items, Order.PaymentMethod paymentMethod, String notes) {
        try {
            List<String> lines = fileStorage.readAll(FILE);
            int newId = FileStorage.generateId(lines);
            Order order = new Order(newId, customerId, customerName,
                    LocalDate.now().toString(), paymentMethod);
            order.setNotes(notes);
            for (OrderItem item : items) {
                order.addItem(item);
                productService.decreaseStock(item.getProductId(), item.getQuantity());
            }
            fileStorage.appendLine(FILE, order.toString());
            return order;
        } catch (Exception e) {
            System.err.println("Order creation error: " + e.getMessage());
            return null;
        }
    }

    public boolean updateStatus(int orderId, Order.Status newStatus) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 4 && parts[0].trim().equals(String.valueOf(orderId))) {
                parts[4] = newStatus.name();
                updated.add(String.join(",", parts));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }

    public boolean cancelOrder(int orderId) {
        return updateStatus(orderId, Order.Status.CANCELLED);
    }

    public boolean deleteOrder(int orderId) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 0 && parts[0].trim().equals(String.valueOf(orderId))) {
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }
}
