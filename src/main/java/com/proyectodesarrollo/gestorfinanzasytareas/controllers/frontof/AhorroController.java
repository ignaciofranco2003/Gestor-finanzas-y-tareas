package com.proyectodesarrollo.gestorfinanzasytareas.controllers.frontof;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jwt.JWTClaimsSet;
import com.proyectodesarrollo.gestorfinanzasytareas.config.jwt.services.impl.JWTUtilityServiceImpl;
import com.proyectodesarrollo.gestorfinanzasytareas.controllers.frontof.DTOS.AhorroDTO;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Ahorro;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Cuenta;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;
import com.proyectodesarrollo.gestorfinanzasytareas.services.AhorroService;
import com.proyectodesarrollo.gestorfinanzasytareas.services.CuentaService;
import com.proyectodesarrollo.gestorfinanzasytareas.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/ahorros")
public class AhorroController {

    @Autowired
    private AhorroService ahorroService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtilityServiceImpl jwtUtilityService;

    private Optional<Cuenta> getCuentaFromToken(String token) {
        try {
            JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
            String userEmail = claims.getStringClaim("email");
            User user = userService.getUserByEmail(userEmail);
            return cuentaService.findByUserId(user.getUserid());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllAhorros() {
        List<Ahorro> ahorros = ahorroService.getAllAhorros();
        List<AhorroDTO> response = ahorros.stream().map(ahorro -> {
            Long cuentaId = ahorro.getCuenta() != null ? ahorro.getCuenta().getId() : null;
            return new AhorroDTO(ahorro.getId(), ahorro.getNombreAhorro(), ahorro.getMontoActual(), ahorro.getMontoFinal(), cuentaId);
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<Map<String, Object>> getAhorrosByCuentaId(@PathVariable Long cuentaId) {
        List<Ahorro> ahorros = ahorroService.getAhorrosByCuentaId(cuentaId);
        List<AhorroDTO> response = ahorros.stream().map(ahorro -> {
            return new AhorroDTO(ahorro.getId(), ahorro.getNombreAhorro(), ahorro.getMontoActual(), ahorro.getMontoFinal(), cuentaId);
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getAhorroById(@PathVariable Long id) {
        Optional<Ahorro> ahorro = ahorroService.getAhorroById(id);
        if (ahorro.isPresent()) {
            Ahorro a = ahorro.get();
            Long cuentaId = a.getCuenta() != null ? a.getCuenta().getId() : null;
            AhorroDTO response = new AhorroDTO(a.getId(), a.getNombreAhorro(), a.getMontoActual(), a.getMontoFinal(), cuentaId);

            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("data", response);
            return ResponseEntity.ok(result);
        } else {
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("message", "Ahorro no encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createAhorro(@RequestBody Ahorro ahorro, HttpServletRequest request) {
        String token = jwtUtilityService.extractTokenFromRequest(request);
        Map<String, Object> response = new HashMap<>();
        if (token != null && jwtUtilityService.isUser(token)) {
            Optional<Cuenta> cuenta = getCuentaFromToken(token);
            if (cuenta.isPresent()) {
                ahorro.setCuenta(cuenta.get());
                ahorroService.createAhorro(ahorro);
                response.put("success", true);
                response.put("message", "Ahorro creado exitosamente.");
                return ResponseEntity.status(HttpStatus.CREATED).body(response);
            }
            response.put("success", false);
            response.put("message", "Cuenta no encontrada.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } else {
            response.put("success", false);
            response.put("message", "Acceso denegado.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateAhorro(@PathVariable Long id, @RequestBody Ahorro ahorro, HttpServletRequest request) {
        String token = jwtUtilityService.extractTokenFromRequest(request);
        Map<String, Object> response = new HashMap<>();
        if (token != null && jwtUtilityService.isUser(token)) {
            try {
                ahorroService.updateAhorro(id, ahorro);
                response.put("success", true);
                response.put("message", "Ahorro actualizado exitosamente.");
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                response.put("success", false);
                response.put("message", "Ahorro no encontrado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } else {
            response.put("success", false);
            response.put("message", "Acceso denegado.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteAhorro(@PathVariable Long id, HttpServletRequest request) {
        String token = jwtUtilityService.extractTokenFromRequest(request);
        Map<String, Object> response = new HashMap<>();
        if (token != null && jwtUtilityService.isUser(token)) {
            ahorroService.deleteAhorro(id);
            response.put("success", true);
            response.put("message", "Ahorro eliminado exitosamente.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Acceso denegado.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }
}
