package com.proyectodesarrollo.gestorfinanzasytareas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.CategoriaIngreso;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.CategoriaIngresoRepository;

@Service
public class CategoriaIngresoService {

    @Autowired
    private CategoriaIngresoRepository categoriaIngresoRepository;

    public List<CategoriaIngreso> getAllCategorias() {
        return categoriaIngresoRepository.findAll();
    }

    public Optional<CategoriaIngreso> getCategoriaById(Long id) {
        return categoriaIngresoRepository.findById(id);
    }

    public CategoriaIngreso createCategoria(CategoriaIngreso categoriaIngreso) {
        return categoriaIngresoRepository.save(categoriaIngreso);
    }

    public CategoriaIngreso updateCategoria(Long id, CategoriaIngreso categoriaDetails) {
        return categoriaIngresoRepository.findById(id)
            .map(categoria -> {
                categoria.setNombre(categoriaDetails.getNombre());
                return categoriaIngresoRepository.save(categoria);
            }).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public void deleteCategoria(Long id) {
        categoriaIngresoRepository.deleteById(id);
    }
}
