package com.enagbem.qm_inventory.service;

import com.enagbem.qm_inventory.dto.SupplierDTO;
import com.enagbem.qm_inventory.exception.ResourceNotFoundException;
import com.enagbem.qm_inventory.model.Supplier;
import com.enagbem.qm_inventory.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Transactional(readOnly = true)
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));
        return convertToDTO(supplier);
    }

    @Transactional
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        Supplier supplier = Supplier.builder()
                .name(supplierDTO.getName())
                .contactPerson(supplierDTO.getContactPerson())
                .email(supplierDTO.getEmail())
                .phone(supplierDTO.getPhone())
                .address(supplierDTO.getAddress())
                .leadTimeDays(supplierDTO.getLeadTimeDays())
                .reliabilityScore(supplierDTO.getReliabilityScore())
                .build();

        Supplier savedSupplier = supplierRepository.save(supplier);
        return convertToDTO(savedSupplier);
    }

    @Transactional
    public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier existingSupplier = supplierRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Supplier not found with id: " + id));

        existingSupplier.setName(supplierDTO.getName());
        existingSupplier.setContactPerson(supplierDTO.getContactPerson());
        existingSupplier.setEmail(supplierDTO.getEmail());
        existingSupplier.setPhone(supplierDTO.getPhone());
        existingSupplier.setAddress(supplierDTO.getAddress());
        existingSupplier.setLeadTimeDays(supplierDTO.getLeadTimeDays());
        existingSupplier.setReliabilityScore(supplierDTO.getReliabilityScore());

        Supplier updatedSupplier = supplierRepository.save(existingSupplier);
        return convertToDTO(updatedSupplier);
    }

    @Transactional
    public void deleteSupplier(Long id) {
        System.out.println("Checking existence of supplier with ID: " + id);
        if (!supplierRepository.existsById(id)) {
            throw new ResourceNotFoundException("Supplier not found with id: " + id);
        }
        System.out.println("Counting products for supplier ID: " + id);
        long productCount = supplierRepository.countProductsBySupplierId(id);
        System.out.println("Product count: " + productCount);
        if (productCount > 0) {
            throw new IllegalStateException("Cannot delete supplier with associated products");
        }
        System.out.println("Deleting supplier with ID: " + id);
        supplierRepository.deleteById(id);
        System.out.println("Delete completed for ID: " + id);
    }

    @Transactional(readOnly = true)
    public List<SupplierDTO> getReliableSuppliers(Double minScore) {
        return supplierRepository.findByReliabilityScoreGreaterThanEqual(minScore).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private SupplierDTO convertToDTO(Supplier supplier) {
        return SupplierDTO.builder()
                .supplierId(supplier.getSupplierId())
                .name(supplier.getName())
                .contactPerson(supplier.getContactPerson())
                .email(supplier.getEmail())
                .phone(supplier.getPhone())
                .address(supplier.getAddress())
                .leadTimeDays(supplier.getLeadTimeDays())
                .reliabilityScore(supplier.getReliabilityScore())
                .build();
    }
}