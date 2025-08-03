package com.devsu.cuentas.repository;

import com.devsu.cuentas.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {
    List<Movimiento> findByCuentaId(Long cuentaId);

    Optional<Movimiento> findTopByCuentaIdOrderByFechaDesc(Long cuentaId);
}