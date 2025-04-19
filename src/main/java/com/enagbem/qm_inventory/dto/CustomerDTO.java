package com.enagbem.qm_inventory.dto;

public class CustomerDTO {
    private Long customerId;
    private String name;
    private String email;
    private String phone;
    private String address;

    // No-args constructor
    public CustomerDTO() {}

    // All-args constructor
    public CustomerDTO(Long customerId, String name, String email, String phone, String address) {
        this.customerId = customerId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    // Getters and setters
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // Manual Builder
    public static class Builder {
        private Long customerId;
        private String name;
        private String email;
        private String phone;
        private String address;

        public Builder() {}

        public Builder customerId(Long customerId) {
            this.customerId = customerId;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder phone(String phone) {
            this.phone = phone;
            return this;
        }

        public Builder address(String address) {
            this.address = address;
            return this;
        }

        public CustomerDTO build() {
            return new CustomerDTO(customerId, name, email, phone, address);
        }
    }
}
