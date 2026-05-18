package com.example.bakery.service;

import com.example.bakery.model.*;
import com.example.bakery.util.FileStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Service
public class ReviewService {

    private static final String FILE = "reviews.txt";

    @Autowired
    private FileStorage fileStorage;

    public List<Review> getAllReviews() {
        List<String> lines = fileStorage.readAll(FILE);
        List<Review> reviews = new ArrayList<>();
        for (String line : lines) {
            Review r = Review.fromLine(line);
            if (r != null) reviews.add(r);
        }
        return reviews;
    }

    public List<Review> getApprovedReviews() {
        List<Review> all = getAllReviews();
        List<Review> approved = new ArrayList<>();
        for (Review r : all) {
            if (r.isApproved()) approved.add(r);
        }
        return approved;
    }

    public List<Review> getReviewsByProduct(int productId) {
        List<Review> approved = getApprovedReviews();
        List<Review> result = new ArrayList<>();
        for (Review r : approved) {
            if (r.getProductId() == productId) result.add(r);
        }
        return result;
    }

    public List<Review> getReviewsByCustomer(int customerId) {
        List<Review> all = getAllReviews();
        List<Review> result = new ArrayList<>();
        for (Review r : all) {
            if (r.getCustomerId() == customerId) result.add(r);
        }
        return result;
    }

    public boolean postReview(int customerId, String username, int productId, String productName,
                               int rating, String comment) {
        try {
            List<String> lines = fileStorage.readAll(FILE);
            int newId = FileStorage.generateId(lines);
            Review r = new Review(newId, customerId, username, productId, productName,
                    rating, comment, LocalDate.now().toString());
            fileStorage.appendLine(FILE, r.toString());
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean postVerifiedReview(int customerId, String username, int productId, String productName,
                                       int rating, String comment, int orderId) {
        try {
            List<String> lines = fileStorage.readAll(FILE);
            int newId = FileStorage.generateId(lines);
            VerifiedPurchaseReview r = new VerifiedPurchaseReview(newId, customerId, username, productId,
                    productName, rating, comment, LocalDate.now().toString(), orderId);
            fileStorage.appendLine(FILE, r.toString());
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean updateReview(int reviewId, int rating, String comment) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 6 && parts[0].trim().equals(String.valueOf(reviewId))) {
                parts[5] = String.valueOf(rating);
                parts[6] = comment;
                updated.add(String.join(",", parts));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }

    public boolean approveReview(int reviewId) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 8 && parts[0].trim().equals(String.valueOf(reviewId))) {
                parts[8] = "true";
                updated.add(String.join(",", parts));
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }

    public boolean deleteReview(int reviewId) {
        List<String> lines = fileStorage.readAll(FILE);
        List<String> updated = new ArrayList<>();
        boolean found = false;
        for (String line : lines) {
            String[] parts = line.split(",", -1);
            if (parts.length > 0 && parts[0].trim().equals(String.valueOf(reviewId))) {
                found = true;
            } else {
                updated.add(line);
            }
        }
        if (found) fileStorage.writeAll(FILE, updated);
        return found;
    }

    // Calculate average rating for a product
    public double getAverageRating(int productId) {
        List<Review> reviews = getReviewsByProduct(productId);
        if (reviews.isEmpty()) return 0.0;
        int total = 0;
        for (Review r : reviews) { total += r.getRating(); }
        return (double) total / reviews.size();
    }
}
