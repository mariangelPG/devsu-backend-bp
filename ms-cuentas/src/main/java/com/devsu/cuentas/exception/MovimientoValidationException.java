package com.devsu.cuentas.exception;

public class MovimientoValidationException extends RuntimeException {

    public MovimientoValidationException(String message) {
        super(message);
    }

    public MovimientoValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
