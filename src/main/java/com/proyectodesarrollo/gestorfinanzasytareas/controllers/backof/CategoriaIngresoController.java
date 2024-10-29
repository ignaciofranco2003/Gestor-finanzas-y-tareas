package com.proyectodesarrollo.gestorfinanzasytareas.controllers.backof;

import java.util.List;
import java.util.Optional;

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
import com.proyectodesarrollo.gestorfinanzasytareas.entities.CategoriaIngreso;
import com.proyectodesarrollo.gestorfinanzasytareas.services.CategoriaIngresoService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/backof/categorias-ingreso")
public class CategoriaIngresoController {

    @Autowired
    private CategoriaIngresoService categoriaIngresoService;

    @Autowired
    private JWTUtilityServiceImpl jwtUtilityService;

    private boolean isAdmin(String token) {
        try {
            JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
            String userRole = claims.getStringClaim("role"); // Asume que el rol está en el claim "role"
            return "ADMIN".equals(userRole); // Verifica si el rol es ADMIN
        } catch (Exception e) {
            return false; // Si ocurre un error al validar el token, no es administrador
        }
    }

    @GetMapping("/all")
    public List<CategoriaIngreso> getAllCategorias() {
        return categoriaIngresoService.getAllCategorias();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoriaIngreso> getCategoriaById(@PathVariable Long id) {
        Optional<CategoriaIngreso> categoria = categoriaIngresoService.getCategoriaById(id);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<CategoriaIngreso> createCategoria(@RequestBody CategoriaIngreso categoriaIngreso, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isAdmin(token)) {
            return ResponseEntity.ok(categoriaIngresoService.createCategoria(categoriaIngreso));
        } else {
            return ResponseEntity.status(403).build(); // Forbidden
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoriaIngreso> updateCategoria(@PathVariable Long id, @RequestBody CategoriaIngreso categoriaIngreso, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isAdmin(token)) {
            try {
                return ResponseEntity.ok(categoriaIngresoService.updateCategoria(id, categoriaIngreso));
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(403).build(); // Forbidden
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoria(@PathVariable Long id, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isAdmin(token)) {
            categoriaIngresoService.deleteCategoria(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(403).build(); // Forbidden
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
            //sumimos que se proporciona la API Key directamente
            return header; // Retorna la API Key directamente
        }
    }
}