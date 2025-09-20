package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.dto.BadgeDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.BadgeEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.BadgeMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.BadgeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class BadgeDAO {

    private final BadgeRepository badgeRepository;
    private final BadgeMapper badgeMapper;

    public BadgeDTO save(BadgeDTO badgeDTO) {
        BadgeEntity entity = badgeMapper.toEntity(badgeDTO);
        BadgeEntity savedEntity = badgeRepository.save(entity);
        return badgeMapper.toDTO(savedEntity);
    }

    public Optional<BadgeDTO> findById(Integer id) {
        return badgeRepository.findById(id)
                .map(badgeMapper::toDTO);
    }

    public List<BadgeDTO> findAll() {
        List<BadgeEntity> entities = badgeRepository.findAll();
        return badgeMapper.toDTOList(entities);
    }

    public Optional<BadgeDTO> update(Integer id, BadgeDTO badgeDTO) {
        return badgeRepository.findById(id)
                .map(existingEntity -> {
                    badgeMapper.updateEntityFromDTO(badgeDTO, existingEntity);
                    BadgeEntity updatedEntity = badgeRepository.save(existingEntity);
                    return badgeMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (badgeRepository.existsById(id)) {
            badgeRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<BadgeDTO> findByNombre(String nombre) {
        List<BadgeEntity> entities = badgeRepository.findByNombreContainingIgnoreCase(nombre);
        return badgeMapper.toDTOList(entities);
    }

    public List<BadgeDTO> findByCriterio(String criterio) {
        List<BadgeEntity> entities = badgeRepository.findByCriterioContainingIgnoreCase(criterio);
        return badgeMapper.toDTOList(entities);
    }

    public List<BadgeDTO> findMasOtorgados() {
        List<BadgeEntity> entities = badgeRepository.findBadgesMasOtorgados();
        return badgeMapper.toDTOList(entities);
    }

    public List<BadgeDTO> findConAsignaciones() {
        List<BadgeEntity> entities = badgeRepository.findBadgesConAsignaciones();
        return badgeMapper.toDTOList(entities);
    }

    public List<BadgeDTO> findSinAsignaciones() {
        List<BadgeEntity> entities = badgeRepository.findBadgesSinAsignaciones();
        return badgeMapper.toDTOList(entities);
    }

    public boolean existsByNombre(String nombre) {
        return badgeRepository.existsByNombre(nombre);
    }

    public long count() {
        return badgeRepository.count();
    }
}
