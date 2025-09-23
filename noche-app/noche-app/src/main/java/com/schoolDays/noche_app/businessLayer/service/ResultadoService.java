package com.schoolDays.noche_app.businessLayer.service;

import com.schoolDays.noche_app.businessLayer.dto.ResultadoDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ResultadoService {

    /**
     * Registrar resultado de evaluación
     *
     * VALIDACIONES:
     * - Usuario debe estar inscrito en el curso
     * - Evaluación debe existir
     * - No duplicar resultados
     * - Puntaje válido (0 <= puntaje <= puntajeMax)
     */
    ResultadoDTO registrarResultado(ResultadoDTO resultadoDTO);

    /**
     * Buscar resultado por ID
     */
    ResultadoDTO getResultadoById(Integer id);

    /**
     * Buscar todos los resultados
     */
    List<ResultadoDTO> getAllResultados();

    /**
     * Actualizar resultado (para correcciones)
     */
    ResultadoDTO updateResultado(Integer id, ResultadoDTO resultadoDTO);

    /**
     * Eliminar resultado
     */
    void deleteResultado(Integer id);

    /**
     * Buscar resultados por usuario
     */
    List<ResultadoDTO> getResultadosByUsuario(Integer idUsuario);

    /**
     * Buscar resultados por evaluación
     */
    List<ResultadoDTO> getResultadosByEvaluacion(Integer idEvaluacion);

    /**
     * Buscar resultado específico de usuario en evaluación
     */
    ResultadoDTO getResultadoByUsuarioAndEvaluacion(Integer idUsuario, Integer idEvaluacion);

    /**
     * Verificar si usuario ya tiene resultado en evaluación
     */
    boolean existeResultado(Integer idUsuario, Integer idEvaluacion);

    /**
     * Buscar resultados por rango de puntaje
     */
    List<ResultadoDTO> getResultadosByRangoPuntaje(BigDecimal puntajeMin, BigDecimal puntajeMax);

    /**
     * Buscar resultados por fecha
     */
    List<ResultadoDTO> getResultadosByFecha(LocalDate fecha);

    /**
     * Buscar resultados en rango de fechas
     */
    List<ResultadoDTO> getResultadosByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin);

    /**
     * Obtener mejores resultados ordenados por puntaje
     */
    List<ResultadoDTO> getMejoresResultados();

    /**
     * Obtener mejores resultados de una evaluación específica
     */
    List<ResultadoDTO> getMejoresResultadosByEvaluacion(Integer idEvaluacion);

    /**
     * Obtener resultados aprobatorios (>= puntaje mínimo)
     */
    List<ResultadoDTO> getResultadosAprobatorios(BigDecimal puntajeMinimo);

    /**
     * Obtener mejores resultados por curso
     */
    List<ResultadoDTO> getMejoresResultadosByCurso(Integer cursoId);

    /**
     * Obtener todos los resultados de un usuario en un curso específico
     */
    List<ResultadoDTO> getResultadosUsuarioEnCurso(Integer usuarioId, Integer cursoId);

    /**
     * Calcular promedio de puntajes en una evaluación
     */
    BigDecimal getPromedioPuntajeByEvaluacion(Integer evaluacionId);

    /**
     * Calcular promedio de puntajes de un usuario
     */
    BigDecimal getPromedioPuntajeByUsuario(Integer usuarioId);

    /**
     * Calcular estadísticas de una evaluación
     */
    Object getEstadisticasEvaluacion(Integer idEvaluacion);

    /**
     * Obtener ranking de usuarios por promedio
     */
    List<Object[]> getRankingUsuariosByPromedio();

    /**
     * Contar participantes en evaluación
     */
    long countParticipantesByEvaluacion(Integer idEvaluacion);

    /**
     * Verificar si usuario aprobó evaluación
     */
    boolean aproboEvaluacion(Integer usuarioId, Integer evaluacionId, BigDecimal puntajeMinimo);

    /**
     * Calcular resultado automáticamente desde respuestas de usuario
     */
    ResultadoDTO calcularResultadoAutomatico(Integer usuarioId, Integer evaluacionId);
}
