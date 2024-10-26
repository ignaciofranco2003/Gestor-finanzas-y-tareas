package com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.IAuthService;
import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.IJWTUtilityService;
import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.models.dtos.LoginDTO;
import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.models.dtos.ResponseDTO;
import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.models.validations.UserValidations;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;
import com.proyectodesarrollo.gestorfinanzasytareas.repositories.UserRepository;

@Service
public class AuthServiceImpl implements IAuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IJWTUtilityService jwtUtilityService;

    @Autowired
    private UserValidations userValidations;

    @Override
    public HashMap<String, String> login(LoginDTO loginRequest) throws Exception {
        try {
            HashMap<String, String> jwt = new HashMap<>();
            Optional<User> user = userRepository.findByEmailop(loginRequest.getEmail());

            // User useraux = userRepository.findByEmail(loginRequest.getEmail());
            // System.out.println(useraux.getUsername());

            if (user.isEmpty()) {
                jwt.put("error", "User not registered!");
                return jwt;
            }
            if (verifyPassword(loginRequest.getPassword(), user.get().getPassword())) {
                // jwt.put("jwt", jwtUtilityService.generateJWT(user.get().getUserid()));
                jwt.put("jwt", jwtUtilityService.generateJWT(
                    user.get().getUserid(),
                    user.get().getUsername(),
                    user.get().getRole(),
                    user.get().getEmail() 
                ));
            } else {
                jwt.put("error", "Authentication failed");
            }
            return jwt;
        } catch (IllegalArgumentException e) {
            System.err.println("Error generating JWT: " + e.getMessage());
            throw new Exception("Error generating JWT", e);
        } catch (Exception e) {
            System.err.println("Unknown error: " + e.toString());
            throw new Exception("Unknown error", e);
        }
    }

    @Override
    public ResponseDTO register(User user) throws Exception {
        try {
            ResponseDTO response = userValidations.validate(user);
            List<User> getAllUsers = userRepository.findAll();

            if (response.getNumOfErrors() > 0){
                return response;
            }

            for (User repeatFields : getAllUsers) {
                if (repeatFields != null) {
                    response.setMessage("User already exists!");
                    return response;
                }
            }

            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
            user.setPassword(encoder.encode(user.getPassword()));
            userRepository.save(user);
            response.setMessage("User created successfully!");
            return response;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    private boolean verifyPassword(String enteredPassword, String storedPassword) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(enteredPassword, storedPassword);
    }
}
