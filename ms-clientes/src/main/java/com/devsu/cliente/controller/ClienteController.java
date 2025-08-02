package com.devsu.cliente.controller;


import com.devsu.cliente.model.Cliente;
import com.devsu.cliente.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        Cliente nuevoCliente = clienteService.guardarCliente(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable Long clienteId) {

        Optional<Cliente> cliente = clienteService.obtenerClientePorId(clienteId);
        return cliente.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long clienteId, @RequestBody Cliente clienteActualizado) {
        Optional<Cliente> clienteExistente = clienteService.obtenerClientePorId(clienteId);

        if (clienteExistente.isPresent()) {
            clienteActualizado.setClienteId(clienteId);
            Cliente clienteGuardado = clienteService.guardarCliente(clienteActualizado);
            return new ResponseEntity<>(clienteGuardado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Void> eliminarCliente(@PathVariable Long clienteId) {
        Optional<Cliente> clienteExistente = clienteService.obtenerClientePorId(clienteId);

        if (clienteExistente.isPresent()) {
            clienteService.eliminarCliente(clienteId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{clienteId}")
    public ResponseEntity<Cliente> actualizarCampo(@PathVariable Long clienteId, @RequestBody Cliente camposActualizados) {
        Optional<Cliente> clienteExistente = clienteService.obtenerClientePorId(clienteId);

        if (clienteExistente.isPresent()) {
            Cliente clienteGuardado = clienteService.actualizarCampos(clienteExistente.get(), camposActualizados);
            return new ResponseEntity<>(clienteGuardado, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}

