package com.clinica.limatambo.repository;

import com.clinica.limatambo.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    java.util.List<Cita> findByIdMedicoOrderByFechaCitaAscHoraCitaAsc(Integer idMedico);
    java.util.List<Cita> findByIdPacienteOrderByFechaCitaDesc(Integer idPaciente);
    java.util.List<Cita> findByIdMedicoAndFechaCitaAndEstadoNot(Integer idMedico, java.time.LocalDate fechaCita, String estado);
    boolean existsByIdMedicoAndFechaCitaAndHoraCitaAndEstadoNot(Integer idMedico, java.time.LocalDate fechaCita, java.time.LocalTime horaCita, String estado);
}
