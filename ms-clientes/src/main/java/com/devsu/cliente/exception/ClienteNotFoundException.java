package com.devsu.cliente.exception;

public class ClienteNotFoundException extends RuntimeException {

    public ClienteNotFoundException(String message) {
        super(message);
    }

    public ClienteNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClienteNotFoundException(Long clienteId) {
        super(String.format("Cliente con ID %d no encontrado", clienteId));
    }
}

