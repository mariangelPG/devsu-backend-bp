package com.devsu.cliente.model;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

public class ClienteTest {

    private Validator validator;
    private Cliente clienteValido;

    @BeforeEach
    void setUp() {
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            validator = factory.getValidator();
        }

        clienteValido = new Cliente();
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
    void clienteValido() {
        Set<ConstraintViolation<Cliente>> violations = validator.validate(clienteValido);
        assertTrue(violations.isEmpty(), "El cliente válido no debería tener violaciones de validación");
    }

    @Test
    void nombreConNumeros() {
        clienteValido.setNombre("Juan 123");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(clienteValido);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("El nombre solo puede contener letras y espacios", violations.iterator().next().getMessage());
    }

    @Test
    void edadMenorDe18() {
        clienteValido.setEdad(17);
        Set<ConstraintViolation<Cliente>> violations = validator.validate(clienteValido);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La edad debe ser mayor o igual a 18 años", violations.iterator().next().getMessage());
    }

    @Test
    void edadMayorDe120() {
        clienteValido.setEdad(121);
        Set<ConstraintViolation<Cliente>> violations = validator.validate(clienteValido);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La edad debe ser menor o igual a 120 años", violations.iterator().next().getMessage());
    }

    @Test
    void generoInvalido() {
        clienteValido.setGenero("Novalido");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(clienteValido);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("El género debe ser: Masculino, Femenino u Otro", violations.iterator().next().getMessage());
    }

    @Test
    void direccionCorta() {
        clienteValido.setDireccion("abc");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(clienteValido);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La dirección debe tener entre 5 y 200 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    void telefonoInvalido() {
        clienteValido.setTelefono("12345");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(clienteValido);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("El teléfono debe tener entre 10 y 15 dígitos, opcionalmente comenzando con +", violations.iterator().next().getMessage());
    }

    @Test
    void contrasenaSinMayuscula() {
        clienteValido.setContrasena("password123");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(clienteValido);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La contraseña debe contener al menos una mayúscula, una minúscula y un número", violations.iterator().next().getMessage());
    }

    @Test
    void contrasenaSinMinuscula() {
        clienteValido.setContrasena("PASSWORD123");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(clienteValido);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La contraseña debe contener al menos una mayúscula, una minúscula y un número", violations.iterator().next().getMessage());
    }

    @Test
    void contrasenaSinNumero() {
        clienteValido.setContrasena("Password");
        Set<ConstraintViolation<Cliente>> violations = validator.validate(clienteValido);
        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());
        assertEquals("La contraseña debe contener al menos una mayúscula, una minúscula y un número", violations.iterator().next().getMessage());
    }

    @Test
    void multiplesErrores() {
        clienteValido.setNombre(null);
        clienteValido.setEdad(10);
        clienteValido.setContrasena("abc");

        Set<ConstraintViolation<Cliente>> violations = validator.validate(clienteValido);
        assertFalse(violations.isEmpty());

        assertEquals(4, violations.size());

        Set<String> mensajesDeError = violations.stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toSet());

        assertTrue(mensajesDeError.contains("El nombre es obligatorio"));
        assertTrue(mensajesDeError.contains("La edad debe ser mayor o igual a 18 años"));
        assertTrue(mensajesDeError.contains("La contraseña debe tener entre 6 y 100 caracteres"));
        assertTrue(mensajesDeError.contains("La contraseña debe contener al menos una mayúscula, una minúscula y un número"));
    }
}

