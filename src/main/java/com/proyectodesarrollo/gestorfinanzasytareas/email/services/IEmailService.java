package com.proyectodesarrollo.gestorfinanzasytareas.email.services;

import com.proyectodesarrollo.gestorfinanzasytareas.email.services.models.CorreoRequest;

public interface IEmailService {
    void enviarCorreo(CorreoRequest correoRequest);
}
