package com.clinica.limatambo.controller;

import com.clinica.limatambo.repository.EspecialidadRepository;
import com.clinica.limatambo.repository.MedicoRepository;
import com.clinica.limatambo.repository.UsuarioRepository;
import com.clinica.limatambo.repository.PacienteRepository;
import com.clinica.limatambo.model.Usuario;
import com.clinica.limatambo.model.Paciente;
import com.clinica.limatambo.service.DescuentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Optional;

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

    @GetMapping("/ayuda")
    public String mostrarAyuda() {
        return "ayuda";
    }

    @GetMapping("/farmacia")
    public String mostrarFarmacia(Model model, Authentication authentication) {
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
        
        return "farmacia";
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
