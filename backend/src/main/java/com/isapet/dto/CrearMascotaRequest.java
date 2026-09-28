package com.isapet.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.isapet.validation.StrictIntegerDeserializer;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CrearMascotaRequest(
        @NotBlank(message = "El nombre de la mascota es obligatorio.")
        @Size(max = 100, message = "El nombre de la mascota no puede superar los 100 caracteres.")
        String nombre,

        @NotBlank(message = "La especie es obligatoria.")
        @Pattern(
                regexp = "Perro|Gato|Ave|Conejo|Hámster|Pez|Tortuga|Otro",
                message = "Selecciona una especie válida.")
        String especie,

        @NotBlank(message = "La raza es obligatoria.")
        @Size(max = 100, message = "La raza no puede superar los 100 caracteres.")
        String raza,

        @NotNull(message = "La edad es obligatoria.")
        @Min(value = 0, message = "La edad no puede ser negativa.")
        @Max(value = 100, message = "La edad no puede superar los 100 años.")
        @JsonDeserialize(using = StrictIntegerDeserializer.class)
        Integer edad,

        @NotBlank(message = "El color es obligatorio.")
        @Size(max = 50, message = "El color no puede superar los 50 caracteres.")
        String color
) {
}