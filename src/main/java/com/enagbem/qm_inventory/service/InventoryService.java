package com.enagbem.qm_inventory.service;


import com.enagbem.qm_inventory.dto.InventoryLogDTO;
import com.enagbem.qm_inventory.exception.ResourceNotFoundException;
import com.enagbem.qm_inventory.model.InventoryLog;
import com.enagbem.qm_inventory.model.Product;
import com.enagbem.qm_inventory.repository.InventoryLogRepository;
import com.enagbem.qm_inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {

    private final ProductRepository productRepository;
    private final InventoryLogRepository inventoryLogRepository;

    public InventoryService(ProductRepository productRepository, InventoryLogRepository inventoryLogRepository) {
        this.productRepository = productRepository;
        this.inventoryLogRepository = inventoryLogRepository;
    }

    @Transactional
    public void adjustInventory(Long productId, int quantityChange, String changeType,
                                String referenceType, Long referenceId, String notes) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        int newStock = product.getCurrentStock() + quantityChange;
        if (newStock < 0) {
            throw new IllegalStateException("Insufficient stock for product: " + product.getName());
        }

        product.setCurrentStock(newStock);
        productRepository.save(product);

        InventoryLog log = InventoryLog.builder()
                .product(product)
                .quantityChange(quantityChange)
                .changeType(changeType)
                .referenceType(referenceType)
                .referenceId(referenceId)
                .notes(notes)
                .createdBy("system") // In real app, use authenticated user
                .build();

        inventoryLogRepository.save(log);
    }

    @Transactional(readOnly = true)
    public List<InventoryLogDTO> getInventoryHistory(Long productId) {
        return inventoryLogRepository.findByProduct_ProductId(productId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InventoryLogDTO> getInventoryChangesBetweenDates(Long productId,
                                                                 LocalDateTime start,
                                                                 LocalDateTime end) {
        return inventoryLogRepository.findByProduct_ProductIdAndCreatedAtBetween(productId, start, end)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<Product> getLowStockItems() {
        return productRepository.findAll().stream()
                .filter(p -> p.getCurrentStock() <= p.getMinStockLevel())
                .collect(Collectors.toList());
    }

    private InventoryLogDTO convertToDTO(InventoryLog log) {
        return new InventoryLogDTO.Builder()
                .logId(log.getLogId())
                .productId(log.getProduct().getProductId())
                .productName(log.getProduct().getName())
                .changeType(log.getChangeType())
                .quantityChange(log.getQuantityChange())
                .referenceId(log.getReferenceId())
                .referenceType(log.getReferenceType())
                .notes(log.getNotes())
                .createdBy(log.getCreatedBy())
                .createdAt(log.getCreatedAt())
                .build();
    }
}
