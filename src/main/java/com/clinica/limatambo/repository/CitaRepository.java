package com.clinica.limatambo.repository;

import com.clinica.limatambo.model.Cita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {
    List<Cita> findByPaciente_IdPaciente(Integer idPaciente);

    List<Cita> findByMedico_IdMedico(Integer idMedico);

    List<Cita> findByFechaCita(LocalDate fechaCita);

    List<Cita> findByEstado(String estado);
}