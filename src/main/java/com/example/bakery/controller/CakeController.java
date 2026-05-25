package com.example.bakery.controller;

import com.example.bakery.model.CustomCake;
import com.example.bakery.service.CakeService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cakes")
public class CakeController {

    @Autowired
    private CakeService cakeService;

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("userId") != null;
    }

    private boolean isAdmin(HttpSession session) {
        return "ADMIN".equals(session.getAttribute("userRole"));
    }

    // Customer: cake gallery
    @GetMapping("/gallery")
    public String cakeGallery(HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        return "cake/cake-catalog";
    }

    // Customer: request form
    @GetMapping("/request")
    public String requestForm(HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        return "cake/cake-request";
    }

    @PostMapping("/request/birthday")
    public String submitBirthday(@RequestParam String flavor, @RequestParam int tiers,
                                  @RequestParam String message, @RequestParam String deliveryDate,
                                  @RequestParam String theme, @RequestParam String ageDecoration,
                                  @RequestParam(defaultValue="1.0") double weightKg,
                                  HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        int customerId = (int) session.getAttribute("userId");
        String customerName = (String) session.getAttribute("username");
        int cakeId = cakeService.submitBirthdayBookingGetId(customerId, customerName, flavor, tiers, message, deliveryDate, theme, ageDecoration, weightKg);
        if (cakeId < 0) {
            model.addAttribute("error", "Error submitting booking. Please try again.");
            return "cake/cake-request";
        }
        return "redirect:/cakes/bill/" + cakeId;
    }

    @PostMapping("/request/wedding")
    public String submitWedding(@RequestParam String flavor, @RequestParam int tiers,
                                 @RequestParam String message, @RequestParam String deliveryDate,
                                 @RequestParam String couplesNames, @RequestParam String frostingStyle,
                                 @RequestParam(defaultValue="4.0") double weightKg,
                                 HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        int customerId = (int) session.getAttribute("userId");
        String customerName = (String) session.getAttribute("username");
        int cakeId = cakeService.submitWeddingBookingGetId(customerId, customerName, flavor, tiers, message, deliveryDate, couplesNames, frostingStyle, weightKg);
        if (cakeId < 0) {
            model.addAttribute("error", "Error submitting booking. Please try again.");
            return "cake/cake-request";
        }
        return "redirect:/cakes/bill/" + cakeId;
    }

    // Customer: booking status
    @GetMapping("/status")
    public String myBookings(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        int customerId = (int) session.getAttribute("userId");
        model.addAttribute("bookings", cakeService.getBookingsByCustomer(customerId));
        return "cake/booking-status";
    }

    // Customer: edit design
    @GetMapping("/edit/{id}")
    public String editBooking(@PathVariable int id, HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        CustomCake cake = cakeService.findById(id);
        model.addAttribute("cake", cake);
        return "cake/cake-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBooking(@PathVariable int id, @RequestParam String flavor,
                                 @RequestParam int tiers, @RequestParam String message,
                                 HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        cakeService.updateDesign(id, flavor, tiers, message);
        return "redirect:/cakes/status";
    }

    // Customer: cancel booking
    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable int id, HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        cakeService.updateBookingStatus(id, CustomCake.BookingStatus.CANCELLED);
        return "redirect:/cakes/status";
    }

    // Admin: admin panel
    @GetMapping("/admin")
    public String adminPanel(HttpSession session, Model model) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        model.addAttribute("bookings", cakeService.getAllBookings());
        return "cake/admin-bookings";
    }

    @PostMapping("/admin/status/{id}")
    public String updateStatus(@PathVariable int id, @RequestParam String status, HttpSession session) {
        if (!isAdmin(session)) return "redirect:/admin-login";
        cakeService.updateBookingStatus(id, CustomCake.BookingStatus.valueOf(status));
        return "redirect:/cakes/admin";
    }

    // Show bill page after booking submission
    @GetMapping("/bill/{id}")
    public String showBill(@PathVariable int id, HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        CustomCake cake = cakeService.findById(id);
        if (cake == null) return "redirect:/cakes/request";
        model.addAttribute("cake", cake);
        return "cake/cake-bill";
    }

    // Process payment and show confirmation
    @PostMapping("/payment")
    public String processPayment(@RequestParam int cakeId,
                                  @RequestParam String paymentMethod,
                                  @RequestParam(required = false) String cardNumber,
                                  HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/customer-login";
        CustomCake cake = cakeService.findById(cakeId);
        if (cake == null) return "redirect:/cakes/request";

        // Save payment method on the booking
        cakeService.savePaymentMethod(cakeId, paymentMethod);

        model.addAttribute("cake", cake);
        model.addAttribute("paymentMethod", paymentMethod);
        return "cake/payment-confirm";
    }

}
