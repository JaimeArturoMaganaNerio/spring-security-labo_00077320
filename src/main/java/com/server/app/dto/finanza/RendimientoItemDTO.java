package com.server.app.dto.finanza;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RendimientoItemDTO {
    private String activoSimbolo;
    private Integer cantidad;
    private Double precioCompra;
    private Double precioMercado;
    private Double gananciaPerdida;
}