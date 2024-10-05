package com.proyectodesarrollo.gestorfinanzasytareas.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}