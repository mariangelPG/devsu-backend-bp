package com.devsu.cliente.service;


import com.devsu.cliente.dto.ClienteDTO;
import com.devsu.cliente.dto.SuccessResponseDTO;
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
    void guardarCliente() {
        when(clienteRepository.save(any(Cliente.class))).thenReturn(clienteValido);

        SuccessResponseDTO response = clienteService.guardarCliente(clienteValido);

        assertNotNull(response);
        assertTrue(response.getEstado());
        assertEquals("Cliente guardado exitosamente con ID: 1", response.getMensaje());
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void guardarCliente_ErrorBD() {
        when(clienteRepository.save(any(Cliente.class))).thenThrow(new DataAccessException("Error de DB") {});

        assertThrows(RuntimeException.class, () -> clienteService.guardarCliente(clienteValido));
        verify(clienteRepository, times(1)).save(any(Cliente.class));
    }

    @Test
    void obtenerClientePorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(clienteValido));

        Optional<Cliente> clienteEncontrado = clienteService.obtenerClientePorId(1L);

        assertTrue(clienteEncontrado.isPresent());
        assertEquals(clienteValido.getClienteId(), clienteEncontrado.get().getClienteId());
    }

    @Test
    void obtenerClientePorId_NoExiste() {
        when(clienteRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ClienteNotFoundException.class, () -> clienteService.obtenerClientePorId(2L));
    }

    @Test
    void eliminarCliente() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        doNothing().when(clienteRepository).deleteById(1L);

        assertDoesNotThrow(() -> clienteService.eliminarCliente(1L));
        verify(clienteRepository, times(1)).deleteById(1L);
    }

    @Test
    void eliminarCliente_NoExiste() {
        when(clienteRepository.existsById(2L)).thenReturn(false);

        assertThrows(ClienteNotFoundException.class, () -> clienteService.eliminarCliente(2L));
        verify(clienteRepository, never()).deleteById(anyLong());
    }

    @Test
    void actualizarCampos_nulo() {
        assertThrows(ClienteValidationException.class, () -> clienteService.actualizarCampos(null, new Cliente()));
    }

}
