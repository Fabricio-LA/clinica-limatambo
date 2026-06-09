package com.clinica.limatambo.service;

import com.clinica.limatambo.model.*;
import com.clinica.limatambo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private HistorialRepository historialRepository;

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedInsumos();
        seedPacientesCitasYPagos();
    }

    private void seedInsumos() {
        if (insumoRepository.count() < 10) {
            List<Insumo> productos = Arrays.asList(
                createInsumo("Metformina 500mg", "Caja × 30 tabletas", 25.90, true, 50, "Medicamentos"),
                createInsumo("Ibuprofeno 400mg", "Caja × 20 tabletas", 8.50, false, 100, "Medicamentos"),
                createInsumo("Amoxicilina 500mg", "Caja × 21 cápsulas", 18.00, true, 30, "Medicamentos"),
                createInsumo("Losartán 50mg", "Caja × 30 tabletas", 22.50, true, 0, "Medicamentos"),
                createInsumo("Loratadina 10mg", "Caja × 10 tabletas", 15.00, false, 40, "Medicamentos"),
                createInsumo("Omeprazol 20mg", "Caja × 14 cápsulas", 12.00, false, 80, "Medicamentos"),
                createInsumo("Paracetamol 500mg", "Caja × 20 tabletas", 5.50, false, 200, "Medicamentos"),
                createInsumo("Atorvastatina 20mg", "Caja × 30 tabletas", 38.00, true, 25, "Medicamentos"),
                createInsumo("Eucerin Ph5", "Tubo × 40ml", 85.00, false, 15, "Dermocosmética"),
                createInsumo("Vitamina C 1g", "Caja × 10 tabletas", 32.00, false, 60, "Vitaminas y Nutrición"),
                createInsumo("Colágeno Hidrolizado", "Frasco × 60 cápsulas", 45.00, false, 40, "Vitaminas y Nutrición"),
                createInsumo("Magnesio", "Frasco × 60 cápsulas", 68.00, false, 35, "Vitaminas y Nutrición"),
                createInsumo("Multivitamínico Completo", "Frasco × 100 tabletas", 55.00, false, 80, "Vitaminas y Nutrición"),
                createInsumo("Suplemento Proteico en Polvo", "Lata × 400g", 95.00, false, 20, "Vitaminas y Nutrición"),
                createInsumo("Alcohol en Gel Antibacterial", "Frasco × 250ml", 12.50, false, 150, "Cuidado Personal"),
                createInsumo("Pasta Dental Sensibilidad", "Tubo × 75ml", 18.00, false, 90, "Cuidado Personal"),
                createInsumo("Enjuague Bucal", "Frasco × 500ml", 22.00, false, 110, "Cuidado Personal"),
                createInsumo("Jabón Líquido Antibacterial", "Frasco × 300ml", 15.00, false, 200, "Cuidado Personal"),
                createInsumo("Pañales Desechables Talla M", "Paquete × 40 unidades", 45.00, false, 60, "Mamá y Bebé"),
                createInsumo("Toallitas Húmedas para Bebé", "Paquete × 80 unidades", 18.00, false, 130, "Mamá y Bebé"),
                createInsumo("Tensiómetro Digital", "Unidad", 145.00, false, 15, "Botiquín y Equipos"),
                createInsumo("Oxímetro de Pulso", "Unidad", 85.00, false, 25, "Botiquín y Equipos"),
                createInsumo("Vendas Elásticas 10cm", "Rollo × 4.5m", 12.00, false, 200, "Botiquín y Equipos"),
                createInsumo("Botiquín de Primeros Auxilios", "Kit completo", 65.00, false, 40, "Botiquín y Equipos"),
                createInsumo("Mascarillas Desechables KN95", "Caja × 10 unidades", 25.00, false, 300, "Botiquín y Equipos")
            );
            insumoRepository.saveAll(productos);
            System.out.println("25 Insumos médicos sembrados con éxito.");
        }
    }

    private Insumo createInsumo(String name, String pack, double price, boolean reqRx, int stock, String category) {
        Insumo i = new Insumo();
        i.setNombreInsumo(name);
        i.setDescripcion(pack);
        i.setPrecioUnitario(BigDecimal.valueOf(price));
        i.setRequiereReceta(reqRx);
        i.setStockActual(stock);
        i.setCategoria(category);
        return i;
    }

    private void seedPacientesCitasYPagos() {
        // Verificar si ya inyectamos la data para no duplicar en cada reinicio
        if (usuarioRepository.findByUsername("paciente_gen_1").isPresent()) {
            return; 
        }

        List<Medico> medicos = medicoRepository.findAll();
        if (medicos.isEmpty()) {
            System.out.println("No hay médicos en la BD. Saltando la inyección de citas.");
            return;
        }

        String[] nombres = {"Carlos", "María", "Jorge", "Ana", "Luis", "Carmen", "Juan", "Rosa", "Pedro", "Luz", "Jose", "Marta", "Miguel", "Julia", "Victor"};
        String[] apellidos = {"Quispe", "Mamani", "Condori", "Flores", "Rojas", "Huamán", "Chuquimia", "Chávez", "Díaz", "Vargas", "Gutiérrez", "Cruz", "Pérez", "Castillo", "Mendoza"};
        String[] seguros = {"PARTICULAR", "RIMAC", "PACIFICO", "MAPFRE", "EPS"};
        
        Random r = new Random();
        String pass = passwordEncoder.encode("123123");

        for (int i = 0; i < 15; i++) {
            // Crear Usuario
            Usuario u = new Usuario();
            u.setUsername("paciente_gen_" + (i+1));
            u.setPassword(pass);
            u.setEmail("paciente_gen_" + (i+1) + "@limatambo.com");
            u.setIdRol(3); // Paciente
            u.setEstado(true);
            usuarioRepository.save(u);

            // Crear Paciente
            Paciente p = new Paciente();
            p.setNombre(nombres[i]);
            p.setApellido(apellidos[i]);
            // Generar DNI aleatorio de 8 digitos
            int dniBase = 70000000 + r.nextInt(9000000);
            p.setDni(String.valueOf(dniBase));
            p.setIdUsuario(u.getIdUsuario());
            p.setTipoSeguro(seguros[r.nextInt(seguros.length)]);
            p.setDireccion("Av. Las Lomas " + (100 + r.nextInt(500)) + ", Lima");
            p.setFechaNacimiento(LocalDate.now().minusYears(18 + r.nextInt(50)));
            p.setTelefono("9" + (10000000 + r.nextInt(89999999)));
            pacienteRepository.save(p);

            // Crear Historial
            Historial h = new Historial();
            h.setIdPaciente(p.getIdPaciente());
            historialRepository.save(h);

            // Crear Citas y Pagos (1 a 3 citas)
            int numCitas = 1 + r.nextInt(3);
            for (int j = 0; j < numCitas; j++) {
                Cita c = new Cita();
                c.setIdPaciente(p.getIdPaciente());
                Medico med = medicos.get(r.nextInt(medicos.size()));
                c.setIdMedico(med.getIdMedico());
                
                int tipo = r.nextInt(3);
                if (tipo == 0) { // Pasada (Atendida)
                    c.setFechaCita(LocalDate.now().minusDays(r.nextInt(30) + 1));
                    c.setHoraCita(LocalTime.of(8 + r.nextInt(8), 0));
                    c.setEstado("Atendida");
                    c.setDetalleConsulta("Paciente presentó síntomas generales. Se recetó tratamiento estándar.");
                } else if (tipo == 1) { // Futura (Confirmada)
                    c.setFechaCita(LocalDate.now().plusDays(r.nextInt(15) + 1));
                    c.setHoraCita(LocalTime.of(8 + r.nextInt(8), 0));
                    c.setEstado("Confirmada");
                } else { // Cancelada
                    c.setFechaCita(LocalDate.now().minusDays(r.nextInt(10)));
                    c.setHoraCita(LocalTime.of(10, 0));
                    c.setEstado("Cancelada");
                }
                citaRepository.save(c);

                // Crear Pago si fue confirmada o atendida
                if (!c.getEstado().equals("Cancelada")) {
                    Pago pago = new Pago();
                    pago.setCita(c);
                    pago.setMonto(BigDecimal.valueOf(100.0 - r.nextInt(30))); // Tarifa simulada
                    pago.setMetodoPago(r.nextBoolean() ? "Tarjeta" : "Efectivo");
                    pago.setEstado("Pagado");
                    if (c.getEstado().equals("Atendida")) {
                        pago.setFechaPago(c.getFechaCita().atTime(c.getHoraCita()));
                    } else {
                        pago.setFechaPago(LocalDateTime.now().minusHours(r.nextInt(48)));
                    }
                    pagoRepository.save(pago);
                }
            }
        }
        
        // Simular un par de pagos de Farmacia (Ventas sin cita)
        for (int i = 0; i < 5; i++) {
            Pago pagoFarmacia = new Pago();
            pagoFarmacia.setMonto(BigDecimal.valueOf(15.0 + r.nextInt(80)));
            pagoFarmacia.setMetodoPago("Tarjeta (Farmacia)");
            pagoFarmacia.setEstado("Pagado");
            // Distribuidos en los ultimos 2 dias
            pagoFarmacia.setFechaPago(LocalDateTime.now().minusHours(r.nextInt(48)));
            pagoRepository.save(pagoFarmacia);
        }

        System.out.println("Data de prueba (15 Pacientes, Citas, Historiales y Pagos) inyectada con éxito.");
    }
}
