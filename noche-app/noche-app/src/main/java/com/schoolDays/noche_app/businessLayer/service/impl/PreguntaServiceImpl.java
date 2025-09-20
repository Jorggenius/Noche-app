package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.PreguntaDTO;
import com.schoolDays.noche_app.businessLayer.service.EvaluacionService;
import com.schoolDays.noche_app.businessLayer.service.PreguntaService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class PreguntaServiceImpl implements PreguntaService {

    private final PreguntaDAO preguntaDAO;
    private final EvaluacionService evaluacionService;

    @Override
    public PreguntaDTO createPregunta(PreguntaDTO preguntaDTO) {
        log.info("Creando nueva pregunta para evaluación: {}", preguntaDTO.getIdEvaluacion());

        validatePreguntaData(preguntaDTO);

        // Validar que la evaluación existe
        evaluacionService.getEvaluacionById(preguntaDTO.getIdEvaluacion());

        // Si no se especifica orden, asignar el siguiente disponible
        if (preguntaDTO.getOrden() == null) {
            long count = preguntaDAO.countByEvaluacion(preguntaDTO.getIdEvaluacion());
            preguntaDTO.setOrden((int) (count + 1));
        }

        PreguntaDTO createdPregunta = preguntaDAO.save(preguntaDTO);
        log.info("Pregunta creada exitosamente con ID: {}", createdPregunta.getIdPregunta());
        return createdPregunta;
    }

    @Override
    @Transactional(readOnly = true)
    public PreguntaDTO getPreguntaById(Integer id) {
        return preguntaDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Pregunta no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreguntaDTO> getAllPreguntas() {
        return preguntaDAO.findAll();
    }

    @Override
    public PreguntaDTO updatePregunta(Integer id, PreguntaDTO preguntaDTO) {
        log.info("Actualizando pregunta ID: {}", id);

        getPreguntaById(id); // Verificar existencia
        validatePreguntaUpdateData(preguntaDTO);

        return preguntaDAO.update(id, preguntaDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar pregunta"));
    }

    @Override
    public void deletePregunta(Integer id) {
        log.info("Eliminando pregunta ID: {}", id);

        PreguntaDTO pregunta = getPreguntaById(id);

        if (!preguntaDAO.deleteById(id)) {
            throw new RuntimeException("Error al eliminar pregunta con ID: " + id);
        }

        // Aquí podrías implementar reordenamiento automático de preguntas
        log.info("Pregunta eliminada exitosamente ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreguntaDTO> getPreguntasByEvaluacionOrdenadas(Integer idEvaluacion) {
        evaluacionService.getEvaluacionById(idEvaluacion);
        return preguntaDAO.findByEvaluacionOrdenadas(idEvaluacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreguntaDTO> getPreguntasByTipo(PreguntaEntity.TipoPregunta tipo) {
        return preguntaDAO.findByTipo(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreguntaDTO> getPreguntasByCurso(Integer idCurso) {
        return preguntaDAO.findByCurso(idCurso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreguntaDTO> getPreguntasMultipleChoice(Integer idEvaluacion) {
        return preguntaDAO.findMultipleChoiceByEvaluacion(idEvaluacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreguntaDTO> getPreguntasAbiertas(Integer idEvaluacion) {
        return preguntaDAO.findAbiertasByEvaluacion(idEvaluacion);
    }

    @Override
    public PreguntaDTO cambiarOrdenPregunta(Integer id, Integer nuevoOrden) {
        log.info("Cambiando orden de pregunta ID: {} a {}", id, nuevoOrden);

        if (nuevoOrden <= 0) {
            throw new IllegalArgumentException("El orden debe ser mayor a 0");
        }

        PreguntaDTO updateDTO = new PreguntaDTO();
        updateDTO.setOrden(nuevoOrden);

        return updatePregunta(id, updateDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public long getPreguntasCountByEvaluacion(Integer idEvaluacion) {
        evaluacionService.getEvaluacionById(idEvaluacion);
        return preguntaDAO.countByEvaluacion(idEvaluacion);
    }

    @Override
    public PreguntaDTO duplicarPregunta(Integer id, Integer nuevaEvaluacionId) {
        log.info("Duplicando pregunta ID: {} a evaluación {}", id, nuevaEvaluacionId);

        PreguntaDTO originalPregunta = getPreguntaById(id);
        evaluacionService.getEvaluacionById(nuevaEvaluacionId);

        PreguntaDTO nuevaPregunta = new PreguntaDTO();
        nuevaPregunta.setEnunciado(originalPregunta.getEnunciado());
        nuevaPregunta.setTipoPregunta(originalPregunta.getTipoPregunta());
        nuevaPregunta.setIdEvaluacion(nuevaEvaluacionId);

        return createPregunta(nuevaPregunta);
    }

    private void validatePreguntaData(PreguntaDTO preguntaDTO) {
        if (preguntaDTO.getEnunciado() == null || preguntaDTO.getEnunciado().trim().isEmpty()) {
            throw new IllegalArgumentException("El enunciado de la pregunta es obligatorio");
        }

        if (preguntaDTO.getTipoPregunta() == null || preguntaDTO.getTipoPregunta().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de pregunta es obligatorio");
        }

        if (preguntaDTO.getIdEvaluacion() == null) {
            throw new IllegalArgumentException("La evaluación es obligatoria");
        }
    }

    private void validatePreguntaUpdateData(PreguntaDTO preguntaDTO) {
        if (preguntaDTO.getEnunciado() != null && preguntaDTO.getEnunciado().trim().isEmpty()) {
            throw new IllegalArgumentException("El enunciado no puede estar vacío");
        }

        if (preguntaDTO.getOrden() != null && preguntaDTO.getOrden() <= 0) {
            throw new IllegalArgumentException("El orden debe ser mayor a 0");
        }
    }
}