package com.example.bakery.controller;

import com.example.bakery.model.Person;
import com.example.bakery.service.PersonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {

    @Autowired
    private PersonService personService;

    @GetMapping("/")
    public String index() {
        return "redirect:/customer-login";
    }

    // ── CUSTOMER LOGIN ─────────────────────────────────────────────────────────
    @GetMapping("/customer-login")
    public String customerLoginPage(HttpSession session, Model model,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String registered) {
        if (session.getAttribute("userId") != null &&
                "CUSTOMER".equals(session.getAttribute("userRole"))) {
            return "redirect:/customer/dashboard";
        }
        if ("true".equals(registered)) {
            model.addAttribute("success", "Registration successful! Please login.");
        }
        return "auth/customer-login";
    }

    @PostMapping("/customer-login")
    public String doCustomerLogin(@RequestParam String username,
            @RequestParam String password,
            HttpSession session, Model model) {
        Person person = personService.login(username, password);
        if (person == null || !"CUSTOMER".equals(person.getRole())) {
            model.addAttribute("error", "Invalid customer username or password.");
            return "auth/customer-login";
        }
        session.setAttribute("userId", person.getId());
        session.setAttribute("username", person.getUsername());
        session.setAttribute("userRole", person.getRole());
        return "redirect:/customer/dashboard";
    }

    // ── ADMIN LOGIN ────────────────────────────────────────────────────────────
    @GetMapping("/admin-login")
    public String adminLoginPage(HttpSession session) {
        if (session.getAttribute("userId") != null &&
                "ADMIN".equals(session.getAttribute("userRole"))) {
            return "redirect:/admin/dashboard";
        }
        return "auth/admin-login";
    }

    @PostMapping("/admin-login")
    public String doAdminLogin(@RequestParam String username,
            @RequestParam String password,
            HttpSession session, Model model) {
        Person person = personService.login(username, password);
        if (person == null || (!"ADMIN".equals(person.getRole()) && !"STAFF".equals(person.getRole()))) {
            model.addAttribute("error", "Invalid admin username or password.");
            return "auth/admin-login";
        }
        session.setAttribute("userId", person.getId());
        session.setAttribute("username", person.getUsername());
        session.setAttribute("userRole", "ADMIN"); // force admin role for session
        return "redirect:/admin/dashboard";
    }

    // ── REGISTER ───────────────────────────────────────────────────────────────
    @GetMapping("/register")
    public String registerPage() {
        return "auth/register";
    }

    @PostMapping("/register")
    public String doRegister(@RequestParam String username,
            @RequestParam String password,
            @RequestParam String email,
            @RequestParam String phone,
            @RequestParam String address,
            Model model) {
        if (personService.findByUsername(username) != null) {
            model.addAttribute("error", "Username already exists.");
            return "auth/register";
        }
        if (personService.findByEmail(email) != null) {
            model.addAttribute("error", "An account with this email already exists.");
            return "auth/register";
        }
        boolean success = personService.registerCustomer(username, password, email, phone, address);
        if (!success) {
            model.addAttribute("error", "Registration failed. Please try again.");
            return "auth/register";
        }
        return "redirect:/customer-login?registered=true";
    }

    // ── LOGOUT ─────────────────────────────────────────────────────────────────
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        String role = (String) session.getAttribute("userRole");
        session.invalidate();
        return "ADMIN".equals(role) ? "redirect:/admin-login" : "redirect:/customer-login";
    }

    // ── DASHBOARDS ─────────────────────────────────────────────────────────────
    @GetMapping("/customer/dashboard")
    public String customerDashboard(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null)
            return "redirect:/customer-login";
        if (!"CUSTOMER".equals(session.getAttribute("userRole")))
            return "redirect:/customer-login";
        model.addAttribute("username", session.getAttribute("username"));
        return "auth/customer-dashboard";
    }

    @GetMapping("/admin/dashboard")
    public String adminDashboard(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null)
            return "redirect:/admin-login";
        if (!"ADMIN".equals(session.getAttribute("userRole")))
            return "redirect:/admin-login";
        model.addAttribute("username", session.getAttribute("username"));
        return "auth/staff-dashboard"; // keep view name until user asks to change
    }
}
