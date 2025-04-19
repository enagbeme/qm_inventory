package com.enagbem.qm_inventory.controller;

import com.enagbem.qm_inventory.dto.CategoryDTO;
import com.enagbem.qm_inventory.service.CategoryService;
import com.enagbem.qm_inventory.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @Autowired
    public CategoryController(CategoryService categoryService, UserRepository userRepository) {
        this.categoryService = categoryService;
        this.userRepository = userRepository;
    }

    private void addUserToModel(Model model, Principal principal) {
        if (principal != null) {
            String username = principal.getName();
            userRepository.findByUsername(username).ifPresent(user -> model.addAttribute("user", user));
        }
    }

    @GetMapping
    public String getAllCategories(Model model, Principal principal) {
        addUserToModel(model, principal);
        List<CategoryDTO> categories = categoryService.getAllCategories();
        model.addAttribute("categories", categories);
        return "category-list"; // Ensure this template exists
    }

    @GetMapping("/new")
    public String showCreateCategoryForm(Model model, Principal principal) {
        addUserToModel(model, principal);
        model.addAttribute("categoryDTO", new CategoryDTO());
        return "category-form"; // Ensure this template exists
    }

    @PostMapping
    public String createCategory(@ModelAttribute CategoryDTO categoryDTO,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.createCategory(categoryDTO);
            redirectAttributes.addFlashAttribute("message", "Category created successfully!");
            return "redirect:/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create category: " + e.getMessage());
            return "redirect:/categories/new"; // Redirects back to the form on error
        }
    }

    @GetMapping("/{id}/edit")
    public String showEditCategoryForm(@PathVariable Long id, Model model, Principal principal) {
        addUserToModel(model, principal);
        CategoryDTO category = categoryService.getCategoryById(id);
        model.addAttribute("categoryDTO", category);
        return "category-form"; // Ensure this template exists
    }

    @PostMapping("/{id}")
    public String updateCategory(@PathVariable Long id,
                                 @ModelAttribute CategoryDTO categoryDTO,
                                 RedirectAttributes redirectAttributes) {
        try {
            categoryService.updateCategory(id, categoryDTO);
            redirectAttributes.addFlashAttribute("message", "Category updated successfully!");
            return "redirect:/categories";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update category: " + e.getMessage());
            return "redirect:/categories/" + id + "/edit"; // Redirect back to edit form on error
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteCategory(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            categoryService.deleteCategory(id);
            redirectAttributes.addFlashAttribute("message", "Category deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete category: " + e.getMessage());
        }
        return "redirect:/categories";
    }
}