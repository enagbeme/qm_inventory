package com.enagbem.qm_inventory.model;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "cost_price", nullable = false)
    private BigDecimal costPrice;

    @Column(name = "current_stock", nullable = false)
    private Integer currentStock = 0;

    @Column(name = "min_stock_level", nullable = false)
    private Integer minStockLevel;

    @Column(name = "max_stock_level")
    private Integer maxStockLevel;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "image_path")
    private String imagePath;

    public Product() {
    }

    public Product(Long productId, String name, String description, Category category, BigDecimal price,
                   BigDecimal costPrice, Integer currentStock, Integer minStockLevel, Integer maxStockLevel,
                   Supplier supplier, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.costPrice = costPrice;
        this.currentStock = currentStock;
        this.minStockLevel = minStockLevel;
        this.maxStockLevel = maxStockLevel;
        this.supplier = supplier;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public static ProductBuilder builder() {
        return new ProductBuilder();
    }

    public static class ProductBuilder {
        private final Product product = new Product();

        public ProductBuilder productId(Long productId) {
            product.setProductId(productId);
            return this;
        }

        public ProductBuilder name(String name) {
            product.setName(name);
            return this;
        }

        public ProductBuilder description(String description) {
            product.setDescription(description);
            return this;
        }

        public ProductBuilder category(Category category) {
            product.setCategory(category);
            return this;
        }

        public ProductBuilder price(BigDecimal price) {
            product.setPrice(price);
            return this;
        }

        public ProductBuilder costPrice(BigDecimal costPrice) {
            product.setCostPrice(costPrice);
            return this;
        }

        public ProductBuilder currentStock(Integer currentStock) {
            product.setCurrentStock(currentStock);
            return this;
        }

        public ProductBuilder minStockLevel(Integer minStockLevel) {
            product.setMinStockLevel(minStockLevel);
            return this;
        }

        public ProductBuilder maxStockLevel(Integer maxStockLevel) {
            product.setMaxStockLevel(maxStockLevel);
            return this;
        }

        public ProductBuilder supplier(Supplier supplier) {
            product.setSupplier(supplier);
            return this;
        }

        public ProductBuilder createdAt(LocalDateTime createdAt) {
            product.setCreatedAt(createdAt);
            return this;
        }

        public ProductBuilder updatedAt(LocalDateTime updatedAt) {
            product.setUpdatedAt(updatedAt);
            return this;
        }

        public ProductBuilder imagePath(String imagePath) {
            product.setImagePath(imagePath);
            return this;
        }

        public Product build() {
            return product;
        }
    }

    // Getters and Setters

    public Long getProductId() { return productId; }

    public void setProductId(Long productId) { this.productId = productId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Category getCategory() { return category; }

    public void setCategory(Category category) { this.category = category; }

    public BigDecimal getPrice() { return price; }

    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getCostPrice() { return costPrice; }

    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }

    public Integer getCurrentStock() { return currentStock; }

    public void setCurrentStock(Integer currentStock) { this.currentStock = currentStock; }

    public Integer getMinStockLevel() { return minStockLevel; }

    public void setMinStockLevel(Integer minStockLevel) { this.minStockLevel = minStockLevel; }

    public Integer getMaxStockLevel() { return maxStockLevel; }

    public void setMaxStockLevel(Integer maxStockLevel) { this.maxStockLevel = maxStockLevel; }

    public Supplier getSupplier() { return supplier; }

    public void setSupplier(Supplier supplier) { this.supplier = supplier; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
