-- Configuración inicial
SET client_encoding = 'UTF8';
SET default_transaction_isolation = 'read committed';
SET timezone = 'UTC';

-- =====================================================
-- CREACIÓN DE ESQUEMAS
-- =====================================================

-- Esquema para ms-clientes (Personas y Clientes)
CREATE SCHEMA IF NOT EXISTS customer_schema;

-- Esquema para ms-cuentas (Cuentas y Movimientos)
CREATE SCHEMA IF NOT EXISTS account_schema;

-- =====================================================
-- CONFIGURACIÓN DE PERMISOS
-- =====================================================

-- Permisos para customer_schema
GRANT ALL PRIVILEGES ON SCHEMA customer_schema TO banking_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA customer_schema TO banking_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA customer_schema TO banking_user;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA customer_schema TO banking_user;

-- Permisos para account_schema
GRANT ALL PRIVILEGES ON SCHEMA account_schema TO banking_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA account_schema TO banking_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA account_schema TO banking_user;
GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA account_schema TO banking_user;

-- =====================================================
-- CUSTOMER SCHEMA - ENTIDADES
-- =====================================================

-- Tabla PERSONA
CREATE TABLE customer_schema.persona (
    persona_id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    genero VARCHAR(20) NOT NULL CHECK (genero IN ('MASCULINO', 'FEMENINO', 'OTRO')),
    edad INTEGER NOT NULL CHECK (edad >= 0 AND edad <= 150),
    identificacion VARCHAR(20) NOT NULL UNIQUE,
    direccion TEXT NOT NULL,
    telefono VARCHAR(20) 
);

-- Tabla CLIENTE (Hereda de Persona)
CREATE TABLE customer_schema.cliente (
    cliente_id BIGSERIAL PRIMARY KEY,
    persona_id BIGINT NOT NULL REFERENCES customer_schema.persona(persona_id) ON DELETE CASCADE,
    contrasena VARCHAR(255) NOT NULL, -- Hash de la contraseña
    estado BOOLEAN DEFAULT TRUE
);

-- =====================================================
-- ACCOUNT SCHEMA - ENTIDADES
-- =====================================================

-- Tabla CUENTA
CREATE TABLE account_schema.cuenta (
    cuenta_id BIGSERIAL PRIMARY KEY,
    numero_cuenta VARCHAR(20) NOT NULL UNIQUE,
    tipo_cuenta VARCHAR(20) NOT NULL CHECK (tipo_cuenta IN ('AHORROS', 'CORRIENTE', 'CREDITO')),
    saldo_inicial DECIMAL(15,2) NOT NULL DEFAULT 0.00 CHECK (saldo_inicial >= 0),
    saldo_actual DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    estado VARCHAR(20) NOT NULL DEFAULT 'ACTIVA' CHECK (estado IN ('ACTIVA', 'INACTIVA', 'BLOQUEADA', 'CERRADA')),
    cliente_id VARCHAR(50) NOT NULL, -- Referencia al client_id del microservicio de clientes
    fecha_creacion TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    fecha_actualizacion TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Tabla MOVIMIENTOS
CREATE TABLE account_schema.movimiento (
    movimiento_id BIGSERIAL PRIMARY KEY,
    cuenta_id BIGINT NOT NULL REFERENCES account_schema.cuenta(cuenta_id) ON DELETE CASCADE,
    fecha_movimiento TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento VARCHAR(30) NOT NULL CHECK (tipo_movimiento IN ('DEPOSITO', 'RETIRO', 'TRANSFERENCIA_ENTRADA', 'TRANSFERENCIA_SALIDA', 'PAGO', 'INTERES', 'COMISION')),
    valor DECIMAL(15,2) NOT NULL CHECK (valor != 0), -- Positivo para ingresos, negativo para egresos
    saldo_anterior DECIMAL(15,2) NOT NULL,
    saldo_posterior DECIMAL(15,2) NOT NULL,
);

-- =====================================================
-- ÍNDICES PARA OPTIMIZACIÓN
-- =====================================================

-- Índices para Customer Schema
CREATE INDEX idx_persona_identificacion ON customer_schema.persona(identificacion);
CREATE INDEX idx_persona_nombre ON customer_schema.persona(nombre);

CREATE INDEX idx_cliente_cliente_id ON customer_schema.cliente(cliente_id);
CREATE INDEX idx_cliente_persona_id ON customer_schema.cliente(persona_id);
CREATE INDEX idx_cliente_estado ON customer_schema.cliente(estado);


-- Índices para Account Schema
CREATE INDEX idx_cuenta_numero_cuenta ON account_schema.cuenta(numero_cuenta);
CREATE INDEX idx_cuenta_cliente_id ON account_schema.cuenta(cliente_id);
CREATE INDEX idx_cuenta_tipo_cuenta ON account_schema.cuenta(tipo_cuenta);
CREATE INDEX idx_cuenta_estado ON account_schema.cuenta(estado);

CREATE INDEX idx_movimiento_cuenta_id ON account_schema.movimiento(cuenta_id);
CREATE INDEX idx_movimiento_fecha ON account_schema.movimiento(fecha_movimiento);
CREATE INDEX idx_movimiento_tipo ON account_schema.movimiento(tipo_movimiento);

-- Índice compuesto para consultas de movimientos por cuenta y fecha
CREATE INDEX idx_movimiento_cuenta_fecha ON account_schema.movimiento(cuenta_id, fecha_movimiento DESC);