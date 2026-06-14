package com.server.app.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdatePasswordDTO {
    @NotBlank(message = "La contraseña antigua es obligatoria")
    private String oldpassword;

    @NotBlank(message = "La nueva contraseña es obligatoria")
    private String newpassword;

    @NotBlank(message = "Confirmar contraseña es obligatorio")
    private String confirmpassword;
}