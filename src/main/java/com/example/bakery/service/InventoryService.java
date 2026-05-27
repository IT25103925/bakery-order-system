package com.example.bakery.service;

import com.example.bakery.model.*;
import com.example.bakery.util.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private static final String SUPPLIERS_FILE = "suppliers.txt";
    private static final String INGREDIENTS_FILE = "ingredients.txt";
    private static final String PURCHASE_ORDERS_FILE = "purchase_orders.txt";

    @Autowired
    private FileStorage fileStorage;

    // SUPPLIER METHODS

    public List<Supplier> getAllSuppliers() {
        List<String> lines = fileStorage.readAll(SUPPLIERS_FILE);
        List<Supplier> suppliers = new ArrayList<>();
        for (String line : lines) {
            Supplier s = Supplier.fromLine(line);
            if (s != null) suppliers.add(s);
        }
        return suppliers;
    }

    public Supplier findSupplierById(int id) {
        for (Supplier s : getAllSuppliers()) {
            if (s.getId() == id) return s;
        }
        return null;
    }

    public boolean addLocalSupplier(String name, String email, String phone, String address, String locality) {
        try {
            List<String> lines = fileStorage.readAll(SUPPLIERS_FILE);
            int newId = FileStorage.generateId(lines);
            LocalSupplier s = new LocalSupplier(newId, name, email, phone, address, locality);
            fileStorage.appendLine(SUPPLIERS_FILE, s.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean addWholesaleSupplier(String name, String email, String phone, String address, int moq) {
        try {
            List<String> lines = fileStorage.readAll(SUPPLIERS_FILE);
            int newId = FileStorage.generateId(lines);
            WholesaleSupplier s = new WholesaleSupplier(newId, name, email, phone, address, moq);
            fileStorage.appendLine(SUPPLIERS_FILE, s.toString());
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean updateSupplierContact(int id, String email, String phone) {
        List<String> lines = fileStorage.readAll(SUPPLIERS_FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 3 && parts[0].trim().equals(String.valueOf(id))) {
                parts[2] = email;
                parts[3] = phone;
                updated.add(String.join(",", parts));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(SUPPLIERS_FILE, updated);
        return found;
    }

    public boolean deleteSupplier(int id) {
        List<String> lines = fileStorage.readAll(SUPPLIERS_FILE);
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
        if (found) fileStorage.writeAll(SUPPLIERS_FILE, updated);
        return found;
    }

    // INGREDIENT METHODS

    public List<Ingredient> getAllIngredients() {
        List<String> lines = fileStorage.readAll(INGREDIENTS_FILE);
        List<Ingredient> ingredients = new ArrayList<>();
        for (String line : lines) {
            Ingredient i = Ingredient.fromLine(line);
            if (i != null) ingredients.add(i);
        }
        return ingredients;
    }

    public List<Ingredient> getLowStockIngredients() {
        List<Ingredient> all = getAllIngredients();
        List<Ingredient> low = new ArrayList<>();
        for (Ingredient i : all) {
            if (i.isLowStock()) low.add(i);
        }
        return low;
    }

    public boolean addIngredient(String name, double qty, String unit, double reorderLevel, int supplierId) {
        try {
            List<String> lines = fileStorage.readAll(INGREDIENTS_FILE);
            int newId = FileStorage.generateId(lines);
            Supplier supplier = findSupplierById(supplierId);
            Ingredient ing = new Ingredient(newId, name, qty, unit, reorderLevel, supplierId);
            ing.setSupplierName(supplier != null ? supplier.getName() : "");
            fileStorage.appendLine(INGREDIENTS_FILE, ing.toString());
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean updateIngredientStock(int id, double newQty) {
        List<String> lines = fileStorage.readAll(INGREDIENTS_FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 2 && parts[0].trim().equals(String.valueOf(id))) {
                parts[2] = String.valueOf(newQty);
                updated.add(String.join(",", parts));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(INGREDIENTS_FILE, updated);
        return found;
    }

    public boolean deleteIngredient(int id) {
        List<String> lines = fileStorage.readAll(INGREDIENTS_FILE);
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
        if (found) fileStorage.writeAll(INGREDIENTS_FILE, updated);
        return found;
    }

    // ---- PURCHASE ORDER METHODS ----

    public List<PurchaseOrder> getAllPurchaseOrders() {
        List<String> lines = fileStorage.readAll(PURCHASE_ORDERS_FILE);
        List<PurchaseOrder> orders = new ArrayList<>();
        for (String line : lines) {
            PurchaseOrder po = PurchaseOrder.fromLine(line);
            if (po != null) orders.add(po);
        }
        return orders;
    }

    public boolean placePurchaseOrder(int supplierId, int ingredientId, double quantity, double unitPrice) {
        try {
            Supplier supplier = findSupplierById(supplierId);
            Ingredient ingredient = findIngredientById(ingredientId);
            if (supplier == null || ingredient == null) return false;

            List<String> lines = fileStorage.readAll(PURCHASE_ORDERS_FILE);
            int newId = FileStorage.generateId(lines);

            PurchaseOrder po = new PurchaseOrder(
                    newId,
                    supplierId, supplier.getName(), supplier.getSupplierType(),
                    ingredientId, ingredient.getName(), ingredient.getUnit(),
                    quantity, unitPrice, supplier.getDiscountRate()
            );
            fileStorage.appendLine(PURCHASE_ORDERS_FILE, po.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean receivePurchaseOrder(int orderId) {
        List<String> lines = fileStorage.readAll(PURCHASE_ORDERS_FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        PurchaseOrder target = null;

        for (String line : lines) {
            PurchaseOrder po = PurchaseOrder.fromLine(line);
            if (po != null && po.getId() == orderId && "PENDING".equals(po.getStatus())) {
                po.setStatus("RECEIVED");
                updated.add(po.toString());
                target = po;
                found = true;
            } else {
                updated.add(line);
            }
        }

        if (found && target != null) {
            fileStorage.writeAll(PURCHASE_ORDERS_FILE, updated);
            updateIngredientStock(target.getIngredientId(),
                    findIngredientById(target.getIngredientId()).getQuantityInStock() + target.getQuantityOrdered());
        }
        return found;
    }

    public boolean cancelPurchaseOrder(int orderId) {
        List<String> lines = fileStorage.readAll(PURCHASE_ORDERS_FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            PurchaseOrder po = PurchaseOrder.fromLine(line);
            if (po != null && po.getId() == orderId && "PENDING".equals(po.getStatus())) {
                po.setStatus("CANCELLED");
                updated.add(po.toString());
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(PURCHASE_ORDERS_FILE, updated);
        return found;
    }

    public Ingredient findIngredientById(int id) {
        for (Ingredient i : getAllIngredients()) {
            if (i.getId() == id) return i;
        }
        return null;
    }
}
