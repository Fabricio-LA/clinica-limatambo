package com.clinica.limatambo.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Insumos")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_insumo")
    private Integer idInsumo;

    @Column(name = "nombre_insumo", nullable = false, length = 100)
    private String nombreInsumo;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "stock_actual")
    private Integer stockActual;

    @Column(name = "stock_minimo")
    private Integer stockMinimo;

    @Column(name = "unidad_medida", length = 20)
    private String unidadMedida;

    @OneToMany(mappedBy = "insumo", cascade = CascadeType.ALL)
    private List<MovimientoInventario> movimientos;

    // Getters y Setters
    public Integer getIdInsumo() { return idInsumo; }
    public void setIdInsumo(Integer idInsumo) { this.idInsumo = idInsumo; }

    public String getNombreInsumo() { return nombreInsumo; }
    public void setNombreInsumo(String nombreInsumo) { this.nombreInsumo = nombreInsumo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Integer getStockActual() { return stockActual; }
    public void setStockActual(Integer stockActual) { this.stockActual = stockActual; }

    public Integer getStockMinimo() { return stockMinimo; }
    public void setStockMinimo(Integer stockMinimo) { this.stockMinimo = stockMinimo; }

    public String getUnidadMedida() { return unidadMedida; }
    public void setUnidadMedida(String unidadMedida) { this.unidadMedida = unidadMedida; }

    public List<MovimientoInventario> getMovimientos() { return movimientos; }
    public void setMovimientos(List<MovimientoInventario> movimientos) { this.movimientos = movimientos; }
}
