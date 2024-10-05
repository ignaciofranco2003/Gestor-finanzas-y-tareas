package com.proyectodesarrollo.gestorfinanzasytareas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.proyectodesarrollo.gestorfinanzasytareas.JWTconfig.JwtRequestFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

        @Autowired
        private JwtRequestFilter jwtRequestFilter;

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
                http
                                .authorizeHttpRequests((authorize) -> authorize
                                                .requestMatchers("/api/users/register").permitAll() // Permitir el
                                                                                                    // registro sin
                                                                                                    // autenticación
                                                .anyRequest().authenticated() // Requerir autenticación para otras
                                                                              // solicitudes
                                )
                                .formLogin(login -> login.permitAll()) // Permitir el acceso a la página de inicio de
                                                                       // sesión
                                .logout(logout -> logout.logoutSuccessUrl("/")); // URL de éxito en el logout

                // Añadir el filtro JWT antes del filtro de autenticación de Spring
                http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

                return http.build();
        }

        @Bean
        public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
                return http.getSharedObject(AuthenticationManagerBuilder.class).build(); // Configurar y devolver
                                                                                         // AuthenticationManager
        }

        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
                return (web) -> web.ignoring().requestMatchers("/api/users/register"); // Ignorar autorización para
                                                                                       // estas rutas
        }
}