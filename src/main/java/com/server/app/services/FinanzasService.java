package com.server.app.services;

import com.server.app.dto.finanza.InversionCreateDTO;
import com.server.app.dto.finanza.PortafolioCreateDTO;
import com.server.app.dto.finanza.RendimientoItemDTO;
import com.server.app.dto.finanza.RendimientoResponseDTO;
import com.server.app.entities.Activo;
import com.server.app.entities.Inversion;
import com.server.app.entities.Portafolio;
import com.server.app.entities.User;
import com.server.app.exceptions.BadRequestException;
import com.server.app.exceptions.NotFoundException;
import com.server.app.repositories.ActivoRepository;
import com.server.app.repositories.InversionRepository;
import com.server.app.repositories.PortafolioRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FinanzasService {

    private final PortafolioRepository portafolioRepository;
    private final ActivoRepository activoRepository;
    private final InversionRepository inversionRepository;

    public Page<Portafolio> getPortafoliosByUser(User user, int page, int size) {
        return portafolioRepository.findByUsuario(user, PageRequest.of(page, size));
    }

    @Transactional
    public Portafolio createPortafolio(User user, PortafolioCreateDTO dto) {
        Portafolio portafolio = Portafolio.builder()
                .nombre(dto.getNombre())
                .riesgoPerfil(dto.getRiesgoPerfil())
                .usuario(user)
                .balanceTotal(0.0) // Balance inicial en 0
                .build();
        return portafolioRepository.save(portafolio);
    }

    public Page<Activo> getAllActivos(int page, int size) {
        return activoRepository.findAll(PageRequest.of(page, size));
    }

    @Transactional
    public Inversion registrarInversion(User user, InversionCreateDTO dto) {
        Portafolio portafolio = portafolioRepository.findById(dto.getPortafolioId())
                .orElseThrow(() -> new NotFoundException("Portafolio no encontrado"));

        // Validamos que el portafolio pertenezca al usuario logueado
        if (portafolio.getUsuario().getId() != user.getId()) {
            throw new BadRequestException("El portafolio no pertenece al usuario autenticado");
        }

        Activo activo = activoRepository.findById(dto.getActivoId())
                .orElseThrow(() -> new NotFoundException("Activo no encontrado"));

        double totalTransaccion = dto.getCantidad() * dto.getPrecioCompra();

        // Actualizamos balance (asumiendo que balance_total representa el dinero invertido)
        if (dto.getTipoOperacion().equalsIgnoreCase("COMPRA")) {
            portafolio.setBalanceTotal(portafolio.getBalanceTotal() + totalTransaccion);
        } else if (dto.getTipoOperacion().equalsIgnoreCase("VENTA")) {
            portafolio.setBalanceTotal(portafolio.getBalanceTotal() - totalTransaccion);
        } else {
            throw new BadRequestException("Tipo de operación inválida. Use COMPRA o VENTA");
        }

        portafolioRepository.save(portafolio);

        Inversion inversion = Inversion.builder()
                .cantidad(dto.getCantidad())
                .precioCompra(dto.getPrecioCompra())
                .fecha(LocalDateTime.now())
                .portafolio(portafolio)
                .activo(activo)
                .estado(dto.getTipoOperacion().equalsIgnoreCase("COMPRA") ? "ABIERTA" : "CERRADA")
                .build();

        return inversionRepository.save(inversion);
    }

    public RendimientoResponseDTO calcularRendimiento(Long portafolioId, User user) {
        Portafolio portafolio = portafolioRepository.findById(portafolioId)
                .orElseThrow(() -> new NotFoundException("Portafolio no encontrado"));

        if (portafolio.getUsuario().getId() != user.getId()) {
            throw new BadRequestException("Acceso denegado a este portafolio");
        }

        List<Inversion> inversionesAbiertas = inversionRepository.findByPortafolioAndEstado(portafolio, "ABIERTA");

        List<RendimientoItemDTO> detalles = new ArrayList<>();
        double totalConsolidado = 0.0;

        for (Inversion inv : inversionesAbiertas) {
            double valorActualMercado = inv.getCantidad() * inv.getActivo().getPrecioMercado();
            double costoCompra = inv.getCantidad() * inv.getPrecioCompra();
            double gananciaPerdida = valorActualMercado - costoCompra;

            RendimientoItemDTO item = new RendimientoItemDTO(
                    inv.getActivo().getSimbolo(),
                    inv.getCantidad(),
                    inv.getPrecioCompra(),
                    inv.getActivo().getPrecioMercado(),
                    gananciaPerdida
            );
            detalles.add(item);
            totalConsolidado += valorActualMercado;
        }

        return new RendimientoResponseDTO(detalles, totalConsolidado);
    }
}