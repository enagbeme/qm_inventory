package com.enagbem.qm_inventory.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class AuthorizationException extends InventoryException {
    public AuthorizationException(String message) {
        super(message);
    }
}
