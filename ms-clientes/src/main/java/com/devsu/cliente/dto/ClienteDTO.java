package com.devsu.cliente.dto;

import com.devsu.cliente.model.Cliente;
import jakarta.validation.constraints.*;

public class ClienteDTO {

    @Size(min = 2, max = 100, message = "El nombre debe tener entre 2 y 100 caracteres")
    @Pattern(regexp = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$",
            message = "El nombre solo puede contener letras y espacios")
    private String nombre;

    @Pattern(regexp = "^(Masculino|Femenino|Otro)$",
            message = "El género debe ser: Masculino, Femenino u Otro")
    private String genero;

    @Min(value = 18, message = "La edad debe ser mayor o igual a 18 años")
    @Max(value = 120, message = "La edad debe ser menor o igual a 120 años")
    private Integer edad;

    @Size(min = 8, max = 20, message = "La identificación debe tener entre 8 y 20 caracteres")
    @Pattern(regexp = "^[0-9]+$", message = "La identificación solo puede contener números")
    private String identificacion;

    @Size(min = 5, max = 200, message = "La dirección debe tener entre 5 y 200 caracteres")
    private String direccion;

    @Pattern(regexp = "^[+]?[0-9]{10,15}$",
            message = "El teléfono debe tener entre 10 y 15 dígitos, opcionalmente comenzando con +")
    private String telefono;

    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "La contraseña debe contener al menos una mayúscula, una minúscula y un número")
    private String contrasena;

    private Boolean estado;

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

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public Boolean getEstado() {
        return estado;
    }

    public void setEstado(Boolean estado) {
        this.estado = estado;
    }


    /**
     * Convierte ClientePatchDTO a Cliente (para validaciones)
     * Solo para uso interno en validaciones
     */
    public static Cliente toCliente(ClienteDTO patchDTO) {
        if (patchDTO == null) {
            return null;
        }

        Cliente cliente = new Cliente();
        if (patchDTO.getNombre() != null) {
            cliente.setNombre(patchDTO.getNombre());
        }

        if (patchDTO.getGenero() != null) {
            cliente.setGenero(patchDTO.getGenero());
        }

        if (patchDTO.getEdad() != null) {
            cliente.setEdad(patchDTO.getEdad());
        }

        if (patchDTO.getIdentificacion() != null) {
            cliente.setIdentificacion(patchDTO.getIdentificacion());
        }

        if (patchDTO.getDireccion() != null) {
            cliente.setDireccion(patchDTO.getDireccion());
        }

        if (patchDTO.getTelefono() != null) {
            cliente.setTelefono(patchDTO.getTelefono());
        }

        if (patchDTO.getContrasena() != null) {
            cliente.setContrasena(patchDTO.getContrasena());
        }

        if (patchDTO.getEstado() != null) {
            cliente.setEstado(patchDTO.getEstado());
        }

        return cliente;
    }


    public static Cliente llenarCliente(Cliente clienteExistente, Cliente clienteDto) {

        if (clienteDto.getNombre() != null) {
            clienteExistente.setNombre(clienteDto.getNombre());
        }

        if (clienteDto.getGenero() != null) {
            clienteExistente.setGenero(clienteDto.getGenero());
        }

        if (clienteDto.getEdad() != null) {
            clienteExistente.setEdad(clienteDto.getEdad());
        }

        if (clienteDto.getIdentificacion() != null) {
            clienteExistente.setIdentificacion(clienteDto.getIdentificacion());
        }

        if (clienteDto.getDireccion() != null) {
            clienteExistente.setDireccion(clienteDto.getDireccion());
        }

        if (clienteDto.getTelefono() != null) {
            clienteExistente.setTelefono(clienteDto.getTelefono());
        }

        if (clienteDto.getContrasena() != null) {
            clienteExistente.setContrasena(clienteDto.getContrasena());
        }

        if (clienteDto.getEstado() != null) {
            clienteExistente.setEstado(clienteDto.isEstado());
        }

        return clienteExistente;
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
