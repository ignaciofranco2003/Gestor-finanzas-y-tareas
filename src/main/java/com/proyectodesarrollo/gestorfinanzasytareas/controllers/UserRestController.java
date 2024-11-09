package com.proyectodesarrollo.gestorfinanzasytareas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Cuenta;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;
import com.proyectodesarrollo.gestorfinanzasytareas.services.CuentaService;
import com.proyectodesarrollo.gestorfinanzasytareas.services.UserService;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    @Autowired
    private CuentaService cuentaService;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            if (!isPasswordValid(user.getPassword())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La contraseña debe tener entre 4 y 16 caracteres.");
            }
            
            if (!isUsernameValid(user.getUsername())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("El nombre de usuario debe tener entre 4 y 20 caracteres.");
            }
            
            if (!isEmailValid(user.getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Formato de correo electrónico inválido.");
            }
            
            userService.registerUser(user);
            Cuenta cuenta = new Cuenta();
            cuenta.setUsuario(user);
            cuenta.setNombre("Cuenta de " + user.getUsername());
            cuentaService.createCuenta(cuenta);
            return new ResponseEntity<>("Usuario y cuenta creados exitosamente", HttpStatus.CREATED);
            
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Correo electrónico en uso", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    // Método para validar la contraseña
    private boolean isPasswordValid(String password) {
        return password != null && password.length() >= 4 && password.length() <= 16;
    }
    
    // Método para validar el nombre de usuario
    private boolean isUsernameValid(String username) {
        return username != null && username.length() >= 4 && username.length() <= 20;
    }
    
    // Método para validar el formato del correo electrónico
    private boolean isEmailValid(String email) {
        return email != null && email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[a-zA-Z0-9]{2,}$");
    }
}
