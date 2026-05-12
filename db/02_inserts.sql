-- =============================================
-- DATA INICIAL
-- =============================================
USE [DB_LimaTambo];
GO

-- 1. Insertar Roles
IF NOT EXISTS (SELECT 1 FROM [dbo].[Roles])
BEGIN
    SET IDENTITY_INSERT [dbo].[Roles] ON;
    INSERT INTO [dbo].[Roles] ([id_rol], [nombre_rol]) VALUES 
    (1, 'ADMIN'), 
    (2, 'MEDICO'), 
    (3, 'PACIENTE');
    SET IDENTITY_INSERT [dbo].[Roles] OFF;
END
GO

-- 2. Limpieza de datos
DELETE FROM [dbo].[Citas];
DELETE FROM [dbo].[Medicos];
DELETE FROM [dbo].[Especialidades];
DELETE FROM [dbo].[Usuarios] WHERE id_rol = 2;
GO

DBCC CHECKIDENT ('[dbo].[Citas]', RESEED, 0);
DBCC CHECKIDENT ('[dbo].[Medicos]', RESEED, 0);
DBCC CHECKIDENT ('[dbo].[Especialidades]', RESEED, 0);
GO

-- 3. Insertar Especialidades
INSERT INTO [dbo].[Especialidades] ([nombre_especialidad]) VALUES 
('Medicina General'),
('Cardiología'),
('Pediatría'),
('Dermatología'),
('Ginecología'),
('Odontología');
GO

-- 4. Insertar Usuarios
IF NOT EXISTS (SELECT 1 FROM [dbo].[Usuarios] WHERE username = 'admin01')
BEGIN
    INSERT INTO [dbo].[Usuarios] ([username], [password], [id_rol], [estado]) VALUES 
    ('admin01', '$2a$10$G1ebiCNzbIH8gvjWMwYc/umW8ZIwP5FpbRdYOvmwUAgMYEAntgy.a', 1, 1),
    ('paciente01', '$2a$10$G1ebiCNzbIH8gvjWMwYc/umW8ZIwP5FpbRdYOvmwUAgMYEAntgy.a', 3, 1);
END
GO

-- 5. Insertar Usuarios Médicos
DECLARE @pass VARCHAR(255) = '$2a$10$G1ebiCNzbIH8gvjWMwYc/umW8ZIwP5FpbRdYOvmwUAgMYEAntgy.a';

INSERT INTO [dbo].[Usuarios] ([username], [password], [id_rol], [estado]) VALUES 
('10000001', @pass, 2, 1), ('10000002', @pass, 2, 1), ('10000003', @pass, 2, 1), ('10000004', @pass, 2, 1),
('20000001', @pass, 2, 1), ('20000002', @pass, 2, 1), ('20000003', @pass, 2, 1), ('20000004', @pass, 2, 1),
('30000001', @pass, 2, 1), ('30000002', @pass, 2, 1), ('30000003', @pass, 2, 1), ('30000004', @pass, 2, 1),
('40000001', @pass, 2, 1), ('40000002', @pass, 2, 1), ('40000003', @pass, 2, 1), ('40000004', @pass, 2, 1),
('50000001', @pass, 2, 1), ('50000002', @pass, 2, 1), ('50000003', @pass, 2, 1), ('50000004', @pass, 2, 1),
('60000001', @pass, 2, 1), ('60000002', @pass, 2, 1), ('60000003', @pass, 2, 1), ('60000004', @pass, 2, 1);
GO

-- 6. Insertar Médicos
DECLARE @idEsp INT;

SET @idEsp = (SELECT id_especialidad FROM Especialidades WHERE nombre_especialidad = 'Medicina General');
INSERT INTO [dbo].[Medicos] (nombre, apellido, id_especialidad, id_usuario, hora_inicio, hora_fin, dias_laborables) VALUES
('Carlos', 'Mendoza', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '10000001'), '08:00', '14:00', '1,2,3,4,5'),
('Ana', 'Suárez', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '10000002'), '14:00', '20:00', '1,2,3,4,5'),
('Jorge', 'Linares', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '10000003'), '09:00', '13:00', '6,7'),
('Elena', 'Vargas', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '10000004'), '10:00', '18:00', '1,3,5');

SET @idEsp = (SELECT id_especialidad FROM Especialidades WHERE nombre_especialidad = 'Cardiología');
INSERT INTO [dbo].[Medicos] (nombre, apellido, id_especialidad, id_usuario, hora_inicio, hora_fin, dias_laborables) VALUES
('Luis', 'Fernández', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '20000001'), '09:00', '15:00', '1,2,3,4,5'),
('Marta', 'Ríos', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '20000002'), '15:00', '21:00', '1,2,3,4,5'),
('Pedro', 'Gómez', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '20000003'), '08:00', '12:00', '6'),
('Sofía', 'Castro', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '20000004'), '08:00', '16:00', '2,4');

