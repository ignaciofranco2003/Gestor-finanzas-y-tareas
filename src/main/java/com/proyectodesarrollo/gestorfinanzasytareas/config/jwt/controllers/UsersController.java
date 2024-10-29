package com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.IUserService;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;

@RestController
@RequestMapping("/user")
public class UsersController {

    @Autowired
    IUserService userService;

    @GetMapping("/all")
    private ResponseEntity<List<User>> getAllUsers(){
        return new ResponseEntity<>(userService.findAllUsers(), HttpStatus.OK);
    }
}
