package com.enagbem.qm_inventory.dto;


import com.enagbem.qm_inventory.model.PurchaseOrder;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class PurchaseOrderDTO {
    private Long poId;
    private Long supplierId;
    private String supplierName;
    private LocalDateTime orderDate;
    private LocalDateTime expectedDeliveryDate;
    private PurchaseOrder.PurchaseOrderStatus status;
    private BigDecimal totalAmount;
    private String notes;
    private List<PurchaseOrderItemDTO> items = new ArrayList<>();

    public PurchaseOrderDTO() {
    }

    public PurchaseOrderDTO(Long poId, Long supplierId, String supplierName, LocalDateTime orderDate,
                            LocalDateTime expectedDeliveryDate, PurchaseOrder.PurchaseOrderStatus status,
                            BigDecimal totalAmount, String notes, List<PurchaseOrderItemDTO> items) {
        this.poId = poId;
        this.supplierId = supplierId;
        this.supplierName = supplierName;
        this.orderDate = orderDate;
        this.expectedDeliveryDate = expectedDeliveryDate;
        this.status = status;
        this.totalAmount = totalAmount;
        this.notes = notes;
        this.items = items;
    }

    public String getFormattedOrderDate() {
        return orderDate != null ? orderDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";
    }

    public String getFormattedDeliveryDate() {
        return expectedDeliveryDate != null ? expectedDeliveryDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")) : "";
    }


    public static PurchaseOrderDTOBuilder builder() {
        return new PurchaseOrderDTOBuilder();
    }

    public static class PurchaseOrderDTOBuilder {
        private final PurchaseOrderDTO purchaseOrderDTO = new PurchaseOrderDTO();

        public PurchaseOrderDTOBuilder poId(Long poId) {
            purchaseOrderDTO.setPoId(poId);
            return this;
        }

        public PurchaseOrderDTOBuilder supplierId(Long supplierId) {
            purchaseOrderDTO.setSupplierId(supplierId);
            return this;
        }

        public PurchaseOrderDTOBuilder supplierName(String supplierName) {
            purchaseOrderDTO.setSupplierName(supplierName);
            return this;
        }

        public PurchaseOrderDTOBuilder orderDate(LocalDateTime orderDate) {
            purchaseOrderDTO.setOrderDate(orderDate);
            return this;
        }

        public PurchaseOrderDTOBuilder expectedDeliveryDate(LocalDateTime expectedDeliveryDate) {
            purchaseOrderDTO.setExpectedDeliveryDate(expectedDeliveryDate);
            return this;
        }

        public PurchaseOrderDTOBuilder status(PurchaseOrder.PurchaseOrderStatus status) {
            purchaseOrderDTO.setStatus(status);
            return this;
        }

        public PurchaseOrderDTOBuilder totalAmount(BigDecimal totalAmount) {
            purchaseOrderDTO.setTotalAmount(totalAmount);
            return this;
        }

        public PurchaseOrderDTOBuilder notes(String notes) {
            purchaseOrderDTO.setNotes(notes);
            return this;
        }

        public PurchaseOrderDTOBuilder items(List<PurchaseOrderItemDTO> items) {
            purchaseOrderDTO.setItems(items);
            return this;
        }

        public PurchaseOrderDTO build() {
            return purchaseOrderDTO;
        }
    }

    // Getters and Setters
    public Long getPoId() { return poId; }

    public void setPoId(Long poId) { this.poId = poId; }

    public Long getSupplierId() { return supplierId; }

    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public String getSupplierName() { return supplierName; }

    public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

    public LocalDateTime getOrderDate() { return orderDate; }

    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }

    public LocalDateTime getExpectedDeliveryDate() { return expectedDeliveryDate; }

    public void setExpectedDeliveryDate(LocalDateTime expectedDeliveryDate) { this.expectedDeliveryDate = expectedDeliveryDate; }

    public PurchaseOrder.PurchaseOrderStatus getStatus() { return status; }

    public void setStatus(PurchaseOrder.PurchaseOrderStatus status) { this.status = status; }

    public BigDecimal getTotalAmount() { return totalAmount; }

    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public String getNotes() { return notes; }

    public void setNotes(String notes) { this.notes = notes; }

    public List<PurchaseOrderItemDTO> getItems() { return items; }

    public void setItems(List<PurchaseOrderItemDTO> items) { this.items = items; }
}


