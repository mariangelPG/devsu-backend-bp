package com.devsu.cuentas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Date;

@Entity
@Schema(description = "Modelo que registra un movimiento financiero en una cuenta.")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Identificador único del movimiento (autogenerado).", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    @Schema(description = "Fecha y hora en que se realizó el movimiento.", example = "2025-08-04 10:30:00", format = "date-time")
    private Date fecha;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Pattern(regexp = "^(Credito|Debito|Deposito|Retiro)$",
            message = "El tipo de movimiento debe ser 'Crédito', 'Débito', 'Depósito' o 'Retiro'")
    @Column(nullable = false)
    @Schema(description = "Tipo de movimiento financiero.", example = "Deposito", allowableValues = {"Credito", "Debito", "Deposito", "Retiro"})
    private String tipo;

    @NotNull(message = "El valor del movimiento es obligatorio")
    @Positive(message = "El valor del movimiento debe ser mayor a cero")
    @DecimalMax(value = "10000.0", message = "El valor del movimiento no puede exceder $10,000.00")
    @Column(nullable = false)
    @Schema(description = "Monto de la transacción.", example = "500.00", minimum = "0.0", maximum = "10000.0")
    private Double valor;

    @Column(nullable = false)
    @Schema(description = "Saldo de la cuenta después de realizar este movimiento.", example = "2000.50")
    private Double saldo;

    @NotNull(message = "El ID de la cuenta es obligatorio")
    @Positive(message = "El ID de la cuenta debe ser un número positivo")
    @Column(nullable = false)
    @Schema(description = "Identificador de la cuenta a la que pertenece el movimiento.", example = "1234567890")
    private Long cuentaId;


    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Double getSaldo() {
        return saldo;
    }

    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "fecha=" + fecha +
                ", tipo='" + tipo + '\'' +
                ", valor=" + valor +
                ", saldo=" + saldo +
                ", cuentaId=" + cuentaId +
                '}';
    }
}
