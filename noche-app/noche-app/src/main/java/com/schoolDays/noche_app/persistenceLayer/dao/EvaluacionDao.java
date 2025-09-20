package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.dto.EvaluacionDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.EvaluacionEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.EvaluacionMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.EvaluacionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class EvaluacionDAO {

    private final EvaluacionRepository evaluacionRepository;
    private final EvaluacionMapper evaluacionMapper;

    public EvaluacionDTO save(EvaluacionDTO evaluacionDTO) {
        EvaluacionEntity entity = evaluacionMapper.toEntity(evaluacionDTO);
        EvaluacionEntity savedEntity = evaluacionRepository.save(entity);
        return evaluacionMapper.toDTO(savedEntity);
    }

    public Optional<EvaluacionDTO> findById(Integer id) {
        return evaluacionRepository.findById(id)
                .map(evaluacionMapper::toDTO);
    }

    public List<EvaluacionDTO> findAll() {
        List<EvaluacionEntity> entities = evaluacionRepository.findAll();
        return evaluacionMapper.toDTOList(entities);
    }

    public Optional<EvaluacionDTO> update(Integer id, EvaluacionDTO evaluacionDTO) {
        return evaluacionRepository.findById(id)
                .map(existingEntity -> {
                    evaluacionMapper.updateEntityFromDTO(evaluacionDTO, existingEntity);
                    EvaluacionEntity updatedEntity = evaluacionRepository.save(existingEntity);
                    return evaluacionMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (evaluacionRepository.existsById(id)) {
            evaluacionRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<EvaluacionDTO> findByModulo(Integer idModulo) {
        List<EvaluacionEntity> entities = evaluacionRepository.findByModulo_IdModulo(idModulo);
        return evaluacionMapper.toDTOList(entities);
    }

    public List<EvaluacionDTO> findByTipo(EvaluacionEntity.TipoEvaluacion tipo) {
        List<EvaluacionEntity> entities = evaluacionRepository.findByTipo(tipo);
        return evaluacionMapper.toDTOList(entities);
    }

    public List<EvaluacionDTO> findByCurso(Integer idCurso) {
        List<EvaluacionEntity> entities = evaluacionRepository.findEvaluacionesByCurso(idCurso);
        return evaluacionMapper.toDTOList(entities);
    }

    public List<EvaluacionDTO> findConPreguntas() {
        List<EvaluacionEntity> entities = evaluacionRepository.findEvaluacionesConPreguntas();
        return evaluacionMapper.toDTOList(entities);
    }

    public List<EvaluacionDTO> findByTitulo(String titulo) {
        List<EvaluacionEntity> entities = evaluacionRepository.findByTituloContainingIgnoreCase(titulo);
        return evaluacionMapper.toDTOList(entities);
    }

    public long countByModulo(Integer idModulo) {
        return evaluacionRepository.countByModulo_IdModulo(idModulo);
    }

    public long count() {
        return evaluacionRepository.count();
    }
}

