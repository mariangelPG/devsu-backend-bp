package com.devsu.cuentas.service;

import com.devsu.cuentas.dto.SuccessResponseDTO;
import com.devsu.cuentas.exception.*;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.repository.CuentaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CuentaService {

    private static final Logger logger = LoggerFactory.getLogger(CuentaService.class);

    @Autowired
    private CuentaRepository cuentaRepository;

    /**
     * Guarda una cuenta nueva
     * @param cuenta
     * @return objeto cuenta
     */
    public SuccessResponseDTO guardarCuenta(Cuenta cuenta) {
        try{

            Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
            logger.info("Cliente guardado exitosamente con ID: {}", cuentaGuardada.getClienteId());
            return new SuccessResponseDTO("Cuenta guardada exitosamente con ID"+ cuentaGuardada.getNumeroCuenta(), true);

        } catch (CuentaValidationException ex) {
            logger.warn("Error de validación al guardar cuenta: {}", ex.getMessage());
            throw ex;
        } catch (DataAccessException ex) {
            logger.error("Error de acceso a datos al guardar la cuenta: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error al acceder a la base de datos", ex);
        } catch (Exception ex) {
            logger.error("Error inesperado al guardar la cuenta: {}", ex.getMessage(), ex);
            throw ex;
        }
    }

    /**
     * Obtiene el detalle de una cuenta en base al ID de la cuenta
     * @param cuentaId
     * @return
     */
    public Optional<Cuenta> obtenerCuentaPorId(Long cuentaId) {

        try{
            Optional<Cuenta> cuenta = cuentaRepository.findById(cuentaId);

            if (!cuenta.isPresent()) {
                logger.info("Cuenta no encontrado con ID: {}", cuentaId);
                throw new CuentaNotFoundException("El ID de la cuenta no se encontro");
            }
            return cuenta;
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al buscar cuenta con ID {}: {}", cuentaId, e.getMessage(), e);
            throw new RuntimeException("Error al acceder a la base de datos", e);
        }

    }

    /**
     * Obtiene la lista de cuentas dado un ID de cliente
     * @param clienteId
     * @return
     */
    public List<Cuenta> obtenerCuentasPorClienteId(Long clienteId) {
        try{
            List<Cuenta> listaCuentas = cuentaRepository.findByClienteId(clienteId);

            if (listaCuentas.isEmpty()) {
                logger.info("Cuenta no encontrado con ID: {}", clienteId);
                throw new CuentaNotFoundException("No se encontraron cuentas con este ID");
            }
            return listaCuentas;
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al buscar cuentas con cliente ID {}: {}", clienteId, e.getMessage(), e);
            throw new RuntimeException("Error al acceder a la base de datos", e);
        }

    }


}
