package com.isapet.repository;

import com.isapet.entity.Mascota;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MascotaRepository extends JpaRepository<Mascota, Long> {

	List<Mascota> findAllByAfiliadoIdOrderByIdAsc(Long afiliadoId);
}