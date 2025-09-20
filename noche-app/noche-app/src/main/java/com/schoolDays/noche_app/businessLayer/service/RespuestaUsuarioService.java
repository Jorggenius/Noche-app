package com.schoolDays.noche_app.businessLayer.service;
import com.schoolDays.noche_app.businessLayer.RespuestaUsuarioDTO;

import java.math.BigDecimal;
import java.util.List;

public interface RespuestaUsuarioService {

    /**
     * Registrar respuesta de usuario
     *
     * VALIDACIONES:
     * - Usuario debe estar inscrito en el curso
     * - Evaluación debe estar activa
     * - No responder dos veces la misma pregunta
     */
    RespuestaUsuarioDTO registrarRespuesta(RespuestaUsuarioDTO respuestaUsuarioDTO);

    /**
     * Buscar respuesta por ID
     */
    RespuestaUsuarioDTO getRespuestaUsuarioById(Integer id);

    /**
     * Buscar todas las respuestas de usuario
     */
    List<RespuestaUsuarioDTO> getAllRespuestasUsuario();

    /**
     * Actualizar respuesta (solo para preguntas abiertas)
     */
    RespuestaUsuarioDTO updateRespuestaUsuario(Integer id, RespuestaUsuarioDTO respuestaUsuarioDTO);

    /**
     * Buscar respuestas por usuario
     */
    List<RespuestaUsuarioDTO> getRespuestasByUsuario(Integer idUsuario);

    /**
     * Buscar respuestas por evaluación
     */
    List<RespuestaUsuarioDTO> getRespuestasByEvaluacion(Integer idEvaluacion);

    /**
     * Buscar respuestas de usuario en evaluación específica
     */
    List<RespuestaUsuarioDTO> getRespuestasUsuarioEnEvaluacion(Integer usuarioId, Integer evaluacionId);

    /**
     * Calificar respuesta automáticamente (MCQ)
     */
    RespuestaUsuarioDTO calificarRespuestaAutomatica(Integer id);

    /**
     * Calificar respuesta manualmente (pregunta abierta)
     */
    RespuestaUsuarioDTO calificarRespuestaManual(Integer id, BigDecimal puntuacion, boolean correcta);

    /**
     * Obtener respuestas correctas de usuario
     */
    List<RespuestaUsuarioDTO> getRespuestasCorrectasByUsuario(Integer idUsuario);

    /**
     * Obtener respuestas incorrectas de usuario
     */
    List<RespuestaUsuarioDTO> getRespuestasIncorrectasByUsuario(Integer idUsuario);

    /**
     * Calcular puntaje total en evaluación
     */
    BigDecimal calcularPuntajeEvaluacion(Integer usuarioId, Integer evaluacionId);

    /**
     * Verificar si usuario completó evaluación
     */
    boolean completoEvaluacion(Integer usuarioId, Integer evaluacionId);

    /**
     * Obtener estadísticas de usuario
     */
    long getCountRespuestasCorrectas(Integer usuarioId);
}