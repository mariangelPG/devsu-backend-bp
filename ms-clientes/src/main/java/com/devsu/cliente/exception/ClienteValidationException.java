package com.devsu.cliente.exception;

public class ClienteValidationException extends RuntimeException {

    public ClienteValidationException(String message) {
        super(message);
    }

    public ClienteValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}