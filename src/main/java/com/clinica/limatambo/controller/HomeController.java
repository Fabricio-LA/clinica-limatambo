package com.clinica.limatambo.controller;

import com.clinica.limatambo.repository.EspecialidadRepository;
import com.clinica.limatambo.repository.MedicoRepository;
import com.clinica.limatambo.repository.UsuarioRepository;
import com.clinica.limatambo.repository.PacienteRepository;
import com.clinica.limatambo.repository.InsumoRepository;
import com.clinica.limatambo.repository.PagoRepository;
import com.clinica.limatambo.model.Usuario;
import com.clinica.limatambo.model.Paciente;
import com.clinica.limatambo.model.Pago;
import com.clinica.limatambo.service.DescuentoService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.util.List;
import java.util.Map;
import com.clinica.limatambo.model.Insumo;

@Controller
public class HomeController {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private DescuentoService descuentoService;

    @Autowired
    private com.clinica.limatambo.service.EmailService emailService;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @GetMapping("/")
    public String inicio(Model model) {
        model.addAttribute("especialidades", especialidadRepository.findAll());
        
        java.util.List<com.clinica.limatambo.model.Medico> medicosDB = medicoRepository.findAll();
        java.util.List<MedicoIndexDTO> medicos = new java.util.ArrayList<>();
        
        for (com.clinica.limatambo.model.Medico m : medicosDB) {
            String especialidadNombre = "General";
            if (m.getIdEspecialidad() != null) {
                java.util.Optional<com.clinica.limatambo.model.Especialidad> espOpt = especialidadRepository.findById(m.getIdEspecialidad());
                if (espOpt.isPresent()) {
                    especialidadNombre = espOpt.get().getNombreEspecialidad();
                }
            }
            medicos.add(new MedicoIndexDTO(m, especialidadNombre));
        }
        
        model.addAttribute("medicos", medicos);
        return "index";
    }

    @GetMapping("/dashboard-router")
    public String dashboardRouter(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            boolean isPaciente = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_PACIENTE"));
            boolean isMedico = authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_MEDICO"));
            if (isPaciente) return "redirect:/paciente/dashboard";
            if (isMedico) return "redirect:/medico/dashboard";
            return "redirect:/admin/dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/ayuda")
    public String mostrarAyuda() {
        return "ayuda";
    }

    @GetMapping("/terminos")
    public String mostrarTerminos() {
        return "terminos";
    }

    @GetMapping("/farmacia")
    public String mostrarFarmacia(Model model, Authentication authentication) {
        String tipoSeguro = "PARTICULAR";
        double descuento = 0.0;
        String clienteNombre = "Público en General";
        
        if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(authentication.getName());
            if (usuarioOpt.isPresent()) {
                Optional<Paciente> pacienteOpt = pacienteRepository.findByIdUsuario(usuarioOpt.get().getIdUsuario());
                if (pacienteOpt.isPresent()) {
                    clienteNombre = pacienteOpt.get().getNombre() + " " + pacienteOpt.get().getApellido();
                    if (pacienteOpt.get().getTipoSeguro() != null && !pacienteOpt.get().getTipoSeguro().isEmpty()) {
                        tipoSeguro = pacienteOpt.get().getTipoSeguro().toUpperCase();
                    }
                } else {
                    clienteNombre = usuarioOpt.get().getUsername();
                }
            }
        }
        
        descuento = descuentoService.obtenerPorcentajeDescuento(tipoSeguro);
        
        model.addAttribute("tipoSeguro", tipoSeguro);
        model.addAttribute("descuentoSeguro", descuento);
        model.addAttribute("clienteNombre", clienteNombre);
        model.addAttribute("insumos", insumoRepository.findAll());
        
