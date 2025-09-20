package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.dto.PreguntaDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.PreguntaEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.PreguntaMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.PreguntaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PreguntaDAO {

    private final PreguntaRepository preguntaRepository;
    private final PreguntaMapper preguntaMapper;

    public PreguntaDTO save(PreguntaDTO preguntaDTO) {
        PreguntaEntity entity = preguntaMapper.toEntity(preguntaDTO);
        PreguntaEntity savedEntity = preguntaRepository.save(entity);
        return preguntaMapper.toDTO(savedEntity);
    }

    public Optional<PreguntaDTO> findById(Integer id) {
        return preguntaRepository.findById(id)
                .map(preguntaMapper::toDTO);
    }

    public List<PreguntaDTO> findAll() {
        List<PreguntaEntity> entities = preguntaRepository.findAll();
        return preguntaMapper.toDTOList(entities);
    }

    public Optional<PreguntaDTO> update(Integer id, PreguntaDTO preguntaDTO) {
        return preguntaRepository.findById(id)
                .map(existingEntity -> {
                    preguntaMapper.updateEntityFromDTO(preguntaDTO, existingEntity);
                    PreguntaEntity updatedEntity = preguntaRepository.save(existingEntity);
                    return preguntaMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (preguntaRepository.existsById(id)) {
            preguntaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<PreguntaDTO> findByEvaluacionOrdenadas(Integer idEvaluacion) {
        List<PreguntaEntity> entities = preguntaRepository.findByEvaluacion_IdEvaluacionOrderByOrdenAsc(idEvaluacion);
        return preguntaMapper.toDTOList(entities);
    }

    public List<PreguntaDTO> findByTipo(PreguntaEntity.TipoPregunta tipo) {
        List<PreguntaEntity> entities = preguntaRepository.findByTipoPregunta(tipo);
        return preguntaMapper.toDTOList(entities);
    }

    public List<PreguntaDTO> findByCurso(Integer idCurso) {
        List<PreguntaEntity> entities = preguntaRepository.findPreguntasByCurso(idCurso);
        return preguntaMapper.toDTOList(entities);
    }

    public List<PreguntaDTO> findConOpciones() {
        List<PreguntaEntity> entities = preguntaRepository.findPreguntasConOpciones();
        return preguntaMapper.toDTOList(entities);
    }

    public List<PreguntaDTO> findMultipleChoiceByEvaluacion(Integer idEvaluacion) {
        List<PreguntaEntity> entities = preguntaRepository.findPreguntasMultipleChoiceByEvaluacion(idEvaluacion);
        return preguntaMapper.toDTOList(entities);
    }

    public List<PreguntaDTO> findAbiertasByEvaluacion(Integer idEvaluacion) {
        List<PreguntaEntity> entities = preguntaRepository.findPreguntasAbiertasByEvaluacion(idEvaluacion);
        return preguntaMapper.toDTOList(entities);
    }

    public long countByEvaluacion(Integer idEvaluacion) {
        return preguntaRepository.countByEvaluacion_IdEvaluacion(idEvaluacion);
    }

    public long count() {
        return preguntaRepository.count();
    }
}
