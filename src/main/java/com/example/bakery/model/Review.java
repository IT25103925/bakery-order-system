package com.example.bakery.model;

public class Review extends BaseEntity {
    private int customerId;
    private String customerUsername; // Information Hiding - masked in public display
    private int productId;
    private String productName;
    private int rating; // 1-5
    private String comment;
    private String reviewDate;
    private boolean approved; // Admin moderation

    public Review() { 
        super();
        this.approved = false; 
    }

    public Review(int id, int customerId, String customerUsername, int productId, String productName,
                  int rating, String comment, String reviewDate) {
        super(id);
        this.customerId = customerId;
        this.customerUsername = customerUsername;
        this.productId = productId;
        this.productName = productName;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
        this.approved = false;
    }

    // Information Hiding - mask username in public display (Lecture 01)
    public String getMaskedUsername() {
        if (customerUsername == null || customerUsername.length() < 2) return "***";
        return customerUsername.charAt(0) + "***" + customerUsername.charAt(customerUsername.length() - 1);
    }

    public String getReviewType() { return "STANDARD"; }

    @Override
    public String toString() {
        return getId() + "," + customerId + "," + customerUsername + "," + productId + "," +
               productName + "," + rating + "," + comment + "," + reviewDate + "," + approved + "," + getReviewType();
    }

    public static Review fromLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 10) return null;
        try {
            String type = p[9].trim();
            if ("VERIFIED".equals(type)) return VerifiedPurchaseReview.fromLine(line);
            Review r = new Review();
            r.setId(Integer.parseInt(p[0].trim()));
            r.setCustomerId(Integer.parseInt(p[1].trim()));
            r.setCustomerUsername(p[2].trim());
            r.setProductId(Integer.parseInt(p[3].trim()));
            r.setProductName(p[4].trim());
            r.setRating(Integer.parseInt(p[5].trim()));
            r.setComment(p[6].trim());
            r.setReviewDate(p[7].trim());
            r.setApproved(Boolean.parseBoolean(p[8].trim()));
            return r;
        } catch (Exception e) { return null; }
    }


    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getCustomerUsername() { return customerUsername; }
    public void setCustomerUsername(String customerUsername) { this.customerUsername = customerUsername; }
    public int getProductId() { return productId; }
    public void setProductId(int productId) { this.productId = productId; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public String getReviewDate() { return reviewDate; }
    public void setReviewDate(String reviewDate) { this.reviewDate = reviewDate; }
    public boolean isApproved() { return approved; }
    public void setApproved(boolean approved) { this.approved = approved; }
}
