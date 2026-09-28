package com.isapet.controller;

import com.isapet.dto.CrearMascotaRequest;
import com.isapet.dto.CrearMascotaResponse;
import com.isapet.service.MascotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/mascotas")
public class MascotaController {

    private final MascotaService mascotaService;

    public MascotaController(MascotaService mascotaService) {
        this.mascotaService = mascotaService;
    }

    @Operation(summary = "Actualizar mascota", description = "Actualiza los datos de la mascota sin cambiar su afiliado propietario.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Mascota actualizada"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada")
    })
    @PutMapping("/{mascotaId}")
    public CrearMascotaResponse actualizar(
            @PathVariable Long mascotaId,
            @Valid @RequestBody CrearMascotaRequest request) {
        return mascotaService.actualizar(mascotaId, request);
    }

    @Operation(summary = "Eliminar mascota", description = "Elimina únicamente la mascota indicada.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Mascota eliminada"),
            @ApiResponse(responseCode = "404", description = "Mascota no encontrada"),
            @ApiResponse(responseCode = "500", description = "Error inesperado")
    })
    @DeleteMapping("/{mascotaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long mascotaId) {
        mascotaService.eliminar(mascotaId);
    }
}