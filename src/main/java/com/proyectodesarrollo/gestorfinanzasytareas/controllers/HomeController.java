package com.proyectodesarrollo.gestorfinanzasytareas.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }

    @GetMapping("/createuser")
    public String createuser() {
        return "crearusuario.html";
    }

    @GetMapping("/recoverpassword")
    public String recoverpassword() {
        return "recuperarcontraseña.html";
    }

    @GetMapping("/") // Ruta para el inicio de sesión
    public String home() {
        // Obtener el contexto de seguridad
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // Verificar si hay autenticación
        if (authentication != null && authentication.isAuthenticated()) {
            // Obtener el rol del usuario
            String userRole = authentication.getAuthorities().stream()
                    .findFirst() // Obtener el primer rol del usuario
                    .map(GrantedAuthority::getAuthority) // Obtener la autoridad como String
                    .orElse(null); // Si no hay roles, devolver null

            // Verificar si el usuario tiene un rol
            if (userRole == null) {
                return "error.html"; // Redirigir a la página de inicio si no hay rol
            }
            // Verificación de roles
            if (userRole.equals("ROLE_ADMIN")) {
                return "admin_dashboard.html"; // Vista para administradores
            } else if (userRole.equals("ROLE_USER")) {
                return "user_dashboard.html"; // Vista para usuarios normales
            }
        }

        // Si no está autenticado, redirigir a la página de "No Autenticado"
        return "no_authenticated.html"; // Página que informa al usuario que no está autenticado
    }

}
