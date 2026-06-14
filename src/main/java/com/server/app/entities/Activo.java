package com.server.app.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "activos")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String simbolo;

    @Column(nullable = false)
    private String mercado;

    @Column(name = "precio_mercado", nullable = false)
    private Double precioMercado;

    @Column(nullable = false)
    private String sector;
}