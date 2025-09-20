package com.schoolDays.noche_app.businessLayer.service;

import com.schoolDays.noche_app.businessLayer.EvaluacionDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.EvaluacionEntity;

import java.util.List;

public interface EvaluacionService {

    /**
     * Crear nueva evaluación
     *
     * VALIDACIONES:
     * - Módulo debe existir
     * - Usuario debe ser instructor del curso
     * - Puntaje máximo válido
     */
    EvaluacionDTO createEvaluacion(EvaluacionDTO evaluacionDTO);

    /**
     * Buscar evaluación por ID
     */
    EvaluacionDTO getEvaluacionById(Integer id);

    /**
     * Buscar todas las evaluaciones
     */
    List<EvaluacionDTO> getAllEvaluaciones();

    /**
     * Actualizar evaluación
     */
    EvaluacionDTO updateEvaluacion(Integer id, EvaluacionDTO evaluacionDTO);

    /**
     * Eliminar evaluación
     */
    void deleteEvaluacion(Integer id);

    /**
     * Buscar evaluaciones por módulo
     */
    List<EvaluacionDTO> getEvaluacionesByModulo(Integer idModulo);

    /**
     * Buscar evaluaciones por tipo
     */
    List<EvaluacionDTO> getEvaluacionesByTipo(EvaluacionEntity.TipoEvaluacion tipo);

    /**
     * Buscar evaluaciones por curso
     */
    List<EvaluacionDTO> getEvaluacionesByCurso(Integer idCurso);

    /**
     * Obtener evaluaciones con preguntas
     */
    List<EvaluacionDTO> getEvaluacionesConPreguntas();

    /**
     * Iniciar evaluación para usuario
     */
    void iniciarEvaluacion(Integer idEvaluacion, Integer idUsuario);

    /**
     * Finalizar evaluación y calcular resultado
     */
    void finalizarEvaluacion(Integer idEvaluacion, Integer idUsuario);

    /**
     * Verificar si usuario puede tomar evaluación
     */
    boolean puedeTomarEvaluacion(Integer idEvaluacion, Integer idUsuario);

    /**
     * Obtener evaluaciones pendientes de usuario
     */
    List<EvaluacionDTO> getEvaluacionesPendientes(Integer idUsuario);
}

