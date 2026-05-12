package com.example.bakery.service;

import com.example.bakery.model.*;
import com.example.bakery.util.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * PersonService - Business logic for Member 01: Person & Profile Management.
 * OOP: Uses Inheritance (Customer/Admin extend Person), Encapsulation.
 */
@Service
public class PersonService {

    private static final String FILE = "users.txt";

    @Autowired
    private FileStorage fileStorage;

    public List<Person> getAllPersons() {
        List<String> lines = fileStorage.readAll(FILE);
        List<Person> persons = new ArrayList<>();
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length < 6) continue;
            String role = parts[5].trim();
            if ("ADMIN".equals(role) || "STAFF".equals(role)) { // allow backward compatibility for STAFF role in file
                Admin a = Admin.fromLine(line);
                if (a != null) persons.add(a);
            } else {
                Customer c = Customer.fromLine(line);
                if (c != null) persons.add(c);
            }
        }
        return persons;
    }

    public List<Customer> getAllCustomers() {
        List<Person> all = getAllPersons();
        List<Customer> customers = new ArrayList<>();
        for (Person p : all) {
            if (p instanceof Customer) {
                customers.add((Customer) p);
            }
        }
        return customers;
    }

    public List<Admin> getAllAdmins() {
        List<Person> all = getAllPersons();
        List<Admin> adminList = new ArrayList<>();
        for (Person p : all) {
            if (p instanceof Admin) {
                adminList.add((Admin) p);
            }
        }
        return adminList;
    }

    public Person findByUsername(String username) {
        List<Person> persons = getAllPersons();
        for (Person p : persons) {
            if (p.getUsername().equalsIgnoreCase(username)) return p;
        }
        return null;
    }

    public Person findById(int id) {
        List<Person> persons = getAllPersons();
        for (Person p : persons) {
            if (p.getId() == id) return p;
        }
        return null;
    }

    public boolean registerCustomer(String username, String password, String email, String phone, String address) {
        try {
            if (findByUsername(username) != null) return false;
            List<String> lines = fileStorage.readAll(FILE);
            int newId = FileStorage.generateId(lines);
            Customer c = new Customer(newId, username, password, email, phone, address);
            fileStorage.appendLine(FILE, c.toString());
            return true;
        } catch (Exception e) {
            System.err.println("Registration error: " + e.getMessage());
            return false;
        }
    }

    public boolean registerAdmin(String username, String password, String email, String phone, String position, double salary) {
        try {
            if (findByUsername(username) != null) return false;
            List<String> lines = fileStorage.readAll(FILE);
            int newId = FileStorage.generateId(lines);
            Admin a = new Admin(newId, username, password, email, phone, position, salary);
            fileStorage.appendLine(FILE, a.toString());
            return true;
        } catch (Exception e) {
            System.err.println("Admin registration error: " + e.getMessage());
            return false;
        }
    }

    public Person login(String username, String password) {
        Person person = findByUsername(username);
        if (person != null && person.getPassword().equals(password)) return person;
        return null;
    }

    public boolean updateProfile(int personId, String email, String phone, String address) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 0 && parts[0].trim().equals(String.valueOf(personId))) {
                if (parts.length >= 8 && "CUSTOMER".equals(parts[5].trim())) {
                    parts[3] = email;
                    parts[4] = phone;
                    parts[6] = address;
                } else {
                    parts[3] = email;
                    parts[4] = phone;
                }
                updated.add(String.join(",", parts));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }

    public boolean deletePerson(int personId) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 0 && parts[0].trim().equals(String.valueOf(personId))) {
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }
}
