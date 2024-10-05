package com.proyectodesarrollo.gestorfinanzasytareas.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Ahorro;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.AhorroRepository;

@Service
public class AhorroService {

    @Autowired
    private AhorroRepository ahorroRepository;

    public Ahorro createAhorro(Ahorro ahorro) {
        return ahorroRepository.save(ahorro);
    }

    public Ahorro getAhorroById(Long id) {
        return ahorroRepository.findById(id).orElse(null);
    }

    public void deleteAhorro(Long id) {
        ahorroRepository.deleteById(id);
    }
}
