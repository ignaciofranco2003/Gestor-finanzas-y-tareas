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
import com.proyectodesarrollo.gestorfinanzasytareas.controllers.frontof.DTOS.GastoDTO;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Cuenta;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.Gasto;
import com.proyectodesarrollo.gestorfinanzasytareas.entities.User;
import com.proyectodesarrollo.gestorfinanzasytareas.services.CuentaService;
import com.proyectodesarrollo.gestorfinanzasytareas.services.GastoService;
import com.proyectodesarrollo.gestorfinanzasytareas.services.UserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/gastos")
public class GastoController {

    @Autowired
    private GastoService gastoService;

    @Autowired
    private CuentaService cuentaService;

    @Autowired
    private UserService userService;

    @Autowired
    private JWTUtilityServiceImpl jwtUtilityService;
    
    private Optional<Cuenta> getCuentaFromToken(String token) {
        try {
            JWTClaimsSet claims = jwtUtilityService.parseJWT(token);
            String useremail = claims.getStringClaim("email");
            System.out.println(useremail);
            User aux = userService.getUserByEmail(useremail);
            System.out.println(aux.getUsername());
            return cuentaService.findByUserId(aux.getUserid());
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<GastoDTO>> getAllGastos() {
        List<Gasto> gastos = gastoService.getAllGastos();
        List<GastoDTO> response = gastos.stream().map(gasto -> {
            Long cuentaId = gasto.getCuenta() != null ? gasto.getCuenta().getId() : null;
            Long categoriaId = gasto.getCategoria() != null ? gasto.getCategoria().getId() : null;
            return new GastoDTO(gasto.getId(), gasto.getMonto(), gasto.getFecha(), cuentaId, categoriaId);
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<GastoDTO> getGastoById(@PathVariable Long id) {
        Optional<Gasto> gasto = gastoService.getGastoById(id);
        return gasto.map(g -> {
            Long cuentaId = g.getCuenta() != null ? g.getCuenta().getId() : null;
            Long categoriaId = g.getCategoria() != null ? g.getCategoria().getId() : null;
            GastoDTO response = new GastoDTO(g.getId(), g.getMonto(), g.getFecha(), cuentaId, categoriaId);
            return ResponseEntity.ok(response);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<String> createGasto(@RequestBody Gasto gasto, HttpServletRequest request) {
        String token = jwtUtilityService.extractTokenFromRequest(request);
        if (token != null && jwtUtilityService.isUser(token)) {
            Optional<Cuenta> cuenta = getCuentaFromToken(token);
            if (cuenta.isPresent()) {
                gasto.setCuenta(cuenta.get());
                Gasto nuevogasto = gastoService.createGasto(gasto);
                return ResponseEntity.ok("Gasto agregado a la cuenta ID " + nuevogasto.getCuenta().getId());
            }
            return ResponseEntity.status(404).build();
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGasto(@PathVariable Long id, @RequestBody Gasto gasto, HttpServletRequest request) {
        String token = jwtUtilityService.extractTokenFromRequest(request);
        if (token != null && jwtUtilityService.isUser(token)) {
            try {
                Gasto gastoActualizado = gastoService.updateGasto(id, gasto);
                return ResponseEntity.ok("Gasto ID " + gastoActualizado.getId() + " actualizado en la cuenta ID " + gastoActualizado.getCuenta().getId());
            } catch (RuntimeException e) {
                return ResponseEntity.notFound().build();
            }
        } else {
            return ResponseEntity.status(403).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGasto(@PathVariable Long id, HttpServletRequest request) {
        String token = jwtUtilityService.extractTokenFromRequest(request);
        if (token != null && jwtUtilityService.isUser(token)) {
            gastoService.deleteGasto(id);
            return ResponseEntity.ok("Gasto ID " + id + " eliminado");
        } else {
            return ResponseEntity.status(403).build();
        }
    }
}
