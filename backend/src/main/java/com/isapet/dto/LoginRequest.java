package com.isapet.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "El nombre de usuario es obligatorio.")
        @Size(max = 100, message = "El nombre de usuario no puede superar los 100 caracteres.")
        String username,

        @NotBlank(message = "La contraseña es obligatoria.")
        @Size(max = 72, message = "La contraseña supera el tamaño permitido por BCrypt.")
        String password
) {
}
