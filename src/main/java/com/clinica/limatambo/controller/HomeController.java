package com.clinica.limatambo.controller;

import com.clinica.limatambo.repository.EspecialidadRepository;
import com.clinica.limatambo.repository.MedicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private EspecialidadRepository especialidadRepository;

    @Autowired
    private MedicoRepository medicoRepository;

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

    @GetMapping("/paciente/reservar")
    public String mostrarWizardReserva(Model model) {
        model.addAttribute("especialidades", especialidadRepository.findAll());
        model.addAttribute("medicos", medicoRepository.findAll());
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
