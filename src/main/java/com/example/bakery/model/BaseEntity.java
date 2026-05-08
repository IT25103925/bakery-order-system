package com.example.bakery.model;

import java.time.LocalDateTime;

/**
 * BaseEntity - Abstract base class for all entities in the system.
 * OOP: Inheritance (base class), Encapsulation.
 */
public abstract class BaseEntity {

    private int id;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public BaseEntity() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public BaseEntity(int id) {
        this.id = id;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public BaseEntity(int id, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.updatedAt = updatedAt != null ? updatedAt : LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
