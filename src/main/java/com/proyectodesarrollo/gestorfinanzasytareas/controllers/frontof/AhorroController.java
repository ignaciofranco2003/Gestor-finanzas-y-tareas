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
    public ResponseEntity<List<AhorroDTO>> getAllAhorros() {
        List<Ahorro> ahorros = ahorroService.getAllAhorros();
        List<AhorroDTO> response = ahorros.stream().map(ahorro -> {
            Long cuentaId = ahorro.getCuenta() != null ? ahorro.getCuenta().getId() : null;
            return new AhorroDTO(ahorro.getId(), ahorro.getNombreAhorro(), ahorro.getMontoActual(), ahorro.getMontoFinal(), cuentaId);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/cuenta/{cuentaId}")
    public ResponseEntity<List<AhorroDTO>> getAhorrosByCuentaId(@PathVariable Long cuentaId) {
        List<Ahorro> ahorros = ahorroService.getAhorrosByCuentaId(cuentaId);
        List<AhorroDTO> response = ahorros.stream().map(ahorro -> {
            return new AhorroDTO(ahorro.getId(), ahorro.getNombreAhorro(), ahorro.getMontoActual(), ahorro.getMontoFinal(), cuentaId);
        }).collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AhorroDTO> getAhorroById(@PathVariable Long id) {
        Optional<Ahorro> ahorro = ahorroService.getAhorroById(id);
        return ahorro.map(a -> {
            Long cuentaId = a.getCuenta() != null ? a.getCuenta().getId() : null;
            AhorroDTO response = new AhorroDTO(a.getId(), a.getNombreAhorro(), a.getMontoActual(), a.getMontoFinal(), cuentaId);
            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAhorro(@RequestBody Ahorro ahorro, HttpServletRequest request) {
        String token = jwtUtilityService.extractTokenFromRequest(request);
        if (token != null && jwtUtilityService.isUser(token)) {
            Optional<Cuenta> cuenta = getCuentaFromToken(token);
            if (cuenta.isPresent()) {
                ahorro.setCuenta(cuenta.get());
                Ahorro nuevoAhorro = ahorroService.createAhorro(ahorro);
                return ResponseEntity.ok("Ahorro creado para la cuenta ID " + nuevoAhorro.getCuenta().getId());
            }
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateAhorro(@PathVariable Long id, @RequestBody Ahorro ahorro, HttpServletRequest request) {
        String token = jwtUtilityService.extractTokenFromRequest(request);
        if (token != null && jwtUtilityService.isUser(token)) {
            try {
                Ahorro ahorroActualizado = ahorroService.updateAhorro(id, ahorro);
                return ResponseEntity.ok("Ahorro ID " + ahorroActualizado.getId() + " actualizado en la cuenta ID " + ahorroActualizado.getCuenta().getId());
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAhorro(@PathVariable Long id, HttpServletRequest request) {
        String token = jwtUtilityService.extractTokenFromRequest(request);
        if (token != null && jwtUtilityService.isUser(token)) {
            ahorroService.deleteAhorro(id);
            return ResponseEntity.ok("Ahorro ID " + id + " eliminado");
        } else {
            return ResponseEntity.status(403).build();
        }
    }

}
