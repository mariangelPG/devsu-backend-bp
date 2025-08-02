package com.devsu.cliente.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.slf4j.MDC;

import java.time.LocalDateTime;
import java.util.UUID;

public class ErrorResponseDTO {

    private String error;
    private String mensaje;
    private int estatus;
    private String ruta;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ErrorResponseDTO() {
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponseDTO(String error, String mensaje, int estatus, String ruta) {
        this();
        this.error = error;
        this.mensaje = mensaje;
        this.estatus = estatus;
        this.ruta = ruta;
    }

    // Builder pattern para facilitar la creación
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private ErrorResponseDTO errorResponse;

        public Builder() {
            this.errorResponse = new ErrorResponseDTO();
        }

        public Builder error(String error) {
            this.errorResponse.error = error;
            return this;
        }

        public Builder mensaje(String mensaje) {
            this.errorResponse.mensaje = mensaje;
            return this;
        }

        public Builder estatus(int estatus) {
            this.errorResponse.estatus = estatus;
            return this;
        }

        public Builder ruta(String ruta) {
            this.errorResponse.ruta = ruta;
            return this;
        }

        public ErrorResponseDTO build() {
            return this.errorResponse;
        }
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getmensaje() {
        return mensaje;
    }

    public void setmensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public int getestatus() {
        return estatus;
    }

    public void setestatus(int estatus) {
        this.estatus = estatus;
    }

    public String getruta() {
        return ruta;
    }

    public void setruta(String ruta) {
        this.ruta = ruta;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}