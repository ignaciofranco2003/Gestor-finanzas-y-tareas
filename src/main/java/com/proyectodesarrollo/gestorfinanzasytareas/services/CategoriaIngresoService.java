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
        boolean exists = categoriaIngresoRepository.findAll().stream()
            .anyMatch(c -> c.getNombre().equalsIgnoreCase(categoriaIngreso.getNombre()));
        if (exists) {
            throw new IllegalArgumentException("La categoría ya existe con ese nombre");
        }
    
        return categoriaIngresoRepository.save(categoriaIngreso);
    }

    public CategoriaIngreso updateCategoria(Long id, CategoriaIngreso categoriaDetails) {
        return categoriaIngresoRepository.findById(id)
            .map(categoria -> {
                // Verificar si existe otra categoría con el mismo nombre y diferente ID
                boolean exists = categoriaIngresoRepository.findAll().stream()
                    .anyMatch(c -> c.getNombre().equalsIgnoreCase(categoriaDetails.getNombre()) && !c.getId().equals(id));
    
                if (exists) {
                    throw new IllegalArgumentException("Ya existe otra categoría con ese nombre.");
                }
    
                // Si no existe duplicado, actualizar el nombre de la categoría
                categoria.setNombre(categoriaDetails.getNombre());
                return categoriaIngresoRepository.save(categoria);
            }).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    public void deleteCategoria(Long id) {
        if (categoriaIngresoRepository.existsById(id)) {
            categoriaIngresoRepository.deleteById(id);
        } else {
            throw new RuntimeException("La categoría no existe.");
        }
    }
}
