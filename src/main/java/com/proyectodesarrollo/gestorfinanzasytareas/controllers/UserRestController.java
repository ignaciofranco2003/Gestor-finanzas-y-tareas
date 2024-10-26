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

    // @PostMapping("/register")
    // public ResponseEntity<String> registerUser(@RequestBody User user) {
    //     try {
    //         userService.registerUser(user);
    //         return new ResponseEntity<>("Usuario creado exitosamente", HttpStatus.CREATED);
    //     } catch (IllegalArgumentException e) {
    //         // Manejo de excepción específica para el correo duplicado
    //         return new ResponseEntity<>("Correo electronico en uso", HttpStatus.CONFLICT);
    //     } catch (Exception e) {
    //         return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    //     }
    // }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            
            // Crear la cuenta asociada al nuevo usuario
            Cuenta cuenta = new Cuenta();
            cuenta.setUsuario(user);
            cuenta.setNombre("Cuenta de " + user.getUsername()); // Ajustar según los requerimientos
            cuentaService.createCuenta(cuenta);

            return new ResponseEntity<>("Usuario y cuenta creados exitosamente", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Correo electronico en uso", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/recoverpassword")
    public ResponseEntity<String> recoverpassword(@RequestBody String email) {
        try {

            return new ResponseEntity<>("Usuario creado exitosamente", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Correo electronico en uso", HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    // public static class LoginRequest {
    //     private String username;
    //     private String password;
    
    //     // Constructor vacío (necesario para la deserialización)
    //     public LoginRequest() {}
    
    //     public String getUsername() {
    //         return username;
    //     }
    
    //     public void setUsername(String username) {
    //         this.username = username;
    //     }
    
    //     public String getPassword() {
    //         return password;
    //     }
    
    //     public void setPassword(String password) {
    //         this.password = password;
    //     }
    // }
}
