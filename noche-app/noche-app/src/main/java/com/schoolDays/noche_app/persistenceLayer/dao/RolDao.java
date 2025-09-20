package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.dto.RolDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.RolEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.RolMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.RolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RolDAO {

    private final RolRepository rolRepository;
    private final RolMapper rolMapper;

    public RolDTO save(RolDTO rolDTO) {
        RolEntity entity = rolMapper.toEntity(rolDTO);
        RolEntity savedEntity = rolRepository.save(entity);
        return rolMapper.toDTO(savedEntity);
    }

    public Optional<RolDTO> findById(Integer id) {
        return rolRepository.findById(id)
                .map(rolMapper::toDTO);
    }

    public List<RolDTO> findAll() {
        List<RolEntity> entities = rolRepository.findAll();
        return rolMapper.toDTOList(entities);
    }

    public Optional<RolDTO> update(Integer id, RolDTO rolDTO) {
        return rolRepository.findById(id)
                .map(existingEntity -> {
                    rolMapper.updateEntityFromDTO(rolDTO, existingEntity);
                    RolEntity updatedEntity = rolRepository.save(existingEntity);
                    return rolMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (rolRepository.existsById(id)) {
            rolRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Optional<RolDTO> findByNombreRol(String nombreRol) {
        return rolRepository.findByNombreRol(nombreRol)
                .map(rolMapper::toDTO);
    }

    public boolean existsByNombreRol(String nombreRol) {
        return rolRepository.existsByNombreRol(nombreRol);
    }

    public long count() {
        return rolRepository.count();
    }
}