package com.server.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "portafolios")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Portafolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private User usuario;

    @Column(name = "balance_total", nullable = false)
    private Double balanceTotal;

    @Column(name = "riesgo_perfil", nullable = false)
    private String riesgoPerfil;
}