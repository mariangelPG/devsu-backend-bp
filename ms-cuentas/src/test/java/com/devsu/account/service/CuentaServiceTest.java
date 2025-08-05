package com.devsu.account.service;

import com.devsu.cuentas.dto.SuccessResponseDTO;
import com.devsu.cuentas.exception.CuentaNotFoundException;
import com.devsu.cuentas.model.Cuenta;
import com.devsu.cuentas.repository.CuentaRepository;
import com.devsu.cuentas.service.CuentaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CuentaServiceTest {

    @Mock
    private CuentaRepository cuentaRepository;

    @InjectMocks
    private CuentaService cuentaService;

    private Cuenta cuentaValida;
    private List<Cuenta> listaCuentas;

    @BeforeEach
    void setUp() {
        cuentaValida = new Cuenta();
        cuentaValida.setNumeroCuenta(12345L);
        cuentaValida.setTipoCuenta("Ahorros");
        cuentaValida.setSaldoInicial(500.0);
        cuentaValida.setEstado(true);
        cuentaValida.setClienteId(1L);

        listaCuentas = Collections.singletonList(cuentaValida);
    }


    @Test
    void guardarCuenta_debeRetornarSuccessResponseDTO_cuandoCuentaEsValida() {

        when(cuentaRepository.save(any(Cuenta.class))).thenReturn(cuentaValida);
        SuccessResponseDTO response = cuentaService.guardarCuenta(cuentaValida);
        assertNotNull(response);
        assertTrue(response.getEstado());
        assertEquals("Cuenta guardada exitosamente con ID: " + cuentaValida.getNumeroCuenta(), response.getMensaje());
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    void guardarCuenta_ErrorBD() {
        when(cuentaRepository.save(any(Cuenta.class))).thenThrow(new DataAccessException("Error de DB") {});

        assertThrows(RuntimeException.class, () -> cuentaService.guardarCuenta(cuentaValida));
        verify(cuentaRepository, times(1)).save(any(Cuenta.class));
    }

    @Test
    void obtenerCuentaPorId_Exitoso() {
        when(cuentaRepository.findById(12345L)).thenReturn(Optional.of(cuentaValida));

        Optional<Cuenta> resultado = cuentaService.obtenerCuentaPorId(12345L);

        assertTrue(resultado.isPresent());
        assertEquals(cuentaValida.getNumeroCuenta(), resultado.get().getNumeroCuenta());
        verify(cuentaRepository, times(1)).findById(12345L);
    }

    @Test
    void obtenerCuentaPorId_NoExiste() {
        when(cuentaRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(CuentaNotFoundException.class, () -> cuentaService.obtenerCuentaPorId(999L));
        verify(cuentaRepository, times(1)).findById(999L);
    }

    @Test
    void obtenerCuentaPorId_ErrorBD() {
        when(cuentaRepository.findById(anyLong())).thenThrow(new DataAccessException("Error de DB") {});

        assertThrows(RuntimeException.class, () -> cuentaService.obtenerCuentaPorId(12345L));
        verify(cuentaRepository, times(1)).findById(12345L);
    }

    @Test
    void obtenerCuentasPorClienteId_Exitoso() {
        when(cuentaRepository.findByClienteId(1L)).thenReturn(listaCuentas);

        List<Cuenta> resultado = cuentaService.obtenerCuentasPorClienteId(1L);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        verify(cuentaRepository, times(1)).findByClienteId(1L);
    }

    @Test
    void obtenerCuentasPorClienteId_NoExiste() {
        when(cuentaRepository.findByClienteId(anyLong())).thenReturn(Collections.emptyList());

        assertThrows(CuentaNotFoundException.class, () -> cuentaService.obtenerCuentasPorClienteId(2L));
        verify(cuentaRepository, times(1)).findByClienteId(2L);
    }

    @Test
    void obtenerCuentasPorClienteId_ErrorBD() {
        when(cuentaRepository.findByClienteId(anyLong())).thenThrow(new DataAccessException("Error de DB") {});

        assertThrows(RuntimeException.class, () -> cuentaService.obtenerCuentasPorClienteId(1L));
        verify(cuentaRepository, times(1)).findByClienteId(1L);
    }
}
