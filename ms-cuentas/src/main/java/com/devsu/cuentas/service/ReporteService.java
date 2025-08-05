package com.devsu.cuentas.service;

import com.devsu.cuentas.dto.CuentaReporteDTO;
import com.devsu.cuentas.dto.ReporteDTO;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.model.Movimiento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class ReporteService {

    private static final Logger logger = LoggerFactory.getLogger(ReporteService.class);

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private MovimientoService movimientoService;

    /**
     * Genera el reporte de los movimientos por cuentas de un usuario especifico
     * @param clienteId
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public ReporteDTO generarReporteCuentasMovimientos(Long clienteId, Date fechaInicio, Date fechaFin){
        try{

            ReporteDTO reporte = new ReporteDTO(clienteId, fechaInicio, fechaFin);
            List<CuentaReporteDTO> listaCuentaDTO = new ArrayList<>();
            List<Cuenta> listaCuentas = cuentaService.obtenerCuentasPorClienteId(clienteId);

            for(Cuenta cuenta: listaCuentas){
                CuentaReporteDTO cuentaReporte = procesarCuentas(cuenta, fechaInicio, fechaFin);
                listaCuentaDTO.add(cuentaReporte);
            }

            reporte.setCuentas(listaCuentaDTO);
            reporte.setFechaGeneracion(new Date());


            return reporte;
        }catch (DataAccessException e) {
            logger.error("Error de acceso a datos al buscar cuentas con cliente ID {}: {}", clienteId, e.getMessage(), e);
            throw new RuntimeException("Error al acceder a la base de datos", e);
        }
    }

    /**
     * Funcion que procesa la cuenta para asignarle el movimiento
     * @param cuenta
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public CuentaReporteDTO procesarCuentas(Cuenta cuenta, Date fechaInicio, Date fechaFin){
        List<Movimiento> listaMovimientos = movimientoService.obtenerMovimientosPorCuentaIdFecha(cuenta.getNumeroCuenta(), fechaInicio, fechaFin);
        CuentaReporteDTO cuentaReporte = new CuentaReporteDTO(cuenta.getNumeroCuenta(), cuenta.getTipoCuenta(), cuenta.getSaldoInicial(), cuenta.getEstado());
        cuentaReporte.setMovimientos(listaMovimientos);
        cuentaReporte.setCantidadMovimientos(listaMovimientos.size());
        return cuentaReporte;
    }

    /**
     * Funcion que parsea el rango de fechas recibida en el URL de la peticion
     * @param rangoFechas
     * @return
     */
    public String[] parsearRangoFechas(String rangoFechas) {
        if (rangoFechas == null || rangoFechas.trim().isEmpty()) {
            throw new IllegalArgumentException("El rango de fechas no puede estar vacío");
        }

        String[] fechas;
        if (rangoFechas.contains(",")) {
            fechas = rangoFechas.split(",");
        } else if (rangoFechas.contains("_")) {
            fechas = rangoFechas.split("_");
        } else if (rangoFechas.contains(":")) {
            fechas = rangoFechas.split(":");
        } else {
            throw new IllegalArgumentException(
                    "Formato de fecha inválido. Use: 'yyyy-MM-dd,yyyy-MM-dd' o 'yyyy-MM-dd_yyyy-MM-dd'");
        }

        if (fechas.length != 2) {
            throw new IllegalArgumentException(
                    "Debe proporcionar exactamente 2 fechas separadas por coma, slash o guión bajo: 'fechaInicio,fechaFin'");
        }

        fechas[0] = fechas[0].trim() + " 00:00:00.000";
        fechas[1] = fechas[1].trim() + " 23:59:59.999";

        logger.debug("Rango de fechas parseado correctamente: {} - {}", fechas[0], fechas[1]);
        return fechas;
    }

}
