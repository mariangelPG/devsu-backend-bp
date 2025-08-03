package com.devsu.cuentas.controller;

import com.devsu.cuentas.model.Movimiento;
import com.devsu.cuentas.service.MovimientoService;
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
public class MovimientoController {

    private static final Logger logger = LoggerFactory.getLogger(MovimientoController.class);

    @Autowired
    private MovimientoService movimientoService;


    @PostMapping
    public ResponseEntity<Movimiento> crearMovimiento(@Valid @RequestBody Movimiento movimiento){
        logger.info("Datos del movimiento a crear: {}", movimiento.toString());

        return new ResponseEntity<>(movimiento, HttpStatus.CREATED);
    }

    @GetMapping("/{movimientoId}")
    public ResponseEntity<Movimiento> obtenerMovimiento(@PathVariable @Positive(message = "El ID del movimiento debe ser un número positivo") Long movimientoId) {

        logger.info("Recibida solicitud para obtener movimiento, con cuenta con ID: {}", movimientoId);
        Optional<Movimiento> movimiento = movimientoService.obtenerMovimientoPorId(movimientoId);
        return new ResponseEntity<>(movimiento.get(), HttpStatus.OK);
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<List<Movimiento>> obtenerMovimientosxCuenta(@PathVariable @Positive(message = "El ID de la cuenta debe ser un número positivo") Long cuentaId) {
        logger.info("Recibida solicitud para obtener lista de movimientos, con cuenta con ID: {}", cuentaId);
        List<Movimiento> listaMovimiento = movimientoService.obtenerMovimientosPorCuentaId(cuentaId);
        return new ResponseEntity<>(listaMovimiento, HttpStatus.OK);
    }

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
