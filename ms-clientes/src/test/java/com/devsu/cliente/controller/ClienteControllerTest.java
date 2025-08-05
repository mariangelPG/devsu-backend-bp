package com.devsu.cliente.controller;

import com.devsu.cliente.ClientApplication;
import com.devsu.cliente.dto.ClienteDTO;
import com.devsu.cliente.dto.SuccessResponseDTO;
import com.devsu.cliente.exception.ClienteNotFoundException;
import com.devsu.cliente.model.Cliente;
import com.devsu.cliente.service.ClienteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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

    private Cliente clienteValido;
    private SuccessResponseDTO successResponseDTO;

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

        successResponseDTO = new SuccessResponseDTO("Operación exitosa", true);
    }

    @Test
    void crearCliente() throws Exception {
        when(clienteService.guardarCliente(any(Cliente.class))).thenReturn(successResponseDTO);

        mockMvc.perform(post("/clientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteValido)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Operación exitosa"));
    }

    @Test
    void obtenerCliente() throws Exception {
        when(clienteService.obtenerClientePorId(1L)).thenReturn(Optional.of(clienteValido));

        mockMvc.perform(get("/clientes/{clienteId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Juan Perez"));
    }

    @Test
    void obtenerCliente_NoExiste() throws Exception {
        when(clienteService.obtenerClientePorId(2L)).thenThrow(new ClienteNotFoundException("ID no encontrado"));

        mockMvc.perform(get("/clientes/{clienteId}", 2L))
                .andExpect(status().isNotFound());
    }

    @Test
    void actualizarCliente() throws Exception {
        when(clienteService.obtenerClientePorId(1L)).thenReturn(Optional.of(clienteValido));
        when(clienteService.guardarCliente(any(Cliente.class))).thenReturn(successResponseDTO);

        mockMvc.perform(put("/clientes/{clienteId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clienteValido)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Operación exitosa"));
    }

    @Test
    void eliminarCliente() throws Exception {
        doNothing().when(clienteService).eliminarCliente(1L);

        mockMvc.perform(delete("/clientes/{clienteId}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(true));
    }

    @Test
    void actualizarCampo() throws Exception {
        ClienteDTO camposActualizados = new ClienteDTO();
        camposActualizados.setNombre("Nuevo Nombre");

        when(clienteService.obtenerClientePorId(1L)).thenReturn(Optional.of(clienteValido));
        when(clienteService.actualizarCampos(any(Cliente.class), any(Cliente.class))).thenReturn(successResponseDTO);

        mockMvc.perform(patch("/clientes/{clienteId}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(camposActualizados)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value(true));
    }
}