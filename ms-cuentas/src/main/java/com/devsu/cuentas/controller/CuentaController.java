package com.devsu.cuentas.controller;

import ch.qos.logback.core.net.server.Client;
import com.devsu.cuentas.dto.SuccessResponseDTO;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.service.CuentaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/cuentas")
@Validated
@Tag(name = "Cuenta Controller", description = "Endpoints de para manejo de las cuentas")
public class CuentaController {

    private static final Logger logger = LoggerFactory.getLogger(CuentaController.class);

    @Autowired
    private CuentaService cuentaService;

    @Operation(summary = "Guarda una cuenta nueva", description = "Retorna la cuenta creada'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<SuccessResponseDTO> crearCuenta(@Valid @RequestBody Cuenta cuenta){
        logger.info("Datos de la cuenta a crear: {}", cuenta.toString());
        SuccessResponseDTO nuevaCuenta = cuentaService.guardarCuenta(cuenta);
        return new ResponseEntity<>(nuevaCuenta, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtiene una cuenta mediante su ID", description = "Retorna la cuenta solicitada'")
    @Parameter(name = "cuentaId", description = "ID de la cuenta a buscar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/id/{cuentaId}")
    public ResponseEntity<Cuenta> obtenerCuenta(@PathVariable @Positive(message = "El ID de la cuenta debe ser un número positivo") Long cuentaId) {

        logger.info("Recibida solicitud para obtener cuenta con ID: {}", cuentaId);
        Optional<Cuenta> cuenta = cuentaService.obtenerCuentaPorId(cuentaId);
        return new ResponseEntity<>(cuenta.get(), HttpStatus.OK);

    }

    @Operation(summary = "Obtiene una cuenta mediante el ID de cliente", description = "Retorna la cuenta solicitada'")
    @Parameter(name = "clienteId", description = "ID del cliente para buscar la cuenta", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Cuenta>> obtenerCuentasxCliente(@PathVariable @Positive(message = "El ID del cliente debe ser un número positivo") Long clienteId) {

        logger.info("Recibida solicitud para obtener las cuentas del cliente con ID: {}", clienteId);
        List<Cuenta> listaCuentas = cuentaService.obtenerCuentasPorClienteId(clienteId);
        return new ResponseEntity<>(listaCuentas, HttpStatus.OK);
    }

    @Operation(summary = "Actualiza una cuenta mediante su ID", description = "Retorna la cuenta actualizada'")
    @Parameter(name = "cuentaId", description = "ID de la cuenta a buscar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{cuentaId}")
    public ResponseEntity<SuccessResponseDTO> actualizarCuenta(
            @PathVariable @Positive(message = "El ID de la cuenta debe ser un número positivo") Long cuentaId,
            @Valid @RequestBody Cuenta cuentaActualizado) {

        logger.info("Datos de la cuenta a actualizar: {}", cuentaActualizado.toString());
        Optional<Cuenta> cuentaExistente = cuentaService.obtenerCuentaPorId(cuentaId);
        cuentaActualizado.setNumeroCuenta(cuentaId);
        SuccessResponseDTO cuentaGuardado = cuentaService.guardarCuenta(cuentaActualizado);
        return new ResponseEntity<>(cuentaGuardado, HttpStatus.OK);
    }

}
