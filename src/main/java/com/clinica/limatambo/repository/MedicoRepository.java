package com.clinica.limatambo.repository;

import com.clinica.limatambo.model.Medico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface MedicoRepository extends JpaRepository<Medico, Integer> {
    List<Medico> findByEspecialidad_IdEspecialidad(Integer idEspecialidad);

    List<Medico> findByApellidoContainingIgnoreCase(String apellido);
}