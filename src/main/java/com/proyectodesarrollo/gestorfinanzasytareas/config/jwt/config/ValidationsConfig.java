package com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.models.validations.UserValidations;

@Configuration
public class ValidationsConfig {

    @Bean
    public UserValidations userValidations(){
        return new UserValidations();
    }
}
