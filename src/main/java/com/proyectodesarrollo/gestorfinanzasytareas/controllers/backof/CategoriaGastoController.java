package com.proyectodesarrollo.gestorfinanzasytareas.controllers.backof;

import java.util.List;
import java.util.Optional;

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
import com.proyectodesarrollo.gestorfinanzasytareas.entities.CategoriaGasto;
import com.proyectodesarrollo.gestorfinanzasytareas.services.CategoriaGastoService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/backof/categorias-gasto")
public class CategoriaGastoController {

    @Autowired
    private CategoriaGastoService categoriaGastoService;

    @Autowired
    private JWTUtilityServiceImpl jwtUtilityService;

    // Método para verificar si el usuario es administrador
    private boolean isAdmin(String token) {
        try {
            JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
            String userRole = claims.getStringClaim("role");
            return "ADMIN".equals(userRole); // Verificar si el rol es ADMIN
        } catch (Exception e) {
            return false; // Si hay error al validar el token, no es administrador
        }
    }

    // Obtener todas las categorías de gasto
    @GetMapping("/all")
    public List<CategoriaGasto> getAllCategorias() {
        return categoriaGastoService.getAllCategorias();
    }

    // Obtener una categoría de gasto por ID
    @GetMapping("/{id}")
    public ResponseEntity<CategoriaGasto> getCategoriaById(@PathVariable Long id) {
        Optional<CategoriaGasto> categoria = categoriaGastoService.getCategoriaById(id);
        return categoria.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear una nueva categoría de gasto
    @PostMapping
    public ResponseEntity<String> createCategoria(@RequestBody CategoriaGasto categoriaGasto, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isAdmin(token)) {
            try {
                categoriaGastoService.createCategoria(categoriaGasto);
                return ResponseEntity.ok("Categoría creada exitosamente");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("La categoría ya existe con ese nombre");
            }
        } else {
            return ResponseEntity.status(403).body("Acceso denegado");
        }
    }

    // Actualizar una categoría de gasto existente
    @PutMapping("/{id}")
    public ResponseEntity<String> updateCategoria(@PathVariable Long id, @RequestBody CategoriaGasto categoriaGasto, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);

        // Verificar si el token existe y si el usuario tiene permisos de administrador
        if (token != null && isAdmin(token)) {
            try {
                // Llama al servicio para actualizar la categoría
                categoriaGastoService.updateCategoria(id, categoriaGasto);
                return ResponseEntity.ok("Modificado con éxito");

            } catch (IllegalArgumentException e) {
                // Captura el caso de nombre duplicado con diferente ID
                return ResponseEntity.badRequest().body("Ya existe otra categoría con ese nombre");

            } catch (RuntimeException e) {
                // Captura el caso de categoría no encontrada
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoría no encontrada");
            }
        } else {
            // Respuesta 403 Forbidden si el usuario no es admin
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para realizar esta acción");
        }
    }

    // Eliminar una categoría de gasto por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategoria(@PathVariable Long id, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        if (token != null && isAdmin(token)) {
            try {
                categoriaGastoService.deleteCategoria(id);
                return ResponseEntity.ok("Categoria eliminada");
            } catch (RuntimeException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("La categoría no existe.");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No se pudo eliminar la categoría");
        }
    }

    // Método auxiliar para extraer el token del encabezado
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