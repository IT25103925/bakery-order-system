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

    // Customer: request form
    @GetMapping("/request")
    public String requestForm(HttpSession session) {
        if (session.getAttribute("userId") == null) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        return "cake/cake-request";
    }

    @PostMapping("/request/birthday")
    public String submitBirthday(@RequestParam String flavor, @RequestParam int tiers,
                                  @RequestParam String message, @RequestParam String deliveryDate,
                                  @RequestParam String theme, @RequestParam String ageDecoration,
                                  HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        int customerId = (int) session.getAttribute("userId");
        String customerName = (String) session.getAttribute("username");
        boolean ok = cakeService.submitBirthdayBooking(customerId, customerName, flavor, tiers, message, deliveryDate, theme, ageDecoration);
        model.addAttribute("success", ok ? "Birthday cake booking submitted!" : "Error submitting booking.");
        return "cake/cake-request";
    }

    @PostMapping("/request/wedding")
    public String submitWedding(@RequestParam String flavor, @RequestParam int tiers,
                                 @RequestParam String message, @RequestParam String deliveryDate,
                                 @RequestParam String couplesNames, @RequestParam String frostingStyle,
                                 HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        int customerId = (int) session.getAttribute("userId");
        String customerName = (String) session.getAttribute("username");
        boolean ok = cakeService.submitWeddingBooking(customerId, customerName, flavor, tiers, message, deliveryDate, couplesNames, frostingStyle);
        model.addAttribute("success", ok ? "Wedding cake booking submitted!" : "Error submitting booking.");
        return "cake/cake-request";
    }

    // Customer: booking status
    @GetMapping("/status")
    public String myBookings(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        int customerId = (int) session.getAttribute("userId");
        model.addAttribute("bookings", cakeService.getBookingsByCustomer(customerId));
        return "cake/booking-status";
    }

    // Customer: edit design
    @GetMapping("/edit/{id}")
    public String editBooking(@PathVariable int id, HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        CustomCake cake = cakeService.findById(id);
        model.addAttribute("cake", cake);
        return "cake/cake-edit";
    }

    @PostMapping("/edit/{id}")
    public String updateBooking(@PathVariable int id, @RequestParam String flavor,
                                 @RequestParam int tiers, @RequestParam String message,
                                 HttpSession session) {
        cakeService.updateDesign(id, flavor, tiers, message);
        return "redirect:/cakes/status";
    }

    // Customer: cancel booking
    @PostMapping("/cancel/{id}")
    public String cancelBooking(@PathVariable int id, HttpSession session) {
        cakeService.updateBookingStatus(id, CustomCake.BookingStatus.CANCELLED);
        return "redirect:/cakes/status";
    }

    // Staff: admin panel
    @GetMapping("/admin")
    public String adminPanel(HttpSession session, Model model) {
        if (!"STAFF".equals(session.getAttribute("userRole"))) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        model.addAttribute("bookings", cakeService.getAllBookings());
        return "cake/admin-bookings";
    }

    @PostMapping("/admin/status/{id}")
    public String updateStatus(@PathVariable int id, @RequestParam String status, HttpSession session) {
        if (!"STAFF".equals(session.getAttribute("userRole"))) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        cakeService.updateBookingStatus(id, CustomCake.BookingStatus.valueOf(status));
        return "redirect:/cakes/admin";
    }
}
