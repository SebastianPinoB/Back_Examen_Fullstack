package com.Back_ev3_Fullstack.controller;

import com.Back_ev3_Fullstack.dto.VentaRequest;
import com.Back_ev3_Fullstack.dto.VentaResponse;
import com.Back_ev3_Fullstack.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    @PostMapping
    public ResponseEntity<VentaResponse> crearVenta(
            @RequestBody VentaRequest request,
            Authentication auth
    ) {
        String correo = auth.getName();
        return ResponseEntity.ok(ventaService.crearVenta(request, correo));
    }

    @GetMapping("/mis-ventas")
    public ResponseEntity<List<VentaResponse>> ventasUsuario(Authentication auth) {
        return ResponseEntity.ok(
                ventaService.listarVentasDeUsuario(auth.getName())
        );
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<VentaResponse>> ventasAdmin() {
        return ResponseEntity.ok(ventaService.listarVentas());
    }
}