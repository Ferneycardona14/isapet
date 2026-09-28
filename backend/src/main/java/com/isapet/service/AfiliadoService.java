package com.isapet.service;

import com.isapet.dto.CrearAfiliadoRequest;
import com.isapet.dto.CrearAfiliadoResponse;
import com.isapet.dto.ActualizarAfiliadoRequest;
import com.isapet.dto.AfiliadoDetalleResponse;
import com.isapet.dto.AfiliadoListaResponse;
import com.isapet.entity.Afiliado;
import com.isapet.exception.AfiliadoNotFoundException;
import com.isapet.exception.DuplicateNumeroIdentificacionException;
import com.isapet.repository.AfiliadoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AfiliadoService {

    private final AfiliadoRepository afiliadoRepository;

    public AfiliadoService(AfiliadoRepository afiliadoRepository) {
        this.afiliadoRepository = afiliadoRepository;
    }

    @Transactional(readOnly = true)
    public Page<AfiliadoListaResponse> listar(String busqueda, Pageable pageable) {
        String criterio = busqueda == null ? "" : busqueda.trim();
        return afiliadoRepository.buscar(criterio, pageable);
    }

    @Transactional(readOnly = true)
    public AfiliadoDetalleResponse obtener(Long afiliadoId) {
        Afiliado afiliado = afiliadoRepository.findById(afiliadoId)
                .orElseThrow(() -> new AfiliadoNotFoundException(afiliadoId));
        return detalle(afiliado);
    }

    @Transactional
    public AfiliadoDetalleResponse actualizar(Long afiliadoId, ActualizarAfiliadoRequest request) {
        Afiliado afiliado = afiliadoRepository.findById(afiliadoId)
                .orElseThrow(() -> new AfiliadoNotFoundException(afiliadoId));
        afiliado.actualizarDatosContacto(
                request.nombres(), request.apellidos(), request.celular(), request.correo(), request.direccion());
        return detalle(afiliado);
    }

    @Transactional
    public CrearAfiliadoResponse crear(CrearAfiliadoRequest request) {
        if (afiliadoRepository.existsByNumeroIdentificacion(request.numeroIdentificacion())) {
            throw new DuplicateNumeroIdentificacionException(request.numeroIdentificacion());
        }

        Afiliado afiliado = afiliadoRepository.save(new Afiliado(
                request.numeroIdentificacion(),
                request.nombres(),
            request.apellidos(),
            request.celular(),
            request.correo(),
            request.direccion()));

        return new CrearAfiliadoResponse(
            afiliado.getId(),
            afiliado.getNumeroIdentificacion(),
            afiliado.getNombres(),
            afiliado.getApellidos(),
            afiliado.getCelular(),
            afiliado.getCorreo(),
            afiliado.getDireccion());
    }

    private AfiliadoDetalleResponse detalle(Afiliado afiliado) {
        return new AfiliadoDetalleResponse(
                afiliado.getId(), afiliado.getNumeroIdentificacion(), afiliado.getNombres(),
                afiliado.getApellidos(), afiliado.getCelular(), afiliado.getCorreo(), afiliado.getDireccion());
    }
}