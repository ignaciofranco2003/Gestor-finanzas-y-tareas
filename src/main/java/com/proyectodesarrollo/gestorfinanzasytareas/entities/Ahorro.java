package com.proyectodesarrollo.gestorfinanzasytareas.entities;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Ahorro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreAhorro;
    private BigDecimal montoActual;
    private BigDecimal montoFinal;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;

    public Ahorro() {
    }

    public Long getId() {
        return id;
    }

    public String getNombreAhorro() {
        return nombreAhorro;
    }

    public void setNombreAhorro(String nombreAhorro) {
        this.nombreAhorro = nombreAhorro;
    }

    public BigDecimal getMontoActual() {
        return montoActual;
    }

    public void setMontoActual(BigDecimal montoActual) {
        this.montoActual = montoActual;
    }

    public BigDecimal getMontoFinal() {
        return montoFinal;
    }

    public void setMontoFinal(BigDecimal montoFinal) {
        this.montoFinal = montoFinal;
    }

    public Cuenta getCuenta() {
        return cuenta;
    }

    public void setCuenta(Cuenta cuenta) {
        this.cuenta = cuenta;
    }
}
