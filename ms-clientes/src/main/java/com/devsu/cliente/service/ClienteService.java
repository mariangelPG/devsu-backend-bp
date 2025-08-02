package com.devsu.cliente.service;

import com.devsu.cliente.dto.ClienteDTO;
import com.devsu.cliente.exception.ClienteNotFoundException;
import com.devsu.cliente.exception.ClienteValidationException;
import com.devsu.cliente.model.Cliente;
import com.devsu.cliente.repository.ClienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ClienteService {

    private static final Logger logger = LoggerFactory.getLogger(ClienteService.class);

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente guardarCliente(Cliente cliente) {
        try{
            validarCliente(cliente);
            Cliente clienteGuardado = clienteRepository.save(cliente);
            logger.info("Cliente guardado exitosamente con ID: {}", clienteGuardado.getClienteId());

            return clienteGuardado;
        }catch (DataAccessException ex){
            logger.error("Error de acceso a datos al guardar cliente: {}", ex.getMessage(), ex);
            throw new RuntimeException("Error al acceder a la base de datos", ex);
        }catch (Exception e) {
            logger.error("Error inesperado al guardar cliente: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Optional<Cliente> obtenerClientePorId(Long clienteId) {
        logger.debug("Buscando cliente con ID: {}", clienteId);

        if (clienteId == null || clienteId <= 0) {
            logger.warn("ID de cliente inválido: {}", clienteId);
            throw new ClienteValidationException("El ID del cliente debe ser un número positivo");
        }

        try{
            Optional<Cliente> cliente = clienteRepository.findById(clienteId);

            if (cliente.isPresent()) {
                logger.info("Cliente encontrado con ID: {}", clienteId);
            } else {
                logger.info("Cliente no encontrado con ID: {}", clienteId);
                throw new ClienteNotFoundException("El ID no existe");

            }

            return cliente;
        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al buscar cliente con ID {}: {}", clienteId, e.getMessage(), e);
            throw new RuntimeException("Error al acceder a la base de datos", e);
        }

    }

    public void eliminarCliente(Long clienteId) {
        logger.info("Iniciando eliminación de cliente con ID: {}", clienteId);

        if (clienteId == null || clienteId <= 0) {
            logger.warn("ID de cliente inválido para eliminación: {}", clienteId);
            throw new ClienteValidationException("El ID del cliente debe ser un número positivo");
        }
        try{
            if (!clienteRepository.existsById(clienteId)) {
                logger.warn("Intento de eliminar cliente inexistente con ID: {}", clienteId);
                throw new ClienteNotFoundException(clienteId);
            }

            clienteRepository.deleteById(clienteId);
            logger.info("Cliente eliminado exitosamente con ID: {}", clienteId);

        } catch (DataAccessException e) {
            logger.error("Error de acceso a datos al eliminar cliente con ID {}: {}", clienteId, e.getMessage(), e);
            throw new RuntimeException("Error al acceder a la base de datos", e);
        }
    }

    public Cliente actualizarCampos(Cliente clienteExistente, Cliente clienteNuevo){

        try{
            if (clienteExistente == null || clienteNuevo == null) {
                logger.error("El cliente enviado, o el cliente existente es null en actualizarCampos");
                throw new ClienteValidationException("Cliente no puede ser null");
            }

            ClienteDTO.llenarCliente(clienteExistente, clienteNuevo);

            validarCliente(clienteExistente);

            Cliente clienteActualizado = clienteRepository.save(clienteExistente);
            logger.info("Cliente actualizado exitosamente con ID: {}", clienteActualizado.getClienteId());

            return clienteActualizado;

        }catch (DataAccessException e){
            logger.error("Error de acceso a datos al actualizar cliente ID {}: {}", clienteExistente.getClienteId(), e.getMessage(), e);
            throw new RuntimeException("Error al acceder a la base de datos", e);
        }
    }

    private void validarCliente(Cliente cliente) {
        logger.debug("Validando datos de cliente");

        if (cliente == null) {
            throw new ClienteValidationException("Cliente no puede ser null");
        }

        logger.debug("Validación de cliente completada exitosamente");
    }

}
