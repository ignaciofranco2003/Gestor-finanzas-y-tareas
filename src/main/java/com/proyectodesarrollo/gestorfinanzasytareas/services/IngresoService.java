package com.proyectodesarrollo.gestorfinanzasytareas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Ingreso;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.IngresoRepository;

@Service
public class IngresoService {

    @Autowired
    private IngresoRepository ingresoRepository;

    public Ingreso createIngreso(Ingreso ingreso) {
        return ingresoRepository.save(ingreso);
    }

    public Ingreso getIngresoById(Long id) {
        return ingresoRepository.findById(id).orElse(null);
    }

    public void deleteIngreso(Long id) {
        ingresoRepository.deleteById(id);
    }
}
