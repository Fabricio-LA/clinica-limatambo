package com.clinica.limatambo.controller;

import com.clinica.limatambo.controller.DashboardController.CitaDTO;
import com.clinica.limatambo.model.Cita;
import com.clinica.limatambo.model.Insumo;
import com.clinica.limatambo.model.Usuario;
import com.clinica.limatambo.repository.CitaRepository;
import com.clinica.limatambo.repository.InsumoRepository;
import com.clinica.limatambo.repository.PacienteRepository;
import com.clinica.limatambo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private CitaRepository citaRepository;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long totalCitas = citaRepository.count();
        long totalPacientes = pacienteRepository.count();
        long totalInsumos = insumoRepository.count();

        model.addAttribute("totalCitas", totalCitas);
        model.addAttribute("totalPacientes", totalPacientes);
        model.addAttribute("totalInsumos", totalInsumos);
        
        return "admin-dashboard";
    }

    @GetMapping("/citas")
    public String adminCitas(Model model) {
        List<Cita> todasLasCitas = citaRepository.findAll();
        List<CitaDTO> citasDTO = new ArrayList<>();
        
        for (Cita cita : todasLasCitas) {
            String nombrePaciente = "Desconocido";
            com.clinica.limatambo.model.Paciente pacienteObj = null;
            Integer edad = null;
            if (cita.getIdPaciente() != null) {
                java.util.Optional<com.clinica.limatambo.model.Paciente> p = pacienteRepository.findById(cita.getIdPaciente());
                if (p.isPresent()) {
                    pacienteObj = p.get();
                    nombrePaciente = p.get().getNombre() + " " + p.get().getApellido();
                    if (p.get().getFechaNacimiento() != null) {
                        edad = java.time.Period.between(p.get().getFechaNacimiento(), java.time.LocalDate.now()).getYears();
                    }
                }
            }
            citasDTO.add(new CitaDTO(cita, nombrePaciente, pacienteObj, edad));
        }

        model.addAttribute("citas", citasDTO);
        return "admin-citas";
    }

    @GetMapping("/inventario")
    public String adminInventario(Model model) {
        List<Insumo> insumos = insumoRepository.findAll();
        model.addAttribute("insumos", insumos);
        return "admin-inventario";
    }

    @GetMapping("/usuarios")
    public String adminUsuarios(Model model) {
        List<Usuario> usuarios = usuarioRepository.findAll();
        model.addAttribute("usuarios", usuarios);
        return "admin-usuarios";
    }

    @GetMapping("/ventas")
    public String adminVentas(Model model) {
        return "admin-ventas";
    }

    @org.springframework.beans.factory.annotation.Autowired
    private com.clinica.limatambo.repository.MedicoRepository medicoRepository;

    @org.springframework.beans.factory.annotation.Autowired
    private org.springframework.security.crypto.password.PasswordEncoder passwordEncoder;

    @org.springframework.web.bind.annotation.PostMapping("/usuarios/crear")
    public String crearUsuario(
            @org.springframework.web.bind.annotation.RequestParam String username,
            @org.springframework.web.bind.annotation.RequestParam String password,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String email,
            @org.springframework.web.bind.annotation.RequestParam Integer idRol,
            @org.springframework.web.bind.annotation.RequestParam String nombre,
            @org.springframework.web.bind.annotation.RequestParam String apellido,
            @org.springframework.web.bind.annotation.RequestParam String dni,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            if (usuarioRepository.findByUsername(username).isPresent()) {
                redirectAttributes.addFlashAttribute("error", "El nombre de usuario ya existe.");
                return "redirect:/admin/usuarios";
            }
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(username);
            nuevoUsuario.setPassword(passwordEncoder.encode(password));
            nuevoUsuario.setEmail(email);
            nuevoUsuario.setIdRol(idRol);
            nuevoUsuario.setEstado(true);

            Usuario guardado = usuarioRepository.save(nuevoUsuario);

            if (idRol == 2) {
                com.clinica.limatambo.model.Medico medico = new com.clinica.limatambo.model.Medico();
                medico.setIdUsuario(guardado.getIdUsuario());
                medico.setNombre(nombre);
                medico.setApellido(apellido);
                medico.setIdEspecialidad(1);
                medicoRepository.save(medico);
            } else if (idRol == 3) {
                com.clinica.limatambo.model.Paciente paciente = new com.clinica.limatambo.model.Paciente();
                paciente.setIdUsuario(guardado.getIdUsuario());
                paciente.setNombre(nombre);
                paciente.setApellido(apellido);
                paciente.setDni(dni);
                pacienteRepository.save(paciente);
            }
            redirectAttributes.addFlashAttribute("success", "Usuario creado con éxito.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al crear usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @org.springframework.web.bind.annotation.PostMapping("/usuarios/editar")
    public String editarUsuario(
            @org.springframework.web.bind.annotation.RequestParam Integer idUsuario,
            @org.springframework.web.bind.annotation.RequestParam(required = false) String email,
            @org.springframework.web.bind.annotation.RequestParam Integer idRol,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            java.util.Optional<Usuario> opt = usuarioRepository.findById(idUsuario);
            if (opt.isPresent()) {
                Usuario u = opt.get();
                u.setEmail(email);
                u.setIdRol(idRol);
                usuarioRepository.save(u);
                redirectAttributes.addFlashAttribute("success", "Usuario actualizado correctamente.");
            } else {
                redirectAttributes.addFlashAttribute("error", "Usuario no encontrado.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al editar usuario: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @org.springframework.web.bind.annotation.PostMapping("/usuarios/toggle")
    public String toggleEstadoUsuario(
            @org.springframework.web.bind.annotation.RequestParam Integer idUsuario,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            java.util.Optional<Usuario> opt = usuarioRepository.findById(idUsuario);
            if (opt.isPresent()) {
                Usuario u = opt.get();
                u.setEstado(!u.getEstado());
                usuarioRepository.save(u);
                String accion = u.getEstado() ? "activado" : "suspendido";
                redirectAttributes.addFlashAttribute("success", "El usuario ha sido " + accion + ".");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cambiar estado: " + e.getMessage());
        }
        return "redirect:/admin/usuarios";
    }

    @org.springframework.web.bind.annotation.PostMapping("/citas/cancelar")
    public String cancelarCita(
            @org.springframework.web.bind.annotation.RequestParam Integer idCita,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            java.util.Optional<Cita> opt = citaRepository.findById(idCita);
            if (opt.isPresent()) {
                Cita cita = opt.get();
                cita.setEstado("Cancelada");
                citaRepository.save(cita);
                redirectAttributes.addFlashAttribute("success", "Cita #" + idCita + " ha sido cancelada.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al cancelar cita: " + e.getMessage());
        }
        return "redirect:/admin/citas";
    }

    @org.springframework.web.bind.annotation.PostMapping("/citas/editar")
    public String editarCita(
            @org.springframework.web.bind.annotation.RequestParam Integer idCita,
            @org.springframework.web.bind.annotation.RequestParam String fechaCita,
            @org.springframework.web.bind.annotation.RequestParam String horaCita,
            org.springframework.web.servlet.mvc.support.RedirectAttributes redirectAttributes) {
        try {
            java.util.Optional<Cita> opt = citaRepository.findById(idCita);
            if (opt.isPresent()) {
                Cita cita = opt.get();
                cita.setFechaCita(java.time.LocalDate.parse(fechaCita));
                cita.setHoraCita(java.time.LocalTime.parse(horaCita));
                citaRepository.save(cita);
                redirectAttributes.addFlashAttribute("success", "Cita #" + idCita + " reprogramada con éxito.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al editar cita: " + e.getMessage());
        }
        return "redirect:/admin/citas";
    }
}
