package com.isapet.dto;

public record AfiliadoListaResponse(
        Long id,
        String numeroIdentificacion,
        String nombres,
        String apellidos,
        String celular,
        String correo,
        String direccion,
        long cantidadMascotas
) {
}