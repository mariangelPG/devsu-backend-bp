package com.devsu.cliente.dto;

import com.devsu.cliente.model.Cliente;

public class ClienteDTO {

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

        if (clienteDto.isEstado()) {
            clienteExistente.setEstado(clienteDto.isEstado());
        }

        return clienteExistente;
    }
}
