USE [DB_LimaTambo];
GO

-- =============================================
-- INSERCIÓN DE DATOS DE PRUEBA (Admin)
-- Nota: Spring Boot ya creó las tablas automáticamente.
-- =============================================

-- Solo insertamos si la tabla Insumos está vacía para no duplicar
IF NOT EXISTS (SELECT 1 FROM [dbo].[Insumos])
BEGIN
    INSERT INTO [dbo].[Insumos] ([nombre_insumo], [descripcion], [stock_actual], [stock_minimo], [unidad_medida]) VALUES 
    ('Paracetamol 500mg', 'Analgésico', 150, 50, 'Caja'),
    ('Jeringas 5ml', 'Jeringas desechables', 200, 100, 'Unidad'),
    ('Guantes de Látex', 'Talla M', 50, 100, 'Caja'),
    ('Alcohol 96°', 'Alcohol etílico', 30, 20, 'Litro'),
    ('Mascarillas N95', 'Mascarillas de protección', 8, 50, 'Caja');
END
GO

-- Solo insertamos si Parametros_Clinica está vacío
IF NOT EXISTS (SELECT 1 FROM [dbo].[Parametros_Clinica])
BEGIN
    INSERT INTO [dbo].[Parametros_Clinica] ([clave], [valor], [descripcion]) VALUES 
    ('NOMBRE_CLINICA', 'Clínica Limatambo', 'Nombre oficial de la clínica'),
    ('RUC', '20123456789', 'RUC de la empresa'),
    ('DIRECCION', 'Av. República de Panamá 3606, San Isidro', 'Dirección sede principal'),
    ('TELEFONO_CONTACTO', '(01) 444-5555', 'Teléfono central'),
    ('PRECIO_BASE_CONSULTA', '120.00', 'Precio base en soles para consultas generales');
END
GO
