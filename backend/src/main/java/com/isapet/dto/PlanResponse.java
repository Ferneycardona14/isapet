package com.isapet.dto;

import java.util.List;

public record PlanResponse(
        Long id,
        String codigo,
        String nombre,
        boolean activo,
        List<VersionPlanResponse> versiones
) {
}
