# Gestor de finanzas y tareas

Aplicación backend en Spring Boot para gestionar finanzas personales (ingresos, gastos, cuentas y ahorros) junto con tareas. Incluye autenticación con JWT y recuperación de contraseña por correo electrónico.

## Características

- Gestión de ingresos, gastos, cuentas, ahorros y categorías.
- Gestión de tareas.
- Autenticación y autorización con JWT.
- Recuperación de contraseña con envío de correo.

## Requisitos

- JDK 17
- Maven
- MySQL Server

## Configuración

Actualiza los archivos de configuración según tu entorno:

- `src/main/resources/application.properties`: conexión a la base de datos (URL, usuario y contraseña).
- `src/main/resources/token.properties`: clave y expiración del JWT.
- `src/main/resources/email.properties`: servidor SMTP y credenciales de correo.

> Recomendación: reemplaza las credenciales de ejemplo por variables de entorno o valores locales seguros.

## Ejecutar en desarrollo

1. Asegúrate de que MySQL esté activo y que el usuario y la base de datos existan (o permite la creación automática).
2. Compila y ejecuta la aplicación:

```bash
mvn spring-boot:run
```

La aplicación se iniciará usando la configuración definida en `application.properties`.

## Compilar
El servidor corre en el puerto 8080:

Para compilar el proyecto, utiliza el siguiente comando:

```bash
mvn clean install
```

## Estructura del proyecto

- `src/main/java/com/proyectodesarrollo/gestorfinanzasytareas`: código fuente (controladores, servicios, repositorios y entidades).
- `src/main/resources`: configuración y plantillas de correo.

## Notas

Si necesitas ajustar CORS, seguridad o validaciones, revisa los paquetes en `config/jwt` y los controladores de autenticación.

Este proyecto es el BackEnd de https://github.com/ignaciofranco2003/Gestion-finanzas-y-tareas-front
