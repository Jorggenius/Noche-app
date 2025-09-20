package com.schoolDays.noche_app.businessLayer.service;

import com.schoolDays.noche_app.businessLayer.PreguntaDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.PreguntaEntity;

import java.util.List;

public interface PreguntaService {

    /**
     * Crear nueva pregunta
     *
     * VALIDACIONES:
     * - Evaluación debe existir
     * - Usuario debe ser instructor del curso
     * - Orden único en la evaluación
     */
    PreguntaDTO createPregunta(PreguntaDTO preguntaDTO);

    /**
     * Buscar pregunta por ID
     */
    PreguntaDTO getPreguntaById(Integer id);

    /**
     * Buscar todas las preguntas
     */
    List<PreguntaDTO> getAllPreguntas();

    /**
     * Actualizar pregunta
     */
    PreguntaDTO updatePregunta(Integer id, PreguntaDTO preguntaDTO);

    /**
     * Eliminar pregunta
     *
     * REGLAS:
     * - Reordenar preguntas restantes
     * - Solo si no tiene respuestas de usuarios
     */
    void deletePregunta(Integer id);

    /**
     * Buscar preguntas por evaluación ordenadas
     */
    List<PreguntaDTO> getPreguntasByEvaluacionOrdenadas(Integer idEvaluacion);

    /**
     * Buscar preguntas por tipo
     */
    List<PreguntaDTO> getPreguntasByTipo(PreguntaEntity.TipoPregunta tipo);

    /**
     * Buscar preguntas por curso
     */
    List<PreguntaDTO> getPreguntasByCurso(Integer idCurso);

    /**
     * Obtener preguntas de opción múltiple
     */
    List<PreguntaDTO> getPreguntasMultipleChoice(Integer idEvaluacion);

    /**
     * Obtener preguntas abiertas
     */
    List<PreguntaDTO> getPreguntasAbiertas(Integer idEvaluacion);

    /**
     * Cambiar orden de pregunta
     */
    PreguntaDTO cambiarOrdenPregunta(Integer id, Integer nuevoOrden);

    /**
     * Obtener conteo por evaluación
     */
    long getPreguntasCountByEvaluacion(Integer idEvaluacion);

    /**
     * Duplicar pregunta
     */
    PreguntaDTO duplicarPregunta(Integer id, Integer nuevaEvaluacionId);
}