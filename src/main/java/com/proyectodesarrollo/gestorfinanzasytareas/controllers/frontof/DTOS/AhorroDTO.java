package com.proyectodesarrollo.gestorfinanzasytareas.controllers.frontof.DTOS;

import java.math.BigDecimal;

public class AhorroDTO {

    private Long id;
    private String nombreAhorro;
    private BigDecimal montoActual;
    private BigDecimal montoFinal;
    private Long cuentaId;

    public AhorroDTO() {
    }

    public AhorroDTO(Long id, String nombreAhorro, BigDecimal montoTotal, BigDecimal montoFinal, Long cuentaId) {
        this.id = id;
        this.nombreAhorro = nombreAhorro;
        this.montoActual = montoTotal;
        this.montoFinal = montoFinal;
        this.cuentaId = cuentaId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombreAhorro() {
        return nombreAhorro;
    }

    public void setNombreAhorro(String nombreAhorro) {
        this.nombreAhorro = nombreAhorro;
    }

    public BigDecimal getmontoActual() {
        return montoActual;
    }

    public void setmontoActual(BigDecimal montoActual) {
        this.montoActual = montoActual;
    }

    public BigDecimal getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(BigDecimal montoFinal) {
        this.montoFinal = montoFinal;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public void setCuentaId(Long cuentaId) {
        this.cuentaId = cuentaId;
    }
}
