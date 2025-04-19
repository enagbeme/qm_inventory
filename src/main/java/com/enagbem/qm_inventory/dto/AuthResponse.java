package com.enagbem.qm_inventory.dto;

import java.util.List;
import java.util.Set;

public class AuthResponse {
    private String token;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private List<String> roles;

    // Private constructor
    private AuthResponse(Builder builder) {
        this.token = builder.token;
        this.username = builder.username;
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.roles = (List<String>) builder.roles;
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public List<String> getRoles() {
        return roles;
    }





    // Static Builder class
    public static class Builder {
        private String token;
        private String username;
        private String email;
        private String firstName;
        private String lastName;
        private Set<String> roles;

        public Builder token(String token) {
            this.token = token;
            return this;
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder roles(Set<String> roles) {
            this.roles = roles;
            return this;
        }

        public AuthResponse build() {
            return new AuthResponse(this);
        }
    }
}