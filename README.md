# Devsu Backend BP - Microservicios Bancarios

Repositorio destinado para el proyecto de microservicios bancarios que implementa una arquitectura distribuida para el manejo de clientes y cuentas bancarias.

## Descripción del Proyecto

Este proyecto implementa una solución bancaria utilizando una arquitectura de microservicios compuesta por dos servicios principales:

- **Microservicio de Clientes**: Gestiona la información de los clientes del banco
- **Microservicio de Cuentas**: Maneja las cuentas bancarias y transacciones

### Base de Datos

PostgreSQL con dos esquemas separados:
- `customer_schema`: Para datos de clientes
- `account_schema`: Para cuentas y movimientos

## Requisitos

- Java 21
- Docker y Docker Compose
- PostgreSQL 15+
- Gradle

## Estructura del Proyecto

```
devsu-backend-bp/
├── ms-clientes/                                   # Microservicio de Clientes
│   ├── src/
│   ├── Dockerfile
│   └── ...
├── ms-cuentas/                                    # Microservicio de Cuentas y Movimientos
│   ├── src/
│   ├── Dockerfile
│   └── ...
├── scripts/                                       # Scripts de base de datos
│   ├── BaseDatos.sql                              # Script inicializador de BD
│   └── devsu-backend-bp.postman_collection.json   # Export de Postman
├── docker-compose.yml                             # Orquestación de servicios
└── README.md
```
## Instalación y Ejecución

### 1. Clonar el repositorio

```bash
git clone https://github.com/mariangelPG/devsu-backend-bp.git
cd devsu-backend-bp
```

### 2. Ejecutar con Docker Compose

```bash
# Construir y ejecutar todos los servicios
docker compose up --build

# Ejecutar en segundo plano
docker compose up -d --build
```

Esto iniciará:
- PostgreSQL en puerto 5432
- Microservicio de Clientes en puerto 8091
- Microservicio de Cuentas en puerto 8082


### 3. Verificar los servicios

```bash
# Verificar que todos los contenedores estén ejecutándose
docker ps

# Ver logs de los servicios
docker logs -f ms-clientes
docker logs -f cuentas
```

### 4. Acceder a la base de datos

```bash
docker compose exec postgres psql -U [USER] -d [DATABASE]
```

### 5. Ejecución Local

Si prefieres ejecutar los servicios localmente:

1. Asegúrate de tener PostgreSQL corriendo
2. Ejecuta el script de base de datos:
```bash
psql -U postgres -f scripts/BaseDatos.sql
```

3. Para cada microservicio:
```bash
cd ms-clientes
./gradlew bootRun
```
## Endpoints Principales

### Servicio de Clientes (Puerto 8091)

- `POST /clientes` - Crear nuevo cliente
- `GET /clientes/{id}` - Obtener cliente por ID
- `PUT /clientes/{id}` - Actualizar cliente
- `DELETE /clientes/{id}` - Eliminar cliente
- `PATCH /clientes/{id}` - Actualiza la informacion de un cliente parcialmente

### Servicio de Cuentas (Puerto 8082)

- `GET /cuentas/id/{id}` - Obtener el detalle de una cuenta
- `GET /cuentas/cliente/{id}` - Obtener las cuentas de un cliente
- `POST /cuentas` - Crear nueva cuenta
- `PUT /cuentas/{id}` - Actualizar una cuenta
- `GET /movimientos/id/{id}` - Obtener el detalle de un movimiento por su ID
- `GET /movimientos/cuenta/{id}` - Obtener todos los movimientos de una cuenta
- `POST /movimientos` - Registrar movimiento
- `PUT /movimientos/{id}` - Actualizar un movimiento
- `GET /reportes?fecha=[yyyy-MM-dd_yyyy-MM-dd]&cliente=` - Obtiene todas las cuentas de un cliente, con sus movimientos por rango de fecha.

## Logs

Los logs de cada servicio se guardan en:
- ms-clientes: `logs/cliente-service.log`
- ms-cuentas: `logs/cuentas-service.log`

## Links de Interés

### Documentación del Código
- [Documentación API Clientes](http://localhost:8091/swagger-ui/index.html)
- [Documentación API Cuentas](http://localhost:8082/swagger-ui/index.html)

## Decisiones Técnicas

### Arquitectura de Microservicios

**Separación de Responsabilidades**: Se decidió dividir el sistema en dos microservicios independientes (Clientes y Cuentas). Esto significa que para mantener el verdadero estandar de microservicios, los mismos no se comunican entre si.


### Base de Datos Compartida vs Comunicación Entre Servicios

**Decisión**: Aunque los microservicios comparten una base de datos PostgreSQL, **NO deben comunicarse directamente entre sí**.

**Justificación**:
- Mantener la separación de servicios permitirá que no existan cuellos de botella y que en caso de una caida crítica de algun servicio, el resto seguirá funcionando.

- Una base de datos compartida facilita las transacciones ACID cuando se requiere consistencia fuerte


**Consideraciones Futuras**:
- Migrar hacia **Database per Service** cuando el sistema madure.


## Contacto

- **Desarrollador**: [Mariángel Pérez]
- **Email**: [tmariangelperezgazcon@gmail.com]



