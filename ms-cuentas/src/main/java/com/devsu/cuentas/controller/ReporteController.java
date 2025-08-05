package com.devsu.cuentas.controller;

import com.devsu.cuentas.dto.ReporteDTO;
import com.devsu.cuentas.model.Movimiento;
import com.devsu.cuentas.service.ReporteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jdk.dynalink.linker.LinkerServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/reportes")
@Validated
@Tag(name = "Reporte Controller", description = "Endpoints de para manejo de los reportes")
public class ReporteController {

    private static final Logger logger = LoggerFactory.getLogger(ReporteController.class);

    @Autowired
    private ReporteService reporteService;

    @Operation(summary = "Obtiene el detalle de los movimientos por cuentas de un cliente", description = "Mediante un rango de fechas obtiene todas las cuentas del cliente y sus movimientos asociados'")
    @Parameter(name = "fecha", description = "Rango de fechas para buscar los movimientos", required = true)
    @Parameter(name = "cliente", description = "Id del cliente para buscar las cuentas del mismo", required = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Operación exitosa"),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    @GetMapping
    public ResponseEntity<ReporteDTO> obtenerDetalleCuentasMovimientos(@RequestParam @NotBlank(message = "El rango de fechas es obligatorio") String fecha,
                                                                       @RequestParam @Positive(message = "El ID del cliente debe ser un número positivo") Long cliente){
        logger.info("Recibida solicitud de reporte de estado de cuenta - Cliente: {}, Rango: {}",
                cliente, fecha);
        String[] fechas = reporteService.parsearRangoFechas(fecha);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        ReporteDTO reporte = null;
        try {
            reporte = reporteService.generarReporteCuentasMovimientos(cliente,formatter.parse(fechas[0]), formatter.parse(fechas[1]));
            return new ResponseEntity<>(reporte, HttpStatus.OK);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

    }
}
