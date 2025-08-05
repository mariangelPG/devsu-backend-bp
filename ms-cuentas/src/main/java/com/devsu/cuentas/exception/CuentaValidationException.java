package com.devsu.cuentas.exception;

public class CuentaValidationException extends RuntimeException {

    public CuentaValidationException(String message) {
        super(message);
    }

    public CuentaValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
