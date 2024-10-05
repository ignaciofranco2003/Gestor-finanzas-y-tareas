package com.proyectodesarrollo.gestorfinanzasytareas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.CategoriaIngreso;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.CategoriaIngresoRepository;

@Service
public class CategoriaIngresoService {

    @Autowired
    private CategoriaIngresoRepository categoriaIngresoRepository;

    public CategoriaIngreso createCategoria(CategoriaIngreso categoria) {
        return categoriaIngresoRepository.save(categoria);
    }

    public CategoriaIngreso getCategoriaById(Long id) {
        return categoriaIngresoRepository.findById(id).orElse(null);
    }

    public void deleteCategoria(Long id) {
        categoriaIngresoRepository.deleteById(id);
    }
}
