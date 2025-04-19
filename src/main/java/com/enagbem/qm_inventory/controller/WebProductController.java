package com.enagbem.qm_inventory.controller;

import com.enagbem.qm_inventory.dto.ProductDTO;
import com.enagbem.qm_inventory.repository.InventoryLogRepository;
import com.enagbem.qm_inventory.service.CategoryService;
import com.enagbem.qm_inventory.service.InventoryService;
import com.enagbem.qm_inventory.service.ProductService;
import com.enagbem.qm_inventory.service.SupplierService;
import com.enagbem.qm_inventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.UUID;
import java.nio.file.StandardCopyOption;

@Controller
@RequestMapping("/products")
public class WebProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final SupplierService supplierService;
    private final UserRepository userRepository;


    @Value("${app.upload.dir:${user.home}}")
    private String uploadDir;

    public WebProductController(ProductService productService,
                                CategoryService categoryService,
                                SupplierService supplierService,
                                UserRepository userRepository) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.supplierService = supplierService;
        this.userRepository = userRepository;


    }

    private void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            userRepository.findByUsername(username).ifPresent(user -> model.addAttribute("user", user));
        }
    }

    @GetMapping
    public String getAllProducts(Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("products", productService.getAllProducts());
        return "product-management";
    }

    @GetMapping("/new")
    public String showCreateProductForm(Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("productDTO", new ProductDTO());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "product-form";
    }

    @PostMapping
    public String createProduct(@ModelAttribute ProductDTO productDTO,
                               @RequestParam(value = "image", required = false) MultipartFile image,
                               RedirectAttributes redirectAttributes) {
        try {
            if (image != null && !image.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                productDTO.setImagePath(fileName);
            }
            
            productService.createProduct(productDTO);




            redirectAttributes.addFlashAttribute("message", "Product created successfully!");
            return "redirect:/products";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
            return "redirect:/products/new";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create product: " + e.getMessage());
            return "redirect:/products/new";
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditProductForm(@PathVariable Long id, Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("productDTO", productService.getProductById(id));
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("suppliers", supplierService.getAllSuppliers());
        return "product-form";
    }

    @PostMapping("/{id}")
    public String updateProduct(@PathVariable Long id,
                               @ModelAttribute ProductDTO productDTO,
                               @RequestParam(value = "image", required = false) MultipartFile image,
                               RedirectAttributes redirectAttributes) {
        try {
            if (image != null && !image.isEmpty()) {
                String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(image.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                productDTO.setImagePath(fileName);
            }
            
            productService.updateProduct(id, productDTO);
            redirectAttributes.addFlashAttribute("message", "Product updated successfully!");
            return "redirect:/products";
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload image: " + e.getMessage());
            return "redirect:/products/" + id + "/edit";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update product: " + e.getMessage());
            return "redirect:/products/" + id + "/edit";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        return "redirect:/products";
    }

    @GetMapping("/category/{categoryId}")
    public String getProductsByCategory(@PathVariable Long categoryId, Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("products", productService.getProductsByCategory(categoryId));
        model.addAttribute("categories", categoryService.getAllCategories());
        return "product-management";
    }
}