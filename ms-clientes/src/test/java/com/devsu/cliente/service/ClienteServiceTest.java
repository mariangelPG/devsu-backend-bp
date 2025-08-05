package com.devsu.cliente.service;


import com.devsu.cliente.exception.ClienteNotFoundException;
import com.devsu.cliente.exception.ClienteValidationException;
import com.devsu.cliente.model.Cliente;
import com.devsu.cliente.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {
    @Mock
    private ClienteRepository clienteRepository;

    @InjectMocks
    private ClienteService clienteService;

    private Cliente clienteValido;

    @BeforeEach
    void setUp() {
        clienteValido = new Cliente();
        clienteValido.setClienteId(1L);
        clienteValido.setNombre("Juan Perez");
        clienteValido.setGenero("Masculino");
        clienteValido.setEdad(30);
        clienteValido.setIdentificacion("1234567890");
        clienteValido.setDireccion("Calle Falsa 123");
        clienteValido.setTelefono("0991234567");
        clienteValido.setContrasena("Password123");
        clienteValido.setEstado(true);
    }

    @Test
    void guardarCliente_exitoso() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteValido);

        Cliente resultado = clienteService.guardarCliente(clienteValido);

        assertNotNull(resultado);
        assertEquals(clienteValido.getNombre(), resultado.getNombre());
        assertNotNull(resultado.getContrasena());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void guardarCliente_ClienteNull() {
        assertThrows(ClienteValidationException.class, () -> clienteService.guardarCliente(null));
        verify(clienteRepository, never()).save(any(Cliente.class));
    }

    @Test
    void guardarCliente_ErrorBD() {
        when(clienteRepository.save(any(Cliente.class))).thenThrow(new DataAccessException("Error de DB") {});

        assertThrows(RuntimeException.class, () -> clienteService.guardarCliente(clienteValido));
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void obtenerClientePorId_Exitoso() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));

        Optional<Cliente> resultado = clienteService.obtenerClientePorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(clienteValido.getNombre(), resultado.get().getNombre());
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void obtenerClientePorId_NoExiste() {
        when(clienteRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(ClienteNotFoundException.class, () -> clienteService.obtenerClientePorId(99L));
        verify(clienteRepository, times(1)).findById(99L);
    }

    @Test
    void obtenerClientePorId_ErrorBD() {
        when(clienteRepository.findById(anyLong())).thenThrow(new DataAccessException("Error de DB") {});

        assertThrows(RuntimeException.class, () -> clienteService.obtenerClientePorId(1L));
        verify(clienteRepository, times(1)).findById(1L);
    }

    @Test
    void eliminarCliente_Exitoso() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        assertDoesNotThrow(() -> clienteService.eliminarCliente(1L));
        verify(clienteRepository, times(1)).existsById(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarCliente_NoExiste() {
        when(clienteRepository.existsById(anyLong())).thenReturn(false);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.eliminarCliente(99L));
        verify(clienteRepository, times(1)).existsById(99L);
        verify(clienteRepository, never()).deleteById(anyLong());
    }

    @Test
    void eliminarCliente_ErrorBD() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doThrow(new DataAccessException("Error de DB") {}).when(clienteRepository).deleteById(1L);

        assertThrows(RuntimeException.class, () -> clienteService.eliminarCliente(1L));
        verify(clienteRepository, times(1)).existsById(1L);
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void actualizarCampos_Exitoso() {
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setNombre("Nuevo Nombre");
        clienteActualizado.setTelefono("0998765432");

        Cliente clienteGuardado = new Cliente();
        clienteGuardado.setClienteId(1L);
        clienteGuardado.setNombre("Nuevo Nombre");
        clienteGuardado.setTelefono("0998765432");

        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteGuardado);

        Cliente resultado = clienteService.actualizarCampos(clienteValido, clienteActualizado);

        assertNotNull(resultado);
        assertEquals("Nuevo Nombre", resultado.getNombre());
        assertEquals("0998765432", resultado.getTelefono());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }
}
