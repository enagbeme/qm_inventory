package com.enagbem.qm_inventory.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class AuthenticationException extends InventoryException {
    public AuthenticationException(String message) {
        super(message);
    }
}
