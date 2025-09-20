package com.schoolDays.noche_app.businessLayer.service;

import com.schoolDays.noche_app.businessLayer.RespuestaDTO;

import java.util.List;

public interface RespuestaService {

    /**
     * Crear nueva opción de respuesta
     *
     * VALIDACIONES:
     * - Pregunta debe existir y ser de tipo MCQ
     * - Al menos una opción correcta por pregunta
     * - Máximo número de opciones por pregunta
     */
    RespuestaDTO createRespuesta(RespuestaDTO respuestaDTO);

    /**
     * Buscar respuesta por ID
     */
    RespuestaDTO getRespuestaById(Integer id);

    /**
     * Buscar todas las respuestas
     */
    List<RespuestaDTO> getAllRespuestas();

    /**
     * Actualizar respuesta
     */
    RespuestaDTO updateRespuesta(Integer id, RespuestaDTO respuestaDTO);

    /**
     * Eliminar respuesta
     *
     * REGLAS:
     * - No eliminar si es la única opción correcta
     * - Reordenar opciones restantes
     */
    void deleteRespuesta(Integer id);

    /**
     * Buscar opciones por pregunta ordenadas
     */
    List<RespuestaDTO> getRespuestasByPreguntaOrdenadas(Integer idPregunta);

    /**
     * Obtener respuestas correctas de una pregunta
     */
    List<RespuestaDTO> getRespuestasCorrectas(Integer idPregunta);

    /**
     * Obtener respuestas incorrectas de una pregunta
     */
    List<RespuestaDTO> getRespuestasIncorrectas(Integer idPregunta);

    /**
     * Cambiar orden de respuesta
     */
    RespuestaDTO cambiarOrdenRespuesta(Integer id, Integer nuevoOrden);

    /**
     * Validar que pregunta tenga al menos una respuesta correcta
     */
    boolean tieneRespuestaCorrecta(Integer idPregunta);

    /**
     * Obtener conteo de respuestas correctas
     */
    long getCountRespuestasCorrectas(Integer idPregunta);
}