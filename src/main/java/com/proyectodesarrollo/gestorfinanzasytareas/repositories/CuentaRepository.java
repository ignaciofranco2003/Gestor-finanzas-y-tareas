package com.proyectodesarrollo.gestorfinanzasytareas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
}
