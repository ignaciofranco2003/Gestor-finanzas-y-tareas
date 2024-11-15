package com.proyectodesarrollo.gestorfinanzasytareas.controllers;

import java.util.HashMap;
import java.util.Map;

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
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (!isPasswordValid(user.getPassword())) {
                response.put("success", false);
                response.put("message", "La contraseña debe tener entre 4 y 16 caracteres.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (!isUsernameValid(user.getUsername())) {
                response.put("success", false);
                response.put("message", "El nombre de usuario debe tener entre 4 y 20 caracteres.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            if (!isEmailValid(user.getEmail())) {
                response.put("success", false);
                response.put("message", "Formato de correo electrónico inválido.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            userService.registerUser(user);
            Cuenta cuenta = new Cuenta();
            cuenta.setUsuario(user);
            cuenta.setNombre("Cuenta de " + user.getUsername());
            cuentaService.createCuenta(cuenta);

            response.put("success", true);
            response.put("message", "Usuario y cuenta creados exitosamente.");
            return ResponseEntity.status(HttpStatus.CREATED).body(response);

        } catch (IllegalArgumentException e) {
            response.put("success", false);
            response.put("message", "Correo electrónico en uso.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error interno del servidor.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
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
