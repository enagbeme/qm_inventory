package com.enagbem.qm_inventory.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "parent_category")
    private ParentCategory parentCategory;  // Use enum here

    @OneToMany(mappedBy = "parentCategory")
    private Set<Category> subCategories = new HashSet<>();

    @OneToMany(mappedBy = "category")
    private Set<Product> products = new HashSet<>();

    // No-args constructor
    public Category() {}

    // All-args constructor
    public Category(Long categoryId, String name, String description, ParentCategory parentCategory, Set<Category> subCategories, Set<Product> products) {
        this.categoryId = categoryId;
        this.name = name;
        this.description = description;
        this.parentCategory = parentCategory;
        this.subCategories = subCategories;
        this.products = products;
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

    public Set<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(Set<Category> subCategories) {
        this.subCategories = subCategories;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    // Manual Builder class
    public static class Builder {
        private Long categoryId;
        private String name;
        private String description;
        private ParentCategory parentCategory;
        private Set<Category> subCategories = new HashSet<>();
        private Set<Product> products = new HashSet<>();

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

        public Builder parentCategory(ParentCategory parentCategory) {
            this.parentCategory = parentCategory;
            return this;
        }

        public Builder subCategories(Set<Category> subCategories) {
            this.subCategories = subCategories;
            return this;
        }

        public Builder products(Set<Product> products) {
            this.products = products;
            return this;
        }

        public Category build() {
            return new Category(categoryId, name, description, parentCategory, subCategories, products);
        }
    }
}