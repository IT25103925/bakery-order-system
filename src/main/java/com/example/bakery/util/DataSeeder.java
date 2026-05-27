package com.example.bakery.util;

import com.example.bakery.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DataSeeder - Seeds initial demo data on first startup.
 * Implements CommandLineRunner - runs after Spring context loads.
 * Uses static method FileStorage.generateId() (Lecture 05)
 * Exception Handling with try-catch (Lecture 06)
 */
@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private FileStorage fileStorage;

    @Override
    public void run(String... args) {
        seedUsers();
        seedProducts();
        seedSuppliers();
        seedIngredients();
        System.out.println("✅ SweetBite Bakery - Data seeding complete.");
    }

    private void seedUsers() {
        List<String> lines = fileStorage.readAll("users.txt");
        if (!lines.isEmpty()) return; // Already seeded

        // Staff account (admin)
        Admin admin = new Admin(1, "admin", "admin123", "admin@sweetbite.lk", "0112345678", "Manager", 85000);
        fileStorage.appendLine("users.txt", admin.toString());

        Admin baker = new Admin(2, "baker01", "baker123", "baker@sweetbite.lk", "0113456789", "Head Baker", 55000);
        fileStorage.appendLine("users.txt", baker.toString());

        // Customer accounts
        Customer c1 = new Customer(3, "john", "john123", "john@example.com", "0771234567", "45 Main St, Colombo", 120);
        fileStorage.appendLine("users.txt", c1.toString());

        Customer c2 = new Customer(4, "sarah", "sarah123", "sarah@example.com", "0779876543", "12 Park Rd, Kandy", 80);
        fileStorage.appendLine("users.txt", c2.toString());

        Customer c3 = new Customer(5, "priya", "priya123", "priya@example.com", "0762345678", "78 Lake View, Galle", 0);
        fileStorage.appendLine("users.txt", c3.toString());
    }

    private void seedProducts() {
        List<String> lines = fileStorage.readAll("products.txt");
        if (!lines.isEmpty()) return;

        // Perishable products
        PerishableProduct p1 = new PerishableProduct(1, "Sourdough Bread", "Traditional sourdough with crispy crust", 350.0, 20, "BREAD", "2025-12-31");
        fileStorage.appendLine("products.txt", p1.toString());

        PerishableProduct p2 = new PerishableProduct(2, "Butter Croissant", "Flaky French-style croissant with real butter", 180.0, 30, "PASTRY", "2025-12-31");
        fileStorage.appendLine("products.txt", p2.toString());

        PerishableProduct p3 = new PerishableProduct(3, "Black Forest Cake Slice", "Rich chocolate cake with cherries and cream", 450.0, 15, "CAKE", "2025-12-31");
        fileStorage.appendLine("products.txt", p3.toString());

        PerishableProduct p4 = new PerishableProduct(4, "Cinnamon Roll", "Warm cinnamon roll with cream cheese glaze", 220.0, 25, "PASTRY", "2025-12-31");
        fileStorage.appendLine("products.txt", p4.toString());

        PerishableProduct p5 = new PerishableProduct(5, "Whole Wheat Bread", "Healthy whole wheat loaf, freshly baked", 280.0, 18, "BREAD", "2025-12-31");
        fileStorage.appendLine("products.txt", p5.toString());

        // Non-perishable products
        NonPerishableProduct np1 = new NonPerishableProduct(6, "Chocolate Chip Cookies (6 pack)", "Classic chocolate chip cookies, crispy outside soft inside", 320.0, 50, "COOKIE", 90);
        fileStorage.appendLine("products.txt", np1.toString());

        NonPerishableProduct np2 = new NonPerishableProduct(7, "Shortbread Tin", "Traditional Scottish shortbread in a gift tin", 850.0, 25, "COOKIE", 180);
        fileStorage.appendLine("products.txt", np2.toString());

        NonPerishableProduct np3 = new NonPerishableProduct(8, "Brownie Box (4 pack)", "Fudgy chocolate brownies, individually wrapped", 480.0, 35, "CAKE", 60);
        fileStorage.appendLine("products.txt", np3.toString());

        NonPerishableProduct np4 = new NonPerishableProduct(9, "Almond Biscotti (8 pack)", "Italian-style almond biscotti, perfect with coffee", 390.0, 40, "COOKIE", 120);
        fileStorage.appendLine("products.txt", np4.toString());

        PerishableProduct p6 = new PerishableProduct(10, "Vanilla Cupcake", "Moist vanilla cupcake with buttercream frosting", 150.0, 24, "CAKE", "2025-12-31");
        fileStorage.appendLine("products.txt", p6.toString());
    }

    private void seedSuppliers() {
        List<String> lines = fileStorage.readAll("suppliers.txt");
        if (!lines.isEmpty()) return;

        LocalSupplier ls1 = new LocalSupplier(1, "Colombo Fresh Dairy", "dairy@cfresh.lk", "0112223344", "123 Maradana Rd, Colombo", "Colombo");
        fileStorage.appendLine("suppliers.txt", ls1.toString());

        LocalSupplier ls2 = new LocalSupplier(2, "Kandy Hill Eggs", "eggs@kandyhill.lk", "0812345678", "56 Peradeniya Rd, Kandy", "Kandy");
        fileStorage.appendLine("suppliers.txt", ls2.toString());

        WholesaleSupplier ws1 = new WholesaleSupplier(3, "Prima Flour Mills", "orders@prima.lk", "0113456789", "Prima Industrial Zone, Kelaniya", 100);
        fileStorage.appendLine("suppliers.txt", ws1.toString());

        WholesaleSupplier ws2 = new WholesaleSupplier(4, "Ceylon Sugar Corp", "supply@ceylonsugar.lk", "0112345678", "20 Industrial Estate, Ekala", 200);
        fileStorage.appendLine("suppliers.txt", ws2.toString());
    }

    private void seedIngredients() {
        List<String> lines = fileStorage.readAll("ingredients.txt");
        if (!lines.isEmpty()) return;

        Ingredient i1 = new Ingredient(1, "All-purpose Flour", 50.0, "kg", 10.0, 3);
        i1.setSupplierName("Prima Flour Mills");
        fileStorage.appendLine("ingredients.txt", i1.toString());

        Ingredient i2 = new Ingredient(2, "Butter", 15.0, "kg", 5.0, 1);
        i2.setSupplierName("Colombo Fresh Dairy");
        fileStorage.appendLine("ingredients.txt", i2.toString());

        Ingredient i3 = new Ingredient(3, "White Sugar", 30.0, "kg", 8.0, 4);
        i3.setSupplierName("Ceylon Sugar Corp");
        fileStorage.appendLine("ingredients.txt", i3.toString());

        Ingredient i4 = new Ingredient(4, "Eggs", 4.5, "dozen", 2.0, 2);
        i4.setSupplierName("Kandy Hill Eggs");
        fileStorage.appendLine("ingredients.txt", i4.toString());

        Ingredient i5 = new Ingredient(5, "Whole Milk", 8.0, "litre", 3.0, 1);
        i5.setSupplierName("Colombo Fresh Dairy");
        fileStorage.appendLine("ingredients.txt", i5.toString());

        Ingredient i6 = new Ingredient(6, "Cocoa Powder", 3.0, "kg", 2.0, 3);
        i6.setSupplierName("Prima Flour Mills");
        fileStorage.appendLine("ingredients.txt", i6.toString());

        Ingredient i7 = new Ingredient(7, "Yeast", 0.8, "kg", 1.0, 3); // Low stock!
        i7.setSupplierName("Prima Flour Mills");
        fileStorage.appendLine("ingredients.txt", i7.toString());

        Ingredient i8 = new Ingredient(8, "Vanilla Extract", 0.5, "litre", 0.5, 1);
        i8.setSupplierName("Colombo Fresh Dairy");
        fileStorage.appendLine("ingredients.txt", i8.toString());
    }
}
