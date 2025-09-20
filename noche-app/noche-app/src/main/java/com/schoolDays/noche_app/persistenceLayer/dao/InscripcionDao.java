package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.dto.InscripcionDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.InscripcionEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.InscripcionMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.InscripcionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class InscripcionDAO {

    private final InscripcionRepository inscripcionRepository;
    private final InscripcionMapper inscripcionMapper;

    public InscripcionDTO save(InscripcionDTO inscripcionDTO) {
        InscripcionEntity entity = inscripcionMapper.toEntity(inscripcionDTO);
        InscripcionEntity savedEntity = inscripcionRepository.save(entity);
        return inscripcionMapper.toDTO(savedEntity);
    }

    public Optional<InscripcionDTO> findById(Integer id) {
        return inscripcionRepository.findById(id)
                .map(inscripcionMapper::toDTO);
    }

    public List<InscripcionDTO> findAll() {
        List<InscripcionEntity> entities = inscripcionRepository.findAll();
        return inscripcionMapper.toDTOList(entities);
    }

    public Optional<InscripcionDTO> update(Integer id, InscripcionDTO inscripcionDTO) {
        return inscripcionRepository.findById(id)
                .map(existingEntity -> {
                    inscripcionMapper.updateEntityFromDTO(inscripcionDTO, existingEntity);
                    InscripcionEntity updatedEntity = inscripcionRepository.save(existingEntity);
                    return inscripcionMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (inscripcionRepository.existsById(id)) {
            inscripcionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<InscripcionDTO> findByUsuario(Integer idUsuario) {
        List<InscripcionEntity> entities = inscripcionRepository.findByUsuario_IdUsuario(idUsuario);
        return inscripcionMapper.toDTOList(entities);
    }

    public List<InscripcionDTO> findByCurso(Integer idCurso) {
        List<InscripcionEntity> entities = inscripcionRepository.findByCurso_IdCurso(idCurso);
        return inscripcionMapper.toDTOList(entities);
    }

    public Optional<InscripcionDTO> findByUsuarioAndCurso(Integer idUsuario, Integer idCurso) {
        return inscripcionRepository.findByUsuario_IdUsuarioAndCurso_IdCurso(idUsuario, idCurso)
                .map(inscripcionMapper::toDTO);
    }

    public List<InscripcionDTO> findByEstado(InscripcionEntity.Estado estado) {
        List<InscripcionEntity> entities = inscripcionRepository.findByEstado(estado);
        return inscripcionMapper.toDTOList(entities);
    }

    public List<InscripcionDTO> findEnProgreso() {
        List<InscripcionEntity> entities = inscripcionRepository.findInscripcionesEnProgreso();
        return inscripcionMapper.toDTOList(entities);
    }

    public List<InscripcionDTO> findRecientes() {
        List<InscripcionEntity> entities = inscripcionRepository.findAllByOrderByFechaInscripcionDesc();
        return inscripcionMapper.toDTOList(entities);
    }

    public List<InscripcionDTO> findByDepartamento(String departamento) {
        List<InscripcionEntity> entities = inscripcionRepository.findByUsuarioDepartamento(departamento);
        return inscripcionMapper.toDTOList(entities);
    }

    public boolean existeInscripcion(Integer idUsuario, Integer idCurso) {
        return inscripcionRepository.existsByUsuario_IdUsuarioAndCurso_IdCurso(idUsuario, idCurso);
    }

    public long countByCurso(Integer idCurso) {
        return inscripcionRepository.countByCurso_IdCurso(idCurso);
    }

    public long countCursosCompletadosByUsuario(Integer usuarioId) {
        return inscripcionRepository.countCursosCompletadosByUsuario(usuarioId);
    }

    public long count() {
        return inscripcionRepository.count();
    }
}
