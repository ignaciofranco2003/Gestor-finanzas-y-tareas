package com.proyectodesarrollo.gestorfinanzasytareas.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // @Override
    // public UserDetails loadUserByUsername(String email) throws
    // UsernameNotFoundException {
    // User user = userRepository.findByEmail(email);
    // if (user == null) {
    // throw new UsernameNotFoundException("Usuario no encontrado: " + email);
    // }
    // return new org.springframework.security.core.userdetails.User(
    // user.getEmail(), user.getPassword(), new ArrayList<>());
    // }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado: " + email);
        }

        // Crear la lista de autoridades usando el rol del usuario
        List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }
}
