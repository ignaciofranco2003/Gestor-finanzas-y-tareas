package com.proyectodesarrollo.gestorfinanzasytareas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Tarea;

public interface TareaRepository extends JpaRepository<Tarea, Long> {
}
