package com.devsu.cliente.controller;

import com.devsu.cliente.ClientApplication;
import com.devsu.cliente.dto.ClienteDTO;
import com.devsu.cliente.exception.ClienteNotFoundException;
import com.devsu.cliente.model.Cliente;
import com.devsu.cliente.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ClienteController.class)
class ClienteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClienteService clienteService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void crearCliente() throws Exception {
        Cliente clienteACrear = new Cliente();
        clienteACrear.setNombre("Carlos Gomez");
        clienteACrear.setContrasena("Password123");
        clienteACrear.setEstado(true);
        clienteACrear.setIdentificacion("1712345678");
        clienteACrear.setEdad(25);
        clienteACrear.setDireccion("Av. Principal");
        clienteACrear.setTelefono("0991234567");

        Cliente clienteCreado = new Cliente();
        clienteCreado.setClienteId(1L);
        clienteCreado.setNombre("Carlos Gomez");

        when(clienteService.guardarCliente(any(Cliente.class))).thenReturn(clienteCreado);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteACrear)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.clienteId").value(1L))
                .andExpect(jsonPath("$.nombre").value("Carlos Gomez"));

        verify(clienteService, times(1)).guardarCliente(any(Cliente.class));
    }

    @Test
    void crearCliente_fallido() throws Exception {
        Cliente clienteInvalido = new Cliente();
        clienteInvalido.setContrasena("Password123"); // Falta el nombre, que es @NotBlank
        clienteInvalido.setEstado(true);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteInvalido)))
                .andExpect(status().isBadRequest());

        verify(clienteService, never()).guardarCliente(any(Cliente.class));
    }

    @Test
    void obtenerCliente() throws Exception {
        Cliente cliente = new Cliente();
        cliente.setClienteId(1L);
        cliente.setNombre("Maria Lopez");
        when(clienteService.obtenerClientePorId(1L)).thenReturn(Optional.of(cliente));

        mockMvc.perform(get("/clientes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clienteId").value(1L))
                .andExpect(jsonPath("$.nombre").value("Maria Lopez"));

        verify(clienteService, times(1)).obtenerClientePorId(1L);
    }

    @Test
    void obtenerCliente_NoExiste() throws Exception {
        when(clienteService.obtenerClientePorId(99L)).thenThrow(new ClienteNotFoundException("El ID no existe"));

        mockMvc.perform(get("/clientes/99"))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).obtenerClientePorId(99L);
    }

    @Test
    void obtenerCliente_fallido() throws Exception {
        mockMvc.perform(get("/clientes/abc"))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/clientes/-1"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void eliminarCliente() throws Exception {
        doNothing().when(clienteService).eliminarCliente(1L);

        mockMvc.perform(delete("/clientes/1"))
                .andExpect(status().isOk());

        verify(clienteService, times(1)).eliminarCliente(1L);
    }

    @Test
    void eliminarCliente_NoExiste() throws Exception {
        doThrow(new ClienteNotFoundException(99L)).when(clienteService).eliminarCliente(99L);

        mockMvc.perform(delete("/clientes/99"))
                .andExpect(status().isNotFound());

        verify(clienteService, times(1)).eliminarCliente(99L);
    }

    @Test
    void actualizarCampo() throws Exception {
        Cliente clienteExistente = new Cliente();
        clienteExistente.setClienteId(1L);
        clienteExistente.setNombre("Original");

        ClienteDTO camposActualizados = new ClienteDTO();
        camposActualizados.setNombre("Nombre Parcial");

        Cliente clienteFinal = new Cliente();
        clienteFinal.setClienteId(1L);
        clienteFinal.setNombre("Nombre Parcial");

        when(clienteService.obtenerClientePorId(1L)).thenReturn(Optional.of(clienteExistente));
        when(clienteService.actualizarCampos(any(Cliente.class), any(Cliente.class))).thenReturn(clienteFinal);

        mockMvc.perform(patch("/clientes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(camposActualizados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Nombre Parcial"));
    }
}
