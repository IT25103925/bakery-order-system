package com.example.bakery.controller;

import com.example.bakery.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Customer: submit review
    @GetMapping("/submit/{productId}")
    public String submitReviewPage(@PathVariable int productId, HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        model.addAttribute("productId", productId);
        return "review/review-submit";
    }

    @PostMapping("/submit")
    public String submitReview(@RequestParam int productId, @RequestParam String productName,
                                @RequestParam int rating, @RequestParam String comment,
                                HttpSession session) {
        if (session.getAttribute("userId") == null) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        int customerId = (int) session.getAttribute("userId");
        String username = (String) session.getAttribute("username");
        reviewService.postReview(customerId, username, productId, productName, rating, comment);
        return "redirect:/products/" + productId;
    }

    // Customer: my reviews
    @GetMapping("/my")
    public String myReviews(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        int customerId = (int) session.getAttribute("userId");
        model.addAttribute("reviews", reviewService.getReviewsByCustomer(customerId));
        return "review/review-list";
    }

    // Customer: edit review
    @PostMapping("/edit/{id}")
    public String editReview(@PathVariable int id, @RequestParam int rating,
                              @RequestParam String comment, HttpSession session) {
        if (session.getAttribute("userId") == null) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        reviewService.updateReview(id, rating, comment);
        return "redirect:/reviews/my";
    }

    // Admin: moderation page
    @GetMapping("/admin")
    public String adminModerationPage(HttpSession session, Model model) {
        if (!"STAFF".equals(session.getAttribute("userRole"))) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        model.addAttribute("allReviews", reviewService.getAllReviews());
        return "review/admin-moderation";
    }

    // Admin: approve
    @PostMapping("/admin/approve/{id}")
    public String approveReview(@PathVariable int id, HttpSession session) {
        if (!"STAFF".equals(session.getAttribute("userRole"))) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        reviewService.approveReview(id);
        return "redirect:/reviews/admin";
    }

    // Admin: delete
    @PostMapping("/admin/delete/{id}")
    public String deleteReview(@PathVariable int id, HttpSession session) {
        if (!"STAFF".equals(session.getAttribute("userRole"))) return session.getAttribute("userRole") != null && "STAFF".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        reviewService.deleteReview(id);
        return "redirect:/reviews/admin";
    }
}
