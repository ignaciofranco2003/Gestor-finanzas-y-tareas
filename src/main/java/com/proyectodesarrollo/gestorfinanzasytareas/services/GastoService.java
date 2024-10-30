package com.proyectodesarrollo.gestorfinanzasytareas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Gasto;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.GastoRepository;

@Service
public class GastoService {

    @Autowired
    private GastoRepository gastoRepository;

    // Obtener todos los gastos
    public List<Gasto> getAllGastos() {
        return gastoRepository.findAll();
    }

    // Obtener un gasto por ID
    public Optional<Gasto> getGastoById(Long id) {
        return gastoRepository.findById(id);
    }

    public List<Gasto> getGastosByCuentaId(Long cuentaId) {
        return gastoRepository.findByCuenta_Id(cuentaId);
    }

    // Crear un nuevo gasto
    public Gasto createGasto(Gasto gasto) {
        return gastoRepository.save(gasto);
    }

    // Actualizar un gasto existente
    public Gasto updateGasto(Long id, Gasto gastoActualizado) {
        return gastoRepository.findById(id)
                .map(gasto -> {
                    gasto.setMonto(gastoActualizado.getMonto());
                    gasto.setFecha(gastoActualizado.getFecha());
                    gasto.setCuenta(gastoActualizado.getCuenta());
                    gasto.setCategoria(gastoActualizado.getCategoria());
                    return gastoRepository.save(gasto);
                }).orElseThrow(() -> new RuntimeException("Gasto no encontrado"));
    }

    // Eliminar un gasto
    public void deleteGasto(Long id) {
        gastoRepository.deleteById(id);
    }
}
