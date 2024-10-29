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
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Ahorro;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Cuenta;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Gasto;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Ingreso;
import com.proyectodesarrollo.gestorfinanzasytareas.services.CuentaService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/cuentas")
public class CuentaController {

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private JWTUtilityServiceImpl jwtUtilityService;

    // DTO para respuesta simplificada
    public static class CuentaResponse {
        private Long id;
        private String nombre;
        private List<Long> gastosIds;
        private List<Long> ahorrosIds;
        private List<Long> ingresosIds;

        // Constructor
        public CuentaResponse(Long id, String nombre, List<Long> gastosIds, List<Long> ahorrosIds, List<Long> ingresosIds) {
            this.id = id;
            this.nombre = nombre;
            this.gastosIds = gastosIds;
            this.ahorrosIds = ahorrosIds;
            this.ingresosIds = ingresosIds;
        }

        // Getters
        public Long getId() { return id; }
        public String getNombre() { return nombre; }
        public List<Long> getGastosIds() { return gastosIds; }
        public List<Long> getAhorrosIds() { return ahorrosIds; }
        public List<Long> getIngresosIds() { return ingresosIds; }
    }

    private boolean isAdmin(String token) {
        try {
            JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
            String userRole = claims.getStringClaim("role");
            return "ADMIN".equals(userRole); // Verificar si el rol es ADMIN
        } catch (Exception e) {
            return false; // Si hay error al validar el token, no es administrador
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

    @GetMapping("/all")
    public ResponseEntity<List<CuentaResponse>> getAllCuentas() {
            List<Cuenta> cuentas = cuentaService.getAllCuentas();
            List<CuentaResponse> response = cuentas.stream().map(cuenta -> {
                List<Long> gastosIds = cuenta.getGastos().stream().map(Gasto::getId).collect(Collectors.toList());
                List<Long> ahorrosIds = cuenta.getAhorros().stream().map(Ahorro::getId).collect(Collectors.toList());
                List<Long> ingresosIds = cuenta.getIngresos().stream().map(Ingreso::getId).collect(Collectors.toList());
                return new CuentaResponse(cuenta.getId(), cuenta.getNombre(), gastosIds, ahorrosIds, ingresosIds);
            }).collect(Collectors.toList());
            
            return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CuentaResponse> getCuentaById(@PathVariable Long id) {
            Optional<Cuenta> cuenta = cuentaService.getCuentaById(id);
            return cuenta.map(c -> {
                List<Long> gastosIds = c.getGastos().stream().map(Gasto::getId).collect(Collectors.toList());
                List<Long> ahorrosIds = c.getAhorros().stream().map(Ahorro::getId).collect(Collectors.toList());
                List<Long> ingresosIds = c.getIngresos().stream().map(Ingreso::getId).collect(Collectors.toList());
                CuentaResponse response = new CuentaResponse(c.getId(), c.getNombre(), gastosIds, ahorrosIds, ingresosIds);
                return ResponseEntity.ok(response);
            }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Cuenta> createCuenta(@RequestBody Cuenta cuenta, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isAdmin(token)) {
            Cuenta nuevaCuenta = cuentaService.createCuenta(cuenta);
            return ResponseEntity.ok(nuevaCuenta);
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Cuenta> updateCuenta(@PathVariable Long id, @RequestBody Cuenta cuenta, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isAdmin(token)) {
            try {
                Cuenta cuentaActualizada = cuentaService.updateCuenta(id, cuenta);
                return ResponseEntity.ok(cuentaActualizada);
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable Long id, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isAdmin(token)) {
            cuentaService.deleteCuenta(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
