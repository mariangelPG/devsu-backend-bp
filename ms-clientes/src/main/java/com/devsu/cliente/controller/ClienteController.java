package com.devsu.cliente.controller;


import com.devsu.cliente.model.Cliente;
import com.devsu.cliente.service.ClienteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public ResponseEntity<Cliente> crearCliente(@RequestBody Cliente cliente) {
        logger.info("Recibida solicitud para crear nuevo cliente");
        logger.debug("Datos del cliente a crear: {}", cliente);

        Cliente nuevoCliente = clienteService.guardarCliente(cliente);
        return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable Long clienteId) {

        logger.info("Recibida solicitud para obtener cliente con ID: {}", clienteId);

        Optional<Cliente> cliente = clienteService.obtenerClientePorId(clienteId);

        return new ResponseEntity<>(cliente.get(), HttpStatus.OK);
    }

    @PutMapping("/{clienteId}")
    public ResponseEntity<Cliente> actualizarCliente(@PathVariable Long clienteId, @RequestBody Cliente clienteActualizado) {
        Optional<Cliente> clienteExistente = clienteService.obtenerClientePorId(clienteId);

        clienteActualizado.setClienteId(clienteId);
        Cliente clienteGuardado = clienteService.guardarCliente(clienteActualizado);
        return new ResponseEntity<>(clienteGuardado, HttpStatus.OK);
    }

    @DeleteMapping("/{clienteId}")
    public ResponseEntity<Cliente> eliminarCliente(@PathVariable Long clienteId) {
        logger.info("Recibida solicitud para eliminar cliente con ID: {}", clienteId);

        Cliente cliente = new Cliente();
        cliente.setClienteId(clienteId);
        clienteService.eliminarCliente(clienteId);

        return new ResponseEntity<>(cliente, HttpStatus.OK);
    }

    @PatchMapping("/{clienteId}")
    public ResponseEntity<Cliente> actualizarCampo(@PathVariable Long clienteId, @RequestBody Cliente camposActualizados) {
        logger.info("Recibida solicitud para actualización parcial de cliente con ID: {}", clienteId);
        logger.debug("Campos a actualizar: {}", camposActualizados);

        Optional<Cliente> clienteExistente = clienteService.obtenerClientePorId(clienteId);

        Cliente clienteGuardado = clienteService.actualizarCampos(clienteExistente.get(), camposActualizados);

        return new ResponseEntity<>(clienteGuardado, HttpStatus.OK);
    }
}

