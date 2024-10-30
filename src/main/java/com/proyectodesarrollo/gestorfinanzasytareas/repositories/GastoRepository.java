package com.proyectodesarrollo.gestorfinanzasytareas.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Gasto;

public interface GastoRepository extends JpaRepository<Gasto, Long> {
    List<Gasto> findByCuenta_Id(Long cuentaId);
}
