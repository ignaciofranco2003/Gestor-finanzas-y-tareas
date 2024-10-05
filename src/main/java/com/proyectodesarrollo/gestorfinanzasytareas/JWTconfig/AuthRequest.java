package com.proyectodesarrollo.gestorfinanzasytareas.JWTconfig;

public class AuthRequest {
    private String email;
    private String password;

    // Constructor vacío (requerido para deserialización)
    public AuthRequest() {
    }

    // Getters y Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
