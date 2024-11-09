package com.proyectodesarrollo.gestorfinanzasytareas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.CategoriaGasto;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.CategoriaGastoRepository;

@Service
public class CategoriaGastoService {

    @Autowired
    private CategoriaGastoRepository categoriaGastoRepository;

    // Obtener todas las categorías de gasto
    public List<CategoriaGasto> getAllCategorias() {
        return categoriaGastoRepository.findAll();
    }

    // Obtener una categoría de gasto por ID
    public Optional<CategoriaGasto> getCategoriaById(Long id) {
        return categoriaGastoRepository.findById(id);
    }

    public CategoriaGasto createCategoria(CategoriaGasto categoriaGasto) {
        // Verificar si ya existe una categoría con el mismo nombre (ignore case)
        boolean exists = categoriaGastoRepository.findAll().stream()
            .anyMatch(c -> c.getNombre().equalsIgnoreCase(categoriaGasto.getNombre()));

        if (exists) {
            throw new IllegalArgumentException("La categoría ya existe con ese nombre");
        }

        return categoriaGastoRepository.save(categoriaGasto);
    }

    public CategoriaGasto updateCategoria(Long id, CategoriaGasto categoriaDetails) {
        return categoriaGastoRepository.findById(id)
            .map(categoria -> {
                // Verificar si existe otra categoría con el mismo nombre y diferente ID
                boolean exists = categoriaGastoRepository.findAll().stream()
                    .anyMatch(c -> c.getNombre().equalsIgnoreCase(categoriaDetails.getNombre()) && !c.getId().equals(id));
    
                if (exists) {
                    throw new IllegalArgumentException("Ya existe otra categoría con ese nombre");
                }
    
                // Si no existe duplicado, actualizar el nombre de la categoría
                categoria.setNombre(categoriaDetails.getNombre());
                return categoriaGastoRepository.save(categoria);
            }).orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
    }

    // Eliminar una categoría de gasto por ID
    public void deleteCategoria(Long id) {
        if (categoriaGastoRepository.existsById(id)) {
            categoriaGastoRepository.deleteById(id);
        }else{
            throw new RuntimeException("La categoría no existe.");
        }
    }
}