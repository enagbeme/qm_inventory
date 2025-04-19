package com.enagbem.qm_inventory.dto;

import com.enagbem.qm_inventory.model.ParentCategory;

public class CategoryDTO {
    private Long categoryId;
    private String name;
    private String description;
    private ParentCategory parentCategory; // Use enum directly

    // No-args constructor
    public CategoryDTO() {}

    // All-args constructor
    public CategoryDTO(Long categoryId, String name, String description, ParentCategory parentCategory) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.parentCategory = parentCategory; // Initialize this field
    }

    // Getters and setters
    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ParentCategory getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(ParentCategory parentCategory) {
        this.parentCategory = parentCategory;
    }

    // Method to get the name of the parent category
    public String getParentCategoryName() {
        return parentCategory != null ? parentCategory.name() : null;
    }

    // Builder class
    public static class Builder {
        private Long categoryId;
        private String name;
        private String description;
        private ParentCategory parentCategory; // Use enum directly

        public Builder() {}

        public Builder categoryId(Long categoryId) {
            this.categoryId = categoryId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public Builder parentCategory(ParentCategory parentCategory) { // Use enum directly
            this.parentCategory = parentCategory;
            return this;
        }

        public CategoryDTO build() {
            return new CategoryDTO(categoryId, name, description, parentCategory);
        }
    }
}