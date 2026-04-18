package com.clinica.limatambo.repository;

import com.clinica.limatambo.model.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Integer> {
    List<Consulta> findByHistorial_IdHistorial(Integer idHistorial);

    Optional<Consulta> findByCita_IdCita(Integer idCita);
}