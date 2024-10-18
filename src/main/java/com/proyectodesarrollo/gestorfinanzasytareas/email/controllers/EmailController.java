package com.proyectodesarrollo.gestorfinanzasytareas.email.controllers;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyectodesarrollo.gestorfinanzasytareas.email.services.IEmailService;
import com.proyectodesarrollo.gestorfinanzasytareas.email.services.models.CorreoRequest;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;
import com.proyectodesarrollo.gestorfinanzasytareas.services.UserService;

@RestController
@RequestMapping("/api/users")
public class EmailController {

    @Autowired
    IEmailService emailService;

    @Autowired
    private UserService userService;

    @PostMapping("/enviar-correo")
    public ResponseEntity<String> enviarCorreo(@RequestBody CorreoRequest correoRequest) {
        try {
            User aux = userService.getUserByEmail(correoRequest.getDestinatario());

            Random random = new Random();
            int numeroAleatorio = 100000 + random.nextInt(900000); // Genera un número entre 100000 y 999999
            String mensaje = "Su codigo de verificacion es "+ numeroAleatorio;

            String numeroAleatorioStr = String.valueOf(numeroAleatorio);

            aux.setVerificationCode(numeroAleatorioStr);
            userService.saveUser(aux);

            correoRequest.setMensaje(mensaje);

            emailService.enviarCorreo(correoRequest);
            return new ResponseEntity<>("Correo enviado exitosamente.", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error al enviar el correo: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}