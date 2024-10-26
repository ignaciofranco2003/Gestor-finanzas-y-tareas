package com.proyectodesarrollo.gestorfinanzasytareas.controllers.frontof;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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
    public ResponseEntity<List<IngresoDTO>> getAllIngresos() {
        List<Ingreso> ingresos = ingresoService.getAllIngresos();
        List<IngresoDTO> response = ingresos.stream().map(ingreso -> {
            Long cuentaId = ingreso.getCuenta() != null ? ingreso.getCuenta().getId() : null;
            Long categoriaId = ingreso.getCategoria() != null ? ingreso.getCategoria().getId() : null;
            return new IngresoDTO(ingreso.getId(), ingreso.getMonto(), cuentaId, categoriaId);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<IngresoDTO> getIngresoById(@PathVariable Long id) {
        Optional<Ingreso> ingreso = ingresoService.getIngresoById(id);
        return ingreso.map(i -> {
            Long cuentaId = i.getCuenta() != null ? i.getCuenta().getId() : null;
            Long categoriaId = i.getCategoria() != null ? i.getCategoria().getId() : null;
            IngresoDTO response = new IngresoDTO(i.getId(), i.getMonto(), cuentaId, categoriaId);
            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createIngreso(@RequestBody Ingreso ingreso, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isUser(token)) {
            Optional<Cuenta> cuenta = getCuentaFromToken(token);
            if (cuenta.isPresent()) {
                ingreso.setCuenta(cuenta.get());
                Ingreso nuevaIngreso = ingresoService.createIngreso(ingreso);
                return ResponseEntity.ok("Ingreso agregado a la cuenta ID " + nuevaIngreso.getCuenta().getId());
            }
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateIngreso(@PathVariable Long id, @RequestBody Ingreso ingreso, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isUser(token)) {
            try {
                Ingreso ingresoActualizado = ingresoService.updateIngreso(id, ingreso);
                return ResponseEntity.ok("Ingreso ID " + ingresoActualizado.getId() + " actualizado en la cuenta ID " + ingresoActualizado.getCuenta().getId());
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteIngreso(@PathVariable Long id, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isUser(token)) {
            ingresoService.deleteIngreso(id);
            return ResponseEntity.ok("Ingreso ID " + id + " eliminado");
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);
    }
}
