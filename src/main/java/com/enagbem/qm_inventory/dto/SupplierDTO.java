package com.enagbem.qm_inventory.dto;



import lombok.*;

@Data
@Builder
public class SupplierDTO {
    private Long supplierId;
    private String name;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private Integer leadTimeDays;
    private Double reliabilityScore;

    public SupplierDTO() {
    }

    public SupplierDTO(Long supplierId, String name, String contactPerson, String email, String phone, String address,
                       Integer leadTimeDays, Double reliabilityScore) {
        this.supplierId = supplierId;
        this.name = name;
        this.contactPerson = contactPerson;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.leadTimeDays = leadTimeDays;
        this.reliabilityScore = reliabilityScore;
    }

    public static SupplierDTOBuilder builder() {
        return new SupplierDTOBuilder();
    }

    public static class SupplierDTOBuilder {
        private final SupplierDTO supplierDTO = new SupplierDTO();

        public SupplierDTOBuilder supplierId(Long supplierId) {
            supplierDTO.setSupplierId(supplierId);
            return this;
        }

        public SupplierDTOBuilder name(String name) {
            supplierDTO.setName(name);
            return this;
        }

        public SupplierDTOBuilder contactPerson(String contactPerson) {
            supplierDTO.setContactPerson(contactPerson);
            return this;
        }

        public SupplierDTOBuilder email(String email) {
            supplierDTO.setEmail(email);
            return this;
        }

        public SupplierDTOBuilder phone(String phone) {
            supplierDTO.setPhone(phone);
            return this;
        }

        public SupplierDTOBuilder address(String address) {
            supplierDTO.setAddress(address);
            return this;
        }

        public SupplierDTOBuilder leadTimeDays(Integer leadTimeDays) {
            supplierDTO.setLeadTimeDays(leadTimeDays);
            return this;
        }

        public SupplierDTOBuilder reliabilityScore(Double reliabilityScore) {
            supplierDTO.setReliabilityScore(reliabilityScore);
            return this;
        }

        public SupplierDTO build() {
            return supplierDTO;
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
}

