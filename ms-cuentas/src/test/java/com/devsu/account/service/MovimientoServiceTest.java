package com.devsu.account.service;

import com.devsu.cuentas.dto.SuccessResponseDTO;
import com.devsu.cuentas.exception.*;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.model.Movimiento;
import com.devsu.cuentas.repository.MovimientoRepository;
import com.devsu.cuentas.service.CuentaService;
import com.devsu.cuentas.service.MovimientoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

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

    @Captor
    private ArgumentCaptor<Movimiento> movimientoCaptor;
    @Captor
    private ArgumentCaptor<Cuenta> cuentaCaptor;


    private Cuenta cuenta;
    private Movimiento movimientoDeposito;
    private Movimiento movimientoRetiro;
    private static final Long CUENTA_ID = 1234L;
    private static final Long MOVIMIENTO_ID = 1L;

    @BeforeEach
    void setUp() {
        cuenta = new Cuenta();
        cuenta.setNumeroCuenta(CUENTA_ID);
        cuenta.setSaldoInicial(100.0);
        cuenta.setEstado(true);

        movimientoDeposito = new Movimiento();
        movimientoDeposito.setId(MOVIMIENTO_ID);
        movimientoDeposito.setTipo("Deposito");
        movimientoDeposito.setValor(50.0);
        movimientoDeposito.setCuentaId(CUENTA_ID);

        movimientoRetiro = new Movimiento();
        movimientoRetiro.setId(2L);
        movimientoRetiro.setTipo("Retiro");
        movimientoRetiro.setValor(50.0);
        movimientoRetiro.setCuentaId(CUENTA_ID);
    }

    @Test
    void guardarMovimiento_DepositoExitoso() {
        when(cuentaService.obtenerCuentaPorId(CUENTA_ID)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimientoDeposito);

        SuccessResponseDTO response = movimientoService.guardarMovimiento(movimientoDeposito);

        assertNotNull(response);
        assertTrue(response.getEstado());
        assertEquals("Movimiento guardado exitosamente con ID: " + MOVIMIENTO_ID, response.getMensaje());

        verify(movimientoRepository).save(movimientoCaptor.capture());
        Movimiento movimientoGuardado = movimientoCaptor.getValue();
        assertEquals(150.0, movimientoGuardado.getSaldo());

    }

    @Test
    void guardarMovimiento_RetiroExitoso() {
        when(cuentaService.obtenerCuentaPorId(CUENTA_ID)).thenReturn(Optional.of(cuenta));
        when(movimientoRepository.save(any(Movimiento.class))).thenReturn(movimientoRetiro);

        SuccessResponseDTO response = movimientoService.guardarMovimiento(movimientoRetiro);

        assertNotNull(response);
        assertTrue(response.getEstado());
        assertEquals("Movimiento guardado exitosamente con ID: " + movimientoRetiro.getId(), response.getMensaje());

        verify(movimientoRepository).save(movimientoCaptor.capture());
        Movimiento movimientoGuardado = movimientoCaptor.getValue();
        assertEquals(50.0, movimientoGuardado.getSaldo());

    }

    @Test
    void guardarMovimiento_SaldoInsuficiente() {
        movimientoRetiro.setValor(150.0);

        when(cuentaService.obtenerCuentaPorId(CUENTA_ID)).thenReturn(Optional.of(cuenta));

        SaldoInsuficienteException excepcion = assertThrows(SaldoInsuficienteException.class, () -> movimientoService.guardarMovimiento(movimientoRetiro));
        assertEquals("Saldo insuficiente. Saldo actual: 100.00, Monto solicitado: 150.00", excepcion.getMessage());

        verify(movimientoRepository, never()).save(any(Movimiento.class));
        verify(cuentaService, never()).guardarCuenta(any(Cuenta.class));
    }

    @Test
    void guardarMovimiento_CuentaNoExiste() {
        when(cuentaService.obtenerCuentaPorId(anyLong())).thenThrow(new CuentaNotFoundException("Cuenta no encontrada"));

        assertThrows(CuentaNotFoundException.class, () -> movimientoService.guardarMovimiento(movimientoRetiro));
        verify(movimientoRepository, never()).save(any(Movimiento.class));
    }

    @Test
    void obtenerMovimientoPorId() {
        when(movimientoRepository.findById(MOVIMIENTO_ID)).thenReturn(Optional.of(movimientoDeposito));

        Optional<Movimiento> movimientoEncontrado = movimientoService.obtenerMovimientoPorId(MOVIMIENTO_ID);

        assertTrue(movimientoEncontrado.isPresent());
        assertEquals(MOVIMIENTO_ID, movimientoEncontrado.get().getId());
    }

    @Test
    void obtenerMovimientoPorId_NoExiste() {
        when(movimientoRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(MovimientoNotFoundException.class, () -> movimientoService.obtenerMovimientoPorId(99L));
    }
}