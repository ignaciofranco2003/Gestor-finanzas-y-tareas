package com.proyectodesarrollo.gestorfinanzasytareas.controllers.frontof.DTOS;

import java.math.BigDecimal;
import java.time.LocalDate;

public class GastoDTO {
    private Long id;
    private BigDecimal monto;
    private LocalDate fecha;
    private Long cuentaId; // ID de la cuenta
    private Long categoriaId; // ID de la categoría

    // Constructor
    public GastoDTO(Long id, BigDecimal monto, LocalDate fecha, Long cuentaId, Long categoriaId) {
        this.id = id;
        this.monto = monto;
        this.fecha = fecha;
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

    public LocalDate getFecha() {
        return fecha;
    }

    public Long getCuentaId() {
        return cuentaId;
    }

    public Long getCategoriaId() {
        return categoriaId;
    }
}
