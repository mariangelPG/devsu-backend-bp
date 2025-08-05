package com.devsu.cliente.controller;


import com.devsu.cliente.dto.ClienteDTO;
import com.devsu.cliente.dto.SuccessResponseDTO;
import com.devsu.cliente.model.Cliente;
import com.devsu.cliente.service.ClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.*;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.annotation.Validated;

import java.util.Optional;

@RestController
@RequestMapping("/clientes")
@Validated
@Tag(name = "Cliente Controller", description = "Endpoints de para clientes")
public class ClienteController {

    private static final Logger logger = LoggerFactory.getLogger(ClienteController.class);

    @Autowired
    private ClienteService clienteService;

    @Operation(summary = "Guarda un cliente nuevo", description = "Retorna el cliente creado'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<SuccessResponseDTO> crearCliente(@Valid @RequestBody Cliente cliente) {
        logger.info("Datos del cliente a crear: {}", cliente.toString());

        SuccessResponseDTO respuesta = clienteService.guardarCliente(cliente);
        return new ResponseEntity<>(respuesta, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtiene un cliente mediante un ID", description = "Retorna el cliente solicitado'")
    @Parameter(name = "clienteId", description = "ID del cliente a buscar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/{clienteId}")
    public ResponseEntity<Cliente> obtenerCliente(@PathVariable @Positive(message = "El ID del cliente debe ser un número positivo") Long clienteId) {

        logger.info("Recibida solicitud para obtener cliente con ID: {}", clienteId);

        Optional<Cliente> cliente = clienteService.obtenerClientePorId(clienteId);

        return new ResponseEntity<>(cliente.get(), HttpStatus.OK);
    }

    @Operation(summary = "Actualiza todo un cliente mediante el ID", description = "Se le debe pasar todos los datos del cliente'")
    @Parameter(name = "clienteId", description = "ID del cliente a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{clienteId}")
    public ResponseEntity<SuccessResponseDTO> actualizarCliente(
            @PathVariable @Positive(message = "El ID del cliente debe ser un número positivo") Long clienteId,
            @Valid @RequestBody Cliente clienteActualizado) {

        logger.info("Datos del cliente a actualizar: {}", clienteActualizado.toString());

        Optional<Cliente> clienteExistente = clienteService.obtenerClientePorId(clienteId);

        clienteActualizado.setClienteId(clienteId);
        SuccessResponseDTO clienteGuardado = clienteService.guardarCliente(clienteActualizado);
        return new ResponseEntity<>(clienteGuardado, HttpStatus.OK);
    }

    @Operation(summary = "Elimina a un cliente mediante el ID", description = "Retorna el ID del cliente eliminado'")
    @Parameter(name = "clienteId", description = "ID del cliente a eliminar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @DeleteMapping("/{clienteId}")
    public ResponseEntity<SuccessResponseDTO> eliminarCliente(@PathVariable @Positive(message = "El ID del cliente debe ser un número positivo") Long clienteId) {
        logger.info("Recibida solicitud para eliminar cliente con ID: {}", clienteId);

        Cliente cliente = new Cliente();
        cliente.setClienteId(clienteId);
        clienteService.eliminarCliente(clienteId);
        return new ResponseEntity<>(new SuccessResponseDTO("Cliente eliminado con ID: "+ clienteId, true), HttpStatus.OK);
    }

    @Operation(summary = "Actualiza parcialmente a un cliente mediante el ID", description = "Se le pueden enviar solo los campos a actualizar'")
    @Parameter(name = "clienteId", description = "ID del cliente a actualizar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PatchMapping("/{clienteId}")
    public ResponseEntity<SuccessResponseDTO> actualizarCampo(@PathVariable @Positive(message = "El ID del cliente debe ser un número positivo") Long clienteId,
                                                   @RequestBody ClienteDTO camposActualizados) {
        logger.info("Recibida solicitud para actualización parcial de cliente: {}", camposActualizados.toString());

        Optional<Cliente> clienteExistente = clienteService.obtenerClientePorId(clienteId);

        SuccessResponseDTO clienteGuardado = clienteService.actualizarCampos(clienteExistente.get(), ClienteDTO.toCliente(camposActualizados));

        return new ResponseEntity<>(clienteGuardado, HttpStatus.OK);
    }
}

