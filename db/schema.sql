/*
  PROYECTO: Clínica Lima Tambo
  DATABASE: SQL Server
  DESCRIPCIÓN: Script para replicar la estructura de la base de datos.
  ORDEN: Ejecutar este script completo para crear las 10 tablas y relaciones.
*/

-- Crear Base de Datos si no existe
IF NOT EXISTS (SELECT * FROM sys.databases WHERE name = 'DB_LimaTambo')
BEGIN
  CREATE DATABASE DB_LimaTambo;
END
GO

USE DB_LimaTambo;
GO

-- 1. ROLES
CREATE TABLE Roles (
    id_rol INT PRIMARY KEY IDENTITY(1,1),
    nombre_rol VARCHAR(20) NOT NULL -- 'ADMIN', 'MEDICO', 'PACIENTE'
);

-- 2. USUARIOS (Login)
CREATE TABLE Usuarios (
    id_usuario INT PRIMARY KEY IDENTITY(1,1),
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    id_rol INT,
    estado BIT DEFAULT 1, -- 1: Activo, 0: Inactivo
    FOREIGN KEY (id_rol) REFERENCES Roles(id_rol)
);

-- 3. ESPECIALIDADES
CREATE TABLE Especialidades (
    id_especialidad INT PRIMARY KEY IDENTITY(1,1),
    nombre_especialidad VARCHAR(100) NOT NULL
);

-- 4. MEDICOS
CREATE TABLE Medicos (
    id_medico INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    id_especialidad INT,
    id_usuario INT, 
    FOREIGN KEY (id_especialidad) REFERENCES Especialidades(id_especialidad),
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

-- 5. PACIENTES
CREATE TABLE Pacientes (
    id_paciente INT PRIMARY KEY IDENTITY(1,1),
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    dni CHAR(8) UNIQUE NOT NULL,
    fecha_nacimiento DATE,
    id_usuario INT, 
    FOREIGN KEY (id_usuario) REFERENCES Usuarios(id_usuario)
);

-- 6. CITAS MÉDICAS
CREATE TABLE Citas (
    id_cita INT PRIMARY KEY IDENTITY(1,1),
    id_paciente INT,
    id_medico INT,
    fecha_cita DATE NOT NULL,
    hora_cita TIME NOT NULL,
    estado VARCHAR(20) DEFAULT 'Pendiente', -- 'Pendiente', 'Completada', 'Cancelada'
    FOREIGN KEY (id_paciente) REFERENCES Pacientes(id_paciente),
    FOREIGN KEY (id_medico) REFERENCES Medicos(id_medico)
);

-- 7. HISTORIAL CLÍNICO
CREATE TABLE Historiales (
    id_historial INT PRIMARY KEY IDENTITY(1,1),
    id_paciente INT UNIQUE,
    fecha_creacion DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (id_paciente) REFERENCES Pacientes(id_paciente)
);

-- 8. CONSULTAS / DIAGNÓSTICOS (Detalle del Historial)
CREATE TABLE Consultas (
    id_consulta INT PRIMARY KEY IDENTITY(1,1),
    id_historial INT,
    id_cita INT UNIQUE, -- Una cita genera un solo diagnóstico
    motivo_consulta TEXT,
    sintomas TEXT,
    diagnostico TEXT NOT NULL,
    tratamiento TEXT,
    FOREIGN KEY (id_historial) REFERENCES Historiales(id_historial),
    FOREIGN KEY (id_cita) REFERENCES Citas(id_cita)
);

-- 9. RECETAS
CREATE TABLE Recetas (
    id_receta INT PRIMARY KEY IDENTITY(1,1),
    id_consulta INT,
    medicamento VARCHAR(255),
    indicaciones TEXT,
    FOREIGN KEY (id_consulta) REFERENCES Consultas(id_consulta)
);

-- 10. AUDITORÍA DE CITAS (Para el Administrador)
CREATE TABLE Log_Citas (
    id_log INT PRIMARY KEY IDENTITY(1,1),
    id_cita INT,
    accion VARCHAR(50), -- 'CREACION', 'CAMBIO ESTADO', 'CANCELACION'
    fecha_accion DATETIME DEFAULT GETDATE(),
    usuario_responsable VARCHAR(50)
);