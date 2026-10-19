package com.isapet.service;

import com.isapet.dto.CrearPlanRequest;
import com.isapet.dto.CrearVersionPlanRequest;
import com.isapet.dto.PlanResponse;
import com.isapet.dto.VersionPlanResponse;
import com.isapet.entity.Plan;
import com.isapet.entity.VersionPlan;
import com.isapet.exception.PlanCodeAlreadyExistsException;
import com.isapet.exception.PlanInactiveException;
import com.isapet.exception.PlanNotFoundException;
import com.isapet.exception.VersionPlanConflictException;
import com.isapet.repository.PlanRepository;
import com.isapet.repository.VersionPlanRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Service
@Validated
public class PlanService {

    private final PlanRepository planRepository;
    private final VersionPlanRepository versionPlanRepository;

    public PlanService(PlanRepository planRepository, VersionPlanRepository versionPlanRepository) {
        this.planRepository = planRepository;
        this.versionPlanRepository = versionPlanRepository;
    }

    @Transactional(readOnly = true)
    public List<PlanResponse> listarPlanes() {
        return planRepository.findAllByOrderByCodigoAsc().stream()
                .map(this::response)
                .toList();
    }

    @Transactional(readOnly = true)
    public PlanResponse obtenerPlan(Long planId) {
        Plan plan = planRepository.findWithVersionesById(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
        return response(plan);
    }

    @Transactional(readOnly = true)
    public Optional<VersionPlanResponse> obtenerVersionAplicable(
            @NotNull @Positive Long planId,
            @NotNull LocalDate fecha) {
        if (!planRepository.existsById(planId)) {
            throw new PlanNotFoundException(planId);
        }

        return versionPlanRepository
                .findFirstByPlanIdAndVigenteDesdeLessThanEqualOrderByVigenteDesdeDesc(planId, fecha)
                .map(version -> {
                    LocalDate vigenteHastaExclusiva = versionPlanRepository
                            .findFirstByPlanIdAndVigenteDesdeGreaterThanOrderByVigenteDesdeAsc(
                                    planId,
                                    version.getVigenteDesde())
                            .map(VersionPlan::getVigenteDesde)
                            .orElse(null);
                    return versionResponse(version, vigenteHastaExclusiva);
                });
    }

    @Transactional
    public PlanResponse crearPlan(@Valid CrearPlanRequest request) {
        String codigo = request.codigo().trim().toUpperCase(Locale.ROOT);
        if (planRepository.existsByCodigoIgnoreCase(codigo)) {
            throw new PlanCodeAlreadyExistsException(codigo);
        }

        Plan plan = planRepository.save(new Plan(codigo, request.nombre().trim()));
        VersionPlan version = new VersionPlan(
                plan,
                request.precioMensual(),
                request.maximoMascotas(),
                request.limiteAtenciones(),
                request.vigenteDesde());
        plan.agregarVersion(version);
        versionPlanRepository.saveAndFlush(version);
        return response(plan);
    }

    @Transactional
    public PlanResponse publicarVersion(Long planId, @Valid CrearVersionPlanRequest request) {
        Plan plan = planRepository.findByIdForUpdate(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
        if (!plan.isActivo()) {
            throw new PlanInactiveException(planId);
        }

        VersionPlan ultimaVersion = versionPlanRepository.findFirstByPlanIdOrderByVigenteDesdeDesc(planId)
                .orElseThrow(() -> new IllegalStateException(
                        "El plan no tiene una versión comercial inicial."));
        if (!request.vigenteDesde().isAfter(ultimaVersion.getVigenteDesde())) {
            throw new VersionPlanConflictException(request.vigenteDesde(), ultimaVersion.getVigenteDesde());
        }
        if (request.vigenteDesde().isBefore(LocalDate.now())) {
            throw new VersionPlanConflictException(
                    "La fecha de vigencia de una nueva versión no puede ser anterior a la fecha actual.");
        }

        VersionPlan version = new VersionPlan(
                plan,
                request.precioMensual(),
                request.maximoMascotas(),
                request.limiteAtenciones(),
                request.vigenteDesde());
        plan.agregarVersion(version);
        versionPlanRepository.saveAndFlush(version);
        return response(plan);
    }

    @Transactional
    public PlanResponse desactivarPlan(Long planId) {
        Plan plan = planRepository.findByIdForUpdate(planId)
                .orElseThrow(() -> new PlanNotFoundException(planId));
        plan.desactivar();
        return response(plan);
    }

    private PlanResponse response(Plan plan) {
        List<VersionPlan> versiones = new ArrayList<>(plan.getVersiones());
        versiones.sort((first, second) -> first.getVigenteDesde().compareTo(second.getVigenteDesde()));

        List<VersionPlanResponse> respuestas = new ArrayList<>();
        for (int index = 0; index < versiones.size(); index++) {
            VersionPlan version = versiones.get(index);
            var vigenteHastaExclusiva = index + 1 < versiones.size()
                    ? versiones.get(index + 1).getVigenteDesde()
                    : null;
            respuestas.add(versionResponse(version, vigenteHastaExclusiva));
        }

        return new PlanResponse(
                plan.getId(),
                plan.getCodigo(),
                plan.getNombre(),
                plan.isActivo(),
                List.copyOf(respuestas));
    }

    private VersionPlanResponse versionResponse(VersionPlan version, LocalDate vigenteHastaExclusiva) {
        Integer limiteAtenciones = version.getLimiteAtenciones();
        return new VersionPlanResponse(
                version.getId(),
                version.getPrecioMensual(),
                version.getMaximoMascotas(),
                limiteAtenciones,
                limiteAtenciones == null,
                version.getVigenteDesde(),
                vigenteHastaExclusiva);
    }
}
