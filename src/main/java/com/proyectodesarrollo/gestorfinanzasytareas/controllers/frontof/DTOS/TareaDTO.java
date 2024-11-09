package com.proyectodesarrollo.gestorfinanzasytareas.controllers.frontof.DTOS;

public class TareaDTO {
    private Long id;
    private String descripcion;
    private boolean completada;
    private Long cuentaId; // ID de la cuenta

    // Constructor
    public TareaDTO(Long id, String descripcion, boolean completada, Long cuentaId) {
        this.id = id;
        this.descripcion = descripcion;
        this.completada = completada;
        this.cuentaId = cuentaId;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public boolean isCompletada() {
        return completada;
    }

    public Long getCuentaId() {
        return cuentaId;
    }
}

