package com.enagbem.qm_inventory.repository;

import com.enagbem.qm_inventory.model.Category;
import com.enagbem.qm_inventory.model.ParentCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByParentCategoryIsNull(); // For top-level categories
    // Change this method to accept the ParentCategory enum directly
    List<Category> findByParentCategory(ParentCategory parentCategory);
}