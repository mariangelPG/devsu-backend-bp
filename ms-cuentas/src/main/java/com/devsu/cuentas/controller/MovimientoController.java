package com.devsu.cuentas.controller;

import com.devsu.cuentas.model.Movimiento;
import com.devsu.cuentas.service.MovimientoService;
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
@RequestMapping("/movimientos")
@Validated
@Tag(name = "Movimiento Controller", description = "Endpoints de para manejo de los movimientos")
public class MovimientoController {

    private static final Logger logger = LoggerFactory.getLogger(MovimientoController.class);

    @Autowired
    private MovimientoService movimientoService;


    @Operation(summary = "Guarda un movimiento nuevo", description = "Retorna el movimiento creado'")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PostMapping
    public ResponseEntity<Movimiento> crearMovimiento(@Valid @RequestBody Movimiento movimiento){
        logger.info("Datos del movimiento a crear: {}", movimiento.toString());
        Movimiento movimientoGuardado = movimientoService.guardarMovimiento(movimiento);
        return new ResponseEntity<>(movimientoGuardado, HttpStatus.CREATED);
    }

    @Operation(summary = "Obtiene un movimiento mediante su ID", description = "Retorna el movimiento solicitado'")
    @Parameter(name = "movimientoId", description = "ID del movimiento a buscar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/id/{movimientoId}")
    public ResponseEntity<Movimiento> obtenerMovimiento(@PathVariable @Positive(message = "El ID del movimiento debe ser un número positivo") Long movimientoId) {
        logger.info("Recibida solicitud para obtener movimiento, con cuenta con ID: {}", movimientoId);
        Optional<Movimiento> movimiento = movimientoService.obtenerMovimientoPorId(movimientoId);
        return new ResponseEntity<>(movimiento.get(), HttpStatus.OK);
    }

    @Operation(summary = "Obtiene los movimientos mediante el ID de la cuenta", description = "Retorna la lista de movimientos'")
    @Parameter(name = "cuentaId", description = "ID de la cuenta para obtener el movimiento", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosxCuenta(@PathVariable @Positive(message = "El ID de la cuenta debe ser un número positivo") Long cuentaId) {
        logger.info("Recibida solicitud para obtener lista de movimientos, con cuenta con ID: {}", cuentaId);
        List<Movimiento> listaMovimiento = movimientoService.obtenerMovimientosPorCuentaId(cuentaId);
        return new ResponseEntity<>(listaMovimiento, HttpStatus.OK);
    }

    @Operation(summary = "Actualiza un movimeinto mediante su ID", description = "Retorna el movimiento actualizado'")
    @Parameter(name = "movimientoId", description = "ID del movimiento a buscar", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @PutMapping("/{movimientoId}")
    public ResponseEntity<Movimiento> actualizarCuenta(
            @PathVariable @Positive(message = "El ID del movimiento debe ser un número positivo") Long movimientoId,
            @Valid @RequestBody Movimiento movimientoActualizado) {

        logger.info("Datos del movimiento a actualizar: {}", movimientoActualizado.toString());
        Optional<Movimiento> movimientoExistente = movimientoService.obtenerMovimientoPorId(movimientoId);
        movimientoActualizado.setId(movimientoId);
        Movimiento cuentaGuardado = movimientoService.guardarMovimiento(movimientoActualizado);
        return new ResponseEntity<>(cuentaGuardado, HttpStatus.OK);
    }
}
