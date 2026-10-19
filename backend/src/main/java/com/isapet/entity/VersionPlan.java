package com.isapet.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "version_plan")
public class VersionPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "plan_id", nullable = false)
    private Plan plan;

    @Column(name = "precio_mensual", nullable = false, precision = 14, scale = 0)
    private BigDecimal precioMensual;

    @Column(name = "maximo_mascotas", nullable = false)
    private Integer maximoMascotas;

    @Column(name = "limite_atenciones")
    private Integer limiteAtenciones;

    @Column(name = "vigente_desde", nullable = false)
    private LocalDate vigenteDesde;

    protected VersionPlan() {
    }

    public VersionPlan(
            Plan plan,
            BigDecimal precioMensual,
            Integer maximoMascotas,
            Integer limiteAtenciones,
            LocalDate vigenteDesde) {
        this.plan = plan;
        this.precioMensual = precioMensual;
        this.maximoMascotas = maximoMascotas;
        this.limiteAtenciones = limiteAtenciones;
        this.vigenteDesde = vigenteDesde;
    }

    public Long getId() {
        return id;
    }

    public Plan getPlan() {
        return plan;
    }

    public BigDecimal getPrecioMensual() {
        return precioMensual;
    }

    public Integer getMaximoMascotas() {
        return maximoMascotas;
    }

    public Integer getLimiteAtenciones() {
        return limiteAtenciones;
    }

    public LocalDate getVigenteDesde() {
        return vigenteDesde;
    }
}
