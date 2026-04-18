package com.clinica.limatambo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Especialidades")

public class Especialidad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_especialidad")
    private Integer id;

    @Column(name = "nombre_especialidad", nullable = false, length = 100)
    private String nombre;
}