package com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.impl.JWTUtilityServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {

        // @Autowired
        // private IJWTUtilityService jwtUtilityService;

        @Autowired
        private JWTUtilityServiceImpl jwtUtilityService;

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
                return http
                        .csrf(csrf ->
                                csrf.disable())
                        .authorizeHttpRequests(authRequest ->
                                authRequest
                                        .requestMatchers("/css/**","/js/**","/","/home-admin","/home-usuario","/auth/**","/api/users/**","/recoverpassword/**","/createuser","/backof/categorias-gasto/all","/backof/categorias-ingreso/all").permitAll()
                                        .anyRequest().authenticated()
                        )
                        .formLogin(formLogin -> formLogin
                                .loginPage("/login") // La URL del formulario de login personalizado
                                .permitAll() // Permitir acceso a todos a la página de login
                        ) 
                        .sessionManagement(sessionManager ->
                                sessionManager
                                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .addFilterBefore(new JWTAuthorizationFilter((JWTUtilityServiceImpl) jwtUtilityService), UsernamePasswordAuthenticationFilter.class)
                        .exceptionHandling(exceptionHandling ->
                                exceptionHandling
                                        .authenticationEntryPoint((request, response, authException) -> {
                                        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                                        }))
                        .build();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                return http.getSharedObject(AuthenticationManagerBuilder.class).build();
        }

        @Bean
        public JWTAuthorizationFilter jwtAuthorizationFilter() {
                return new JWTAuthorizationFilter(jwtUtilityService);
        }
}

