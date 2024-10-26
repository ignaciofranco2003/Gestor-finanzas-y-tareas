package com.proyectodesarrollo.gestorfinanzasytareas.controllers.frontof.DTOS;

import java.math.BigDecimal;

public class IngresoDTO {
    private Long id;
    private BigDecimal monto;
    private Long cuentaId; // ID de la cuenta
    private Long categoriaId; // ID de la categoría

    // Constructor
    public IngresoDTO(Long id, BigDecimal monto, Long cuentaId, Long categoriaId) {
        this.id = id;
        this.monto = monto;
        this.cuentaId = cuentaId;
        this.categoriaId = categoriaId;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }
}
