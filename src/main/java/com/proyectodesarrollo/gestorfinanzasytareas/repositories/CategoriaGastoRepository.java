package com.proyectodesarrollo.gestorfinanzasytareas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.CategoriaGasto;

public interface CategoriaGastoRepository extends JpaRepository<CategoriaGasto, Long> {
}
