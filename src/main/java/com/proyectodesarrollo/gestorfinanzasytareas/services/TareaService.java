package com.proyectodesarrollo.gestorfinanzasytareas.services;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Tarea;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.TareaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TareaService {

    @Autowired
    private TareaRepository tareaRepository;

    public Tarea createTarea(Tarea tarea) {
        return tareaRepository.save(tarea);
    }

    public Tarea getTareaById(Long id) {
        return tareaRepository.findById(id).orElse(null);
    }

    public void deleteTarea(Long id) {
        tareaRepository.deleteById(id);
    }
}
