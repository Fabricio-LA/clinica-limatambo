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

    @Autowired private RolRepository rolRepository;
    @Autowired private EspecialidadRepository especialidadRepository;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private HistorialRepository historialRepository;
    @Autowired private CitaRepository citaRepository;
    @Autowired private PagoRepository pagoRepository;
    @Autowired private InsumoRepository insumoRepository;
    @Autowired private MedicoRepository medicoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        seedRolesYEspecialidades();
        seedUsuariosYMedicos();
        seedInsumos();
        seedPacientesCitasYPagos();
    }

    private void seedRolesYEspecialidades() {
        if (rolRepository.count() == 0) {
            Rol r1 = new Rol(); r1.setNombreRol("ADMIN");
            Rol r2 = new Rol(); r2.setNombreRol("MEDICO");
            Rol r3 = new Rol(); r3.setNombreRol("PACIENTE");
            rolRepository.saveAll(Arrays.asList(r1, r2, r3));
            System.out.println("Roles sembrados.");
        }
        if (especialidadRepository.count() == 0) {
            Especialidad e1 = new Especialidad(); e1.setNombreEspecialidad("Medicina General");
            Especialidad e2 = new Especialidad(); e2.setNombreEspecialidad("Cardiología");
            Especialidad e3 = new Especialidad(); e3.setNombreEspecialidad("Pediatría");
            Especialidad e4 = new Especialidad(); e4.setNombreEspecialidad("Odontología");
            especialidadRepository.saveAll(Arrays.asList(e1, e2, e3, e4));
            System.out.println("Especialidades sembradas.");
        }
    }

    private void seedUsuariosYMedicos() {
        if (usuarioRepository.findByUsername("admin_clinica").isEmpty()) {
            Usuario admin = new Usuario();
            admin.setUsername("admin_clinica");
            admin.setPassword(passwordEncoder.encode("123456"));
            admin.setEmail("admin@limatambo.com");
            admin.setIdRol(1); // ADMIN
            admin.setEstado(true);
            usuarioRepository.save(admin);
        }

        if (usuarioRepository.findByUsername("medico1").isEmpty()) {
            Usuario m1 = new Usuario();
            m1.setUsername("medico1");
            m1.setPassword(passwordEncoder.encode("123123"));
            m1.setEmail("medico1@limatambo.com");
            m1.setIdRol(2); // MEDICO
            m1.setEstado(true);
            usuarioRepository.save(m1);

            Usuario m2 = new Usuario();
            m2.setUsername("medico2");
            m2.setPassword(passwordEncoder.encode("123123"));
            m2.setEmail("medico2@limatambo.com");
            m2.setIdRol(2);
            m2.setEstado(true);
            usuarioRepository.save(m2);

            // Crear los médicos correspondientes
            Medico med1 = new Medico();
            med1.setNombre("Carlos");
            med1.setApellido("Vera");
            med1.setIdEspecialidad(1);
            med1.setIdUsuario(m1.getIdUsuario());
            med1.setHoraInicio(LocalTime.of(8, 0));
            med1.setHoraFin(LocalTime.of(16, 0));
            med1.setDiasLaborables("1,2,3,4,5");
            med1.setFotoPerfil("doc_m_1.jpg");
            medicoRepository.save(med1);

            Medico med2 = new Medico();
            med2.setNombre("Ana");
            med2.setApellido("Salas");
            med2.setIdEspecialidad(2);
            med2.setIdUsuario(m2.getIdUsuario());
            med2.setHoraInicio(LocalTime.of(10, 0));
            med2.setHoraFin(LocalTime.of(18, 0));
            med2.setDiasLaborables("1,3,5");
            med2.setFotoPerfil("doc_f_1.jpg");
            medicoRepository.save(med2);
            System.out.println("Usuarios base y médicos sembrados.");
        }
    }

    private void seedInsumos() {
        if (insumoRepository.count() < 10) {
            List<Insumo> productos = Arrays.asList(
                createInsumo("Metformina 500mg", "Caja × 30 tabletas", 25.90, true, 50, "Medicamentos", "/images/medicamentos/Metformina_500mg.jpg"),
                createInsumo("Ibuprofeno 400mg", "Caja × 20 tabletas", 8.50, false, 100, "Medicamentos", "/images/medicamentos/Ibuprofeno_400mg.jpg"),
                createInsumo("Amoxicilina 500mg", "Caja × 21 cápsulas", 18.00, true, 30, "Medicamentos", "/images/medicamentos/Amoxicilina_500mg.jpg"),
                createInsumo("Losartán 50mg", "Caja × 30 tabletas", 22.50, true, 0, "Medicamentos", "/images/medicamentos/losartan_50mg.jpg"),
                createInsumo("Loratadina 10mg", "Caja × 10 tabletas", 15.00, false, 40, "Medicamentos", "/images/medicamentos/Loratadina_10mg.jpg"),
                createInsumo("Omeprazol 20mg", "Caja × 14 cápsulas", 12.00, false, 80, "Medicamentos", "/images/medicamentos/Omeprazol_20mg.jpg"),
                createInsumo("Paracetamol 500mg", "Caja × 20 tabletas", 5.50, false, 200, "Medicamentos", "/images/medicamentos/Paracetamol_500mg.jpg"),
                createInsumo("Eucerin Ph5", "Tubo × 40ml", 85.00, false, 15, "Dermocosmética", "/images/medicamentos/Eucerin_Ph5.JPG"),
                createInsumo("Vitamina C 1g", "Caja × 10 tabletas", 32.00, false, 60, "Vitaminas y Nutrición", "/images/medicamentos/vitamina_c_1g.jpg"),
                createInsumo("Alcohol en Gel Antibacterial", "Frasco × 250ml", 12.50, false, 150, "Cuidado Personal", "/images/medicamentos/alcohol_gel.jpg"),
                createInsumo("Tensiómetro Digital", "Unidad", 145.00, false, 15, "Botiquín y Equipos", "/images/medicamentos/tensiometro.jpg")
            );
            insumoRepository.saveAll(productos);
            System.out.println("Insumos sembrados con imágenes.");
        }
    }

    private Insumo createInsumo(String name, String pack, double price, boolean reqRx, int stock, String category, String image) {
        Insumo i = new Insumo();
        i.setNombreInsumo(name);
        i.setDescripcion(pack);
        i.setPrecioUnitario(BigDecimal.valueOf(price));
        i.setRequiereReceta(reqRx);
        i.setStockActual(stock);
        i.setCategoria(category);
        i.setImagen(image);
        return i;
    }

    private void seedPacientesCitasYPagos() {
        if (usuarioRepository.findByUsername("paciente_gen_1").isPresent()) {
            return; 
        }
        List<Medico> medicos = medicoRepository.findAll();
        if (medicos.isEmpty()) return;

        String[] nombres = {"Carlos", "María", "Jorge", "Ana", "Luis", "Carmen", "Juan", "Rosa", "Pedro", "Luz"};
        String[] apellidos = {"Quispe", "Mamani", "Condori", "Flores", "Rojas", "Huamán", "Chuquimia", "Chávez", "Díaz", "Vargas"};
        String[] seguros = {"PARTICULAR", "RIMAC", "PACIFICO", "MAPFRE", "EPS"};
        
        Random r = new Random();
        String pass = passwordEncoder.encode("123123");

        for (int i = 0; i < 10; i++) {
            Usuario u = new Usuario();
            u.setUsername("paciente_gen_" + (i+1));
            u.setPassword(pass);
            u.setEmail("paciente_gen_" + (i+1) + "@limatambo.com");
            u.setIdRol(3); // Paciente
            u.setEstado(true);
            usuarioRepository.save(u);

            Paciente p = new Paciente();
            p.setNombre(nombres[i]);
            p.setApellido(apellidos[i]);
            p.setDni(String.valueOf(70000000 + r.nextInt(9000000)));
            p.setIdUsuario(u.getIdUsuario());
            p.setTipoSeguro(seguros[r.nextInt(seguros.length)]);
            p.setDireccion("Av. Lima " + (100 + r.nextInt(500)));
            p.setFechaNacimiento(LocalDate.now().minusYears(20 + r.nextInt(40)));
            p.setTelefono("9" + (10000000 + r.nextInt(89999999)));
            pacienteRepository.save(p);

            Historial h = new Historial();
            h.setIdPaciente(p.getIdPaciente());
            historialRepository.save(h);

            int numCitas = 1 + r.nextInt(3);
            for (int j = 0; j < numCitas; j++) {
                Cita c = new Cita();
                c.setIdPaciente(p.getIdPaciente());
                c.setIdMedico(medicos.get(r.nextInt(medicos.size())).getIdMedico());
                
                int tipo = r.nextInt(3);
                if (tipo == 0) {
                    c.setFechaCita(LocalDate.now().minusDays(1 + r.nextInt(30)));
                    c.setHoraCita(LocalTime.of(9 + r.nextInt(6), 0));
                    c.setEstado("Atendida");
                    c.setDetalleConsulta("Consulta de rutina.");
                } else if (tipo == 1) {
                    c.setFechaCita(LocalDate.now().plusDays(1 + r.nextInt(15)));
                    c.setHoraCita(LocalTime.of(9 + r.nextInt(6), 0));
                    c.setEstado("Confirmada");
                } else {
                    c.setFechaCita(LocalDate.now().minusDays(r.nextInt(10)));
                    c.setHoraCita(LocalTime.of(10, 0));
                    c.setEstado("Cancelada");
                }
                citaRepository.save(c);

                if (!c.getEstado().equals("Cancelada")) {
                    Pago pago = new Pago();
                    pago.setCita(c);
                    pago.setMonto(BigDecimal.valueOf(100.0 - r.nextInt(30)));
                    pago.setMetodoPago("Tarjeta");
                    pago.setEstado("Pagado");
                    pago.setFechaPago(LocalDateTime.now().minusHours(r.nextInt(48)));
                    pagoRepository.save(pago);
                }
            }
        }
        
        for (int i = 0; i < 3; i++) {
            Pago p = new Pago();
            p.setMonto(BigDecimal.valueOf(30.0 + r.nextInt(50)));
            p.setMetodoPago("Tarjeta (Farmacia)");
            p.setEstado("Pagado");
            p.setFechaPago(LocalDateTime.now().minusHours(r.nextInt(24)));
            pagoRepository.save(p);
        }
        System.out.println("Citas y pagos sembrados.");
    }
}
