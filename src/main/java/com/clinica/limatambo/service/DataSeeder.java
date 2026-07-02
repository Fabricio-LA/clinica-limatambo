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
    private RolRepository rolRepository;

    @Autowired
    private EspecialidadRepository especialidadRepository;

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
        seedRolesYUsuariosAdmin();
        seedEspecialidadesYMedicos();
        seedInsumos();
        seedPacientesCitasYPagos();
    }

    private void seedRolesYUsuariosAdmin() {
        if (rolRepository.count() == 0) {
            Rol r1 = new Rol(); r1.setNombreRol("Administrador");
            Rol r2 = new Rol(); r2.setNombreRol("Medico");
            Rol r3 = new Rol(); r3.setNombreRol("Paciente");
            Rol r4 = new Rol(); r4.setNombreRol("Recepcionista");
            rolRepository.saveAll(Arrays.asList(r1, r2, r3, r4));
            System.out.println("Roles sembrados con éxito.");
        }

        if (usuarioRepository.findByUsername("admin").isEmpty()) {
            Usuario u = new Usuario();
            u.setUsername("admin");
            u.setPassword(passwordEncoder.encode("admin123"));
            u.setEmail("admin@limatambo.com");
            u.setIdRol(1); // Administrador
            u.setEstado(true);
            usuarioRepository.save(u);
            System.out.println("Usuario Administrador sembrado.");
        }

        if (usuarioRepository.findByUsername("recepcion").isEmpty()) {
            Usuario u = new Usuario();
            u.setUsername("recepcion");
            u.setPassword(passwordEncoder.encode("recepcion123"));
            u.setEmail("recepcion@limatambo.com");
            u.setIdRol(4); // Recepcionista
            u.setEstado(true);
            usuarioRepository.save(u);
            System.out.println("Usuario Recepcionista sembrado.");
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
            System.out.println("Especialidades sembradas con éxito.");
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
            System.out.println("10 Médicos sembrados con éxito.");
        }
    }

    private void seedInsumos() {
        if (insumoRepository.count() < 10) {
            List<Insumo> productos = Arrays.asList(
                createInsumo("Metformina 500mg", "Caja × 30 tabletas", 25.90, true, 50, "Medicamentos", "Metformina_500mg.jpg"),
                createInsumo("Ibuprofeno 400mg", "Caja × 20 tabletas", 8.50, false, 100, "Medicamentos", "Ibuprofeno_400mg.jpg"),
                createInsumo("Amoxicilina 500mg", "Caja × 21 cápsulas", 18.00, true, 30, "Medicamentos", "Amoxicilina_500mg.jpg"),
                createInsumo("Losartán 50mg", "Caja × 30 tabletas", 22.50, true, 0, "Medicamentos", "Losartán_50mg.JPG"),
                createInsumo("Loratadina 10mg", "Caja × 10 tabletas", 15.00, false, 40, "Medicamentos", "Loratadina_10mg.jpg"),
                createInsumo("Omeprazol 20mg", "Caja × 14 cápsulas", 12.00, false, 80, "Medicamentos", "Omeprazol_20mg.jpg"),
                createInsumo("Paracetamol 500mg", "Caja × 20 tabletas", 5.50, false, 200, "Medicamentos", "Paracetamol_500mg.jpg"),
                createInsumo("Atorvastatina 20mg", "Caja × 30 tabletas", 38.00, true, 25, "Medicamentos", "Atorvastatina_20mg.JPG"),
                createInsumo("Eucerin Ph5", "Tubo × 40ml", 85.00, false, 15, "Dermocosmética", "Eucerin_Ph5.JPG"),
                createInsumo("Vitamina C 1g", "Caja × 10 tabletas", 32.00, false, 60, "Vitaminas y Nutrición", "Vitamina C_1g.JPG"),
                createInsumo("Colágeno Hidrolizado", "Frasco × 60 cápsulas", 45.00, false, 40, "Vitaminas y Nutrición", "Colágeno_Hidrolizado.JPG"),
                createInsumo("Magnesio", "Frasco × 60 cápsulas", 68.00, false, 35, "Vitaminas y Nutrición", "Magnesio_60.JPG"),
                createInsumo("Multivitamínico Completo", "Frasco × 100 tabletas", 55.00, false, 80, "Vitaminas y Nutrición", "Multivitamínico_Completo.JPG"),
                createInsumo("Suplemento Proteico en Polvo", "Lata × 400g", 95.00, false, 20, "Vitaminas y Nutrición", "Suplemento Proteico en Polvo.JPG"),
                createInsumo("Alcohol en Gel Antibacterial", "Frasco × 250ml", 12.50, false, 150, "Cuidado Personal", "Alcohol en Gel Antibacterial.JPG"),
                createInsumo("Pasta Dental Sensibilidad", "Tubo × 75ml", 18.00, false, 90, "Cuidado Personal", "Pasta Dental Sensibilidad.JPG"),
                createInsumo("Enjuague Bucal", "Frasco × 500ml", 22.00, false, 110, "Cuidado Personal", "Enjuague Bucal.JPG"),
                createInsumo("Jabón Líquido Antibacterial", "Frasco × 300ml", 15.00, false, 200, "Cuidado Personal", "Jabón Líquido Antibacterial.JPG"),
                createInsumo("Pañales Desechables Talla M", "Paquete × 40 unidades", 45.00, false, 60, "Mamá y Bebé", "Pañales Desechables Talla M.JPG"),
                createInsumo("Toallitas Húmedas para Bebé", "Paquete × 80 unidades", 18.00, false, 130, "Mamá y Bebé", "Toallitas Húmedas para Bebé.JPG"),
                createInsumo("Tensiómetro Digital", "Unidad", 145.00, false, 15, "Botiquín y Equipos", "Tensiómetro Digital.JPG"),
                createInsumo("Oxímetro de Pulso", "Unidad", 85.00, false, 25, "Botiquín y Equipos", "Oxímetro de Pulso.JPG"),
                createInsumo("Vendas Elásticas 10cm", "Rollo × 4.5m", 12.00, false, 200, "Botiquín y Equipos", "Vendas Elásticas 10cm.jpg"),
                createInsumo("Botiquín de Primeros Auxilios", "Kit completo", 65.00, false, 40, "Botiquín y Equipos", "Botiquín de Primeros Auxilios.JPG"),
                createInsumo("Mascarillas Desechables KN95", "Caja × 10 unidades", 25.00, false, 300, "Botiquín y Equipos", "Mascarillas Desechables KN95.JPG"),
                createInsumo("Guantes de Látex", "Caja x 100 unidades", 35.00, false, 100, "Botiquín y Equipos", "GuantesdeLatex.jpg"),
                createInsumo("Jeringas 5ml", "Caja x 100 unidades", 25.00, false, 50, "Botiquín y Equipos", "Jeringas_5ml.jpg"),
                createInsumo("Alcohol 96°", "Frasco x 1 litro", 10.00, false, 80, "Botiquín y Equipos", "Alcohol_96.jpg"),
                createInsumo("Mascarilla N95", "Caja x 20 unidades", 40.00, false, 120, "Botiquín y Equipos", "Mascarilla_N95.JPG")
            );
            insumoRepository.saveAll(productos);
            System.out.println(productos.size() + " Insumos médicos sembrados con éxito.");
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
