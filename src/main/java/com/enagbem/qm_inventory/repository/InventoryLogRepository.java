package com.enagbem.qm_inventory.repository;


import com.enagbem.qm_inventory.model.InventoryLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface InventoryLogRepository extends JpaRepository<InventoryLog, Long> {
    List<InventoryLog> findByProduct_ProductId(Long productId);
    List<InventoryLog> findByProduct_ProductIdAndChangeType(Long productId, String changeType);
    List<InventoryLog> findByProduct_ProductIdAndCreatedAtBetween(Long productId, LocalDateTime start, LocalDateTime end);
}
