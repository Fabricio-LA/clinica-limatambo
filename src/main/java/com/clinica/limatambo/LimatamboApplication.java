package com.clinica.limatambo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.boot.CommandLineRunner;

@SpringBootApplication
public class LimatamboApplication {
    public static void main(String[] args) {
        SpringApplication.run(LimatamboApplication.class, args);
    }

    @Bean
    public CommandLineRunner alterTableColumns(JdbcTemplate jdbcTemplate) {
        return args -> {
            try {
                jdbcTemplate.execute("ALTER TABLE Pacientes ADD direccion VARCHAR(255)");
                System.out.println("Columna 'direccion' agregada con éxito.");
            } catch (Exception e) {
                System.out.println("Columna 'direccion' ya existe o no se pudo agregar.");
            }
            try {
                jdbcTemplate.execute("ALTER TABLE Pacientes ADD foto_perfil VARCHAR(255)");
                System.out.println("Columna 'foto_perfil' agregada con éxito.");
            } catch (Exception e) {
                System.out.println("Columna 'foto_perfil' ya existe o no se pudo agregar.");
            }
            try {
                jdbcTemplate.execute("ALTER TABLE Citas ADD detalle_consulta VARCHAR(1000)");
                System.out.println("Columna 'detalle_consulta' agregada con éxito.");
            } catch (Exception e) {
                System.out.println("Columna 'detalle_consulta' ya existe o no se pudo agregar.");
            }
            try {
                // SQL Server Uses BIT for booleans. Default 0.
                jdbcTemplate.execute("ALTER TABLE Citas ADD notificacion_medico BIT DEFAULT 0");
                // Update existing to 0 to avoid null issues
                jdbcTemplate.execute("UPDATE Citas SET notificacion_medico = 0 WHERE notificacion_medico IS NULL");
                System.out.println("Columna 'notificacion_medico' agregada con éxito.");
            } catch (Exception e) {
                System.out.println("Columna 'notificacion_medico' ya existe o no se pudo agregar.");
            }
            try {
                // Hacemos que id_cita sea NULLABLE para permitir ventas de Farmacia sin cita
                jdbcTemplate.execute("ALTER TABLE Pagos ALTER COLUMN id_cita INT NULL");
                System.out.println("Columna 'id_cita' en Pagos modificada a NULL.");
            } catch (Exception e) {
                System.out.println("No se pudo alterar la columna 'id_cita' o ya es nullable.");
            }
            try {
                // Arreglamos los insumos antiguos sin precio
                jdbcTemplate.execute("UPDATE Insumos SET precio_unitario = 10.00 WHERE precio_unitario IS NULL OR precio_unitario = 0");
            } catch (Exception e) {}

        };
    }
}
