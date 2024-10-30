package com.proyectodesarrollo.gestorfinanzasytareas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Ingreso;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.IngresoRepository;

@Service
public class IngresoService {

    @Autowired
    private IngresoRepository ingresoRepository;

    // Obtener todos los ingresos
    public List<Ingreso> getAllIngresos() {
        return ingresoRepository.findAll();
    }

    // Obtener un ingreso por ID
    public Optional<Ingreso> getIngresoById(Long id) {
        return ingresoRepository.findById(id);
    }

    public List<Ingreso> getIngresosByCuentaId(Long cuentaId) {
        return ingresoRepository.findByCuenta_Id(cuentaId);
    }

    // Crear un nuevo ingreso
    public Ingreso createIngreso(Ingreso ingreso) {
        return ingresoRepository.save(ingreso);
    }

    // Actualizar un ingreso existente
    public Ingreso updateIngreso(Long id, Ingreso ingreso) {
        return ingresoRepository.findById(id)
                .map(ingresoExistente -> {
                    ingresoExistente.setMonto(ingreso.getMonto());
                    ingresoExistente.setCuenta(ingreso.getCuenta());
                    ingresoExistente.setCategoria(ingreso.getCategoria());
                    return ingresoRepository.save(ingresoExistente);
                }).orElseThrow(() -> new RuntimeException("Ingreso no encontrado con ID: " + id));
    }

    // Eliminar un ingreso por ID
    public void deleteIngreso(Long id) {
        ingresoRepository.deleteById(id);
    }
}
