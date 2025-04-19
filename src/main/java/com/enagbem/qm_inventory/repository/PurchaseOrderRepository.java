package com.enagbem.qm_inventory.repository;


import com.enagbem.qm_inventory.model.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    List<PurchaseOrder> findByStatus(PurchaseOrder.PurchaseOrderStatus status);
    List<PurchaseOrder> findBySupplier_SupplierId(Long supplierId);
    List<PurchaseOrder> findByOrderDateBetween(LocalDateTime start, LocalDateTime end);
}
