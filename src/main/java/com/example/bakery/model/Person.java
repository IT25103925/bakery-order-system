package com.example.bakery.model;

/**
 * Person - Base abstract class for all human entities (Customer, Admin).
 * OOP: Encapsulation, Abstraction (abstract method), Inheritance (extends BaseEntity).
 */
public abstract class Person extends BaseEntity {

    private String username;
    private String password;
    private String email;
    private String phone;
    private String role;

    public Person() {
        super();
    }

    public Person(int id, String username, String password, String email, String phone, String role) {
        super(id);
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.role = role;
    }

    // Abstract method forces subclasses to provide specific behavior
    public abstract String getDashboardPath();

    // Method Overriding - toString
    @Override
    public String toString() {
        return getId() + "," + username + "," + password + "," + email + "," + phone + "," + role;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
