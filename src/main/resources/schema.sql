-- Paso 1: crear el schema (base de datos)
CREATE SCHEMA IF NOT EXISTS veterinaria;

USE veterinaria;

-- Paso 2: crear la tabla mascotas
-- Ejecutar este script en MySQL Workbench o HeidiSQL antes de correr la aplicación Java
CREATE TABLE IF NOT EXISTS mascotas (
    id          INT PRIMARY KEY AUTO_INCREMENT,
    nombre      VARCHAR(45)  NOT NULL,
    especie     VARCHAR(45)  NOT NULL,
    propietario VARCHAR(100) NOT NULL,
    edad        INT          NOT NULL
);
