package com.proyectodesarrollo.gestorfinanzasytareas.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Ahorro;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.AhorroRepository;

@Service
public class AhorroService {

    @Autowired
    private AhorroRepository ahorroRepository;

    public List<Ahorro> getAllAhorros() {
        return ahorroRepository.findAll();
    }

    public Optional<Ahorro> getAhorroById(Long id) {
        return ahorroRepository.findById(id);
    }

    public List<Ahorro> getAhorrosByCuentaId(Long cuentaId) {
        return ahorroRepository.findByCuentaId(cuentaId);
    }

    public Ahorro createAhorro(Ahorro ahorro) {
        return ahorroRepository.save(ahorro);
    }

    public Ahorro updateAhorro(Long id, Ahorro ahorroActualizado) {
        return ahorroRepository.findById(id)
                .map(ahorro -> {
                    ahorro.setNombreAhorro(ahorroActualizado.getNombreAhorro());
                    ahorro.setMontoActual(ahorroActualizado.getMontoActual());
                    ahorro.setMontoFinal(ahorroActualizado.getMontoFinal());
                    ahorro.setCuenta(ahorroActualizado.getCuenta());
                    ahorro.setCompletado(ahorroActualizado.isCompletado());
                    return ahorroRepository.save(ahorro);
                }).orElseThrow(() -> new RuntimeException("Ahorro no encontrado"));
    }
    
    public void deleteAhorro(Long id) {
        ahorroRepository.deleteById(id);
    }
}
