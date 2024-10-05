package com.proyectodesarrollo.gestorfinanzasytareas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Cuenta;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.CuentaRepository;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    public Cuenta createCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    public Cuenta getCuentaById(Long id) {
        return cuentaRepository.findById(id).orElse(null);
    }

    public void deleteCuenta(Long id) {
        cuentaRepository.deleteById(id);
    }
}
