package com.schoolDays.noche_app.persistenceLayer.dao;

import com.schoolDays.noche_app.businessLayer.dto.ResultadoDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.ResultadoEntity;
import com.schoolDays.noche_app.persistenceLayer.mapper.ResultadoMapper;
import com.schoolDays.noche_app.persistenceLayer.repository.ResultadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ResultadoDAO {

    private final ResultadoRepository resultadoRepository;
    private final ResultadoMapper resultadoMapper;

    public ResultadoDTO save(ResultadoDTO resultadoDTO) {
        ResultadoEntity entity = resultadoMapper.toEntity(resultadoDTO);
        ResultadoEntity savedEntity = resultadoRepository.save(entity);
        return resultadoMapper.toDTO(savedEntity);
    }

    public Optional<ResultadoDTO> findById(Integer id) {
        return resultadoRepository.findById(id)
                .map(resultadoMapper::toDTO);
    }

    public List<ResultadoDTO> findAll() {
        List<ResultadoEntity> entities = resultadoRepository.findAll();
        return resultadoMapper.toDTOList(entities);
    }

    public Optional<ResultadoDTO> update(Integer id, ResultadoDTO resultadoDTO) {
        return resultadoRepository.findById(id)
                .map(existingEntity -> {
                    resultadoMapper.updateEntityFromDTO(resultadoDTO, existingEntity);
                    ResultadoEntity updatedEntity = resultadoRepository.save(existingEntity);
                    return resultadoMapper.toDTO(updatedEntity);
                });
    }

    public boolean deleteById(Integer id) {
        if (resultadoRepository.existsById(id)) {
            resultadoRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<ResultadoDTO> findByUsuario(Integer idUsuario) {
        List<ResultadoEntity> entities = resultadoRepository.findByUsuario_IdUsuario(idUsuario);
        return resultadoMapper.toDTOList(entities);
    }

    public List<ResultadoDTO> findByEvaluacion(Integer idEvaluacion) {
        List<ResultadoEntity> entities = resultadoRepository.findByEvaluacion_IdEvaluacion(idEvaluacion);
        return resultadoMapper.toDTOList(entities);
    }

    public Optional<ResultadoDTO> findByUsuarioAndEvaluacion(Integer idUsuario, Integer idEvaluacion) {
        return resultadoRepository.findByUsuario_IdUsuarioAndEvaluacion_IdEvaluacion(idUsuario, idEvaluacion)
                .map(resultadoMapper::toDTO);
    }

    public boolean existeResultado(Integer idUsuario, Integer idEvaluacion) {
        return resultadoRepository.existsByUsuario_IdUsuarioAndEvaluacion_IdEvaluacion(idUsuario, idEvaluacion);
    }

    public List<ResultadoDTO> findByRangoPuntaje(BigDecimal puntajeMin, BigDecimal puntajeMax) {
        List<ResultadoEntity> entities = resultadoRepository.findByPuntajeBetween(puntajeMin, puntajeMax);
        return resultadoMapper.toDTOList(entities);
    }

    public List<ResultadoDTO> findByFecha(LocalDate fecha) {
        List<ResultadoEntity> entities = resultadoRepository.findByFechaRealizacion(fecha);
        return resultadoMapper.toDTOList(entities);
    }

    public List<ResultadoDTO> findByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<ResultadoEntity> entities = resultadoRepository.findByFechaRealizacionBetween(fechaInicio, fechaFin);
        return resultadoMapper.toDTOList(entities);
    }

    public List<ResultadoDTO> findMejoresPuntajes() {
        List<ResultadoEntity> entities = resultadoRepository.findAllByOrderByPuntajeDesc();
        return resultadoMapper.toDTOList(entities);
    }

    public List<ResultadoDTO> findMejoresPuntajesByEvaluacion(Integer idEvaluacion) {
        List<ResultadoEntity> entities = resultadoRepository.findByEvaluacion_IdEvaluacionOrderByPuntajeDesc(idEvaluacion);
        return resultadoMapper.toDTOList(entities);
    }

    public List<ResultadoDTO> findAprobatorios(BigDecimal puntajeMinimo) {
        List<ResultadoEntity> entities = resultadoRepository.findResultadosAprobatorios(puntajeMinimo);
        return resultadoMapper.toDTOList(entities);
    }

    public List<ResultadoDTO> findMejoresByCurso(Integer cursoId) {
        List<ResultadoEntity> entities = resultadoRepository.findMejoresResultadosByCurso(cursoId);
        return resultadoMapper.toDTOList(entities);
    }

    public List<ResultadoDTO> findResultadosUsuarioEnCurso(Integer usuarioId, Integer cursoId) {
        List<ResultadoEntity> entities = resultadoRepository.findResultadosUsuarioEnCurso(usuarioId, cursoId);
        return resultadoMapper.toDTOList(entities);
    }

    public BigDecimal getPromedioPuntajeByEvaluacion(Integer evaluacionId) {
        return resultadoRepository.findPromedioPuntajeByEvaluacion(evaluacionId);
    }

    public BigDecimal getPromedioPuntajeByUsuario(Integer usuarioId) {
        return resultadoRepository.findPromedioPuntajeByUsuario(usuarioId);
    }

    public long countByEvaluacion(Integer idEvaluacion) {
        return resultadoRepository.countByEvaluacion_IdEvaluacion(idEvaluacion);
    }

    public long count() {
        return resultadoRepository.count();
    }
}
