package com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.IUserService;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.UserRepository;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public List<User> findAllUsers(){
        return userRepository.findAll();
    }
}
