package com.devsu.cuentas.service;

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

    public Cuenta guardarCuenta(Cuenta cuenta) {
        try{
            Cuenta cuentaGuardada = cuentaRepository.save(cuenta);
            logger.info("Cliente guardado exitosamente con ID: {}", cuentaGuardada.getClienteId());

            return cuentaGuardada;
        }catch (DataAccessException ex){
            logger.error("Error de acceso a datos al guardar la cuenta: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error al acceder a la base de datos", ex);
        }catch (Exception e) {
            logger.error("Error inesperado al guardar la cuenta: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Optional<Cuenta> obtenerCuentaPorId(Long cuentaId) {

        try{
            Optional<Cuenta> cuenta = cuentaRepository.findById(cuentaId);

            if (!cuenta.isPresent()) {
                logger.info("Cliente no encontrado con ID: {}", cuentaId);
                //throw new ClienteNotFoundException("El ID no existe");
            }
            return cuenta;
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al buscar cuenta con ID {}: {}", cuentaId, e.getMessage(), e);
            throw new RuntimeException("Error al acceder a la base de datos", e);
        }

    }

    public List<Cuenta> obtenerCuentasPorClienteId(Long clienteId) {
        try{
            return cuentaRepository.findByClienteId(clienteId);
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al buscar cuentas con cliente ID {}: {}", clienteId, e.getMessage(), e);
            throw new RuntimeException("Error al acceder a la base de datos", e);
        }

    }


}
