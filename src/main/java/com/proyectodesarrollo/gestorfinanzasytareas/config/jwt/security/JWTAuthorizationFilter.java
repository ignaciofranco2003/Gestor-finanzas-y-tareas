package com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.security;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.ParseException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jwt.JWTClaimsSet;
import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.impl.JWTUtilityServiceImpl;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTUtilityServiceImpl jwtUtilityService;

    public JWTAuthorizationFilter(JWTUtilityServiceImpl jwtUtilityService) {
        this.jwtUtilityService = jwtUtilityService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        String header = request.getHeader("Authorization");
        String token;

        // Comprobar si hay un encabezado de Authorization
        if (header == null) {
            filterChain.doFilter(request, response);
            return;
        }

        // Si el token tiene el prefijo "Bearer ", remuévelo
        if (header.startsWith("Bearer ")) {
            token = header.substring(7);
        } else {
            // Usa la API key directamente como el token
            token = header;
        }

        try {
            // Procesar el JWT y establecer el contexto de seguridad
            JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), null, Collections.emptyList());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (JOSEException | ParseException | NoSuchAlgorithmException | InvalidKeySpecException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Unauthorized: Token invalid or not recognized");
            return;
        }

        // Continuar con la cadena de filtros
        filterChain.doFilter(request, response);
    }
}