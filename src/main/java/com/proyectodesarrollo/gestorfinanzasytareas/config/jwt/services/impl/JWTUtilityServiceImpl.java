package com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.impl;

import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.IJWTUtilityService;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Cuenta;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Role;
import com.proyectodesarrollo.gestorfinanzasytareas.services.CuentaService;

import jakarta.servlet.http.HttpServletRequest;

@Service
@PropertySource("classpath:token.properties")
public class JWTUtilityServiceImpl implements IJWTUtilityService {

    @Value("${jwt.key}")
    private String SECRET_KEY;

    @Value("${jwt.exptime}")
    private int exptime;

    @Autowired
    private JWTUtilityServiceImpl jwtUtilityService;

    @Autowired
    private CuentaService cuentaService;

    @Override
    public String generateJWT(Long userId, String name, Role role, String email) throws JOSEException {

        byte[] secretKeyBytes = SECRET_KEY.getBytes();

        JWSSigner signer = new MACSigner(secretKeyBytes);

        Optional<Cuenta> cuenta = cuentaService.findByUserId(userId);

        Date now = new Date();
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                .subject(userId.toString())
                .claim("userid", userId)
                .claim("name", name)
                .claim("role", role)
                .claim("email", email)
                .claim("IDcuenta", cuenta.get().getId())
                .issueTime(now)
                .expirationTime(new Date(now.getTime() + exptime))
                .build();

        SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);
        signedJWT.sign(signer);

        return signedJWT.serialize();
    }

    @Override
    public JWTClaimsSet parseJWT(String jwt) throws JOSEException, ParseException {
        byte[] secretKeyBytes = SECRET_KEY.getBytes();

        SignedJWT signedJWT = SignedJWT.parse(jwt);

        JWSVerifier verifier = new MACVerifier(secretKeyBytes);
        if (!signedJWT.verify(verifier)) {
            throw new JOSEException("Firma inválida");
        }

        JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();
        if (claimsSet.getExpirationTime().before(new Date())) {
            throw new JOSEException("Token expirado");
        }

        return claimsSet;
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null) {
            return null; // No se proporciona encabezado
        }

        if (header.startsWith("Bearer ")) {
            return header.substring(7); // Extrae el token sin "Bearer "
        } else {
            return header; // Retorna la API Key directamente
        }
    }

    public boolean isAdmin(String token) {
        try {
            JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
            String userRole = claims.getStringClaim("role");
            return "ADMIN".equals(userRole);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isUser(String token) {
        try {
            JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
            String userRole = claims.getStringClaim("role");
            return "USER".equals(userRole);
        } catch (Exception e) {
            return false;
        }
    }
}
