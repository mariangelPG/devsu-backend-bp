package com.devsu.cuentas.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public class ReporteDTO {
    private Long clienteId;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaInicio;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date fechaFin;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date fechaGeneracion;

    private List<CuentaReporteDTO> cuentas;

    public ReporteDTO(Long clienteId, Date fechaInicio, Date fechaFin) {
        this.clienteId = clienteId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Date getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(Date fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    public List<CuentaReporteDTO> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<CuentaReporteDTO> cuentas) {
        this.cuentas = cuentas;
    }

    @Override
    public String toString() {
        return "ReporteDTO{" +
                "clienteId=" + clienteId +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", fechaGeneracion=" + fechaGeneracion +
                ", cuentas=" + cuentas +
                '}';
    }
}
