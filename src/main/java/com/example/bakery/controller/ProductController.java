package com.example.bakery.controller;

import com.example.bakery.model.Product;
import com.example.bakery.service.ProductService;
import com.example.bakery.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * ProductController - Member 02: Bakery Product Catalog.
 */
@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @Autowired
    private ReviewService reviewService;

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }

    // Customer: view all products
    @GetMapping
    public String listProducts(HttpSession session, Model model,
                                @RequestParam(required = false) String search) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        List<Product> products;
        if (search != null && !search.isBlank()) {
            products = productService.searchProducts(search);
            model.addAttribute("search", search);
        } else {
            products = productService.getAvailableProducts();
        }
        model.addAttribute("products", products);
        return "product/product-list";
    }

    // Product detail page
    @GetMapping("/{id}")
    public String productDetail(@PathVariable int id, HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        Product product = productService.findById(id);
        if (product == null) return "redirect:/products";
        model.addAttribute("product", product);
        model.addAttribute("reviews", reviewService.getReviewsByProduct(id));
        model.addAttribute("avgRating", reviewService.getAverageRating(id));
        return "product/product-detail";
    }

    // Admin: product management panel
    @GetMapping("/manage")
    public String manageProducts(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("products", productService.getAllProducts());
        return "product/product-manage";
    }

    // Admin: add product form
    @GetMapping("/add")
    public String addProductPage(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        return "product/product-add";
    }

    @PostMapping("/add")
    public String addProduct(@RequestParam String name, @RequestParam String description,
                              @RequestParam double price, @RequestParam int quantity,
                              @RequestParam String category, @RequestParam String type,
                              @RequestParam(required = false) String expiryDate,
                              @RequestParam(required = false, defaultValue = "30") int shelfLifeDays,
                              HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        boolean ok;
        if ("PERISHABLE".equals(type)) {
            ok = productService.addPerishableProduct(name, description, price, quantity, category, expiryDate);
        } else {
            ok = productService.addNonPerishableProduct(name, description, price, quantity, category, shelfLifeDays);
        }
        model.addAttribute("success", ok ? "Product added successfully!" : "Error adding product.");
        model.addAttribute("products", productService.getAllProducts());
        return "product/product-manage";
    }

    // Admin: update product
    @PostMapping("/update/{id}")
    public String updateProduct(@PathVariable int id, @RequestParam String name,
                                 @RequestParam String description, @RequestParam double price,
                                 @RequestParam int quantity, HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        productService.updateProduct(id, name, description, price, quantity);
        return "redirect:/products/manage";
    }

    // Admin: delete product
    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable int id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        productService.deleteProduct(id);
        return "redirect:/products/manage";
    }
}
