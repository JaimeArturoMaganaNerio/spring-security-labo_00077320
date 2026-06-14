package com.server.app.dto.finanza;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RendimientoResponseDTO {
    private List<RendimientoItemDTO> detalles;
    private Double totalConsolidado;
}