SET @idEsp = (SELECT id_especialidad FROM Especialidades WHERE nombre_especialidad = 'Pediatría');
INSERT INTO [dbo].[Medicos] (nombre, apellido, id_especialidad, id_usuario, hora_inicio, hora_fin, dias_laborables) VALUES
('Lucía', 'Martínez', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '30000001'), '08:00', '14:00', '1,2,3,4,5'),
('Ricardo', 'Paz', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '30000002'), '14:00', '18:00', '1,2,3,4,5'),
('Julia', 'Rojas', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '30000003'), '09:00', '14:00', '6,7'),
('Andrés', 'Soto', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '30000004'), '10:00', '19:00', '1,3,5');

SET @idEsp = (SELECT id_especialidad FROM Especialidades WHERE nombre_especialidad = 'Dermatología');
INSERT INTO [dbo].[Medicos] (nombre, apellido, id_especialidad, id_usuario, hora_inicio, hora_fin, dias_laborables) VALUES
('Carmen', 'Torres', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '40000001'), '10:00', '16:00', '1,2,3,4,5'),
('David', 'Ruiz', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '40000002'), '16:00', '20:00', '1,2,3,4,5'),
('Valeria', 'Cruz', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '40000003'), '08:00', '13:00', '6'),
('Héctor', 'Salas', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '40000004'), '09:00', '17:00', '2,4');

SET @idEsp = (SELECT id_especialidad FROM Especialidades WHERE nombre_especialidad = 'Ginecología');
INSERT INTO [dbo].[Medicos] (nombre, apellido, id_especialidad, id_usuario, hora_inicio, hora_fin, dias_laborables) VALUES
('Laura', 'Pérez', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '50000001'), '08:00', '15:00', '1,2,3,4,5'),
('Beatriz', 'Luna', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '50000002'), '15:00', '21:00', '1,2,3,4,5'),
('Miguel', 'Vega', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '50000003'), '09:00', '14:00', '6,7'),
('Rosa', 'Silva', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '50000004'), '08:00', '16:00', '1,3,5');

SET @idEsp = (SELECT id_especialidad FROM Especialidades WHERE nombre_especialidad = 'Odontología');
INSERT INTO [dbo].[Medicos] (nombre, apellido, id_especialidad, id_usuario, hora_inicio, hora_fin, dias_laborables) VALUES
('Hugo', 'Quispe', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '60000001'), '09:00', '18:00', '1,2,3,4,5'),
('Natalia', 'Ponce', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '60000002'), '14:00', '20:00', '1,2,3,4,5'),
('Óscar', 'Díaz', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '60000003'), '08:00', '15:00', '6'),
('Diana', 'García', @idEsp, (SELECT id_usuario FROM Usuarios WHERE username = '60000004'), '10:00', '19:00', '2,4');
GO

-- 7. Insertar Pacientes
IF NOT EXISTS (SELECT 1 FROM [dbo].[Pacientes])
BEGIN
    DECLARE @IdUsuarioPaciente INT = (SELECT id_usuario FROM [dbo].[Usuarios] WHERE username = 'paciente01');

    INSERT INTO [dbo].[Pacientes] ([nombre], [apellido], [dni], [fecha_nacimiento], [id_usuario]) 
    VALUES ('María', 'González', '12345678', '1990-05-15', @IdUsuarioPaciente);
END
GO

-- 8. Insertar Insumos
INSERT INTO [dbo].[Insumos] ([nombre_insumo], [descripcion], [stock_actual], [stock_minimo], [unidad_medida]) VALUES 
('Paracetamol 500mg', 'Analgésico', 150, 50, 'Caja'),
('Jeringas 5ml', 'Jeringas desechables', 200, 100, 'Unidad'),
('Guantes de Látex', 'Talla M', 50, 100, 'Caja'),
('Alcohol 96°', 'Alcohol etílico', 30, 20, 'Litro'),
('Mascarillas N95', 'Mascarillas de protección', 8, 50, 'Caja');
GO

-- 9. Insertar Parámetros Clínica
INSERT INTO [dbo].[Parametros_Clinica] ([clave], [valor], [descripcion]) VALUES 
('NOMBRE_CLINICA', 'Clínica Limatambo', 'Nombre oficial de la clínica'),
('RUC', '20123456789', 'RUC de la empresa'),
('DIRECCION', 'Av. República de Panamá 3606, San Isidro', 'Dirección sede principal'),
('TELEFONO_CONTACTO', '(01) 444-5555', 'Teléfono central'),
('PRECIO_BASE_CONSULTA', '120.00', 'Precio base en soles para consultas generales');
GO
