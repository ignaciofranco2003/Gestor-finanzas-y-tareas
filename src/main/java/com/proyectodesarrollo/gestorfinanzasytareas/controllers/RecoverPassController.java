package com.proyectodesarrollo.gestorfinanzasytareas.controllers;

import java.util.HashMap;
import java.util.Map;
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
    public ResponseEntity<Map<String, Object>> verificarEmail(@RequestBody EmailReq email) {
        Map<String, Object> response = new HashMap<>();
        if (userService.getUserByEmail(email.getEmail()) != null) {
            response.put("success", true);
            response.put("message", "El email existe.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "El email no existe.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PostMapping("/enviar-codigo")
    public ResponseEntity<Map<String, Object>> enviarCodigo(@RequestBody CorreoRequest correoRequest) {
        Map<String, Object> response = new HashMap<>();
        try {
            User aux = userService.getUserByEmail(correoRequest.getDestinatario());

            Random random = new Random();
            int numeroAleatorio = 100000 + random.nextInt(900000);
            String mensaje = "Su código de verificación es " + numeroAleatorio;

            String numeroAleatorioStr = String.valueOf(numeroAleatorio);

            aux.setVerificationCode(numeroAleatorioStr);
            userService.saveUser(aux);

            correoRequest.setMensaje(mensaje);
            emailService.enviarCorreo(correoRequest);

            response.put("success", true);
            response.put("message", "Correo enviado exitosamente.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error al enviar el correo: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/validar-codigo")
    public ResponseEntity<Map<String, Object>> validarCodigo(@RequestBody CodigoRequest codigoRequest) {
        Map<String, Object> response = new HashMap<>();
        User aux = userService.getUserByEmail(codigoRequest.getEmail());
        if (aux != null && aux.getVerificationCode().equals(codigoRequest.getCodigo())) {
            response.put("success", true);
            response.put("message", "Código verificado correctamente.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Código de verificación incorrecto.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    @PostMapping("/cambiar-password")
    public ResponseEntity<Map<String, Object>> cambiarPassword(
            @RequestBody PasswordChangeRequest passwordChangeRequest) {
        Map<String, Object> response = new HashMap<>();
        User aux = userService.getUserByEmail(passwordChangeRequest.getEmail());
        if (aux != null && aux.getVerificationCode().equals(passwordChangeRequest.getCodigo())) {
            if (passwordChangeRequest.getNuevaPassword() != null
                    && passwordChangeRequest.getNuevaPassword().length() >= 4
                    && passwordChangeRequest.getNuevaPassword().length() <= 16) {
                aux.setPassword(passwordEncoder.encode(passwordChangeRequest.getNuevaPassword()));
                aux.setVerificationCode(null); // Limpia el código de verificación
                userService.saveUser(aux);
                response.put("success", true);
                response.put("message", "Contraseña cambiada exitosamente.");
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "La contraseña debe tener entre 4 y 16 caracteres.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        } else {
            response.put("success", false);
            response.put("message", "Código de verificación incorrecto.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }

    static class EmailReq {
        private String email;

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

    }

}
