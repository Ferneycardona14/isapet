package com.isapet.repository;

import com.isapet.entity.Afiliado;
import com.isapet.dto.AfiliadoListaResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AfiliadoRepository extends JpaRepository<Afiliado, Long> {

	boolean existsByNumeroIdentificacion(String numeroIdentificacion);

	@Query(value = """
			select new com.isapet.dto.AfiliadoListaResponse(
				a.id, a.numeroIdentificacion, a.nombres, a.apellidos,
				a.celular, a.correo, a.direccion,
				(select count(m) from Mascota m where m.afiliado.id = a.id))
			from Afiliado a
			where :busqueda = ''
				or lower(a.numeroIdentificacion) like lower(concat('%', :busqueda, '%'))
				or lower(a.nombres) like lower(concat('%', :busqueda, '%'))
				or lower(a.apellidos) like lower(concat('%', :busqueda, '%'))
				or lower(a.correo) like lower(concat('%', :busqueda, '%'))
			""",
			countQuery = """
			select count(a) from Afiliado a
			where :busqueda = ''
				or lower(a.numeroIdentificacion) like lower(concat('%', :busqueda, '%'))
				or lower(a.nombres) like lower(concat('%', :busqueda, '%'))
				or lower(a.apellidos) like lower(concat('%', :busqueda, '%'))
				or lower(a.correo) like lower(concat('%', :busqueda, '%'))
			""")
	Page<AfiliadoListaResponse> buscar(@Param("busqueda") String busqueda, Pageable pageable);
}