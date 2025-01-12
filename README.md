# Gestión de Turnos

## Descripción

Esta aplicación permite gestionar turnos para diferentes trámites y ciudadanos. Los usuarios pueden agregar nuevos turnos, listar los turnos existentes y filtrarlos por estado y fecha.

## Requisitos del Sistema

- Java Development Kit (JDK) 8 o superior
- Apache Maven
- Servidor de aplicaciones compatible con Java EE (por ejemplo, Apache Tomcat)
- MySQL Server

# Configuración del proyecto

## Base de Datos

1. Instalar MySQL Server si aún no está instalado.
2. Crear una base de datos llamada turnos en MySQL.
3. Ejecutar el siguiente script SQL para crear las tablas necesarias:

```
sql
CREATE DATABASE turnos;
USE turnos;

CREATE TABLE Ciudadano (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255),
    apellido VARCHAR(255)
);

CREATE TABLE Turno (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    numero INT,
    fecha DATE,
    descripcionDelTramite VARCHAR(255),
    estado VARCHAR(255),
    ciudadano_id BIGINT,
    FOREIGN KEY (ciudadano_id) REFERENCES Ciudadano(id)
);
```

## Configuración de la aplicación

1. Clonar el repositorio de GitHub:
  ```
  git clone https://github.com/rosmander/BlancoPerezManuel_pruebatec2.git
  cd BlancoPerezManuel_pruebatec2
  ```
2. Configurar las credenciales de la base de datos en el archivo `src/main/resources/META-INF/persistence.xml `:
  ```
  <property name="javax.persistence.jdbc.url" value="jdbc:mysql://localhost:3306/turnos?serverTimezone=UTC"/>
  <property name="javax.persistence.jdbc.user" value="root"/>
  <property name="javax.persistence.jdbc.password" value="tu_contraseña"/>
  ```
## Compilación y Despliegue

1. Compilar el proyecto utilizando Apache Maven:
  ```
  mvn clean install
  ```
2. Desplegar el archivo WAR generado (`target/pruebatec2-1.0-SNAPSHOT.war`) en el servidor de aplicaciones (por ejemplo, Apache Tomcat).

## Ejecución de la Aplicación

1. Iniciar el servidor de aplicaciones.
2. Acceder a la aplicación en el navegador web utilizando la URL:
  ```
  http://localhost:8080/pruebatec2-1.0-SNAPSHOT
  ```
# Pruebas
## 1. Agregar Nuevo Ciudadano:

• Navegar a la sección "Agregar Nuevo Ciudadano" en la página principal.

• Completar los campos "Nombre" y "Apellido".

• Hacer clic en "Agregar Ciudadano".

• Verificar que el ciudadano haya sido agregado exitosamente y que se muestre un mensaje de éxito.

## 2. Agregar Nuevo Turno:

• Navegar a la sección "Agregar Nuevo Turno" en la página principal.

• Completar los campos "Número de Turno", "Fecha", "Descripción del Trámite", "Estado" y "ID del Ciudadano".

• Hacer clic en "Agregar Turno".

• Verificar que el turno haya sido agregado exitosamente y que se muestre un mensaje de éxito.

## 3. Listar Turnos:

• Navegar a la sección "Listado de Turnos" en la página principal.

• Completar los campos "Fecha" y "Estado".

• Hacer clic en "Buscar Turnos".

• Verificar que se muestren los turnos filtrados por la fecha y el estado proporcionados.

## 4. Validaciones:

• Intentar agregar un ciudadano sin nombre o apellido y verificar que se muestre un mensaje de error.

• Intentar agregar un turno con un número de turno, descripción del trámite o ID de ciudadano inválido y verificar que se muestren los mensajes de error correspondientes.

# Supuestos

• Se asume que la base de datos MySQL está en ejecución y accesible desde la máquina donde se ejecuta la aplicación.

• Las credenciales de la base de datos están configuradas correctamente en el archivo `persistence.xml`.

• El servidor de aplicaciones está configurado para desplegar aplicaciones WAR y está en ejecución.
