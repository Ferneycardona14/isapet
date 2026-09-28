package com.isapet.dto;

public record AfiliadoDetalleResponse(
        Long id,
        String numeroIdentificacion,
        String nombres,
        String apellidos,
        String celular,
        String correo,
        String direccion
) {
}