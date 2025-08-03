package com.devsu.cuentas.exception;

public class MovimientoNotFoundException extends RuntimeException {

    public MovimientoNotFoundException(String message) {
        super(message);
    }

    public MovimientoNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public MovimientoNotFoundException(Long movimientoId) {
        super(String.format("Movimiento con ID %d no encontrado", movimientoId));
    }
}
