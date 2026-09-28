package com.isapet.dto;

public record CrearMascotaResponse(
        Long id,
        Long afiliadoId,
        String nombre,
        String especie,
        String raza,
        Integer edad,
        String color
) {
}