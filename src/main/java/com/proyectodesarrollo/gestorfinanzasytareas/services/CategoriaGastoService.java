package com.proyectodesarrollo.gestorfinanzasytareas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.CategoriaGasto;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.CategoriaGastoRepository;

@Service
public class CategoriaGastoService {

    @Autowired
    private CategoriaGastoRepository categoriaGastoRepository;

    public CategoriaGasto createCategoria(CategoriaGasto categoria) {
        return categoriaGastoRepository.save(categoria);
    }

    public CategoriaGasto getCategoriaById(Long id) {
        return categoriaGastoRepository.findById(id).orElse(null);
    }

    public void deleteCategoria(Long id) {
        categoriaGastoRepository.deleteById(id);
    }
}
