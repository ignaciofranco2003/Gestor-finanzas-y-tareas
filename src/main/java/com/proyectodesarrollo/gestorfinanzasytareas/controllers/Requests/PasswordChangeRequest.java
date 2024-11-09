package com.proyectodesarrollo.gestorfinanzasytareas.controllers.Requests;

public class PasswordChangeRequest {
    private String email;
    private String codigo;
    private String nuevaPassword;
    
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getCodigo() {
        return codigo;
    }
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
    public String getNuevaPassword() {
        return nuevaPassword;
    }
    public void setNuevaPassword(String nuevaPassword) {
        this.nuevaPassword = nuevaPassword;
    }

    
}