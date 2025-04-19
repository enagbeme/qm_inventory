package com.enagbem.qm_inventory.dto;

import java.util.List;

public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;
    private List<String> errors;

    // Private constructor to prevent direct instantiation
    private ErrorResponse(Builder builder) {
        this.status = builder.status;
        this.message = builder.message;
        this.timestamp = builder.timestamp;
        this.errors = builder.errors;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    // Static builder class
    public static class Builder {
        private int status;
        private String message;
        private long timestamp;
        private List<String> errors;

        public Builder setStatus(int status) {
            this.status = status;
            return this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Builder setTimestamp(long timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public Builder setErrors(List<String> errors) {
            this.errors = errors;
            return this;
        }

        // Method to build the ErrorResponse
        public ErrorResponse build() {
            return new ErrorResponse(this);
        }
    }
}
