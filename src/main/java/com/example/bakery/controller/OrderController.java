package com.example.bakery.controller;

import com.example.bakery.model.*;
import com.example.bakery.service.OrderService;
import com.example.bakery.service.ProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderController - Member 03: Shopping Cart and Order Processing.
 */
@Controller
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    @Autowired
    private ProductService productService;

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }

    // View cart (stored in session)
    @GetMapping("/cart")
    public String viewCart(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        List<OrderItem> cart = getCart(session);
        double total = cart.stream().mapToDouble(OrderItem::getSubtotal).sum();
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "order/cart";
    }

    // Add item to cart
    @PostMapping("/cart/add")
    public String addToCart(@RequestParam int productId, @RequestParam int quantity,
                             HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        Product product = productService.findById(productId);
        if (product != null && product.isAvailable()) {
            List<OrderItem> cart = getCart(session);
            boolean found = false;
            for (OrderItem item : cart) {
                if (item.getProductId() == productId) {
                    item.setQuantity(item.getQuantity() + quantity);
                    found = true;
                    break;
                }
            }
            if (!found) {
                cart.add(new OrderItem(productId, product.getName(), quantity, product.getPrice()));
            }
            session.setAttribute("cart", cart);
        }
        return "redirect:/orders/cart";
    }

    // Remove from cart
    @PostMapping("/cart/remove/{productId}")
    public String removeFromCart(@PathVariable int productId, HttpSession session) {
        List<OrderItem> cart = getCart(session);
        cart.removeIf(item -> item.getProductId() == productId);
        session.setAttribute("cart", cart);
        return "redirect:/orders/cart";
    }

    // Checkout page
    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        List<OrderItem> cart = getCart(session);
        if (cart.isEmpty()) return "redirect:/orders/cart";
        double total = cart.stream().mapToDouble(OrderItem::getSubtotal).sum();
        model.addAttribute("cart", cart);
        model.addAttribute("total", total);
        return "order/checkout";
    }

    // Place order
    @PostMapping("/place")
    public String placeOrder(@RequestParam String paymentMethod,
                              @RequestParam(required = false) String notes,
                              HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        int customerId = (int) session.getAttribute("userId");
        String customerName = (String) session.getAttribute("username");
        List<OrderItem> cart = getCart(session);
        if (cart.isEmpty()) return "redirect:/orders/cart";

        Order.PaymentMethod pm = Order.PaymentMethod.valueOf(paymentMethod);
        Order order = orderService.createOrder(customerId, customerName, cart, pm, notes);
        session.removeAttribute("cart");
        if (order != null) {
            model.addAttribute("order", order);
            return "order/order-confirmation";
        }
        return "redirect:/orders/cart";
    }

    // Order history
    @GetMapping("/history")
    public String orderHistory(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        int customerId = (int) session.getAttribute("userId");
        model.addAttribute("orders", orderService.getOrdersByCustomer(customerId));
        return "order/order-history";
    }

    // Cancel order
    @PostMapping("/cancel/{id}")
    public String cancelOrder(@PathVariable int id, HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        orderService.cancelOrder(id);
        return "redirect:/orders/history";
    }

    // Admin: all orders
    @GetMapping("/admin/all")
    public String allOrders(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("orders", orderService.getAllOrders());
        return "order/admin-orders";
    }

    // Admin: update order status
    @PostMapping("/admin/status/{id}")
    public String updateStatus(@PathVariable int id, @RequestParam String status, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        orderService.updateStatus(id, Order.Status.valueOf(status));
        return "redirect:/orders/admin/all";
    }

    @SuppressWarnings("unchecked")
    private List<OrderItem> getCart(HttpSession session) {
        List<OrderItem> cart = (List<OrderItem>) session.getAttribute("cart");
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute("cart", cart);
        }
        return cart;
    }
}
