package com.proyectodesarrollo.gestorfinanzasytareas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Ahorro;

public interface AhorroRepository extends JpaRepository<Ahorro, Long> {
    List<Ahorro> findByCuentaId(Long cuentaId);
}
