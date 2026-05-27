package com.example.bakery.exception;

public class ProductNotFoundException extends BakeryException {
    public ProductNotFoundException(String id) {
        super("Product not found: " + id, "PRODUCT_NOT_FOUND");
    }
}
