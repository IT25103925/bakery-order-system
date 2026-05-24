package com.example.bakery.controller;

import com.example.bakery.service.InventoryService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }

    // Supplier directory

    @GetMapping("/suppliers")
    public String supplierDirectory(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("suppliers", inventoryService.getAllSuppliers());
        return "inventory/supplier-directory";
    }

    @GetMapping("/suppliers/add")
    public String addSupplierPage(HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        return "inventory/supplier-add";
    }

    @PostMapping("/suppliers/add")
    public String addSupplier(@RequestParam String name, @RequestParam String email,
                              @RequestParam String phone, @RequestParam String address,
                              @RequestParam String type,
                              @RequestParam(required = false) String locality,
                              @RequestParam(required = false, defaultValue = "50") int moq,
                              HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        boolean ok;
        if ("LOCAL".equals(type)) {
            ok = inventoryService.addLocalSupplier(name, email, phone, address, locality != null ? locality : "");
        } else {
            ok = inventoryService.addWholesaleSupplier(name, email, phone, address, moq);
        }
        if (ok) return "redirect:/inventory/suppliers";
        model.addAttribute("error", "Error adding supplier.");
        return "inventory/supplier-add";
    }

    @PostMapping("/suppliers/update/{id}")
    public String updateSupplier(@PathVariable int id, @RequestParam String email,
                                 @RequestParam String phone, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        inventoryService.updateSupplierContact(id, email, phone);
        return "redirect:/inventory/suppliers";
    }

    @PostMapping("/suppliers/delete/{id}")
    public String deleteSupplier(@PathVariable int id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        inventoryService.deleteSupplier(id);
        return "redirect:/inventory/suppliers";
    }

    // Inventory dashboard
    @GetMapping("/dashboard")
    public String inventoryDashboard(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("ingredients", inventoryService.getAllIngredients());
        model.addAttribute("lowStock", inventoryService.getLowStockIngredients());
        model.addAttribute("suppliers", inventoryService.getAllSuppliers());
        return "inventory/inventory-dashboard";
    }

    // Add ingredient
    @GetMapping("/ingredients/add")
    public String addIngredientPage(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("suppliers", inventoryService.getAllSuppliers());
        return "inventory/ingredient-add";
    }

    @PostMapping("/ingredients/add")
    public String addIngredient(@RequestParam String name, @RequestParam double quantity,
                                @RequestParam String unit, @RequestParam double reorderLevel,
                                @RequestParam int supplierId, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        inventoryService.addIngredient(name, quantity, unit, reorderLevel, supplierId);
        return "redirect:/inventory/dashboard";
    }

    // Quick Order - place orders for all selected low stock ingredients
    @PostMapping("/ingredients/quick-order")
    public String quickOrder(@RequestParam int supplierId,
                             @RequestParam(value = "selectedIds", required = false) List<Integer> selectedIds,
                             @RequestParam Map<String, String> allParams,
                             HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        if (selectedIds == null || selectedIds.isEmpty()) {
            return "redirect:/inventory/dashboard";
        }
        int successCount = 0;
        for (int ingId : selectedIds) {
            String qtyKey = "qty_" + ingId;
            String priceKey = "price_" + ingId;
            try {
                double qty = Double.parseDouble(allParams.getOrDefault(qtyKey, "1"));
                double price = Double.parseDouble(allParams.getOrDefault(priceKey, "100"));
                boolean ok = inventoryService.placePurchaseOrder(supplierId, ingId, qty, price);
                if (ok) successCount++;
            } catch (NumberFormatException ignored) {}
        }
        return "redirect:/inventory/purchase-orders";
    }

    @PostMapping("/ingredients/update/{id}")
    public String updateStock(@PathVariable int id, @RequestParam double quantity, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        inventoryService.updateIngredientStock(id, quantity);
        return "redirect:/inventory/dashboard";
    }

    @PostMapping("/ingredients/delete/{id}")
    public String deleteIngredient(@PathVariable int id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        inventoryService.deleteIngredient(id);
        return "redirect:/inventory/dashboard";
    }

    //PURCHASE ORDER ENDPOINTS

    @GetMapping("/purchase-orders")
    public String purchaseOrders(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("purchaseOrders", inventoryService.getAllPurchaseOrders());
        return "inventory/purchase-orders";
    }

    @GetMapping("/purchase-orders/place")
    public String placePurchaseOrderPage(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("suppliers", inventoryService.getAllSuppliers());
        model.addAttribute("ingredients", inventoryService.getAllIngredients());
        return "inventory/purchase-order-place";
    }

    @PostMapping("/purchase-orders/place")
    public String placePurchaseOrder(@RequestParam int supplierId,
                                     @RequestParam int ingredientId,
                                     @RequestParam double quantity,
                                     @RequestParam double unitPrice,
                                     HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        boolean ok = inventoryService.placePurchaseOrder(supplierId, ingredientId, quantity, unitPrice);
        if (ok) return "redirect:/inventory/purchase-orders";
        model.addAttribute("error", "Order place කිරීමේ error එකක් ඇත.");
        model.addAttribute("suppliers", inventoryService.getAllSuppliers());
        model.addAttribute("ingredients", inventoryService.getAllIngredients());
        return "inventory/purchase-order-place";
    }

    @PostMapping("/purchase-orders/receive/{id}")
    public String receivePurchaseOrder(@PathVariable int id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        inventoryService.receivePurchaseOrder(id);
        return "redirect:/inventory/purchase-orders";
    }

    @PostMapping("/purchase-orders/cancel/{id}")
    public String cancelPurchaseOrder(@PathVariable int id, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        inventoryService.cancelPurchaseOrder(id);
        return "redirect:/inventory/purchase-orders";
    }
}
