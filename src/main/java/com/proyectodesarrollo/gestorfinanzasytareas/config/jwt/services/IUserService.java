package com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services;

import java.util.List;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;

public interface IUserService {

    public List<User> findAllUsers();
}
