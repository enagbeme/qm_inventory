package com.enagbem.qm_inventory.repository;


import com.enagbem.qm_inventory.model.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    List<Supplier> findByNameContainingIgnoreCase(String name);
    List<Supplier> findByReliabilityScoreGreaterThanEqual(Double minScore);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.supplier.supplierId = :supplierId")
    long countProductsBySupplierId(@Param("supplierId") Long supplierId);
}
