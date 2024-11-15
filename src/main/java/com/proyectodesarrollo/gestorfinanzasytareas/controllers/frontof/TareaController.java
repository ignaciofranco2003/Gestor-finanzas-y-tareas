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
import com.proyectodesarrollo.gestorfinanzasytareas.controllers.frontof.DTOS.TareaDTO;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Cuenta;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Tarea;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;
import com.proyectodesarrollo.gestorfinanzasytareas.services.CuentaService;
import com.proyectodesarrollo.gestorfinanzasytareas.services.TareaService;
import com.proyectodesarrollo.gestorfinanzasytareas.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/tareas")
public class TareaController {

    @Autowired
    private TareaService tareaService;

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
            String useremail = claims.getStringClaim("email");
            User aux = userService.getUserByEmail(useremail);
            return cuentaService.findByUserId(aux.getUserid());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    // @GetMapping("/all")
    // public ResponseEntity<List<TareaDTO>> getAllTareas() {
    // List<Tarea> tareas = tareaService.getAllTareas();
    // List<TareaDTO> response = tareas.stream().map(tarea -> {
    // Long cuentaId = tarea.getCuenta() != null ? tarea.getCuenta().getId() : null;
    // return new TareaDTO(tarea.getId(), tarea.getDescripcion(),
    // tarea.isCompletada(), cuentaId);
    // }).collect(Collectors.toList());

    // return ResponseEntity.ok(response);
    // }

    // @GetMapping("/cuenta/{cuentaId}")
    // public ResponseEntity<List<TareaDTO>> getTareasByCuentaId(@PathVariable Long
    // cuentaId) {
    // List<Tarea> tareas = tareaService.getTareasByCuentaId(cuentaId);
    // List<TareaDTO> response = tareas.stream().map(tarea ->
    // new TareaDTO(tarea.getId(), tarea.getDescripcion(), tarea.isCompletada(),
    // cuentaId)
    // ).collect(Collectors.toList());

    // return ResponseEntity.ok(response);
    // }

    // @GetMapping("/{id}")
    // public ResponseEntity<TareaDTO> getTareaById(@PathVariable Long id) {
    // Optional<Tarea> tarea = tareaService.getTareaById(id);
    // return tarea.map(t -> {
    // Long cuentaId = t.getCuenta() != null ? t.getCuenta().getId() : null;
    // TareaDTO response = new TareaDTO(t.getId(), t.getDescripcion(),
    // t.isCompletada(), cuentaId);
    // return ResponseEntity.ok(response);
    // }).orElseGet(() -> ResponseEntity.notFound().build());
    // }

    // @PostMapping("/create")
    // public ResponseEntity<String> createTarea(@RequestBody Tarea tarea,
    // HttpServletRequest request) {
    // String token = extractTokenFromRequest(request);
    // if (token != null && isUser(token)) {
    // Optional<Cuenta> cuenta = getCuentaFromToken(token);
    // if (cuenta.isPresent()) {
    // tarea.setCuenta(cuenta.get());
    // Tarea nuevaTarea = tareaService.createTarea(tarea);
    // return ResponseEntity.ok("Tarea agregada a la cuenta ID " +
    // nuevaTarea.getCuenta().getId());
    // }
    // return ResponseEntity.status(404).build();
    // } else {
    // return ResponseEntity.status(403).build();
    // }
    // }

    // @PutMapping("/{id}")
    // public ResponseEntity<String> updateTarea(@PathVariable Long id, @RequestBody
    // Tarea tarea, HttpServletRequest request) {
    // String token = extractTokenFromRequest(request);
    // if (token != null && isUser(token)) {
    // try {
    // Tarea tareaActualizada = tareaService.updateTarea(id, tarea);
    // return ResponseEntity.ok("Tarea ID " + tareaActualizada.getId() + "
    // actualizada en la cuenta ID " + tareaActualizada.getCuenta().getId());
    // } catch (RuntimeException e) {
    // return ResponseEntity.notFound().build();
    // }
    // } else {
    // return ResponseEntity.status(403).build();
    // }
    // }

    // @DeleteMapping("/{id}")
    // public ResponseEntity<String> deleteTarea(@PathVariable Long id,
    // HttpServletRequest request) {
    // String token = extractTokenFromRequest(request);
    // if (token != null && isUser(token)) {
    // tareaService.deleteTarea(id);
    // return ResponseEntity.ok("Tarea ID " + id + " eliminada");
    // } else {
    // return ResponseEntity.status(403).build();
    // }
    // }

    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllTareas() {
        List<Tarea> tareas = tareaService.getAllTareas();
        List<TareaDTO> response = tareas.stream().map(tarea -> {
            Long cuentaId = tarea.getCuenta() != null ? tarea.getCuenta().getId() : null;
            return new TareaDTO(tarea.getId(), tarea.getDescripcion(), tarea.isCompletada(), cuentaId);
        }).collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<Map<String, Object>> getTareasByCuentaId(@PathVariable Long cuentaId) {
        List<Tarea> tareas = tareaService.getTareasByCuentaId(cuentaId);
        List<TareaDTO> response = tareas.stream()
                .map(tarea -> new TareaDTO(tarea.getId(), tarea.getDescripcion(), tarea.isCompletada(), cuentaId))
                .collect(Collectors.toList());

        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("data", response);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getTareaById(@PathVariable Long id) {
        Optional<Tarea> tarea = tareaService.getTareaById(id);
        Map<String, Object> result = new HashMap<>();
        if (tarea.isPresent()) {
            Tarea t = tarea.get();
            Long cuentaId = t.getCuenta() != null ? t.getCuenta().getId() : null;
            TareaDTO response = new TareaDTO(t.getId(), t.getDescripcion(), t.isCompletada(), cuentaId);

            result.put("success", true);
            result.put("data", response);
            return ResponseEntity.ok(result);
        } else {
            result.put("success", false);
            result.put("message", "Tarea no encontrada.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createTarea(@RequestBody Tarea tarea, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        Map<String, Object> response = new HashMap<>();
        if (token != null && isUser(token)) {
            Optional<Cuenta> cuenta = getCuentaFromToken(token);
            if (cuenta.isPresent()) {
                tarea.setCuenta(cuenta.get());
                Tarea nuevaTarea = tareaService.createTarea(tarea);
                response.put("success", true);
                response.put("message", "Tarea creada exitosamente.");
                response.put("data", nuevaTarea);
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
    public ResponseEntity<Map<String, Object>> updateTarea(@PathVariable Long id, @RequestBody Tarea tarea,
            HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        Map<String, Object> response = new HashMap<>();
        if (token != null && isUser(token)) {
            try {
                Tarea tareaActualizada = tareaService.updateTarea(id, tarea);
                response.put("success", true);
                response.put("message", "Tarea actualizada exitosamente.");
                response.put("data", tareaActualizada);
                return ResponseEntity.ok(response);
            } catch (RuntimeException e) {
                response.put("success", false);
                response.put("message", "Tarea no encontrada.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } else {
            response.put("success", false);
            response.put("message", "Acceso denegado.");
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteTarea(@PathVariable Long id, HttpServletRequest request) {
        String token = extractTokenFromRequest(request);
        Map<String, Object> response = new HashMap<>();
        if (token != null && isUser(token)) {
            tareaService.deleteTarea(id);
            response.put("success", true);
            response.put("message", "Tarea eliminada exitosamente.");
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
