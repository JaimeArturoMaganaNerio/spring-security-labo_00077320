package com.server.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "inversiones")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Inversion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer cantidad;

    @Column(name = "precio_compra", nullable = false)
    private Double precioCompra;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "portafolio_id", nullable = false)
    private Portafolio portafolio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "activo_id", nullable = false)
    private Activo activo;

    @Column(nullable = false)
    private String estado; // Valores permitidos: "ABIERTA" o "CERRADA"
}