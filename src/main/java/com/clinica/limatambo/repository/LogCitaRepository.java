package com.clinica.limatambo.repository;

import com.clinica.limatambo.model.LogCita;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface LogCitaRepository extends JpaRepository<LogCita, Integer> {
    List<LogCita> findByIdCita(Integer idCita);

    List<LogCita> findByAccion(String accion);
}