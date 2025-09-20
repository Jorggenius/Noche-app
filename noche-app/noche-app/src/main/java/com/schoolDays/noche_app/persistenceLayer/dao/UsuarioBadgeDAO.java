package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.dto.UsuarioBadgeDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.UsuarioBadgeEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.UsuarioBadgeMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.UsuarioBadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UsuarioBadgeDAO {

    private final UsuarioBadgeRepository usuarioBadgeRepository;
    private final UsuarioBadgeMapper usuarioBadgeMapper;

    public UsuarioBadgeDTO save(UsuarioBadgeDTO usuarioBadgeDTO) {
        UsuarioBadgeEntity entity = usuarioBadgeMapper.toEntity(usuarioBadgeDTO);
        UsuarioBadgeEntity savedEntity = usuarioBadgeRepository.save(entity);
        return usuarioBadgeMapper.toDTO(savedEntity);
    }

    public Optional<UsuarioBadgeDTO> findById(Integer id) {
        return usuarioBadgeRepository.findById(id)
                .map(usuarioBadgeMapper::toDTO);
    }

    public List<UsuarioBadgeDTO> findAll() {
        List<UsuarioBadgeEntity> entities = usuarioBadgeRepository.findAll();
        return usuarioBadgeMapper.toDTOList(entities);
    }

    public Optional<UsuarioBadgeDTO> update(Integer id, UsuarioBadgeDTO usuarioBadgeDTO) {
        return usuarioBadgeRepository.findById(id)
                .map(existingEntity -> {
                    usuarioBadgeMapper.updateEntityFromDTO(usuarioBadgeDTO, existingEntity);
                    UsuarioBadgeEntity updatedEntity = usuarioBadgeRepository.save(existingEntity);
                    return usuarioBadgeMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (usuarioBadgeRepository.existsById(id)) {
            usuarioBadgeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<UsuarioBadgeDTO> findByUsuario(Integer idUsuario) {
        List<UsuarioBadgeEntity> entities = usuarioBadgeRepository.findByUsuario_IdUsuario(idUsuario);
        return usuarioBadgeMapper.toDTOList(entities);
    }

    public List<UsuarioBadgeDTO> findByBadge(Integer idBadge) {
        List<UsuarioBadgeEntity> entities = usuarioBadgeRepository.findByBadge_IdBadge(idBadge);
        return usuarioBadgeMapper.toDTOList(entities);
    }

    public List<UsuarioBadgeDTO> findRecientes() {
        List<UsuarioBadgeEntity> entities = usuarioBadgeRepository.findAllByOrderByFechaOtorgadoDesc();
        return usuarioBadgeMapper.toDTOList(entities);
    }

    public List<UsuarioBadgeDTO> findByDepartamento(String departamento) {
        List<UsuarioBadgeEntity> entities = usuarioBadgeRepository.findByUsuarioDepartamento(departamento);
        return usuarioBadgeMapper.toDTOList(entities);
    }

    public boolean existeAsignacion(Integer idUsuario, Integer idBadge) {
        return usuarioBadgeRepository.existsByUsuario_IdUsuarioAndBadge_IdBadge(idUsuario, idBadge);
    }

    public long countByUsuario(Integer idUsuario) {
        return usuarioBadgeRepository.countByUsuario_IdUsuario(idUsuario);
    }

    public long countByBadge(Integer idBadge) {
        return usuarioBadgeRepository.countByBadge_IdBadge(idBadge);
    }

    public long count() {
        return usuarioBadgeRepository.count();
    }
}