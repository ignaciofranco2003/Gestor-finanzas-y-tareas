package com.proyectodesarrollo.gestorfinanzasytareas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Gasto;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.GastoRepository;

@Service
public class GastoService {

    @Autowired
    private GastoRepository gastoRepository;

    public Gasto createGasto(Gasto gasto) {
        return gastoRepository.save(gasto);
    }

    public Gasto getGastoById(Long id) {
        return gastoRepository.findById(id).orElse(null);
    }

    public void deleteGasto(Long id) {
        gastoRepository.deleteById(id);
    }
}
