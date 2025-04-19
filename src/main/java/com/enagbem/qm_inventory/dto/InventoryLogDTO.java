package com.enagbem.qm_inventory.dto;

import java.time.LocalDateTime;

public class InventoryLogDTO {
    private Long logId;
    private Long productId;
    private String productName;
    private String changeType;
    private Integer quantityChange;
    private Long referenceId;
    private String referenceType;
    private String notes;
    private String createdBy;
    private LocalDateTime createdAt;

    public InventoryLogDTO() {}

    public InventoryLogDTO(Long logId, Long productId, String productName, String changeType,
                           Integer quantityChange, Long referenceId, String referenceType,
                           String notes, String createdBy, LocalDateTime createdAt) {
        this.logId = logId;
        this.productId = productId;
        this.productName = productName;
        this.changeType = changeType;
        this.quantityChange = quantityChange;
        this.referenceId = referenceId;
        this.referenceType = referenceType;
        this.notes = notes;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    // Getters and setters omitted for brevity


    public Long getLogId() {
        return logId;
    }

    public void setLogId(Long logId) {
        this.logId = logId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getChangeType() {
        return changeType;
    }

    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    public Integer getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(Integer quantityChange) {
        this.quantityChange = quantityChange;
    }

    public Long getReferenceId() {
        return referenceId;
    }

    public void setReferenceId(Long referenceId) {
        this.referenceId = referenceId;
    }

    public String getReferenceType() {
        return referenceType;
    }

    public void setReferenceType(String referenceType) {
        this.referenceType = referenceType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public static class Builder {
        private Long logId;
        private Long productId;
        private String productName;
        private String changeType;
        private Integer quantityChange;
        private Long referenceId;
        private String referenceType;
        private String notes;
        private String createdBy;
        private LocalDateTime createdAt;

        public Builder logId(Long logId) { this.logId = logId; return this; }
        public Builder productId(Long productId) { this.productId = productId; return this; }
        public Builder productName(String productName) { this.productName = productName; return this; }
        public Builder changeType(String changeType) { this.changeType = changeType; return this; }
        public Builder quantityChange(Integer quantityChange) { this.quantityChange = quantityChange; return this; }
        public Builder referenceId(Long referenceId) { this.referenceId = referenceId; return this; }
        public Builder referenceType(String referenceType) { this.referenceType = referenceType; return this; }
        public Builder notes(String notes) { this.notes = notes; return this; }
        public Builder createdBy(String createdBy) { this.createdBy = createdBy; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }

        public InventoryLogDTO build() {
            return new InventoryLogDTO(logId, productId, productName, changeType,
                    quantityChange, referenceId, referenceType, notes, createdBy, createdAt);
        }
    }
}
