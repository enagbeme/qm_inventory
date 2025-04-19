package com.enagbem.qm_inventory.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "purchase_order_items")
public class PurchaseOrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poItemId;

    @ManyToOne
    @JoinColumn(name = "po_id", nullable = false)
    @ToString.Exclude
    private PurchaseOrder purchaseOrder;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;

    @Column(name = "unit_cost", nullable = false)
    private BigDecimal unitCost;

    @Column(name = "received_quantity", nullable = false)
    private Integer receivedQuantity = 0;

    public PurchaseOrderItem() {
    }

    public PurchaseOrderItem(Long poItemId, PurchaseOrder purchaseOrder, Product product, Integer quantity, BigDecimal unitCost, Integer receivedQuantity) {
        this.poItemId = poItemId;
        this.purchaseOrder = purchaseOrder;
        this.product = product;
        this.quantity = quantity;
        this.unitCost = unitCost;
        this.receivedQuantity = receivedQuantity;
    }

    public static PurchaseOrderItemBuilder builder() {
        return new PurchaseOrderItemBuilder();
    }

    public static class PurchaseOrderItemBuilder {
        private final PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();

        public PurchaseOrderItemBuilder poItemId(Long poItemId) {
            purchaseOrderItem.setPoItemId(poItemId);
            return this;
        }

        public PurchaseOrderItemBuilder purchaseOrder(PurchaseOrder purchaseOrder) {
            purchaseOrderItem.setPurchaseOrder(purchaseOrder);
            return this;
        }

        public PurchaseOrderItemBuilder product(Product product) {
            purchaseOrderItem.setProduct(product);
            return this;
        }

        public PurchaseOrderItemBuilder quantity(Integer quantity) {
            purchaseOrderItem.setQuantity(quantity);
            return this;
        }

        public PurchaseOrderItemBuilder unitCost(BigDecimal unitCost) {
            purchaseOrderItem.setUnitCost(unitCost);
            return this;
        }

        public PurchaseOrderItemBuilder receivedQuantity(Integer receivedQuantity) {
            purchaseOrderItem.setReceivedQuantity(receivedQuantity);
            return this;
        }

        public PurchaseOrderItem build() {
            return purchaseOrderItem;
        }
    }

    // Getters and Setters
    public Long getPoItemId() { return poItemId; }

    public void setPoItemId(Long poItemId) { this.poItemId = poItemId; }

    public PurchaseOrder getPurchaseOrder() { return purchaseOrder; }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) { this.purchaseOrder = purchaseOrder; }

    public Product getProduct() { return product; }

    public void setProduct(Product product) { this.product = product; }

    public Integer getQuantity() { return quantity; }

    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getUnitCost() { return unitCost; }

    public void setUnitCost(BigDecimal unitCost) { this.unitCost = unitCost; }

    public Integer getReceivedQuantity() { return receivedQuantity; }

    public void setReceivedQuantity(Integer receivedQuantity) { this.receivedQuantity = receivedQuantity; }

    @Transient
    public BigDecimal getSubtotal() {
        return unitCost.multiply(BigDecimal.valueOf(quantity));
    }
}
