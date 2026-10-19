package com.isapet.repository;

import com.isapet.entity.Plan;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlanRepository extends JpaRepository<Plan, Long> {

    boolean existsByCodigoIgnoreCase(String codigo);

    @EntityGraph(attributePaths = "versiones")
    List<Plan> findAllByOrderByCodigoAsc();

    @EntityGraph(attributePaths = "versiones")
    Optional<Plan> findWithVersionesById(Long id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Plan p where p.id = :id")
    Optional<Plan> findByIdForUpdate(@Param("id") Long id);
}
