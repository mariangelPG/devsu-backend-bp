package com.devsu.cliente.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@DynamicUpdate
@Schema(description = "Representa la información detallada de un cliente, extendiendo la información de una persona.")
public class Cliente extends Persona {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Schema(description = "Identificador único del cliente (autogenerado)", accessMode = Schema.AccessMode.READ_ONLY)
    private Long clienteId;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "La contraseña debe contener al menos una mayúscula, una minúscula y un número")
    @Schema(description = "Contraseña para el acceso del cliente. Debe contener al menos una mayúscula, una minúscula y un número.",
            example = "Password123")
    private String contrasena;

    @NotNull(message = "El estado es obligatorio")
    @Schema(description = "Estado del cliente (true o false)", example = "true")
    private Boolean estado;


    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isEstado() {
        return estado;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }


    @Override
    public String toString() {
        return "Cliente{" +
                "nombre='" + nombre + '\'' +
                ", genero='" + genero + '\'' +
                ", edad=" + edad +
                ", identificacion='" + identificacion + '\'' +
                ", direccion='" + direccion + '\'' +
                ", telefono='" + telefono + '\'' +
                ", estado=" + estado +
                '}';
    }
}
