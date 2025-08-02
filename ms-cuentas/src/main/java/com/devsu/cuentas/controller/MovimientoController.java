package com.devsu.cuentas.controller;

import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.model.Movimiento;
import com.devsu.cuentas.service.CuentaService;
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

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<Movimiento> obtenerMovimientos(@PathVariable @Positive(message = "El ID de la cuenta debe ser un número positivo") Long cuentaId) {

        logger.info("Recibida solicitud para obtener movimiento, con cuenta con ID: {}", cuentaId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{cuentaId}")
    public ResponseEntity<Movimiento> actualizarCuenta(
            @PathVariable @Positive(message = "El ID del movimiento debe ser un número positivo") Long cuentaId,
            @Valid @RequestBody Movimiento movimientoActualizado) {

        logger.info("Datos del movimiento a actualizar: {}", movimientoActualizado.toString());

        return new ResponseEntity<>(HttpStatus.OK);
    }
}
