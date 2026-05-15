package com.example.bakery.controller;

import com.example.bakery.model.*;
import com.example.bakery.service.PersonService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * PersonController - Member 01: Profile management and admin user operations.
 */
@Controller
@RequestMapping("/users")
public class PersonController {

    @Autowired
    private PersonService personService;

    // View profile
    @GetMapping("/profile")
    public String viewProfile(HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return session.getAttribute("userRole") != null && "ADMIN".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        int userId = (int) session.getAttribute("userId");
        Person person = personService.findById(userId);
        model.addAttribute("user", person); // keeping model attribute name generic for views
        return "auth/profile";
    }

    // Update profile
    @PostMapping("/profile/update")
    public String updateProfile(@RequestParam String email,
                                 @RequestParam String phone,
                                 @RequestParam(required = false) String address,
                                 HttpSession session, Model model) {
        if (session.getAttribute("userId") == null) return session.getAttribute("userRole") != null && "ADMIN".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        int userId = (int) session.getAttribute("userId");
        personService.updateProfile(userId, email, phone, address != null ? address : "");
        model.addAttribute("success", "Profile updated successfully!");
        Person person = personService.findById(userId);
        model.addAttribute("user", person);
        return "auth/profile";
    }

    // Admin: list all users
    @GetMapping("/admin/list")
    public String listUsers(HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return session.getAttribute("userRole") != null && "ADMIN".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        List<Person> persons = personService.getAllPersons();
        model.addAttribute("users", persons);
        return "auth/user-list";
    }

    // Admin: delete user
    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable int id, HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return session.getAttribute("userRole") != null && "ADMIN".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        personService.deletePerson(id);
        return "redirect:/users/admin/list";
    }

    // Admin: register admin
    @GetMapping("/admin/register-staff")
    public String registerAdminPage(HttpSession session) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return session.getAttribute("userRole") != null && "ADMIN".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        return "auth/register-staff"; // keeping view name same unless asked
    }

    @PostMapping("/admin/register-staff")
    public String registerAdmin(@RequestParam String username, @RequestParam String password,
                                  @RequestParam String email, @RequestParam String phone,
                                  @RequestParam String position, @RequestParam double salary,
                                  HttpSession session, Model model) {
        if (!"ADMIN".equals(session.getAttribute("userRole"))) return session.getAttribute("userRole") != null && "ADMIN".equals(session.getAttribute("userRole")) ? "redirect:/admin-login" : "redirect:/customer-login";
        boolean ok = personService.registerAdmin(username, password, email, phone, position, salary);
        if (!ok) model.addAttribute("error", "Username already exists.");
        else model.addAttribute("success", "Admin registered successfully!");
        return "auth/register-staff";
    }
}
