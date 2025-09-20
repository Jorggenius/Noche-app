package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.dto.RespuestaDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.RespuestaEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.RespuestaMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.RespuestaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RespuestaDAO {

    private final RespuestaRepository respuestaRepository;
    private final RespuestaMapper respuestaMapper;

    public RespuestaDTO save(RespuestaDTO respuestaDTO) {
        RespuestaEntity entity = respuestaMapper.toEntity(respuestaDTO);
        RespuestaEntity savedEntity = respuestaRepository.save(entity);
        return respuestaMapper.toDTO(savedEntity);
    }

    public Optional<RespuestaDTO> findById(Integer id) {
        return respuestaRepository.findById(id)
                .map(respuestaMapper::toDTO);
    }

    public List<RespuestaDTO> findAll() {
        List<RespuestaEntity> entities = respuestaRepository.findAll();
        return respuestaMapper.toDTOList(entities);
    }

    public Optional<RespuestaDTO> update(Integer id, RespuestaDTO respuestaDTO) {
        return respuestaRepository.findById(id)
                .map(existingEntity -> {
                    respuestaMapper.updateEntityFromDTO(respuestaDTO, existingEntity);
                    RespuestaEntity updatedEntity = respuestaRepository.save(existingEntity);
                    return respuestaMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (respuestaRepository.existsById(id)) {
            respuestaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<RespuestaDTO> findByPreguntaOrdenadas(Integer idPregunta) {
        List<RespuestaEntity> entities = respuestaRepository.findByPregunta_IdPreguntaOrderByOrdenAsc(idPregunta);
        return respuestaMapper.toDTOList(entities);
    }

    public List<RespuestaDTO> findCorrectasByPregunta(Integer idPregunta) {
        List<RespuestaEntity> entities = respuestaRepository.findByPregunta_IdPreguntaAndEsCorrectaTrue(idPregunta);
        return respuestaMapper.toDTOList(entities);
    }

    public List<RespuestaDTO> findIncorrectasByPregunta(Integer idPregunta) {
        List<RespuestaEntity> entities = respuestaRepository.findByPregunta_IdPreguntaAndEsCorrectaFalse(idPregunta);
        return respuestaMapper.toDTOList(entities);
    }

    public List<RespuestaDTO> findCorrectasByEvaluacion(Integer idEvaluacion) {
        List<RespuestaEntity> entities = respuestaRepository.findRespuestasCorrectasByEvaluacion(idEvaluacion);
        return respuestaMapper.toDTOList(entities);
    }

    public long countCorrectasByPregunta(Integer preguntaId) {
        return respuestaRepository.countRespuestasCorrectasByPregunta(preguntaId);
    }

    public long count() {
        return respuestaRepository.count();
    }
}
