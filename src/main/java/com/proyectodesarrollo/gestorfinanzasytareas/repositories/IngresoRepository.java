package com.proyectodesarrollo.gestorfinanzasytareas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Ingreso;

public interface IngresoRepository extends JpaRepository<Ingreso, Long> {
}
