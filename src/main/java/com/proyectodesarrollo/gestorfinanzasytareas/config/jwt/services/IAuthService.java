package com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services;

import java.util.HashMap;

import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.models.dtos.LoginDTO;
import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.models.dtos.ResponseDTO;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;

public interface IAuthService {
    public HashMap<String, String> login(LoginDTO loginRequest) throws Exception;
    public ResponseDTO register(User user) throws Exception;
}
