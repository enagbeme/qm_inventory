package com.enagbem.qm_inventory.service;


import com.enagbem.qm_inventory.dto.PurchaseOrderDTO;
import com.enagbem.qm_inventory.dto.PurchaseOrderItemDTO;
import com.enagbem.qm_inventory.exception.BusinessException;
import com.enagbem.qm_inventory.exception.ResourceNotFoundException;
import com.enagbem.qm_inventory.model.Product;
import com.enagbem.qm_inventory.model.PurchaseOrder;
import com.enagbem.qm_inventory.model.PurchaseOrderItem;
import com.enagbem.qm_inventory.model.Supplier;
import com.enagbem.qm_inventory.repository.ProductRepository;
import com.enagbem.qm_inventory.repository.PurchaseOrderRepository;
import com.enagbem.qm_inventory.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final InventoryService inventoryService;


    public PurchaseOrderService(PurchaseOrderRepository purchaseOrderRepository, SupplierRepository supplierRepository, ProductRepository productRepository, InventoryService inventoryService) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrderDTO> getAllPurchaseOrders() {
        return purchaseOrderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PurchaseOrderDTO getPurchaseOrderById(Long id) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with id: " + id));
        return convertToDTO(po);
    }

    @Transactional
    public PurchaseOrderDTO createPurchaseOrder(PurchaseOrderDTO poDTO) {
        Supplier supplier = supplierRepository.findById(poDTO.getSupplierId())
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));

        PurchaseOrder po = new PurchaseOrder();
        po.setSupplier(supplier);
        po.setStatus(PurchaseOrder.PurchaseOrderStatus.DRAFT);
        po.setNotes(poDTO.getNotes());
        po.setOrderDate(poDTO.getOrderDate());
        po.setExpectedDeliveryDate(poDTO.getExpectedDeliveryDate());

        // Add items
        for (PurchaseOrderItemDTO itemDTO : poDTO.getItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + itemDTO.getProductId()));

            PurchaseOrderItem poItem = new PurchaseOrderItem();
            poItem.setPurchaseOrder(po);
            poItem.setProduct(product);
            poItem.setQuantity(itemDTO.getQuantity());
            poItem.setUnitCost(itemDTO.getUnitCost());

            po.getItems().add(poItem);
        }

        // Calculate total amount
        BigDecimal totalAmount = po.getItems().stream()
                .map(PurchaseOrderItem::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        po.setTotalAmount(totalAmount);

        PurchaseOrder savedPO = purchaseOrderRepository.save(po);
        return convertToDTO(savedPO);
    }

    @Transactional
    public PurchaseOrderDTO updatePurchaseOrderStatus(Long id, PurchaseOrder.PurchaseOrderStatus newStatus) {
        PurchaseOrder po = purchaseOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase order not found with id: " + id));

        PurchaseOrder.PurchaseOrderStatus currentStatus = po.getStatus();

        // Validate order status transition
        if (currentStatus == PurchaseOrder.PurchaseOrderStatus.CANCELLED) {
            throw new BusinessException("Cannot update a cancelled purchase order");
        }

        if (currentStatus == PurchaseOrder.PurchaseOrderStatus.RECEIVED) {
            throw new BusinessException("Cannot update a received purchase order");
        }

        switch (newStatus) {
            case ORDERED:
                if (currentStatus != PurchaseOrder.PurchaseOrderStatus.DRAFT) {
                    throw new BusinessException("Purchase order can only be ordered from DRAFT status. Current status: " + currentStatus);
                }
                po.setOrderDate(LocalDateTime.now());
                break;
            case RECEIVED:
                if (currentStatus != PurchaseOrder.PurchaseOrderStatus.ORDERED) {
                    throw new BusinessException("Purchase order can only be received from ORDERED status. Current status: " + currentStatus);
                }
                po.setExpectedDeliveryDate(LocalDateTime.now());
                receivePurchaseOrderItems(po);
                break;
            case CANCELLED:
                if (currentStatus == PurchaseOrder.PurchaseOrderStatus.RECEIVED) {
                    throw new BusinessException("Cannot cancel a received purchase order");
                }
                break;
            case DRAFT:
                throw new BusinessException("Cannot set purchase order status back to DRAFT");
        }

        po.setStatus(newStatus);
        PurchaseOrder updatedPO = purchaseOrderRepository.save(po);
        return convertToDTO(updatedPO);
    }

    private void receivePurchaseOrderItems(PurchaseOrder po) {
        for (PurchaseOrderItem item : po.getItems()) {
            item.setReceivedQuantity(item.getQuantity());

            inventoryService.adjustInventory(
                    item.getProduct().getProductId(),
                    item.getQuantity(),
                    "purchase",
                    "purchase_order",
                    po.getPoId(),
                    "PO receipt"
            );
        }
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrderDTO> getPurchaseOrdersBySupplier(Long supplierId) {
        return purchaseOrderRepository.findBySupplier_SupplierId(supplierId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrderDTO> getPurchaseOrdersByStatus(PurchaseOrder.PurchaseOrderStatus status) {
        return purchaseOrderRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private PurchaseOrderDTO convertToDTO(PurchaseOrder po) {
        List<PurchaseOrderItemDTO> itemDTOs = po.getItems().stream()
                .map(this::convertItemToDTO)
                .collect(Collectors.toList());

        return PurchaseOrderDTO.builder()
                .poId(po.getPoId())
                .supplierId(po.getSupplier().getSupplierId())
                .supplierName(po.getSupplier().getName())
                .orderDate(po.getOrderDate())
                .expectedDeliveryDate(po.getExpectedDeliveryDate())
                .status(po.getStatus())
                .totalAmount(po.getTotalAmount())
                .notes(po.getNotes())
                .items(itemDTOs)
                .build();
    }

    private PurchaseOrderItemDTO convertItemToDTO(PurchaseOrderItem item) {
        return PurchaseOrderItemDTO.builder()
                .poItemId(item.getPoItemId())
                .productId(item.getProduct().getProductId())
                .productName(item.getProduct().getName())
                .quantity(item.getQuantity())
                .unitCost(item.getUnitCost())
                .receivedQuantity(item.getReceivedQuantity())
                .subtotal(item.getSubtotal())
                .build();
    }
}
