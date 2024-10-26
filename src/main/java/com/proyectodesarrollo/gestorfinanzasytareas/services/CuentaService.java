package com.proyectodesarrollo.gestorfinanzasytareas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Cuenta;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.CuentaRepository;

@Service
public class CuentaService {

    @Autowired
    private CuentaRepository cuentaRepository;

    // Obtener todas las cuentas
    public List<Cuenta> getAllCuentas() {
        return cuentaRepository.findAll();
    }

    // Obtener una cuenta por ID
    public Optional<Cuenta> getCuentaById(Long id) {
        return cuentaRepository.findById(id);
    }

    // Crear una nueva cuenta
    public Cuenta createCuenta(Cuenta cuenta) {
        return cuentaRepository.save(cuenta);
    }

    // Actualizar una cuenta existente
    public Cuenta updateCuenta(Long id, Cuenta cuentaActualizada) {
        return cuentaRepository.findById(id).map(cuenta -> {
            cuenta.setNombre(cuentaActualizada.getNombre());
            // cuenta.setSaldo(cuentaActualizada.getSaldo());
            // Agregar otros atributos de `Cuenta` que puedan ser actualizados
            return cuentaRepository.save(cuenta);
        }).orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }

    // Eliminar una cuenta
    public void deleteCuenta(Long id) {
        cuentaRepository.deleteById(id);
    }

    // Obtener cuenta por el ID de usuario
    public Optional<Cuenta> findByUserId(Long userId) {
        return cuentaRepository.findByUserId(userId);
    }
}