        return "farmacia";
    }

    @PostMapping("/api/enviar-boleta")
    @ResponseBody
    public ResponseEntity<?> enviarBoleta(
            @RequestParam("boletaFile") MultipartFile file,
            @RequestParam("boletaNumber") String boletaNumber,
            @RequestParam(value = "total", required = false) BigDecimal total,
            @RequestParam(value = "cartItems", required = false) String cartItemsJson,
            @RequestParam(value = "clienteNombre", required = false) String clienteNombre,
            Authentication authentication) {
        
        try {
            // Create Pago record
            Pago nuevoPago = new Pago();
            nuevoPago.setMonto(total != null ? total : BigDecimal.ZERO);
            nuevoPago.setMetodoPago("Tarjeta (Farmacia)");
            nuevoPago.setEstado("Pagado");
            nuevoPago.setFechaPago(LocalDateTime.now());
            if (clienteNombre != null && !clienteNombre.trim().isEmpty()) {
                nuevoPago.setNombreClienteFarmacia(clienteNombre);
            } else {
                nuevoPago.setNombreClienteFarmacia("Público en General");
            }
            pagoRepository.save(nuevoPago);

            // Deduct stock
            if (cartItemsJson != null && !cartItemsJson.isEmpty()) {
                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, Object>> cartItems = mapper.readValue(cartItemsJson, new TypeReference<List<Map<String, Object>>>() {});
                for (Map<String, Object> item : cartItems) {
                    String idStr = item.get("idInsumo").toString();
                    int cantidad = Integer.parseInt(item.get("cantidad").toString());
                    Optional<Insumo> insumoOpt = insumoRepository.findById(Integer.parseInt(idStr));
                    if (insumoOpt.isPresent()) {
                        Insumo insumo = insumoOpt.get();
                        int nuevoStock = insumo.getStockActual() - cantidad;
                        if (nuevoStock < 0) nuevoStock = 0;
                        insumo.setStockActual(nuevoStock);
                        insumoRepository.save(insumo);
                    }
                }
            }

            // Attempt to send email only if authenticated and has a valid email
            if (authentication != null && authentication.isAuthenticated() && !authentication.getName().equals("anonymousUser")) {
                Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(authentication.getName());
                if (usuarioOpt.isPresent()) {
                    Usuario u = usuarioOpt.get();
                    String email = u.getEmail();
                    if (email != null && email.contains("@")) {
                        emailService.enviarBoletaConAdjunto(email, file.getBytes(), boletaNumber);
                    }
                }
            }
            
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Error procesando pago");
        }
    }

    @GetMapping("/paciente/reservar")
    public String mostrarWizardReserva(Model model, Authentication authentication) {
        model.addAttribute("especialidades", especialidadRepository.findAll());
        model.addAttribute("medicos", medicoRepository.findAll());
        
        String tipoSeguro = "PARTICULAR";
        double descuento = 0.0;
        
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(authentication.getName());
            if (usuarioOpt.isPresent()) {
                Optional<Paciente> pacienteOpt = pacienteRepository.findByIdUsuario(usuarioOpt.get().getIdUsuario());
                if (pacienteOpt.isPresent() && pacienteOpt.get().getTipoSeguro() != null && !pacienteOpt.get().getTipoSeguro().isEmpty()) {
                    tipoSeguro = pacienteOpt.get().getTipoSeguro().toUpperCase();
                }
            }
        }
        
        descuento = descuentoService.obtenerPorcentajeDescuento(tipoSeguro);
        
        model.addAttribute("tipoSeguro", tipoSeguro);
        model.addAttribute("descuentoSeguro", descuento);
        
        return "reserva-wizard";
    }

    public static class MedicoIndexDTO {
        private com.clinica.limatambo.model.Medico medico;
        private String especialidadNombre;

        public MedicoIndexDTO(com.clinica.limatambo.model.Medico medico, String especialidadNombre) {
            this.medico = medico;
            this.especialidadNombre = especialidadNombre;
        }

        public com.clinica.limatambo.model.Medico getMedico() { return medico; }
        public String getEspecialidadNombre() { return especialidadNombre; }
    }
}
