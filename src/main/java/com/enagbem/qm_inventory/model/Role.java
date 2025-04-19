package com.enagbem.qm_inventory.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "roles")
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @Column(nullable = false, unique = true)
    private String name;

    // No-args constructor
    public Role() {
    }

    // All-args constructor
    public Role(Long roleId, String name) {
        this.roleId = roleId;
        this.name = name;
    }

    // Getters and Setters
    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Implementation of GrantedAuthority
    @Override
    public String getAuthority() {
        return name;
    }

    // Manual Builder
    public static class Builder {
        private Long roleId;
        private String name;

        public Builder() {
        }

        public Builder roleId(Long roleId) {
            this.roleId = roleId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Role build() {
            return new Role(roleId, name);
        }
    }

    public static Builder builder() {
        return new Builder();
    }
}
