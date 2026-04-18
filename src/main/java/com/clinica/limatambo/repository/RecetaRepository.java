package com.clinica.limatambo.repository;

import com.clinica.limatambo.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Integer> {
    List<Receta> findByConsulta_IdConsulta(Integer idConsulta);
}