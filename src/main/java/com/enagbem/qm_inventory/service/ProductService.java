package com.enagbem.qm_inventory.service;


import com.enagbem.qm_inventory.dto.ProductDTO;
import com.enagbem.qm_inventory.exception.ResourceNotFoundException;
import com.enagbem.qm_inventory.model.Category;
import com.enagbem.qm_inventory.model.Product;
import com.enagbem.qm_inventory.model.Supplier;
import com.enagbem.qm_inventory.repository.CategoryRepository;
import com.enagbem.qm_inventory.repository.ProductRepository;
import com.enagbem.qm_inventory.repository.SupplierRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;


    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<ProductDTO> getProductsPaginated(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));
        return convertToDTO(product);
    }

    @Transactional
    public ProductDTO createProduct(ProductDTO productDTO) {
        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Supplier supplier = null;
        if (productDTO.getSupplierId() != null) {
            supplier = supplierRepository.findById(productDTO.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        }

        Product product = Product.builder()
                .name(productDTO.getName())
                .description(productDTO.getDescription())
                .category(category)
                .price(productDTO.getPrice())
                .costPrice(productDTO.getCostPrice())
                .currentStock(productDTO.getCurrentStock())
                .minStockLevel(productDTO.getMinStockLevel())
                .maxStockLevel(productDTO.getMaxStockLevel())
                .supplier(supplier)
                .imagePath(productDTO.getImagePath())
                .build();

        Product savedProduct = productRepository.save(product);
        return convertToDTO(savedProduct);
    }

    @Transactional
    public ProductDTO updateProduct(Long id, ProductDTO productDTO) {
        Product existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id: " + id));

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Supplier supplier = null;
        if (productDTO.getSupplierId() != null) {
            supplier = supplierRepository.findById(productDTO.getSupplierId())
                    .orElseThrow(() -> new ResourceNotFoundException("Supplier not found"));
        }

        existingProduct.setName(productDTO.getName());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setCategory(category);
        existingProduct.setPrice(productDTO.getPrice());
        existingProduct.setCostPrice(productDTO.getCostPrice());
        existingProduct.setCurrentStock(productDTO.getCurrentStock());
        existingProduct.setMinStockLevel(productDTO.getMinStockLevel());
        existingProduct.setMaxStockLevel(productDTO.getMaxStockLevel());
        existingProduct.setSupplier(supplier);
        if (productDTO.getImagePath() != null) {
            existingProduct.setImagePath(productDTO.getImagePath());
        }

        Product updatedProduct = productRepository.save(existingProduct);
        return convertToDTO(updatedProduct);
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found with id: " + id);
        }
        productRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getLowStockProducts() {
        return productRepository.findAll().stream()
                .filter(p -> p.getCurrentStock() <= p.getMinStockLevel())
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private ProductDTO convertToDTO(Product product) {
        return ProductDTO.builder()
                .productId(product.getProductId())
                .name(product.getName())
                .description(product.getDescription())
                .categoryId(product.getCategory().getCategoryId())
                .categoryName(product.getCategory().getName())
                .price(product.getPrice())
                .costPrice(product.getCostPrice())
                .currentStock(product.getCurrentStock())
                .minStockLevel(product.getMinStockLevel())
                .maxStockLevel(product.getMaxStockLevel())
                .supplierId(product.getSupplier() != null ? product.getSupplier().getSupplierId() : null)
                .supplierName(product.getSupplier() != null ? product.getSupplier().getName() : null)
                .imagePath(product.getImagePath())
                .build();
    }
}
