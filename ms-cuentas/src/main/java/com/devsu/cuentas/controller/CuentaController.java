package com.devsu.cuentas.controller;

import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.service.CuentaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/cuentas")
@Validated
public class CuentaController {

    private static final Logger logger = LoggerFactory.getLogger(CuentaController.class);

    @Autowired
    private CuentaService cuentaService;

    @PostMapping
    public ResponseEntity<Cuenta> crearCuenta(@Valid @RequestBody Cuenta cuenta){
        logger.info("Datos de la cuenta a crear: {}", cuenta.toString());

        return new ResponseEntity<>(cuenta, HttpStatus.CREATED);
    }

    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<Cuenta> obtenerCuenta(@PathVariable @Positive(message = "El ID de la cuenta debe ser un número positivo") Long cuentaId) {

        logger.info("Recibida solicitud para obtener cuenta con ID: {}", cuentaId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{clienteId}")
    public ResponseEntity<Cuenta> obtenerCuentasxCliente(@PathVariable @Positive(message = "El ID del cliente debe ser un número positivo") Long clienteId) {

        logger.info("Recibida solicitud para obtener las cuentas del cliente con ID: {}", clienteId);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{cuentaId}")
    public ResponseEntity<Cuenta> actualizarCuenta(
            @PathVariable @Positive(message = "El ID de la cuenta debe ser un número positivo") Long cuentaId,
            @Valid @RequestBody Cuenta cuentaActualizado) {

        logger.info("Datos de la cuenta a actualizar: {}", cuentaActualizado.toString());

        return new ResponseEntity<>(HttpStatus.OK);
    }

}
