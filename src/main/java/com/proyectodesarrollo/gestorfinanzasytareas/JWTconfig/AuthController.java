package com.proyectodesarrollo.gestorfinanzasytareas.jwtconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    // @PostMapping("/authenticate")
    // public String createAuthenticationToken(@RequestBody AuthRequest authRequest)
    // throws Exception {
    // // Autenticación del usuario
    // authenticationManager.authenticate(
    // new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
    // authRequest.getPassword()));

    // // Cargar los detalles del usuario
    // final UserDetails userDetails =
    // userDetailsService.loadUserByUsername(authRequest.getEmail());

    // // Generar y devolver el token JWT
    // return jwtUtil.generateToken(userDetails.getUsername());
    // }

    @PostMapping("/authenticate")
    public ResponseEntity<Boolean> createAuthenticationToken(@RequestBody AuthRequest authRequest) {
        try {
            // Autenticación del usuario
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

            // Si la autenticación es exitosa, devolvemos true
            return ResponseEntity.ok(true);
        } catch (Exception e) {
            // Si falla la autenticación, devolvemos false
            return ResponseEntity.ok(false);
        }
    }
}
