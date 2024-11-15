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
import com.proyectodesarrollo.gestorfinanzasytareas.controllers.frontof.DTOS.IngresoDTO;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Cuenta;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Ingreso;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;
import com.proyectodesarrollo.gestorfinanzasytareas.services.CuentaService;
import com.proyectodesarrollo.gestorfinanzasytareas.services.IngresoService;
import com.proyectodesarrollo.gestorfinanzasytareas.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/ingresos")
public class IngresoController {

    @Autowired
    private IngresoService ingresoService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtilityServiceImpl jwtUtilityService;

    private boolean isUser(String token) {
        try {
            JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
            String userRole = claims.getStringClaim("role");
            return "USER".equals(userRole);
        } catch (Exception e) {
            return false;
        }
    }

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
    public ResponseEntity<Map<String, Object>> getAllIngresos() {
        List<Ingreso> ingresos = ingresoService.getAllIngresos();
        List<IngresoDTO> response = ingresos.stream().map(ingreso -> {
            Long cuentaId = ingreso.getCuenta() != null ? ingreso.getCuenta().getId() : null;
            Long categoriaId = ingreso.getCategoria() != null ? ingreso.getCategoria().getId() : null;
            return new IngresoDTO(ingreso.getId(), ingreso.getMonto(), ingreso.getFecha(),cuentaId, categoriaId);
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<Map<String, Object>> getIngresosByCuentaId(@PathVariable Long cuentaId) {
        List<Ingreso> ingresos = ingresoService.getIngresosByCuentaId(cuentaId);
        List<IngresoDTO> response = ingresos.stream().map(ingreso -> {
            Long categoriaId = ingreso.getCategoria() != null ? ingreso.getCategoria().getId() : null;
            return new IngresoDTO(ingreso.getId(), ingreso.getMonto(),ingreso.getFecha(), cuentaId, categoriaId);
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getIngresoById(@PathVariable Long id) {
        Optional<Ingreso> ingreso = ingresoService.getIngresoById(id);
        Map<String, Object> result = new HashMap<>();
        if (ingreso.isPresent()) {
            Ingreso i = ingreso.get();
            Long cuentaId = i.getCuenta() != null ? i.getCuenta().getId() : null;
            Long categoriaId = i.getCategoria() != null ? i.getCategoria().getId() : null;
            IngresoDTO response = new IngresoDTO(i.getId(), i.getMonto(), i.getFecha() ,cuentaId, categoriaId);
            result.put("success", true);
            result.put("data", response);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "Ingreso no encontrado.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createIngreso(@RequestBody Ingreso ingreso, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        Map<String, Object> response = new HashMap<>();
        if (token != null && isUser(token)) {
            Optional<Cuenta> cuenta = getCuentaFromToken(token);
            if (cuenta.isPresent()) {
                ingreso.setCuenta(cuenta.get());
                ingresoService.createIngreso(ingreso);
                response.put("success", true);
                response.put("message", "Ingreso creado exitosamente.");
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
    public ResponseEntity<Map<String, Object>> updateIngreso(@PathVariable Long id, @RequestBody Ingreso ingreso,
            HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        Map<String, Object> response = new HashMap<>();
        if (token != null && isUser(token)) {
            try {
                ingresoService.updateIngreso(id, ingreso);
                response.put("success", true);
                response.put("message", "Ingreso actualizado exitosamente.");
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                response.put("success", false);
                response.put("message", "Ingreso no encontrado.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } else {
            response.put("success", false);
            response.put("message", "Acceso denegado.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteIngreso(@PathVariable Long id, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        Map<String, Object> response = new HashMap<>();
        if (token != null && isUser(token)) {
            ingresoService.deleteIngreso(id);
            response.put("success", true);
            response.put("message", "Ingreso eliminado exitosamente.");
            return ResponseEntity.ok(response);
        } else {
            response.put("success", false);
            response.put("message", "Acceso denegado.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        System.out.println("Header Authorization: " + header); // Verifica el valor recibido
        if (header == null) {
            return null; // No se proporciona encabezado
        }

        if (header.startsWith("Bearer ")) {
            return header.substring(7); // Extrae el token sin "Bearer "
        } else {
            // Aquí asumimos que se proporciona la API Key directamente
            return header; // Retorna la API Key directamente
        }
    }
}
