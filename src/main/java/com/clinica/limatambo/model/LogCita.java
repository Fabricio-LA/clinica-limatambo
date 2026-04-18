package com.clinica.limatambo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "Log_Citas")
public class LogCita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_log")
    private Integer idLog;

    // Sin FK declarada en el SQL, se mapea como valor simple
    @Column(name = "id_cita")
    private Integer idCita;

    // 'CREACION', 'CAMBIO ESTADO', 'CANCELACION'
    @Column(name = "accion", length = 50)
    private String accion;

    @Column(name = "fecha_accion")
    private LocalDateTime fechaAccion;

    @Column(name = "usuario_responsable", length = 50)
    private String usuarioResponsable;

    @PrePersist
    protected void onCreate() {
        if (this.fechaAccion == null) {
            this.fechaAccion = LocalDateTime.now();
        }
    }

    public LogCita() {
    }

    public Integer getIdLog() {
        return idLog;
    }

    public void setIdLog(Integer idLog) {
        this.idLog = idLog;
    }

    public Integer getIdCita() {
        return idCita;
    }

    public void setIdCita(Integer idCita) {
        this.idCita = idCita;
    }

    public String getAccion() {
        return accion;
    }

    public void setAccion(String accion) {
        this.accion = accion;
    }

    public LocalDateTime getFechaAccion() {
        return fechaAccion;
    }

    public void setFechaAccion(LocalDateTime fechaAccion) {
        this.fechaAccion = fechaAccion;
    }

    public String getUsuarioResponsable() {
        return usuarioResponsable;
    }

    public void setUsuarioResponsable(String usuarioResponsable) {
        this.usuarioResponsable = usuarioResponsable;
    }
}