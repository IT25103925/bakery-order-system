package com.example.bakery.service;

import com.example.bakery.model.*;
import com.example.bakery.util.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    private static final String FILE = "products.txt";

    @Autowired
    private FileStorage fileStorage;

    public List<Product> getAllProducts() {
        List<String> lines = fileStorage.readAll(FILE);
        List<Product> products = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length < 7) continue;
            String type = parts[6].trim();
            Product p;
            if ("PERISHABLE".equals(type)) {
                p = PerishableProduct.fromLine(line);
            } else {
                p = NonPerishableProduct.fromLine(line);
            }
            if (p != null) products.add(p);
        }
        return products;
    }

    public List<Product> getAvailableProducts() {
        List<Product> all = getAllProducts();
        List<Product> available = new ArrayList<>();
        for (Product p : all) {
            if (p.isAvailable()) available.add(p);
        }
        return available;
    }

    public List<Product> searchProducts(String keyword) {
        List<Product> all = getAllProducts();
        List<Product> result = new ArrayList<>();
        String kw = keyword.toLowerCase();
        for (Product p : all) {
            if (p.getName().toLowerCase().contains(kw) ||
                p.getCategory().toLowerCase().contains(kw) ||
                p.getDescription().toLowerCase().contains(kw)) {
                result.add(p);
            }
        }
        return result;
    }

    public Product findById(int id) {
        for (Product p : getAllProducts()) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public boolean addPerishableProduct(String name, String desc, double price, int qty, String category, String expiry) {
        try {
            List<String> lines = fileStorage.readAll(FILE);
            int newId = FileStorage.generateId(lines);
            PerishableProduct p = new PerishableProduct(newId, name, desc, price, qty, category, expiry);
            fileStorage.appendLine(FILE, p.toString());
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean addNonPerishableProduct(String name, String desc, double price, int qty, String category, int shelfLife) {
        try {
            List<String> lines = fileStorage.readAll(FILE);
            int newId = FileStorage.generateId(lines);
            NonPerishableProduct p = new NonPerishableProduct(newId, name, desc, price, qty, category, shelfLife);
            fileStorage.appendLine(FILE, p.toString());
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean updateProduct(int id, String name, String desc, double price, int qty) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 0 && parts[0].trim().equals(String.valueOf(id))) {
                parts[1] = name;
                parts[2] = desc;
                parts[3] = String.valueOf(price);
                parts[4] = String.valueOf(qty);
                parts[7] = String.valueOf(qty > 0); // available
                updated.add(String.join(",", parts));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }

    public boolean deleteProduct(int id) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 0 && parts[0].trim().equals(String.valueOf(id))) {
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }

    public boolean decreaseStock(int productId, int quantity) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 4 && parts[0].trim().equals(String.valueOf(productId))) {
                int current = Integer.parseInt(parts[4].trim());
                int newQty = Math.max(0, current - quantity);
                parts[4] = String.valueOf(newQty);
                parts[7] = String.valueOf(newQty > 0);
                updated.add(String.join(",", parts));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }
}
