package com.enagbem.qm_inventory.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class DataIntegrityViolationException extends BusinessException {
    public DataIntegrityViolationException(String message) {
        super(message);
    }

    public DataIntegrityViolationException(String resourceName, String dependentResources) {
        super(String.format("Cannot delete %s because it has associated %s",
                resourceName, dependentResources));
    }
}
