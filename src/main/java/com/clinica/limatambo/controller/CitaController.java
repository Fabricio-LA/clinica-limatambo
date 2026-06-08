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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Controller
@RequestMapping("/citas")
public class CitaController {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private MedicoRepository medicoRepository;
    private boolean esHorarioValido(Integer idMedico, LocalDate fecha, LocalTime hora) {
        Optional<Medico> medicoOpt = medicoRepository.findById(idMedico);
        if (!medicoOpt.isPresent()) return false;
        
        Medico m = medicoOpt.get();
        if (m.getHoraInicio() == null || m.getHoraFin() == null || m.getDiasLaborables() == null) return false;
        int diaSemana = fecha.getDayOfWeek().getValue();
        String[] diasPermitidos = m.getDiasLaborables().split(",");
        boolean trabajaEseDia = false;
        for (String d : diasPermitidos) {
            if (d.trim().equals(String.valueOf(diaSemana))) {
                trabajaEseDia = true;
                break;
            }
        }
        if (!trabajaEseDia) return false;
        if (hora.isBefore(m.getHoraInicio()) || !hora.isBefore(m.getHoraFin())) return false;
        boolean ocupado = citaRepository.existsByIdMedicoAndFechaCitaAndHoraCitaAndEstadoNot(idMedico, fecha, hora, "Cancelada");
        
        return !ocupado;
    }

    @PostMapping("/reservar")
    public String reservarCita(Cita cita, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            if (!esHorarioValido(cita.getIdMedico(), cita.getFechaCita(), cita.getHoraCita())) {
                return "redirect:/paciente/dashboard?errorHorario=true";
            }

            String username = authentication.getName();
            Optional<Usuario> usuarioOpt = usuarioRepository.findByUsername(username);
            
            if (usuarioOpt.isPresent()) {
                Optional<Paciente> pacienteOpt = pacienteRepository.findByIdUsuario(usuarioOpt.get().getIdUsuario());
                if (pacienteOpt.isPresent()) {
                    cita.setIdPaciente(pacienteOpt.get().getIdPaciente());
                    cita.setEstado("Pendiente");
                    citaRepository.save(cita);
                    return "redirect:/paciente/dashboard?reservaExito=true";
                }
            }
        }
        return "redirect:/?error=true";
    }

    @PostMapping("/cancelar/{id}")
    public String cancelarCita(@org.springframework.web.bind.annotation.PathVariable("id") Integer idCita, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Cita> citaOpt = citaRepository.findById(idCita);
            if (citaOpt.isPresent()) {
                Cita cita = citaOpt.get();
                cita.setEstado("Cancelada");
                citaRepository.save(cita);
                return "redirect:/paciente/dashboard?cancelada=true";
            }
        }
        return "redirect:/paciente/dashboard?error=true";
    }

    @PostMapping("/modificar/{id}")
    public String modificarCita(@org.springframework.web.bind.annotation.PathVariable("id") Integer idCita, 
                                @org.springframework.web.bind.annotation.RequestParam("nuevaFecha") java.time.LocalDate nuevaFecha, 
                                @org.springframework.web.bind.annotation.RequestParam("nuevaHora") java.time.LocalTime nuevaHora,
                                Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Cita> citaOpt = citaRepository.findById(idCita);
            if (citaOpt.isPresent()) {
                Cita cita = citaOpt.get();
                if (!cita.getFechaCita().equals(nuevaFecha) || !cita.getHoraCita().equals(nuevaHora)) {
                    if (!esHorarioValido(cita.getIdMedico(), nuevaFecha, nuevaHora)) {
                        return "redirect:/paciente/dashboard?errorHorario=true";
                    }
                }

                cita.setFechaCita(nuevaFecha);
                cita.setHoraCita(nuevaHora);
                citaRepository.save(cita);
                return "redirect:/paciente/dashboard?modificada=true";
            }
        }
        return "redirect:/paciente/dashboard?error=true";
    }

    @PostMapping("/estado/{id}")
    public String cambiarEstadoCita(@org.springframework.web.bind.annotation.PathVariable("id") Integer idCita, 
                                    @org.springframework.web.bind.annotation.RequestParam("nuevoEstado") String nuevoEstado,
                                    Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Optional<Cita> citaOpt = citaRepository.findById(idCita);
            if (citaOpt.isPresent()) {
                Cita cita = citaOpt.get();
                cita.setEstado(nuevoEstado);
                citaRepository.save(cita);
                
                if ("Atendida".equals(nuevoEstado)) {
                    return "redirect:/medico/dashboard?atendida=true";
                } else if ("Ausente".equals(nuevoEstado)) {
                    return "redirect:/medico/dashboard?ausente=true";
                }
                return "redirect:/medico/dashboard";
            }
        }
        return "redirect:/?error=true";
    }
}

