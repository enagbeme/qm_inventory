package com.enagbem.qm_inventory.exception;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InventoryStockException extends BusinessException {
    public InventoryStockException(String message) {
        super(message);
    }

    public InventoryStockException(String productName, int available, int requested) {
        super(String.format("Insufficient stock for %s. Available: %d, Requested: %d",
                productName, available, requested));
    }
}
