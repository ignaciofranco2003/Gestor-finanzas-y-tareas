package com.proyectodesarrollo.gestorfinanzasytareas.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Cuenta;

public interface CuentaRepository extends JpaRepository<Cuenta, Long> {
    //     @Query("SELECT c FROM Cuenta c WHERE c.usuario.id = :userId")
    // Optional<Cuenta> findByUserId(@Param("userId") Long userId);
    // Optional<Cuenta> findByUsuario_Id(Long user_id);
    @Query("SELECT c FROM Cuenta c WHERE c.usuario = (SELECT u FROM User u WHERE u.id = :userId)")
    Optional<Cuenta> findByUserId(@Param("userId") Long userId);
}
