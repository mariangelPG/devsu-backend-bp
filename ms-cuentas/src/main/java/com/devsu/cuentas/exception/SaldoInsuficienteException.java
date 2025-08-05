package com.devsu.cuentas.exception;

public class SaldoInsuficienteException extends RuntimeException {

    public SaldoInsuficienteException(String message) {
        super(message);
    }

    public SaldoInsuficienteException(Double saldoActual, Double montoSolicitado) {
        super(String.format("Saldo insuficiente. Saldo actual: %.2f, Monto solicitado: %.2f",
                saldoActual, montoSolicitado));
    }
}
