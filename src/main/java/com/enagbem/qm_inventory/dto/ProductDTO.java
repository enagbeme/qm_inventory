package com.enagbem.qm_inventory.dto;


import lombok.*;

import java.math.BigDecimal;

@Data
@Builder
public class ProductDTO {
    private Long productId;
    private String name;
    private String description;
    private Long categoryId;
    private String categoryName;
    private BigDecimal price;
    private BigDecimal costPrice;
    private Integer currentStock;
    private Integer minStockLevel;
    private Integer maxStockLevel;
    private Long supplierId;
    private String supplierName;
    private String imagePath;

    public ProductDTO() {
    }

    public ProductDTO(Long productId, String name, String description, Long categoryId, String categoryName,
                      BigDecimal price, BigDecimal costPrice, Integer currentStock, Integer minStockLevel,
                      Integer maxStockLevel, Long supplierId, String supplierName, String imagePath) {
        this.productId = productId;
        this.name = name;
        this.description = description;
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.price = price;
        this.costPrice = costPrice;
        this.currentStock = currentStock;
        this.minStockLevel = minStockLevel;
        this.maxStockLevel = maxStockLevel;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.imagePath = imagePath;
    }

    public static ProductDTOBuilder builder() {
        return new ProductDTOBuilder();
    }

    public static class ProductDTOBuilder {
        private final ProductDTO productDTO = new ProductDTO();

        public ProductDTOBuilder productId(Long productId) {
            productDTO.setProductId(productId);
            return this;
        }

        public ProductDTOBuilder name(String name) {
            productDTO.setName(name);
            return this;
        }

        public ProductDTOBuilder description(String description) {
            productDTO.setDescription(description);
            return this;
        }

        public ProductDTOBuilder categoryId(Long categoryId) {
            productDTO.setCategoryId(categoryId);
            return this;
        }

        public ProductDTOBuilder categoryName(String categoryName) {
            productDTO.setCategoryName(categoryName);
            return this;
        }

        public ProductDTOBuilder price(BigDecimal price) {
            productDTO.setPrice(price);
            return this;
        }

        public ProductDTOBuilder costPrice(BigDecimal costPrice) {
            productDTO.setCostPrice(costPrice);
            return this;
        }

        public ProductDTOBuilder currentStock(Integer currentStock) {
            productDTO.setCurrentStock(currentStock);
            return this;
        }

        public ProductDTOBuilder minStockLevel(Integer minStockLevel) {
            productDTO.setMinStockLevel(minStockLevel);
            return this;
        }

        public ProductDTOBuilder maxStockLevel(Integer maxStockLevel) {
            productDTO.setMaxStockLevel(maxStockLevel);
            return this;
        }

        public ProductDTOBuilder supplierId(Long supplierId) {
            productDTO.setSupplierId(supplierId);
            return this;
        }

        public ProductDTOBuilder supplierName(String supplierName) {
            productDTO.setSupplierName(supplierName);
            return this;
        }

        public ProductDTOBuilder imagePath(String imagePath) {
            productDTO.setImagePath(imagePath);
            return this;
        }

        public ProductDTO build() {
            return productDTO;
        }
    }

    // Getters and Setters
    public Long getProductId() { return productId; }

    public void setProductId(Long productId) { this.productId = productId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public Long getCategoryId() { return categoryId; }

    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }

    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

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

    public Long getSupplierId() { return supplierId; }

    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public String getSupplierName() { return supplierName; }

    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
