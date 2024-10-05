package com.proyectodesarrollo.gestorfinanzasytareas.entities;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User usuario;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    private List<Ingreso> ingresos;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    private List<Gasto> gastos;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    private List<Ahorro> ahorros;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    private List<Tarea> tareas;

    public Cuenta() {
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }

    public List<Ingreso> getIngresos() {
        return ingresos;
    }

    public void setIngresos(List<Ingreso> ingresos) {
        this.ingresos = ingresos;
    }

    public List<Gasto> getGastos() {
        return gastos;
    }

    public void setGastos(List<Gasto> gastos) {
        this.gastos = gastos;
    }

    public List<Ahorro> getAhorros() {
        return ahorros;
    }

    public void setAhorros(List<Ahorro> ahorros) {
        this.ahorros = ahorros;
    }

    public List<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(List<Tarea> tareas) {
        this.tareas = tareas;
    }
}
