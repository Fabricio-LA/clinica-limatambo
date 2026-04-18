package com.clinica.limatambo.repository;

import com.clinica.limatambo.model.Historial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface HistorialRepository extends JpaRepository<Historial, Integer> {
    Optional<Historial> findByPaciente_IdPaciente(Integer idPaciente);
}