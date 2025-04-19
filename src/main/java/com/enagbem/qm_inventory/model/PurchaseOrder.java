package com.enagbem.qm_inventory.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@Table(name = "purchase_orders")
public class PurchaseOrder {

    public enum PurchaseOrderStatus {
        DRAFT, ORDERED, RECEIVED, CANCELLED
    }

    // Getters and Setters
    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long poId;

    @Setter
    @ManyToOne
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @Setter
    @CreationTimestamp
    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Setter
    @Column(name = "expected_delivery_date")
    private LocalDateTime expectedDeliveryDate;

    @Setter
    @Enumerated(EnumType.STRING)
    private PurchaseOrderStatus status = PurchaseOrderStatus.DRAFT;

    @Setter
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @Setter
    private String notes;

    @OneToMany(mappedBy = "purchaseOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<PurchaseOrderItem> items = new ArrayList<>();

    public PurchaseOrder() {
        this.items = new ArrayList<>();
    }

    public PurchaseOrder(Long poId, Supplier supplier, LocalDateTime orderDate, LocalDateTime expectedDeliveryDate,
                         PurchaseOrderStatus status, BigDecimal totalAmount, String notes, List<PurchaseOrderItem> items) {
        this.poId = poId;
        this.supplier = supplier;
        this.orderDate = orderDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.notes = notes;
        this.items = (items != null) ? items : new ArrayList<>();
    }

    public static PurchaseOrderBuilder builder() {
        PurchaseOrderBuilder builder = new PurchaseOrderBuilder();
        builder.purchaseOrder.setItems(new ArrayList<>());  // Ensuring it's always initialized
        return builder;
    }

    public static class PurchaseOrderBuilder {
        private final PurchaseOrder purchaseOrder = new PurchaseOrder();

        public PurchaseOrderBuilder poId(Long poId) {
            purchaseOrder.setPoId(poId);
            return this;
        }

        public PurchaseOrderBuilder supplier(Supplier supplier) {
            purchaseOrder.setSupplier(supplier);
            return this;
        }

        public PurchaseOrderBuilder orderDate(LocalDateTime orderDate) {
            purchaseOrder.setOrderDate(orderDate);
            return this;
        }

        public PurchaseOrderBuilder expectedDeliveryDate(LocalDateTime expectedDeliveryDate) {
            purchaseOrder.setExpectedDeliveryDate(expectedDeliveryDate);
            return this;
        }

        public PurchaseOrderBuilder status(PurchaseOrderStatus status) {
            purchaseOrder.setStatus(status);
            return this;
        }

        public PurchaseOrderBuilder totalAmount(BigDecimal totalAmount) {
            purchaseOrder.setTotalAmount(totalAmount);
            return this;
        }

        public PurchaseOrderBuilder notes(String notes) {
            purchaseOrder.setNotes(notes);
            return this;
        }

        public PurchaseOrderBuilder items(List<PurchaseOrderItem> items) {
            purchaseOrder.setItems(items);
            return this;
        }

        public PurchaseOrder build() {
            return purchaseOrder;
        }
    }

    public void setItems(List<PurchaseOrderItem> items) {
        this.items = (items != null) ? items : new ArrayList<>();
    }
}
