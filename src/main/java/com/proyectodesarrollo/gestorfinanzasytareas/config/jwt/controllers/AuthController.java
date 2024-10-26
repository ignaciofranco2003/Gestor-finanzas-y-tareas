package com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.controllers;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.IAuthService;
import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.models.dtos.LoginDTO;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IAuthService authService;

    @PostMapping("/authenticate")
    private ResponseEntity<HashMap<String, String>> login(@RequestBody LoginDTO loginRequest) throws Exception {
        HashMap<String, String> login = authService.login(loginRequest);
        if (login.containsKey("jwt")) {
            return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>(authService.login(loginRequest), HttpStatus.UNAUTHORIZED);
        }
    }
}
