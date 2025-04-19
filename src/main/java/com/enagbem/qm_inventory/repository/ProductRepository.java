package com.enagbem.qm_inventory.repository;


import com.enagbem.qm_inventory.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_CategoryId(Long categoryId);
    List<Product> findByCurrentStockLessThanEqual(Integer minStockLevel);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Product> findBySupplier_SupplierId(Long supplierId);
}
