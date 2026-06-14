package com.server.app.dto.finanza;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PortafolioCreateDTO {
    @NotBlank(message = "El nombre del portafolio es obligatorio")
    private String nombre;

    @NotBlank(message = "El riesgo de perfil es obligatorio")
    private String riesgoPerfil;
}