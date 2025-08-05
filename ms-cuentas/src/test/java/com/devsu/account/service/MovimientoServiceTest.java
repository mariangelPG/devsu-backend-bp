package com.devsu.account.service;

import com.devsu.cuentas.exception.*;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.model.Movimiento;
import com.devsu.cuentas.repository.MovimientoRepository;
import com.devsu.cuentas.service.CuentaService;
import com.devsu.cuentas.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public @ExtendWith(MockitoExtension.class)
class MovimientoServiceTest {

    @Mock
    private MovimientoRepository movimientoRepository;

    @Mock
    private CuentaService cuentaService;

    @InjectMocks
    private MovimientoService movimientoService;

    private Cuenta cuenta;
    private Movimiento movimientoDebito;
    private Movimiento movimientoCredito;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setNumeroCuenta(12345L);
        cuenta.setTipoCuenta("Ahorros");
        cuenta.setSaldoInicial(100.0);
        cuenta.setEstado(true);

        movimientoDebito = new Movimiento();
        movimientoDebito.setCuentaId(12345L);
        movimientoDebito.setTipo("Retiro");
        movimientoDebito.setValor(50.0);

        movimientoCredito = new Movimiento();
        movimientoCredito.setCuentaId(12345L);
        movimientoCredito.setTipo("Deposito");
        movimientoCredito.setValor(200.0);
    }

    @Test
    void guardarMovimiento_RetiroExitoso() {
        when(cuentaService.obtenerCuentaPorId(12345L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findTopByCuentaIdOrderByFechaDesc(12345L)).thenReturn(Optional.of(new Movimiento(new Date(), "Retiro", 100.0, 100.0, 12345L)));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimientoDebito);

        Movimiento resultado = movimientoService.guardarMovimiento(movimientoDebito);

        assertNotNull(resultado);
        assertEquals(50.0, resultado.getSaldo()); // 100.0 - 50.0 = 50.0
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    void guardarMovimiento_DebitoExitoso() {
        when(cuentaService.obtenerCuentaPorId(12345L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findTopByCuentaIdOrderByFechaDesc(12345L)).thenReturn(Optional.of(new Movimiento(new Date(), "Deposito", 100.0, 100.0, 12345L)));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimientoCredito);

        Movimiento resultado = movimientoService.guardarMovimiento(movimientoCredito);

        assertNotNull(resultado);
        assertEquals(300.0, resultado.getSaldo()); // 100.0 + 200.0 = 300.0
        verify(movimientoRepository, times(1)).save(any(Movimiento.class));
    }

    @Test
    void guardarMovimiento_SaldoInsuficiente() {
        when(cuentaService.obtenerCuentaPorId(12345L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findTopByCuentaIdOrderByFechaDesc(12345L)).thenReturn(Optional.of(new Movimiento(new Date(), "Deposito", 10.0, 10.0, 12345L)));

        movimientoDebito.setValor(20.0);

        assertThrows(SaldoInsuficienteException.class, () -> movimientoService.guardarMovimiento(movimientoDebito));
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void guardarMovimiento_TipoInvalido() {
        movimientoDebito.setTipo("Invalido");
        when(cuentaService.obtenerCuentaPorId(12345L)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.findTopByCuentaIdOrderByFechaDesc(12345L)).thenReturn(Optional.of(new Movimiento(new Date(), "Deposito", 100.0, 100.0, 12345L)));

        assertThrows(MovimientoValidationException.class, () -> movimientoService.guardarMovimiento(movimientoDebito));
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void obtenerMovimientoPorId_Exitoso() {
        movimientoCredito.setId(1L);
        when(movimientoRepository.findById(1L)).thenReturn(Optional.of(movimientoCredito));

        Optional<Movimiento> resultado = movimientoService.obtenerMovimientoPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(movimientoCredito, resultado.get());
        verify(movimientoRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerMovimientoPorId_NoExiste() {
        when(movimientoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(MovimientoNotFoundException.class, () -> movimientoService.obtenerMovimientoPorId(99L));
        verify(movimientoRepository, times(1)).findById(99L);
    }

    @Test
    void obtenerSaldoActual_Movimiento() {
        Movimiento ultimoMovimiento = new Movimiento();
        ultimoMovimiento.setSaldo(250.0);
        when(movimientoRepository.findTopByCuentaIdOrderByFechaDesc(12345L)).thenReturn(Optional.of(ultimoMovimiento));

        Double saldo = movimientoService.obtenerSaldoActual(12345L);

        assertEquals(250.0, saldo);
        verify(movimientoRepository, times(1)).findTopByCuentaIdOrderByFechaDesc(12345L);
        verify(cuentaService, never()).obtenerCuentaPorId(anyLong());
    }

    @Test
    void obtenerSaldoActual_SaldoInicial() {
        when(movimientoRepository.findTopByCuentaIdOrderByFechaDesc(12345L)).thenReturn(Optional.empty());
        when(cuentaService.obtenerCuentaPorId(12345L)).thenReturn(Optional.of(cuenta));

        Double saldo = movimientoService.obtenerSaldoActual(12345L);

        assertEquals(100.0, saldo);
        verify(movimientoRepository, times(1)).findTopByCuentaIdOrderByFechaDesc(12345L);
        verify(cuentaService, times(1)).obtenerCuentaPorId(12345L);
    }

    @Test
    void obtenerMovimientosPorCuentaId_Exitoso() {
        List<Movimiento> movimientos = Collections.singletonList(movimientoCredito);
        when(movimientoRepository.findByCuentaId(12345L)).thenReturn(movimientos);

        List<Movimiento> resultado = movimientoService.obtenerMovimientosPorCuentaId(12345L);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(movimientoRepository, times(1)).findByCuentaId(12345L);
    }
}