package com.example.bakery.model;

public class VerifiedPurchaseReview extends Review {
    private int orderId;

    public VerifiedPurchaseReview() { super(); }

    public VerifiedPurchaseReview(int id, int customerId, String customerUsername, int productId,
                                   String productName, int rating, String comment, String reviewDate, int orderId) {
        super(id, customerId, customerUsername, productId, productName, rating, comment, reviewDate);
        this.orderId = orderId;
        setApproved(true); // Verified reviews auto-approved
    }

    @Override
    public String getReviewType() { return "VERIFIED"; }

    @Override
    public String toString() {
        return super.toString() + "," + orderId;
    }

    public static VerifiedPurchaseReview fromLine(String line) {
        String[] p = line.split(",", -1);
        if (p.length < 10) return null;
        try {
            VerifiedPurchaseReview r = new VerifiedPurchaseReview();
            r.setId(Integer.parseInt(p[0].trim()));
            r.setCustomerId(Integer.parseInt(p[1].trim()));
            r.setCustomerUsername(p[2].trim());
            r.setProductId(Integer.parseInt(p[3].trim()));
            r.setProductName(p[4].trim());
            r.setRating(Integer.parseInt(p[5].trim()));
            r.setComment(p[6].trim());
            r.setReviewDate(p[7].trim());
            r.setApproved(Boolean.parseBoolean(p[8].trim()));
            if (p.length > 10) r.setOrderId(Integer.parseInt(p[10].trim()));
            return r;
        } catch (Exception e) { return null; }
    }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }
}
