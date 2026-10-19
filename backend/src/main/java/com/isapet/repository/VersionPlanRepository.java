package com.isapet.repository;

import com.isapet.entity.VersionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface VersionPlanRepository extends JpaRepository<VersionPlan, Long> {

    List<VersionPlan> findAllByPlanIdOrderByVigenteDesdeAsc(Long planId);

    Optional<VersionPlan> findFirstByPlanIdOrderByVigenteDesdeDesc(Long planId);

    Optional<VersionPlan> findFirstByPlanIdAndVigenteDesdeLessThanEqualOrderByVigenteDesdeDesc(
            Long planId,
            LocalDate fecha);

    Optional<VersionPlan> findFirstByPlanIdAndVigenteDesdeGreaterThanOrderByVigenteDesdeAsc(
            Long planId,
            LocalDate fecha);
}
