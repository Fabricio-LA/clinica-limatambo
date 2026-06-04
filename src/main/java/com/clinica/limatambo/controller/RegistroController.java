package com.clinica.limatambo.controller;

import com.clinica.limatambo.dto.RegistroPacienteDTO;
import com.clinica.limatambo.model.Paciente;
import com.clinica.limatambo.model.Usuario;
import com.clinica.limatambo.repository.PacienteRepository;
import com.clinica.limatambo.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegistroController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarPaciente(RegistroPacienteDTO dto, Model model) {
        try {
            String username = dto.getDni();
            if (usuarioRepository.findByUsername(username).isPresent()) {
                model.addAttribute("error", "Ya existe un usuario registrado con este DNI.");
                return "registro";
            }

            if (pacienteRepository.existsByDni(dto.getDni())) {
                model.addAttribute("error", "Ya existe un paciente registrado con este DNI en nuestro sistema.");
                return "registro";
            }
            
            if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
                model.addAttribute("error", "El correo electrónico ya está registrado en el sistema.");
                return "registro";
            }

            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setUsername(username);
            nuevoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));
            nuevoUsuario.setEmail(dto.getEmail());
            nuevoUsuario.setIdRol(3);
            nuevoUsuario.setEstado(true);
            
            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
            Paciente nuevoPaciente = new Paciente();
            nuevoPaciente.setNombre(dto.getNombre());
            nuevoPaciente.setApellido(dto.getApellido());
            nuevoPaciente.setDni(dto.getDni());
            nuevoPaciente.setTelefono(dto.getTelefono());
            nuevoPaciente.setFechaNacimiento(dto.getFechaNacimiento());
            nuevoPaciente.setIdUsuario(usuarioGuardado.getIdUsuario());
            nuevoPaciente.setTipoSeguro(dto.getTipoSeguro());

            pacienteRepository.save(nuevoPaciente);

            return "redirect:/registro?exito=true";
        } catch (Exception e) {
            model.addAttribute("error", "Error al registrar: " + e.getMessage());
            return "registro";
        }
    }
}
