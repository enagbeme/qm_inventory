package com.enagbem.qm_inventory.model;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "suppliers")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplierId;

    @Column(nullable = false)
    private String name;

    @Column(name = "contact_person")
    private String contactPerson;

    private String email;

    @Column(nullable = false)
    private String phone;

    private String address;

    @Column(name = "lead_time_days")
    private Integer leadTimeDays;

    @Column(name = "reliability_score")
    private Double reliabilityScore;

    @OneToMany(mappedBy = "supplier")
    @ToString.Exclude
    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "supplier")
    @ToString.Exclude
    private List<PurchaseOrder> purchaseOrders = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    public Supplier() {
    }

    public Supplier(Long supplierId, String name, String contactPerson, String email, String phone, String address,
                    Integer leadTimeDays, Double reliabilityScore, List<Product> products, List<PurchaseOrder> purchaseOrders, LocalDateTime createdAt) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.leadTimeDays = leadTimeDays;
        this.reliabilityScore = reliabilityScore;
        this.products = products;
        this.purchaseOrders = purchaseOrders;
        this.createdAt = createdAt;
    }

    public static SupplierBuilder builder() {
        return new SupplierBuilder();
    }

    public static class SupplierBuilder {
        private final Supplier supplier = new Supplier();

        public SupplierBuilder supplierId(Long supplierId) {
            supplier.setSupplierId(supplierId);
            return this;
        }

        public SupplierBuilder name(String name) {
            supplier.setName(name);
            return this;
        }

        public SupplierBuilder contactPerson(String contactPerson) {
            supplier.setContactPerson(contactPerson);
            return this;
        }

        public SupplierBuilder email(String email) {
            supplier.setEmail(email);
            return this;
        }

        public SupplierBuilder phone(String phone) {
            supplier.setPhone(phone);
            return this;
        }

        public SupplierBuilder address(String address) {
            supplier.setAddress(address);
            return this;
        }

        public SupplierBuilder leadTimeDays(Integer leadTimeDays) {
            supplier.setLeadTimeDays(leadTimeDays);
            return this;
        }

        public SupplierBuilder reliabilityScore(Double reliabilityScore) {
            supplier.setReliabilityScore(reliabilityScore);
            return this;
        }

        public SupplierBuilder products(List<Product> products) {
            supplier.setProducts(products);
            return this;
        }

        public SupplierBuilder purchaseOrders(List<PurchaseOrder> purchaseOrders) {
            supplier.setPurchaseOrders(purchaseOrders);
            return this;
        }

        public SupplierBuilder createdAt(LocalDateTime createdAt) {
            supplier.setCreatedAt(createdAt);
            return this;
        }

        public Supplier build() {
            return supplier;
        }
    }

    // Getters and Setters
    public Long getSupplierId() { return supplierId; }

    public void setSupplierId(Long supplierId) { this.supplierId = supplierId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getContactPerson() { return contactPerson; }

    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getAddress() { return address; }

    public void setAddress(String address) { this.address = address; }

    public Integer getLeadTimeDays() { return leadTimeDays; }

    public void setLeadTimeDays(Integer leadTimeDays) { this.leadTimeDays = leadTimeDays; }

    public Double getReliabilityScore() { return reliabilityScore; }

    public void setReliabilityScore(Double reliabilityScore) { this.reliabilityScore = reliabilityScore; }

    public List<Product> getProducts() { return products; }

    public void setProducts(List<Product> products) { this.products = products; }

    public List<PurchaseOrder> getPurchaseOrders() { return purchaseOrders; }

    public void setPurchaseOrders(List<PurchaseOrder> purchaseOrders) { this.purchaseOrders = purchaseOrders; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
