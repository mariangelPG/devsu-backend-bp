package com.devsu.cuentas.repository;

import com.devsu.cuentas.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuentaId(Long cuentaId);

    Optional<Movimiento> findTopByCuentaIdOrderByFechaDesc(Long cuentaId);

    @Query("SELECT m FROM Movimiento m WHERE m.cuentaId = :cuentaId AND m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha ASC")
    List<Movimiento> findByCuentaIdAndFechaBetweenOrderByFechaAsc(
            @Param("cuentaId") Long cuentaId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin
    );

    /**
     * Busca movimientos en un rango de fechas para una cuenta (ordenados por fecha descendente)
     */
    @Query("SELECT m FROM Movimiento m WHERE m.cuentaId = :cuentaId AND m.fecha BETWEEN :fechaInicio AND :fechaFin ORDER BY m.fecha DESC")
    List<Movimiento> findByCuentaIdAndFechaBetweenOrderByFechaDesc(
            @Param("cuentaId") Long cuentaId,
            @Param("fechaInicio") Date fechaInicio,
            @Param("fechaFin") Date fechaFin
    );
}