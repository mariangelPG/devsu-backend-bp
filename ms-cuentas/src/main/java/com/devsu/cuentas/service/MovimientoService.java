package com.devsu.cuentas.service;

import com.devsu.cuentas.exception.*;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.model.Movimiento;
import com.devsu.cuentas.repository.MovimientoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MovimientoService {
    private static final Logger logger = LoggerFactory.getLogger(MovimientoService.class);

    @Autowired
    private MovimientoRepository movimientoRepository;

    @Autowired
    private CuentaService cuentaService;

    public Movimiento guardarMovimiento(Movimiento movimiento){
        try {
            Cuenta cuenta = cuentaService.obtenerCuentaPorId(movimiento.getCuentaId()).get();

            Double saldoActual = obtenerSaldoActual(movimiento.getCuentaId());
            Double nuevoSaldo = calcularNuevoSaldo(saldoActual, movimiento.getValor(), movimiento.getTipo());

            if (("Debito".equals(movimiento.getTipo()) || "Retiro".equals(movimiento.getTipo())) && nuevoSaldo < 0) {
                throw new SaldoInsuficienteException(saldoActual, Math.abs(movimiento.getValor()));
            }

            movimiento.setSaldo(nuevoSaldo);
            movimiento.setFecha(new Date());

            Movimiento movimientoGuardado = movimientoRepository.save(movimiento);


            logger.info("Movimiento guardado exitosamente con ID: {} para cuenta ID: {}",
                    movimientoGuardado.getId(), movimientoGuardado.getCuentaId());

            return movimientoGuardado;

        } catch (MovimientoValidationException | SaldoInsuficienteException | CuentaNotFoundException ex) {
            logger.warn("Error de validación al guardar movimiento: {}", ex.getMessage());
            throw ex;
        } catch (DataAccessException ex) {
            logger.error("Error de acceso a datos al guardar el movimiento: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error al acceder a la base de datos", ex);
        } catch (Exception ex) {
            logger.error("Error inesperado al guardar el movimiento: {}", ex.getMessage(), ex);
            throw ex;
        }
    }


    public Optional<Movimiento> obtenerMovimientoPorId(Long movimientoId) {

        try{
            Optional<Movimiento> movimiento = movimientoRepository.findById(movimientoId);

            if (!movimiento.isPresent()) {
                logger.info("Movimiento no encontrado con ID: {}", movimientoId);
                throw new MovimientoNotFoundException("El ID no existe");
            }
            return movimiento;
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al buscar cuenta con ID {}: {}", movimientoId, e.getMessage(), e);
            throw new RuntimeException("Error al acceder a la base de datos", e);
        }

    }

    public List<Movimiento> obtenerMovimientosPorCuentaId(Long cuentaId) {
        try{
            return movimientoRepository.findByCuentaId(cuentaId);
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al buscar movimientos con cuenta ID {}: {}", cuentaId, e.getMessage(), e);
            throw new RuntimeException("Error al acceder a la base de datos", e);
        }

    }

    public Double obtenerSaldoActual(Long cuentaId) {
        logger.debug("Obteniendo saldo actual para cuenta ID: {}", cuentaId);

        try {
            Optional<Movimiento> ultimoMovimiento = movimientoRepository.findTopByCuentaIdOrderByFechaDesc(cuentaId);

            if (ultimoMovimiento.isPresent()) {
                Double saldo = ultimoMovimiento.get().getSaldo();
                logger.debug("Saldo actual para cuenta {}: {}", cuentaId, saldo);
                return saldo;
            } else {
                Cuenta cuenta = cuentaService.obtenerCuentaPorId(cuentaId).get();
                logger.debug("No hay movimientos, saldo inicial para cuenta {}: {}", cuentaId, cuenta.getSaldoInicial());
                return cuenta.getSaldoInicial();
            }

        } catch (DataAccessException ex) {
            logger.error("Error al obtener saldo actual para cuenta {}: {}", cuentaId, ex.getMessage(), ex);
            throw new RuntimeException("Error al acceder a la base de datos", ex);
        }
    }

    private Double calcularNuevoSaldo(Double saldoActual, Double valor, String tipo) {
        if ("Credito".equals(tipo) || "Deposito".equals(tipo)) {
            return saldoActual + Math.abs(valor);
        } else if ("Debito".equals(tipo) || "Retiro".equals(tipo)) {
            return saldoActual - Math.abs(valor);
        } else {
            throw new MovimientoValidationException("Tipo de movimiento inválido: " + tipo);
        }
    }

}
