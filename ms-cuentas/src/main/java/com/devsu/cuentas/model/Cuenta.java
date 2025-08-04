package com.devsu.cuentas.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Schema(description = "Modelo que representa una cuenta bancaria.")
public class Cuenta {
    @Id
    @NotNull(message = "El número de cuenta es obligatorio")
    @Positive(message = "El número de cuenta debe ser un número positivo")
    @Schema(description = "Número único de la cuenta bancaria. Es el identificador principal.", example = "1234567890", accessMode = Schema.AccessMode.READ_ONLY)
    private Long numeroCuenta;

    @NotBlank(message = "El tipo de cuenta es obligatorio")
    @Pattern(regexp = "^(Ahorros|Corriente)$",
            message = "El tipo de cuenta debe ser 'Ahorros' o 'Corriente'")
    @Column(nullable = false)
    @Schema(description = "Tipo de cuenta bancaria. Solo 'Ahorros' o 'Corriente'.", example = "Ahorros", allowableValues = {"Ahorros", "Corriente"})
    private String tipoCuenta;

    @NotNull(message = "El saldo inicial es obligatorio")
    @DecimalMin(value = "0.0", message = "El saldo inicial no puede ser negativo")
    @Column(nullable = false)
    @Schema(description = "Saldo inicial de la cuenta en el momento de su creación.", example = "1500.50", minimum = "0.0")
    private Double saldoInicial;

    @NotNull(message = "El estado es obligatorio")
    @Column(nullable = false)
    @Schema(description = "Estado actual de la cuenta (activa o inactiva).", example = "true")
    private Boolean estado;

    @NotNull(message = "El ID del cliente es obligatorio")
    @Positive(message = "El ID del cliente debe ser un número positivo")
    @Column(nullable = false)
    @Schema(description = "Identificador del cliente al que pertenece la cuenta.", example = "1")
    private Long clienteId;

    public Long getNumeroCuenta() {
        return numeroCuenta;
    }

    public void setNumeroCuenta(Long numeroCuenta) {
        this.numeroCuenta = numeroCuenta;
    }

    public String getTipoCuenta() {
        return tipoCuenta;
    }

    public void setTipoCuenta(String tipoCuenta) {
        this.tipoCuenta = tipoCuenta;
    }

    public Double getSaldoInicial() {
        return saldoInicial;
    }

    public void setSaldoInicial(Double saldoInicial) {
        this.saldoInicial = saldoInicial;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    @Override
    public String toString() {
        return "Cuenta{" +
                "numeroCuenta=" + numeroCuenta +
                ", tipoCuenta='" + tipoCuenta + '\'' +
                ", saldoInicial=" + saldoInicial +
                ", estado=" + estado +
                ", clienteId=" + clienteId +
                '}';
    }
}
