package com.server.app.dto.finanza;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class InversionCreateDTO {
    @NotNull(message = "El id del portafolio es obligatorio")
    @Positive(message = "El id del portafolio debe ser positivo")
    private Long portafolioId;

    @NotNull(message = "El id del activo es obligatorio")
    @Positive(message = "El id del activo debe ser positivo")
    private Long activoId;

    @NotNull(message = "La cantidad es obligatoria")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Integer cantidad;

    @NotNull(message = "El precio de compra es obligatorio")
    @Positive(message = "El precio de compra debe ser mayor a 0")
    private Double precioCompra;

    @NotBlank(message = "El tipo de operación es obligatorio (COMPRA o VENTA)")
    private String tipoOperacion; // Para saber si sumar o restar al balance
}