package com.devsu.cliente.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.MappedSuperclass;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Column;
import jakarta.validation.constraints.*;

@MappedSuperclass
@DynamicUpdate
@Schema(description = "Representa la información detallada de un cliente, extendiendo la información de una persona.")
public abstract class Persona {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$",
            message = "El nombre solo puede contener letras y espacios")
    @Column(nullable = false)
    @Schema(description = "Nombre del cliente", accessMode = Schema.AccessMode.READ_ONLY)
    protected String nombre;

    @Pattern(regexp = "^(Masculino|Femenino|Otro)$",
            message = "El género debe ser: Masculino, Femenino u Otro")
    @Schema(description = "Género de la persona", example = "Masculino", allowableValues = {"Masculino", "Femenino", "Otro"})
    protected String genero;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 18, message = "La edad debe ser mayor o igual a 18 años")
    @Max(value = 120, message = "La edad debe ser menor o igual a 120 años")
    @Schema(description = "Edad de la persona", example = "35", minimum = "18", maximum = "120")
    protected Integer edad;

    @NotBlank(message = "La identificación es obligatoria")
    @Size(min = 8, max = 20, message = "La identificación debe tener entre 8 y 20 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "La identificación solo puede contener números")
    @Column(unique = true, nullable = false)
    @Schema(description = "Número de identificación único", example = "1725354020", pattern = "^[0-9]+$")
    protected String identificacion;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(min = 5, max = 200, message = "La dirección debe tener entre 5 y 200 caracteres")
    @Schema(description = "Dirección de residencia de la persona", example = "Av. Eloy Alfaro y Amazonas, Quito")
    protected String direccion;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$",
            message = "El teléfono debe tener entre 10 y 15 dígitos, opcionalmente comenzando con +")
    @Schema(description = "Número de teléfono de la persona", example = "0998765432", pattern = "^[+]?[0-9]{10,15}$")
    protected String telefono;



    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getEdad() {
        return edad;
    }

    public void setEdad(Integer edad) {
        this.edad = edad;
    }

    public String getIdentificacion() {
        return identificacion;
    }

    public void setIdentificacion(String identificacion) {
        this.identificacion = identificacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}