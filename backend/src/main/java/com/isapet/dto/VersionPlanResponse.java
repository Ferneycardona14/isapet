package com.isapet.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record VersionPlanResponse(
        Long id,
        BigDecimal precioMensual,
        Integer maximoMascotas,
        Integer limiteAtenciones,
        boolean atencionesIlimitadas,
        LocalDate vigenteDesde,
        LocalDate vigenteHastaExclusiva
) {
}
