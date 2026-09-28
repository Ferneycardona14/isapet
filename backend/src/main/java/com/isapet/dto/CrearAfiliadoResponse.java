package com.isapet.dto;

public record CrearAfiliadoResponse(
        Long id,
        String numeroIdentificacion,
        String nombres,
        String apellidos,
        String celular,
        String correo,
        String direccion
) {
}