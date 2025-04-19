package com.enagbem.qm_inventory.dto;


import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class AuthRequest {
    private String username;
    private String password;


    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}