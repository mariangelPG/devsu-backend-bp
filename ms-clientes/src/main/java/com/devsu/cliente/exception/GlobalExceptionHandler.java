package com.devsu.cliente.exception;

import com.devsu.cliente.dto.ErrorResponseDTO;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleClienteNotFoundException(
            ClienteNotFoundException ex, WebRequest request) {

        logger.warn("Cliente no encontrado: {}", ex.getMessage());

        ErrorResponseDTO ErrorResponseDTO = com.devsu.cliente.dto.ErrorResponseDTO.builder()
                .error("Cliente No Encontrado")
                .mensaje(ex.getMessage())
                .estatus(HttpStatus.NOT_FOUND.value())
                .ruta(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(ErrorResponseDTO, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ClienteValidationException.class)
    public ResponseEntity<ErrorResponseDTO> handleClienteValidationException(
            ClienteValidationException ex, WebRequest request) {

        logger.warn("Error de validación de cliente: {}", ex.getMessage());

        ErrorResponseDTO ErrorResponseDTO = com.devsu.cliente.dto.ErrorResponseDTO.builder()
                .error("Error de Validación")
                .mensaje(ex.getMessage())
                .estatus(HttpStatus.BAD_REQUEST.value())
                .ruta(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(ErrorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    // Manejo de errores de validación con @Valid (en request body)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {

        logger.warn("Error de validación en los datos enviados: {}", ex.getMessage());

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());

        String message = "Errores de validación encontrados: " + String.join(", ", errors);

        ErrorResponseDTO ErrorResponseDTO = com.devsu.cliente.dto.ErrorResponseDTO.builder()
                .error("Error de Validación")
                .mensaje(message)
                .estatus(HttpStatus.BAD_REQUEST.value())
                .ruta(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(ErrorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleConstraintViolationException(
            ConstraintViolationException ex, WebRequest request) {

        logger.warn("Error de validación en parámetros: {}", ex.getMessage());

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(violation -> {
                    String propertyPath = violation.getPropertyPath().toString();
                    String fieldName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
                    return fieldName + ": " + violation.getMessage();
                })
                .collect(Collectors.toList());

        String message = "Errores de validación en parámetros: " + String.join(", ", errors);

        ErrorResponseDTO ErrorResponseDTO = com.devsu.cliente.dto.ErrorResponseDTO.builder()
                .error("Error de Validación de Parámetros")
                .mensaje(message)
                .estatus(HttpStatus.BAD_REQUEST.value())
                .ruta(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(ErrorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex, WebRequest request) {

        logger.error("Error de integridad de datos: {}", ex.getMessage());

        String message = "Los datos proporcionados violan las restricciones de integridad";

        // Detectar errores específicos
        if (ex.getMessage() != null) {
            if (ex.getMessage().contains("identificacion")) {
                message = "Ya existe un cliente con esta identificación";
            } else if (ex.getMessage().contains("email")) {
                message = "Ya existe un cliente con este email";
            }
        }

        ErrorResponseDTO ErrorResponseDTO = com.devsu.cliente.dto.ErrorResponseDTO.builder()
                .error("Error de Integridad de Datos")
                .mensaje(message)
                .estatus(HttpStatus.CONFLICT.value())
                .ruta(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(ErrorResponseDTO, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDTO> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex, WebRequest request) {

        logger.warn("Error de tipo de argumento: {}", ex.getMessage());

        ErrorResponseDTO ErrorResponseDTO = com.devsu.cliente.dto.ErrorResponseDTO.builder()
                .error("Error de Tipo de Parámetro")
                .mensaje(String.format("El parámetro '%s' debe ser de tipo %s",
                        ex.getName(), ex.getRequiredType().getSimpleName()))
                .estatus(HttpStatus.BAD_REQUEST.value())
                .ruta(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(ErrorResponseDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGlobalException(
            Exception ex, WebRequest request) {

        logger.error("Error interno del servidor: ", ex);

        ErrorResponseDTO ErrorResponseDTO = com.devsu.cliente.dto.ErrorResponseDTO.builder()
                .error("Error Interno del Servidor")
                .mensaje("Ha ocurrido un error interno. Por favor contacte al administrador.")
                .estatus(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .ruta(request.getDescription(false).replace("uri=", ""))
                .build();

        return new ResponseEntity<>(ErrorResponseDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}