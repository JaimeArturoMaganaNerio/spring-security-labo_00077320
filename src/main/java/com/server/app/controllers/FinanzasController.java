package com.server.app.controllers;

import com.server.app.dto.finanza.InversionCreateDTO;
import com.server.app.dto.finanza.PortafolioCreateDTO;
import com.server.app.dto.finanza.RendimientoResponseDTO;
import com.server.app.dto.response.Pagination;
import com.server.app.dto.response.PaginationMeta;
import com.server.app.entities.Activo;
import com.server.app.entities.Inversion;
import com.server.app.entities.Portafolio;
import com.server.app.entities.User;
import com.server.app.services.FinanzasService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/finanzas")
public class FinanzasController {

    private final FinanzasService finanzasService;

    public FinanzasController(FinanzasService finanzasService) {
        this.finanzasService = finanzasService;
    }

    @GetMapping("/portafolios")
    public ResponseEntity<Pagination<Portafolio>> getPortafolios(
            @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Portafolio> p = finanzasService.getPortafoliosByUser(user, page, size);

        return ResponseEntity.ok(new Pagination<>(
                p.getContent(),
                new PaginationMeta(p.getNumber(), p.getSize(), p.getTotalPages(), p.getTotalElements())
        ));
    }

    @PostMapping("/portafolios")
    public ResponseEntity<Portafolio> createPortafolio(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PortafolioCreateDTO dto) {
        return ResponseEntity.ok(finanzasService.createPortafolio(user, dto));
    }

    @GetMapping("/activos")
    public ResponseEntity<Pagination<Activo>> getActivos(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<Activo> p = finanzasService.getAllActivos(page, size);

        return ResponseEntity.ok(new Pagination<>(
                p.getContent(),
                new PaginationMeta(p.getNumber(), p.getSize(), p.getTotalPages(), p.getTotalElements())
        ));
    }

    @PostMapping("/inversiones")
    public ResponseEntity<Inversion> registrarInversion(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody InversionCreateDTO dto) {
        return ResponseEntity.ok(finanzasService.registrarInversion(user, dto));
    }

    @GetMapping("/portafolios/{id}/rendimiento")
    public ResponseEntity<RendimientoResponseDTO> getRendimiento(
            @AuthenticationPrincipal User user,
            @PathVariable Long id) {
        return ResponseEntity.ok(finanzasService.calcularRendimiento(id, user));
    }
}