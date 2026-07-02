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

    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private EspecialidadRepository especialidadRepository;
    @Autowired private PacienteRepository pacienteRepository;
    @Autowired private HistorialRepository historialRepository;
    @Autowired private CitaRepository citaRepository;
    @Autowired private PagoRepository pagoRepository;
    @Autowired private InsumoRepository insumoRepository;
    @Autowired private MedicoRepository medicoRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // DB is already seeded manually by the user
    }

    private void seedRolesYUsuariosAdmin() {
        if (rolRepository.count() == 0) {
            Rol r1 = new Rol(); r1.setNombreRol("ADMIN");
            Rol r2 = new Rol(); r2.setNombreRol("MEDICO");
            Rol r3 = new Rol(); r3.setNombreRol("PACIENTE");
            Rol r4 = new Rol(); r4.setNombreRol("RECEPCIONISTA");
            rolRepository.saveAll(Arrays.asList(r1, r2, r3, r4));
        }

        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario u = new Usuario();
            u.setUsername("admin");
            u.setPassword(passwordEncoder.encode("admin123"));
            u.setEmail("admin@limatambo.com");
            u.setIdRol(1); // Administrador
            u.setEstado(true);
            usuarioRepository.save(u);
        }
        if (usuarioRepository.findByUsername("recepcion").isEmpty()) {
            Usuario u = new Usuario();
            u.setUsername("recepcion");
            u.setPassword(passwordEncoder.encode("recepcion123"));
            u.setEmail("recepcion@limatambo.com");
            u.setIdRol(4); // Recepcionista
            u.setEstado(true);
            usuarioRepository.save(u);
        }
    }

    private void seedEspecialidadesYMedicos() {
        if (especialidadRepository.count() == 0) {
            Especialidad e1 = new Especialidad(); e1.setNombreEspecialidad("Cardiología");
            Especialidad e2 = new Especialidad(); e2.setNombreEspecialidad("Dermatología");
            Especialidad e3 = new Especialidad(); e3.setNombreEspecialidad("Pediatría");
            Especialidad e4 = new Especialidad(); e4.setNombreEspecialidad("Medicina General");
            Especialidad e5 = new Especialidad(); e5.setNombreEspecialidad("Neurología");
            especialidadRepository.saveAll(Arrays.asList(e1, e2, e3, e4, e5));
        }

        if (medicoRepository.count() == 0) {
            List<Especialidad> especialidades = especialidadRepository.findAll();
            String[] nombresM = {"Carlos", "Luis", "Jorge", "Mario", "Pedro"};
            String[] apellidosM = {"Gomez", "Perez", "Vargas", "Lopez", "Diaz"};
            String[] fotosM = {"doc_m_1.jpg", "doc_m_2.jpg", "doc_m_3.jpg", "doc_m_4.jpg", "doc_m_5.jpg"};

            String[] nombresF = {"Maria", "Ana", "Laura", "Sofia", "Lucia"};
            String[] apellidosF = {"Rojas", "Silva", "Torres", "Castro", "Ramirez"};
            String[] fotosF = {"doc_f_1.jpg", "doc_f_2.jpg", "doc_f_3.jpg", "doc_f_4.jpg", "doc_f_5.jpg"};

            Random r = new Random();
            
            for (int i = 0; i < 5; i++) {
                // Doctor (Male)
                Usuario uM = new Usuario();
                uM.setUsername("dr_" + nombresM[i].toLowerCase());
                uM.setPassword(passwordEncoder.encode("medico123"));
                uM.setEmail("dr_" + nombresM[i].toLowerCase() + "@limatambo.com");
                uM.setIdRol(2); // Medico
                uM.setEstado(true);
                usuarioRepository.save(uM);

                Medico m1 = new Medico();
                m1.setNombre(nombresM[i]);
                m1.setApellido(apellidosM[i]);
                m1.setIdEspecialidad(especialidades.get(r.nextInt(especialidades.size())).getIdEspecialidad());
                m1.setIdUsuario(uM.getIdUsuario());
                m1.setHoraInicio(LocalTime.of(8, 0));
                m1.setHoraFin(LocalTime.of(16, 0));
                m1.setDiasLaborables("1,2,3,4,5");
                m1.setFotoPerfil(fotosM[i]);
                medicoRepository.save(m1);

                // Doctor (Female)
                Usuario uF = new Usuario();
                uF.setUsername("dra_" + nombresF[i].toLowerCase());
                uF.setPassword(passwordEncoder.encode("medico123"));
                uF.setEmail("dra_" + nombresF[i].toLowerCase() + "@limatambo.com");
                uF.setIdRol(2);
                uF.setEstado(true);
                usuarioRepository.save(uF);

                Medico m2 = new Medico();
                m2.setNombre(nombresF[i]);
                m2.setApellido(apellidosF[i]);
                m2.setIdEspecialidad(especialidades.get(r.nextInt(especialidades.size())).getIdEspecialidad());
                m2.setIdUsuario(uF.getIdUsuario());
                m2.setHoraInicio(LocalTime.of(9, 0));
                m2.setHoraFin(LocalTime.of(17, 0));
                m2.setDiasLaborables("1,3,5");
                m2.setFotoPerfil(fotosF[i]);
                medicoRepository.save(m2);
            }
        }
    }

    private void seedInsumos() {
        if (insumoRepository.count() == 0) {
            List<Insumo> productos = Arrays.asList(
                createInsumo("Metformina 500mg", "Caja × 30 tabletas", 25.90, true, 50, "Medicamentos", "/images/medicamentos/Metformina_500mg.jpg"),
                createInsumo("Ibuprofeno 400mg", "Caja × 20 tabletas", 8.50, false, 100, "Medicamentos", "/images/medicamentos/Ibuprofeno_400mg.jpg"),
                createInsumo("Amoxicilina 500mg", "Caja × 21 cápsulas", 18.00, true, 30, "Medicamentos", "/images/medicamentos/Amoxicilina_500mg.jpg"),
                createInsumo("Losartán 50mg", "Caja × 30 tabletas", 18.50, true, 200, "Medicamentos", "/images/medicamentos/losartan_50mg.jpg"),
                createInsumo("Loratadina 10mg", "Caja × 10 tabletas", 15.00, false, 40, "Medicamentos", "/images/medicamentos/Loratadina_10mg.jpg"),
                createInsumo("Omeprazol 20mg", "Caja × 14 cápsulas", 12.00, false, 80, "Medicamentos", "/images/medicamentos/Omeprazol_20mg.jpg"),
                createInsumo("Paracetamol 500mg", "Caja × 20 tabletas", 5.50, false, 200, "Medicamentos", "/images/medicamentos/Paracetamol_500mg.jpg"),
                createInsumo("Atorvastatina 20mg", "Caja × 30 tabletas", 38.00, true, 25, "Medicamentos", "/images/medicamentos/Atorvastatina_20mg.JPG"),
                createInsumo("Eucerin Ph5", "Tubo × 40ml", 85.00, false, 15, "Dermocosmética", "/images/medicamentos/Eucerin_Ph5.JPG"),
                createInsumo("Vitamina C 1g", "Frasco × 100 tabletas", 45.00, false, 80, "Suplementos", "/images/medicamentos/vitamina_c_1g.jpg"),
                createInsumo("Alcohol en Gel Antibacterial", "Frasco × 250ml", 12.50, false, 150, "Cuidado Personal", "/images/medicamentos/alcohol_gel.jpg"),
                createInsumo("Tensiómetro Digital", "Unidad", 120.00, false, 30, "Equipos Médicos", "/images/medicamentos/tensiometro.jpg")
            );
            insumoRepository.saveAll(productos);
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

        String[] nombres = {"Carlos", "María", "Jorge", "Ana", "Luis", "Carmen", "Juan", "Rosa", "Pedro", "Luz", "Jose", "Marta", "Miguel", "Julia", "Victor"};
        String[] apellidos = {"Quispe", "Mamani", "Condori", "Flores", "Rojas", "Huamán", "Chuquimia", "Chávez", "Díaz", "Vargas", "Gutiérrez", "Cruz", "Pérez", "Castillo", "Mendoza"};
        String[] seguros = {"PARTICULAR", "RIMAC", "PACIFICO", "MAPFRE", "EPS"};
        
        Random r = new Random();
        String pass = passwordEncoder.encode("123123");

        for (int i = 0; i < 15; i++) {
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
            p.setDireccion("Av. Las Lomas " + (100 + r.nextInt(500)) + ", Lima");
            p.setFechaNacimiento(LocalDate.now().minusYears(18 + r.nextInt(50)));
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
                    c.setFechaCita(LocalDate.now().minusDays(r.nextInt(30) + 1));
                    c.setHoraCita(LocalTime.of(8 + r.nextInt(8), 0));
                    c.setEstado("Atendida");
                    c.setDetalleConsulta("Paciente presentó síntomas generales. Se recetó tratamiento estándar.");
                } else if (tipo == 1) { 
                    c.setFechaCita(LocalDate.now().plusDays(r.nextInt(15) + 1));
                    c.setHoraCita(LocalTime.of(8 + r.nextInt(8), 0));
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
                    pago.setMetodoPago(r.nextBoolean() ? "Tarjeta" : "Efectivo");
                    pago.setEstado("Pagado");
                    pagoRepository.save(pago);
                }
            }
        }
        
        for (int i = 0; i < 5; i++) {
            Pago pagoFarmacia = new Pago();
            pagoFarmacia.setMonto(BigDecimal.valueOf(15.0 + r.nextInt(80)));
            pagoFarmacia.setMetodoPago("Tarjeta (Farmacia)");
            pagoFarmacia.setEstado("Pagado");
            pagoFarmacia.setFechaPago(LocalDateTime.now().minusHours(r.nextInt(48)));
            pagoRepository.save(pagoFarmacia);
        }
    }
}
