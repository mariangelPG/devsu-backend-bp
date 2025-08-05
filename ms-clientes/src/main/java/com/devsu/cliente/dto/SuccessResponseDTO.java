package com.devsu.cliente.dto;

public class SuccessResponseDTO {

    private String mensaje;
    private Boolean estado;

    public SuccessResponseDTO(String mensaje, Boolean estado) {
        this.mensaje = mensaje;
        this.estado = estado;
    }

    public SuccessResponseDTO() {
    }


    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }


    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "SuccessResponseDTO{" +
                "mensaje='" + mensaje + '\'' +
                ", estado=" + estado +
                '}';
    }
}
