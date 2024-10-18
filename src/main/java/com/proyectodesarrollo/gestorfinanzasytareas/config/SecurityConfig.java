package com.proyectodesarrollo.gestorfinanzasytareas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.proyectodesarrollo.gestorfinanzasytareas.jwtconfig.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private JwtRequestFilter jwtRequestFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .csrf().disable() // Desactivar CSRF
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/api/users/**",
                                                                "/recoverpassword",
                                                                "/createuser")
                                                .permitAll()
                                                .anyRequest().authenticated() // Requerir autenticación para otros
                                                                              // endpoints
                                )
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/login") // La URL del formulario de login personalizado
                                                .permitAll() // Permitir acceso a todos a la página de login
                                )
                                // Añadir el filtro JWT después de la configuración de autorización
                                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                return http.getSharedObject(AuthenticationManagerBuilder.class).build();
        }
}