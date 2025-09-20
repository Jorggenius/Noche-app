package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.dto.ModuloDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.ModuloEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.ModuloMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.ModuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ModuloDAO {

    private final ModuloRepository moduloRepository;
    private final ModuloMapper moduloMapper;

    public ModuloDTO save(ModuloDTO moduloDTO) {
        ModuloEntity entity = moduloMapper.toEntity(moduloDTO);
        ModuloEntity savedEntity = moduloRepository.save(entity);
        return moduloMapper.toDTO(savedEntity);
    }

    public Optional<ModuloDTO> findById(Integer id) {
        return moduloRepository.findById(id)
                .map(moduloMapper::toDTO);
    }

    public List<ModuloDTO> findAll() {
        List<ModuloEntity> entities = moduloRepository.findAll();
        return moduloMapper.toDTOList(entities);
    }

    public Optional<ModuloDTO> update(Integer id, ModuloDTO moduloDTO) {
        return moduloRepository.findById(id)
                .map(existingEntity -> {
                    moduloMapper.updateEntityFromDTO(moduloDTO, existingEntity);
                    ModuloEntity updatedEntity = moduloRepository.save(existingEntity);
                    return moduloMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (moduloRepository.existsById(id)) {
            moduloRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ModuloDTO> findByCursoOrdenados(Integer idCurso) {
        List<ModuloEntity> entities = moduloRepository.findByCurso_IdCursoOrderByOrdenAsc(idCurso);
        return moduloMapper.toDTOList(entities);
    }

    public List<ModuloDTO> findByTipo(ModuloEntity.TipoModulo tipo) {
        List<ModuloEntity> entities = moduloRepository.findByTipo(tipo);
        return moduloMapper.toDTOList(entities);
    }

    public Optional<ModuloDTO> findPrimerModulo(Integer idCurso) {
        return moduloRepository.findTopByCurso_IdCursoOrderByOrdenAsc(idCurso)
                .map(moduloMapper::toDTO);
    }

    public Optional<ModuloDTO> findUltimoModulo(Integer idCurso) {
        return moduloRepository.findTopByCurso_IdCursoOrderByOrdenDesc(idCurso)
                .map(moduloMapper::toDTO);
    }

    public List<ModuloDTO> findModulosConEvaluaciones() {
        List<ModuloEntity> entities = moduloRepository.findModulosConEvaluaciones();
        return moduloMapper.toDTOList(entities);
    }

    public List<ModuloDTO> findQuizzesByCurso(Integer idCurso) {
        List<ModuloEntity> entities = moduloRepository.findQuizzesByCurso(idCurso);
        return moduloMapper.toDTOList(entities);
    }

    public boolean existeOrdenEnCurso(Integer idCurso, Integer orden) {
        return moduloRepository.existsByCurso_IdCursoAndOrden(idCurso, orden);
    }

    public long countByCurso(Integer idCurso) {
        return moduloRepository.countByCurso_IdCurso(idCurso);
    }

    public long count() {
        return moduloRepository.count();
    }
}

