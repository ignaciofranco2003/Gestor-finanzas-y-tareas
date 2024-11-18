package com.proyectodesarrollo.gestorfinanzasytareas.controllers.backof;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public ResponseEntity<Map<String, Object>> getAllCategorias() {
        List<CategoriaIngreso> categorias = categoriaIngresoService.getAllCategorias();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", categorias);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCategoriaById(@PathVariable Long id) {
        Optional<CategoriaIngreso> categoria = categoriaIngresoService.getCategoriaById(id);
        if (categoria.isPresent()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("data", categoria.get());
            return ResponseEntity.ok(response);
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Categoría no encontrada");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> createCategoria(@RequestBody CategoriaIngreso categoriaIngreso, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isAdmin(token)) {
            try {
                categoriaIngresoService.createCategoria(categoriaIngreso);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Categoría creada exitosamente");
                return ResponseEntity.ok(response);
            } catch (IllegalArgumentException e) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Ya existe una categoría con ese nombre");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No tienes permiso para realizar esta acción");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateCategoria(@PathVariable Long id, @RequestBody CategoriaIngreso categoriaIngreso, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isAdmin(token)) {
            try {
                categoriaIngresoService.updateCategoria(id, categoriaIngreso);
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Modificado con éxito");
                return ResponseEntity.ok(response);
            } catch (IllegalArgumentException e) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Ya existe otra categoría con ese nombre");
                return ResponseEntity.badRequest().body(response);
            } catch (RuntimeException e) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Categoría no encontrada");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } else {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "No tienes permiso para realizar esta acción");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteCategoria(@PathVariable Long id, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isAdmin(token)) {
            try {
                categoriaIngresoService.deleteCategoria(id);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Categoría eliminada");
                return ResponseEntity.ok(response);
            } catch (DataIntegrityViolationException e) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "No se puede eliminar la categoría porque está referenciada en otra entidad.");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            } catch (RuntimeException e) {
                Map<String, String> response = new HashMap<>();
                response.put("error", "La categoría no existe.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } else {
            Map<String, String> response = new HashMap<>();
            response.put("error", "No se pudo eliminar la categoría");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String header = request.getHeader("Authorization");

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