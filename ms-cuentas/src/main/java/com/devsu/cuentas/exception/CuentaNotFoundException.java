package com.devsu.cuentas.exception;

public class CuentaNotFoundException extends RuntimeException {

    public CuentaNotFoundException(String message) {
        super(message);
    }

    public CuentaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public CuentaNotFoundException(Long cuentaId) {
        super(String.format("Cuenta con ID %d no encontrada", cuentaId));
    }
}
