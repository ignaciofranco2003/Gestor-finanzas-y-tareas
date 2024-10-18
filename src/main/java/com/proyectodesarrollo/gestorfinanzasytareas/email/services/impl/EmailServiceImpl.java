package com.proyectodesarrollo.gestorfinanzasytareas.email.services.impl;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.proyectodesarrollo.gestorfinanzasytareas.email.services.IEmailService;
import com.proyectodesarrollo.gestorfinanzasytareas.email.services.models.CorreoRequest;

@Service
public class EmailServiceImpl implements IEmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;

    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void enviarCorreo(CorreoRequest correoRequest) {
        try {
            jakarta.mail.internet.MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(correoRequest.getDestinatario());
            helper.setSubject(correoRequest.getAsunto());

            // Procesar la plantilla Thymeleaf
            Context context = new Context();
            context.setVariable("mensaje", correoRequest.getMensaje());
            String contenidoHtml = templateEngine.process("email", context);
            helper.setText(contenidoHtml, true);

            javaMailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage(), e);
        }
    }
}