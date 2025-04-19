package com.enagbem.qm_inventory.repository;

import com.enagbem.qm_inventory.dto.CustomerDTO;
import com.enagbem.qm_inventory.model.Category;
import com.enagbem.qm_inventory.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Collection<Customer> findByNameContainingIgnoreCase(String query);
}
