package com.proyectodesarrollo.gestorfinanzasytareas.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.nimbusds.jwt.JWTClaimsSet;
import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.IJWTUtilityService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class HomeController {

    @Autowired
    private IJWTUtilityService jwtUtilityService;


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

    // @GetMapping("/") // Ruta para el inicio de sesión
    // public String home() {
    //     // Obtener el contexto de seguridad
    //     Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    //     // Verificar si hay autenticación
    //     if (authentication != null && authentication.isAuthenticated()) {
    //         // Obtener el rol del usuario
    //         String userRole = authentication.getAuthorities().stream()
    //                 .findFirst() // Obtener el primer rol del usuario
    //                 .map(GrantedAuthority::getAuthority) // Obtener la autoridad como String
    //                 .orElse(null); // Si no hay roles, devolver null

    //         // Verificar si el usuario tiene un rol
    //         if (userRole == null) {
    //             return "error.html"; // Redirigir a la página de inicio si no hay rol
    //         }
    //         // Verificación de roles
    //         if (userRole.equals("ROLE_ADMIN")) {
    //             return "admin_dashboard.html"; // Vista para administradores
    //         } else if (userRole.equals("ROLE_USER")) {
    //             return "user_dashboard.html"; // Vista para usuarios normales
    //         }
    //     }

    //     // Si no está autenticado, redirigir a la página de "No Autenticado"
    //     return "login.html"; // Página que informa al usuario que no está autenticado
    // }


    @GetMapping("/")
    public String home(HttpServletRequest request) {
        // Obtener el token del encabezado
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            System.out.println("Primer if");
            return "login.html"; // Redirigir a la página de login si no hay token
        }

        String token = header.substring(7); // Extraer el token sin "Bearer "

        try {
            // Verificar y obtener los reclamos del JWT
            JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
            String userRole = claims.getStringClaim("role"); // Obtener el rol del JWT
            System.out.println("Primer try");
            // Verificación de roles
            if ("ADMIN".equals(userRole)) {
                System.out.println("admin");
                return "admin_dashboard.html"; // Vista para administradores
            } else if ("USER".equals(userRole)) {
                System.out.println("user");
                return "user_dashboard.html"; // Vista para usuarios normales
            }
        } catch (Exception e) {
            // Manejo de excepciones, como token inválido o expirado
            System.out.println("Primer fail");
            return "login.html"; // Redirigir a la página de login en caso de error
        }

        // Si no hay rol, redirigir al login
        System.out.println("exit");
        return "login.html"; // Página que informa al usuario que no está autenticado
    }

    // @GetMapping("/")
    // public String home(HttpServletRequest request) {
    //     // Obtener el token del encabezado
    //     String header = request.getHeader("Authorization");
    //     if (header == null || !header.startsWith("Bearer ")) {
    //         System.out.println("primer if");
    //         return "redirect:/login"; // Redirigir a la página de login si no hay token
    //     }
    
    //     String token = header.substring(7);
    
    //     try {
    //         // Verificar y obtener los reclamos del JWT
    //         JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
    //         String userRole = claims.getStringClaim("role"); // Obtener el rol del JWT
    //         System.out.println("primer try");
    //         // Verificación de roles
    //         if ("ADMIN".equals(userRole)) {
    //             return "redirect:/admin_dashboard"; // Redirigir a la vista para administradores
    //         } else if ("USER".equals(userRole)) {
    //             return "redirect:/user_dashboard"; // Redirigir a la vista para usuarios normales
    //         }
    //     } catch (Exception e) {
    //         // Manejo de excepciones, como token inválido o expirado
    //         System.out.println("fail");
    //         return "redirect:/login"; // Redirigir a la página de login en caso de error
    //     }
    
    //     // Si no hay rol, redirigir al login
    //     System.out.println("fail");
    //     return "redirect:/login"; // Página que informa al usuario que no está autenticado
    // }
}


