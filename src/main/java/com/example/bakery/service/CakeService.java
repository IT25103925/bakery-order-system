package com.example.bakery.service;

import com.example.bakery.model.*;
import com.example.bakery.util.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CakeService {

    private static final String FILE = "cakes.txt";

    @Autowired
    private FileStorage fileStorage;

    public List<CustomCake> getAllBookings() {
        List<String> lines = fileStorage.readAll(FILE);
        List<CustomCake> cakes = new ArrayList<>();
        for (String line : lines) {
            CustomCake c = CustomCake.fromLine(line);
            if (c != null) cakes.add(c);
        }
        return cakes;
    }

    public List<CustomCake> getBookingsByCustomer(int customerId) {
        List<CustomCake> all = getAllBookings();
        List<CustomCake> result = new ArrayList<>();
        for (CustomCake c : all) {
            if (c.getCustomerId() == customerId) result.add(c);
        }
        return result;
    }

    public CustomCake findById(int id) {
        for (CustomCake c : getAllBookings()) {
            if (c.getId() == id) return c;
        }
        return null;
    }

    public boolean submitBirthdayBooking(int customerId, String customerName, String flavor,
                                          int tiers, String message, String deliveryDate,
                                          String theme, String ageDecoration) {
        try {
            List<String> lines = fileStorage.readAll(FILE);
            int newId = FileStorage.generateId(lines);
            BirthdayCake cake = new BirthdayCake(newId, customerId, customerName, flavor,
                    tiers, message, deliveryDate, theme, ageDecoration);
            fileStorage.appendLine(FILE, cake.toString());
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean submitWeddingBooking(int customerId, String customerName, String flavor,
                                         int tiers, String message, String deliveryDate,
                                         String couplesNames, String frostingStyle) {
        try {
            List<String> lines = fileStorage.readAll(FILE);
            int newId = FileStorage.generateId(lines);
            WeddingCake cake = new WeddingCake(newId, customerId, customerName, flavor,
                    tiers, message, deliveryDate, couplesNames, frostingStyle);
            fileStorage.appendLine(FILE, cake.toString());
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean updateBookingStatus(int cakeId, CustomCake.BookingStatus status) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 8 && parts[0].trim().equals(String.valueOf(cakeId))) {
                parts[8] = status.name();
                updated.add(String.join(",", parts));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }

    public boolean updateDesign(int cakeId, String flavor, int tiers, String message) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 5 && parts[0].trim().equals(String.valueOf(cakeId))) {
                parts[3] = flavor;
                parts[4] = String.valueOf(tiers);
                parts[5] = message;
                updated.add(String.join(",", parts));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }

    public boolean deleteBooking(int cakeId) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 0 && parts[0].trim().equals(String.valueOf(cakeId))) {
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }
}
