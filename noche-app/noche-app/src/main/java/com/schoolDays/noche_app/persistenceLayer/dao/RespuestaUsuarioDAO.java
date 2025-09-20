package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.dto.RespuestaUsuarioDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.RespuestaUsuarioEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.RespuestaUsuarioMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.RespuestaUsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RespuestaUsuarioDAO {

    private final RespuestaUsuarioRepository respuestaUsuarioRepository;
    private final RespuestaUsuarioMapper respuestaUsuarioMapper;

    public RespuestaUsuarioDTO save(RespuestaUsuarioDTO respuestaUsuarioDTO) {
        RespuestaUsuarioEntity entity = respuestaUsuarioMapper.toEntity(respuestaUsuarioDTO);
        RespuestaUsuarioEntity savedEntity = respuestaUsuarioRepository.save(entity);
        return respuestaUsuarioMapper.toDTO(savedEntity);
    }

    public Optional<RespuestaUsuarioDTO> findById(Integer id) {
        return respuestaUsuarioRepository.findById(id)
                .map(respuestaUsuarioMapper::toDTO);
    }

    public List<RespuestaUsuarioDTO> findAll() {
        List<RespuestaUsuarioEntity> entities = respuestaUsuarioRepository.findAll();
        return respuestaUsuarioMapper.toDTOList(entities);
    }

    public Optional<RespuestaUsuarioDTO> update(Integer id, RespuestaUsuarioDTO respuestaUsuarioDTO) {
        return respuestaUsuarioRepository.findById(id)
                .map(existingEntity -> {
                    respuestaUsuarioMapper.updateEntityFromDTO(respuestaUsuarioDTO, existingEntity);
                    RespuestaUsuarioEntity updatedEntity = respuestaUsuarioRepository.save(existingEntity);
                    return respuestaUsuarioMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (respuestaUsuarioRepository.existsById(id)) {
            respuestaUsuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<RespuestaUsuarioDTO> findByUsuario(Integer idUsuario) {
        List<RespuestaUsuarioEntity> entities = respuestaUsuarioRepository.findByUsuario_IdUsuario(idUsuario);
        return respuestaUsuarioMapper.toDTOList(entities);
    }

    public List<RespuestaUsuarioDTO> findByPregunta(Integer idPregunta) {
        List<RespuestaUsuarioEntity> entities = respuestaUsuarioRepository.findByPregunta_IdPregunta(idPregunta);
        return respuestaUsuarioMapper.toDTOList(entities);
    }

    public List<RespuestaUsuarioDTO> findByEvaluacion(Integer idEvaluacion) {
        List<RespuestaUsuarioEntity> entities = respuestaUsuarioRepository.findByEvaluacion_IdEvaluacion(idEvaluacion);
        return respuestaUsuarioMapper.toDTOList(entities);
    }

    public List<RespuestaUsuarioDTO> findCorrectasByUsuario(Integer idUsuario) {
        List<RespuestaUsuarioEntity> entities = respuestaUsuarioRepository.findByUsuario_IdUsuarioAndCorrectaTrue(idUsuario);
        return respuestaUsuarioMapper.toDTOList(entities);
    }

    public List<RespuestaUsuarioDTO> findIncorrectasByUsuario(Integer idUsuario) {
        List<RespuestaUsuarioEntity> entities = respuestaUsuarioRepository.findByUsuario_IdUsuarioAndCorrectaFalse(idUsuario);
        return respuestaUsuarioMapper.toDTOList(entities);
    }

    public List<RespuestaUsuarioDTO> findRespuestasEnEvaluacion(Integer usuarioId, Integer evaluacionId) {
        List<RespuestaUsuarioEntity> entities = respuestaUsuarioRepository.findRespuestasUsuarioEnEvaluacion(usuarioId, evaluacionId);
        return respuestaUsuarioMapper.toDTOList(entities);
    }

    public long countCorrectasByUsuario(Integer usuarioId) {
        return respuestaUsuarioRepository.countRespuestasCorrectasByUsuario(usuarioId);
    }

    public long countByEvaluacion(Integer idEvaluacion) {
        return respuestaUsuarioRepository.countByEvaluacion_IdEvaluacion(idEvaluacion);
    }

    public long count() {
        return respuestaUsuarioRepository.count();
    }
}
