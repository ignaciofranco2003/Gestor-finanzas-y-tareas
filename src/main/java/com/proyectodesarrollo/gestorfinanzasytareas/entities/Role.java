package com.proyectodesarrollo.gestorfinanzasytareas.entities;

public enum Role {
    USER,
    ADMIN;

    public String getName() {
        return this.name(); // Retorna el nombre del rol en mayúsculas (USER o ADMIN)
    }
}