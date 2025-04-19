package com.enagbem.qm_inventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
public class PurchaseOrderItemDTO {
    private Long poItemId;
    private Long productId;
    private String productName;
    private Integer quantity;
    private BigDecimal unitCost;
    private Integer receivedQuantity;
    private BigDecimal subtotal;

    public PurchaseOrderItemDTO() {
    }

    public PurchaseOrderItemDTO(Long poItemId, Long productId, String productName, Integer quantity,
                                BigDecimal unitCost, Integer receivedQuantity, BigDecimal subtotal) {
        this.poItemId = poItemId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.receivedQuantity = receivedQuantity;
        this.subtotal = subtotal;
    }

    public static PurchaseOrderItemDTOBuilder builder() {
        return new PurchaseOrderItemDTOBuilder();
    }

    public static class PurchaseOrderItemDTOBuilder {
        private final PurchaseOrderItemDTO purchaseOrderItemDTO = new PurchaseOrderItemDTO();

        public PurchaseOrderItemDTOBuilder poItemId(Long poItemId) {
            purchaseOrderItemDTO.setPoItemId(poItemId);
            return this;
        }

        public PurchaseOrderItemDTOBuilder productId(Long productId) {
            purchaseOrderItemDTO.setProductId(productId);
            return this;
        }

        public PurchaseOrderItemDTOBuilder productName(String productName) {
            purchaseOrderItemDTO.setProductName(productName);
            return this;
        }

        public PurchaseOrderItemDTOBuilder quantity(Integer quantity) {
            purchaseOrderItemDTO.setQuantity(quantity);
            return this;
        }

        public PurchaseOrderItemDTOBuilder unitCost(BigDecimal unitCost) {
            purchaseOrderItemDTO.setUnitCost(unitCost);
            return this;
        }

        public PurchaseOrderItemDTOBuilder receivedQuantity(Integer receivedQuantity) {
            purchaseOrderItemDTO.setReceivedQuantity(receivedQuantity);
            return this;
        }

        public PurchaseOrderItemDTOBuilder subtotal(BigDecimal subtotal) {
            purchaseOrderItemDTO.setSubtotal(subtotal);
            return this;
        }

        public PurchaseOrderItemDTO build() {
            return purchaseOrderItemDTO;
        }
    }

    // Getters and Setters
    public Long getPoItemId() { return poItemId; }

    public void setPoItemId(Long poItemId) { this.poItemId = poItemId; }

    public Long getProductId() { return productId; }

    public void setProductId(Long productId) { this.productId = productId; }

    public String getProductName() { return productName; }

    public void setProductName(String productName) { this.productName = productName; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitCost() { return unitCost; }

    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public Integer getReceivedQuantity() { return receivedQuantity; }

    public void setReceivedQuantity(Integer receivedQuantity) { this.receivedQuantity = receivedQuantity; }

    public BigDecimal getSubtotal() { return subtotal; }

    public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }
}


