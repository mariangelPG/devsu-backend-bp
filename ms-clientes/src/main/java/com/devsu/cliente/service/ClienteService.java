package com.devsu.cliente.service;

import com.devsu.cliente.dto.ClienteDTO;
import com.devsu.cliente.model.Cliente;
import com.devsu.cliente.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente guardarCliente(Cliente cliente) {
        return clienteRepository.save(cliente);
    }

    public Optional<Cliente> obtenerClientePorId(Long clienteId) {
        return clienteRepository.findById(clienteId);
    }

    public void eliminarCliente(Long clienteId) {
        clienteRepository.deleteById(clienteId);
    }

    public Cliente actualizarCampos(Cliente clienteExistente, Cliente clienteNuevo){
        ClienteDTO.llenarCliente(clienteExistente, clienteNuevo);
        return clienteRepository.save(clienteExistente);
    }
}
