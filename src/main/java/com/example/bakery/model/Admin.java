package com.example.bakery.model;

/**
 * Admin - Inherits from Person.
 * OOP: Inheritance (extends Person), Method Overriding.
 */
public class Admin extends Person {

    private String position;
    private double salary;

    public Admin() {
        super();
    }

    public Admin(int id, String username, String password, String email, String phone, String position, double salary) {
        super(id, username, password, email, phone, "ADMIN");
        this.position = position;
        this.salary = salary;
    }

    @Override
    public String getDashboardPath() {
        return "/admin/dashboard";
    }

    @Override
    public String toString() {
        return super.toString() + "," + position + "," + salary;
    }

    public static Admin fromLine(String line) {
        String[] parts = line.split(",", -1);
        if (parts.length < 8) return null;
        try {
            return new Admin(
                Integer.parseInt(parts[0].trim()),
                parts[1].trim(), parts[2].trim(), parts[3].trim(),
                parts[4].trim(), parts[6].trim(),
                Double.parseDouble(parts[7].trim())
            );
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public double getSalary() { return salary; }
    public void setSalary(double salary) { this.salary = salary; }
}
