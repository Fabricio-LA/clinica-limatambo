-- =============================================
-- 1. CREACIÓN DE LA BASE DE DATOS
-- =============================================
USE [master];
GO

IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = N'DB_LimaTambo')
BEGIN
    CREATE DATABASE [DB_LimaTambo];
END
GO

USE [DB_LimaTambo];
GO

-- =============================================
-- 2. CREACIÓN DE TABLAS MAESTRAS
-- =============================================

-- Tabla de Roles
CREATE TABLE [dbo].[Roles](
	[id_rol] [int] IDENTITY(1,1) NOT NULL,
	[nombre_rol] [varchar](20) NOT NULL,
    CONSTRAINT [PK_Roles] PRIMARY KEY CLUSTERED ([id_rol] ASC)
);
GO

-- Tabla de Especialidades
CREATE TABLE [dbo].[Especialidades](
	[id_especialidad] [int] IDENTITY(1,1) NOT NULL,
	[nombre_especialidad] [varchar](100) NOT NULL,
    CONSTRAINT [PK_Especialidades] PRIMARY KEY CLUSTERED ([id_especialidad] ASC)
);
GO

-- =============================================
-- 3. TABLAS DE IDENTIDAD Y USUARIOS
-- =============================================

-- Tabla de Usuarios
CREATE TABLE [dbo].[Usuarios](
	[id_usuario] [int] IDENTITY(1,1) NOT NULL,
	[username] [varchar](50) NOT NULL,
	[password] [varchar](255) NOT NULL,
	[id_rol] [int] NULL,
	[email] [varchar](100) NULL,
	[estado] [bit] DEFAULT ((1)),
    CONSTRAINT [PK_Usuarios] PRIMARY KEY CLUSTERED ([id_usuario] ASC),
    CONSTRAINT [UQ_Username] UNIQUE ([username])
);
GO

-- Tabla de Pacientes
CREATE TABLE [dbo].[Pacientes](
	[id_paciente] [int] IDENTITY(1,1) NOT NULL,
	[nombre] [varchar](100) NOT NULL,
	[apellido] [varchar](100) NOT NULL,
	[dni] [char](8) NOT NULL,
	[fecha_nacimiento] [date] NULL,
	[telefono] [varchar](15) NULL,
	[id_usuario] [int] NULL,
    CONSTRAINT [PK_Pacientes] PRIMARY KEY CLUSTERED ([id_paciente] ASC),
    CONSTRAINT [UQ_DNI] UNIQUE ([dni])
);
GO

-- Tabla de Medicos
CREATE TABLE [dbo].[Medicos](
	[id_medico] [int] IDENTITY(1,1) NOT NULL,
	[nombre] [varchar](100) NOT NULL,
	[apellido] [varchar](100) NOT NULL,
	[id_especialidad] [int] NULL,
	[id_usuario] [int] NULL,
	[hora_inicio] [time](7) NULL,
	[hora_fin] [time](7) NULL,
	[dias_laborables] [varchar](50) NULL,
    CONSTRAINT [PK_Medicos] PRIMARY KEY CLUSTERED ([id_medico] ASC)
);
GO

-- =============================================
-- 4. TABLAS DE PROCESOS CLÍNICOS
-- =============================================

-- Tabla de Historiales
CREATE TABLE [dbo].[Historiales](
	[id_historial] [int] IDENTITY(1,1) NOT NULL,
	[id_paciente] [int] UNIQUE NULL,
	[fecha_creacion] [datetime] DEFAULT (getdate()),
    CONSTRAINT [PK_Historiales] PRIMARY KEY CLUSTERED ([id_historial] ASC)
);
GO

-- Tabla de Citas
CREATE TABLE [dbo].[Citas](
	[id_cita] [int] IDENTITY(1,1) NOT NULL,
	[id_paciente] [int] NULL,
	[id_medico] [int] NULL,
	[fecha_cita] [date] NOT NULL,
	[hora_cita] [time](7) NOT NULL,
	[estado] [varchar](20) DEFAULT ('Pendiente'),
    CONSTRAINT [PK_Citas] PRIMARY KEY CLUSTERED ([id_cita] ASC)
);
GO

