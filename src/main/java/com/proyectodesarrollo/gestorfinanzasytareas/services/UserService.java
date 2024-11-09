package com.proyectodesarrollo.gestorfinanzasytareas.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.entities.Role;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    // Obtener todos los usuarios
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Obtener un usuario por ID
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    // Guardar un usuario
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Eliminar un usuario por ID
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    // Buscar un usuario por email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    // Registrar un nuevo usuario
    public User registerUser(User user) {
        if (getUserByEmail(user.getEmail()) != null) {
            System.out.println(getUserByEmail(user.getEmail()));
            throw new IllegalArgumentException("El correo ya está en uso.");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Cifrar la contraseña
        user.setRole(Role.USER); // Asignar el rol de usuario
        return saveUser(user);
    }

}