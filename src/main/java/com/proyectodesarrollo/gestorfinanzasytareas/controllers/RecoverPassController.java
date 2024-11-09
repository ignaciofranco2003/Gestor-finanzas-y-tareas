package com.proyectodesarrollo.gestorfinanzasytareas.controllers;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyectodesarrollo.gestorfinanzasytareas.controllers.Requests.CodigoRequest;
import com.proyectodesarrollo.gestorfinanzasytareas.controllers.Requests.PasswordChangeRequest;
import com.proyectodesarrollo.gestorfinanzasytareas.email.services.IEmailService;
import com.proyectodesarrollo.gestorfinanzasytareas.email.services.models.CorreoRequest;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;
import com.proyectodesarrollo.gestorfinanzasytareas.services.UserService;

@RestController
@RequestMapping("/recoverpassword")
public class RecoverPassController {

    @Autowired
    private UserService userService;

    @Autowired
    IEmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/verificar-email")
    public ResponseEntity<String> verificarEmail(@RequestBody EmailReq email) {

        if (userService.getUserByEmail(email.getEmail()) != null) {
            return ResponseEntity.ok("El email existe.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("El email no existe.");
        }
    }

    @PostMapping("/enviar-codigo")
    public ResponseEntity<String> enviarCodigo(@RequestBody CorreoRequest correoRequest) {
        try {
            User aux = userService.getUserByEmail(correoRequest.getDestinatario());
            
            Random random = new Random();
            int numeroAleatorio = 100000 + random.nextInt(900000); // Genera un número entre 100000 y 999999
            String mensaje = "Su código de verificación es " + numeroAleatorio;

            String numeroAleatorioStr = String.valueOf(numeroAleatorio);

            aux.setVerificationCode(numeroAleatorioStr);
            userService.saveUser(aux);

            correoRequest.setMensaje(mensaje);
            emailService.enviarCorreo(correoRequest);
            return ResponseEntity.ok("Correo enviado exitosamente.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al enviar el correo: " + e.getMessage());
        }
    }

    @PostMapping("/validar-codigo")
    public ResponseEntity<String> validarCodigo(@RequestBody CodigoRequest codigoRequest) {
        User aux = userService.getUserByEmail(codigoRequest.getEmail());
        if (aux != null && aux.getVerificationCode().equals(codigoRequest.getCodigo())) {
            return ResponseEntity.ok("Código verificado correctamente.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código de verificación incorrecto.");
        }
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<String> cambiarPassword(@RequestBody PasswordChangeRequest passwordChangeRequest) {
        User aux = userService.getUserByEmail(passwordChangeRequest.getEmail());
        if (aux != null && aux.getVerificationCode().equals(passwordChangeRequest.getCodigo())) {
            if (passwordChangeRequest.getNuevaPassword() != null 
                && passwordChangeRequest.getNuevaPassword().length() >= 4 
                && passwordChangeRequest.getNuevaPassword().length() <= 16) {
                aux.setPassword(passwordEncoder.encode(passwordChangeRequest.getNuevaPassword()));
                aux.setVerificationCode(null); // Limpia el código de verificación
                userService.saveUser(aux);
                return ResponseEntity.ok("Contraseña cambiada exitosamente.");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La contraseña debe tener entre 4 y 16 caracteres.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Código de verificación incorrecto.");
        }
    }

    static class EmailReq{
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

}