-- Tabla de Consultas
CREATE TABLE [dbo].[Consultas](
	[id_consulta] [int] IDENTITY(1,1) NOT NULL,
	[id_historial] [int] NULL,
	[id_cita] [int] UNIQUE NULL,
	[motivo_consulta] [text] NULL,
	[sintomas] [text] NULL,
	[diagnostico] [text] NOT NULL,
	[tratamiento] [text] NULL,
    CONSTRAINT [PK_Consultas] PRIMARY KEY CLUSTERED ([id_consulta] ASC)
);
GO

-- Tabla de Recetas
CREATE TABLE [dbo].[Recetas](
	[id_receta] [int] IDENTITY(1,1) NOT NULL,
	[id_consulta] [int] NULL,
	[medicamento] [varchar](255) NULL,
	[indicaciones] [text] NULL,
    CONSTRAINT [PK_Recetas] PRIMARY KEY CLUSTERED ([id_receta] ASC)
);
GO

-- Tabla de Auditoría (Log)
CREATE TABLE [dbo].[Log_Citas](
	[id_log] [int] IDENTITY(1,1) NOT NULL,
	[id_cita] [int] NULL,
	[accion] [varchar](50) NULL,
	[fecha_accion] [datetime] DEFAULT (getdate()),
	[usuario_responsable] [varchar](50) NULL,
    CONSTRAINT [PK_LogCitas] PRIMARY KEY CLUSTERED ([id_log] ASC)
);
GO

-- Tabla de Tokens de Recuperación de Contraseña
CREATE TABLE [dbo].[PasswordResetTokens](
	[id_token] [int] IDENTITY(1,1) NOT NULL,
	[token] [varchar](255) NOT NULL,
	[id_usuario] [int] NOT NULL,
	[fecha_expiracion] [datetime] NOT NULL,
    CONSTRAINT [PK_PasswordResetTokens] PRIMARY KEY CLUSTERED ([id_token] ASC)
);
GO

-- =============================================
-- 5. RELACIONES (LLAVES FORÁNEAS)
-- =============================================

-- Relaciones de Usuarios y Roles
ALTER TABLE [dbo].[Usuarios] ADD FOREIGN KEY([id_rol]) REFERENCES [dbo].[Roles] ([id_rol]);
ALTER TABLE [dbo].[Pacientes] ADD FOREIGN KEY([id_usuario]) REFERENCES [dbo].[Usuarios] ([id_usuario]);
ALTER TABLE [dbo].[Medicos] ADD FOREIGN KEY([id_usuario]) REFERENCES [dbo].[Usuarios] ([id_usuario]);
ALTER TABLE [dbo].[Medicos] ADD FOREIGN KEY([id_especialidad]) REFERENCES [dbo].[Especialidades] ([id_especialidad]);

-- Relaciones Clínicas
ALTER TABLE [dbo].[Historiales] ADD FOREIGN KEY([id_paciente]) REFERENCES [dbo].[Pacientes] ([id_paciente]);
ALTER TABLE [dbo].[Citas] ADD FOREIGN KEY([id_medico]) REFERENCES [dbo].[Medicos] ([id_medico]);
ALTER TABLE [dbo].[Citas] ADD FOREIGN KEY([id_paciente]) REFERENCES [dbo].[Pacientes] ([id_paciente]);
ALTER TABLE [dbo].[Consultas] ADD FOREIGN KEY([id_cita]) REFERENCES [dbo].[Citas] ([id_cita]);
ALTER TABLE [dbo].[Consultas] ADD FOREIGN KEY([id_historial]) REFERENCES [dbo].[Historiales] ([id_historial]);
ALTER TABLE [dbo].[Recetas] ADD FOREIGN KEY([id_consulta]) REFERENCES [dbo].[Consultas] ([id_consulta]);
ALTER TABLE [dbo].[PasswordResetTokens] ADD FOREIGN KEY([id_usuario]) REFERENCES [dbo].[Usuarios] ([id_usuario]);
GO

-- =============================================
-- 6. DATA INICIAL (SEED)
-- =============================================
SET IDENTITY_INSERT [dbo].[Roles] ON;
INSERT INTO [dbo].[Roles] ([id_rol], [nombre_rol]) VALUES (1, 'ADMIN'), (2, 'MEDICO'), (3, 'PACIENTE');
SET IDENTITY_INSERT [dbo].[Roles] OFF;
GO