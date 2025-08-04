package com.devsu.cuentas.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.Date;

@Entity
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(nullable = false)
    private Date fecha;

    @NotBlank(message = "El tipo de movimiento es obligatorio")
    @Pattern(regexp = "^(Credito|Debito|Deposito|Retiro)$",
            message = "El tipo de movimiento debe ser 'Crédito', 'Débito', 'Depósito' o 'Retiro'")
    @Column(nullable = false)
    private String tipo;

    @NotNull(message = "El valor del movimiento es obligatorio")
    @Positive(message = "El valor del movimiento debe ser mayor a cero")
    @DecimalMax(value = "10000.0", message = "El valor del movimiento no puede exceder $10,000.00")
    @Column(nullable = false)
    private Double valor;

    @Column(nullable = false)
    private Double saldo;

    @NotNull(message = "El ID de la cuenta es obligatorio")
    @Positive(message = "El ID de la cuenta debe ser un número positivo")
    @Column(nullable = false)
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
