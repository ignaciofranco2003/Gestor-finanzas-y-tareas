package com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Role;

public interface IJWTUtilityService {

    public String generateJWT(Long subject,String name, Role role, String email) throws JOSEException, ParseException, JOSEException, IOException, NoSuchAlgorithmException, InvalidKeySpecException, JOSEException;

    public JWTClaimsSet parseJWT(String jwt) throws ParseException, JOSEException, IOException, NoSuchAlgorithmException, InvalidKeySpecException;

}
