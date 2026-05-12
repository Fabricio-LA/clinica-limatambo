package com.clinica.limatambo.controller;

import com.clinica.limatambo.model.Cita;
import com.clinica.limatambo.model.Medico;
import com.clinica.limatambo.model.Paciente;
import com.clinica.limatambo.model.Usuario;
import com.clinica.limatambo.repository.CitaRepository;
import com.clinica.limatambo.repository.MedicoRepository;
import com.clinica.limatambo.repository.PacienteRepository;
import com.clinica.limatambo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class DashboardController {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private MedicoRepository medicoRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;


    @GetMapping("/medico/dashboard")
    public String medicoPanel(Authentication authentication, Model model) {
        String username = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        
        if (usuarioOpt.isPresent()) {
            Optional<Medico> medicoOpt = medicoRepository.findByIdUsuario(usuarioOpt.get().getIdUsuario());
            if (medicoOpt.isPresent()) {
                Medico medico = medicoOpt.get();
                List<Cita> citas = citaRepository.findByIdMedicoOrderByFechaCitaAscHoraCitaAsc(medico.getIdMedico());
                
                LocalDate hoy = LocalDate.now();
                List<CitaDTO> citasHoy = new ArrayList<>();
                List<CitaDTO> alertasHoy = new ArrayList<>();
                List<CitaDTO> citasProximas = new ArrayList<>();
                
                for (Cita cita : citas) {
                    Paciente pacienteObj = null;
                    String nombrePaciente = "Desconocido";
                    Integer edad = null;
                    
                    if (cita.getIdPaciente() != null) {
                        Optional<Paciente> p = pacienteRepository.findById(cita.getIdPaciente());
                        if (p.isPresent()) {
                            pacienteObj = p.get();
                            nombrePaciente = p.get().getNombre() + " " + p.get().getApellido();
                            if (p.get().getFechaNacimiento() != null) {
                                edad = Period.between(p.get().getFechaNacimiento(), hoy).getYears();
                            }
                        }
                    }
                    
                    CitaDTO dto = new CitaDTO(cita, nombrePaciente, pacienteObj, edad);
                    
                    if (cita.getFechaCita().equals(hoy)) {
                        if ("Cancelada".equals(cita.getEstado())) {
                            alertasHoy.add(dto);
                        } else {
                            citasHoy.add(dto);
                        }
                    } else if (cita.getFechaCita().isAfter(hoy)) {
                        if (!"Cancelada".equals(cita.getEstado())) {
                            citasProximas.add(dto);
                        }
                    }
                }
                
                model.addAttribute("medico", medico);
                model.addAttribute("citasHoy", citasHoy);
                model.addAttribute("citasProximas", citasProximas);
                model.addAttribute("alertasHoy", alertasHoy);
                model.addAttribute("totalCitasHoy", citasHoy.size());
            }
        }
        return "medico-dashboard";
    }

    @GetMapping("/paciente/dashboard")
    public String pacientePanel(Authentication authentication, Model model) {
        String username = authentication.getName();
        Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
        
        if (usuarioOpt.isPresent()) {
            Optional<Paciente> pacienteOpt = pacienteRepository.findByIdUsuario(usuarioOpt.get().getIdUsuario());
            if (pacienteOpt.isPresent()) {
                Paciente paciente = pacienteOpt.get();
                List<Cita> citas = citaRepository.findByIdPacienteOrderByFechaCitaDesc(paciente.getIdPaciente());
                
                List<CitaPacienteDTO> citasDTO = new ArrayList<>();
                for (Cita cita : citas) {
                    String nombreMedico = "Desconocido";
                    Medico medicoObj = null;
                    if (cita.getIdMedico() != null) {
                        Optional<Medico> m = medicoRepository.findById(cita.getIdMedico());
                        if (m.isPresent()) {
                            medicoObj = m.get();
                            nombreMedico = "Dr. " + m.get().getNombre() + " " + m.get().getApellido();
                        }
                    }
                    citasDTO.add(new CitaPacienteDTO(cita, nombreMedico, medicoObj));
                }
                
                model.addAttribute("paciente", paciente);
                model.addAttribute("citas", citasDTO);
            }
        }
        return "paciente-dashboard";
    }

    public static class CitaDTO {
        private Cita cita;
        private String nombrePaciente;
        private Paciente paciente;
        private Integer edadPaciente;

        public CitaDTO(Cita cita, String nombrePaciente, Paciente paciente, Integer edadPaciente) {
            this.cita = cita;
            this.nombrePaciente = nombrePaciente;
            this.paciente = paciente;
            this.edadPaciente = edadPaciente;
        }

        public Cita getCita() { return cita; }
        public String getNombrePaciente() { return nombrePaciente; }
        public Paciente getPaciente() { return paciente; }
        public Integer getEdadPaciente() { return edadPaciente; }
    }

    public static class CitaPacienteDTO {
        private Cita cita;
        private String nombreMedico;
        private Medico medico;

        public CitaPacienteDTO(Cita cita, String nombreMedico, Medico medico) {
            this.cita = cita;
            this.nombreMedico = nombreMedico;
            this.medico = medico;
        }

        public Cita getCita() { return cita; }
        public String getNombreMedico() { return nombreMedico; }
        public Medico getMedico() { return medico; }
    }
}
