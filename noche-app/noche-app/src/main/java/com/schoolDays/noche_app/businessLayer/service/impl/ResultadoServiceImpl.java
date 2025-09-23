package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.dto.ResultadoDTO;
import com.schoolDays.noche_app.businessLayer.service.ResultadoService;
import com.schoolDays.noche_app.businessLayer.service.UsuarioService;
import com.schoolDays.noche_app.businessLayer.service.EvaluacionService;
import com.schoolDays.noche_app.businessLayer.service.RespuestaUsuarioService;
import com.schoolDays.noche_app.persistenceLayer.dao.ResultadoDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ResultadoServiceImpl implements ResultadoService {

    private final ResultadoDAO resultadoDAO;
    private final UsuarioService usuarioService;
    private final EvaluacionService evaluacionService;
    private final RespuestaUsuarioService respuestaUsuarioService;

    @Override
    public ResultadoDTO registrarResultado(ResultadoDTO resultadoDTO) {
        log.info("Registrando resultado para usuario {} en evaluación {}",
                resultadoDTO.getIdUsuario(), resultadoDTO.getIdEvaluacion());

        validateResultadoData(resultadoDTO);

        // Validar que usuario y evaluación existen
        usuarioService.getUsuarioById(resultadoDTO.getIdUsuario());
        evaluacionService.getEvaluacionById(resultadoDTO.getIdEvaluacion());

        // Verificar que no tenga ya resultado
        if (resultadoDAO.existeResultado(resultadoDTO.getIdUsuario(), resultadoDTO.getIdEvaluacion())) {
            throw new IllegalArgumentException("El usuario ya tiene resultado para esta evaluación");
        }

        resultadoDTO.setFechaRealizacion(LocalDate.now());

        ResultadoDTO createdResultado = resultadoDAO.save(resultadoDTO);
        log.info("Resultado registrado exitosamente con ID: {}", createdResultado.getIdResultado());

        return createdResultado;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoDTO getResultadoById(Integer id) {
        return resultadoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Resultado no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDTO> getAllResultados() {
        return resultadoDAO.findAll();
    }

    @Override
    public ResultadoDTO updateResultado(Integer id, ResultadoDTO resultadoDTO) {
        log.info("Actualizando resultado ID: {}", id);

        getResultadoById(id);
        validateResultadoUpdateData(resultadoDTO);

        return resultadoDAO.update(id, resultadoDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar resultado"));
    }

    @Override
    public void deleteResultado(Integer id) {
        log.info("Eliminando resultado ID: {}", id);

        getResultadoById(id);

        if (!resultadoDAO.deleteById(id)) {
            throw new RuntimeException("Error al eliminar resultado con ID: " + id);
        }

        log.info("Resultado eliminado exitosamente ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDTO> getResultadosByUsuario(Integer idUsuario) {
        usuarioService.getUsuarioById(idUsuario);
        return resultadoDAO.findByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDTO> getResultadosByEvaluacion(Integer idEvaluacion) {
        evaluacionService.getEvaluacionById(idEvaluacion);
        return resultadoDAO.findByEvaluacion(idEvaluacion);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultadoDTO getResultadoByUsuarioAndEvaluacion(Integer idUsuario, Integer idEvaluacion) {
        return resultadoDAO.findByUsuarioAndEvaluacion(idUsuario, idEvaluacion)
                .orElseThrow(() -> new RuntimeException("No se encontró resultado para el usuario y evaluación especificados"));
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeResultado(Integer idUsuario, Integer idEvaluacion) {
        return resultadoDAO.existeResultado(idUsuario, idEvaluacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDTO> getResultadosByRangoPuntaje(BigDecimal puntajeMin, BigDecimal puntajeMax) {
        return resultadoDAO.findByRangoPuntaje(puntajeMin, puntajeMax);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDTO> getResultadosByFecha(LocalDate fecha) {
        return resultadoDAO.findByFecha(fecha);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDTO> getResultadosByRangoFechas(LocalDate fechaInicio, LocalDate fechaFin) {
        return resultadoDAO.findByRangoFechas(fechaInicio, fechaFin);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDTO> getMejoresResultados() {
        return resultadoDAO.findMejoresPuntajes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDTO> getMejoresResultadosByEvaluacion(Integer idEvaluacion) {
        evaluacionService.getEvaluacionById(idEvaluacion);
        return resultadoDAO.findMejoresPuntajesByEvaluacion(idEvaluacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDTO> getResultadosAprobatorios(BigDecimal puntajeMinimo) {
        return resultadoDAO.findAprobatorios(puntajeMinimo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDTO> getMejoresResultadosByCurso(Integer cursoId) {
        return resultadoDAO.findMejoresByCurso(cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultadoDTO> getResultadosUsuarioEnCurso(Integer usuarioId, Integer cursoId) {
        usuarioService.getUsuarioById(usuarioId);
        return resultadoDAO.findResultadosUsuarioEnCurso(usuarioId, cursoId);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getPromedioPuntajeByEvaluacion(Integer evaluacionId) {
        evaluacionService.getEvaluacionById(evaluacionId);
        BigDecimal promedio = resultadoDAO.getPromedioPuntajeByEvaluacion(evaluacionId);
        return promedio != null ? promedio : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getPromedioPuntajeByUsuario(Integer usuarioId) {
        usuarioService.getUsuarioById(usuarioId);
        BigDecimal promedio = resultadoDAO.getPromedioPuntajeByUsuario(usuarioId);
        return promedio != null ? promedio : BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public Object getEstadisticasEvaluacion(Integer idEvaluacion) {
        evaluacionService.getEvaluacionById(idEvaluacion);

        Map<String, Object> estadisticas = new HashMap<>();
        List<ResultadoDTO> resultados = getResultadosByEvaluacion(idEvaluacion);

        if (!resultados.isEmpty()) {
            BigDecimal promedio = getPromedioPuntajeByEvaluacion(idEvaluacion);
            BigDecimal puntajeMax = resultados.stream()
                    .map(ResultadoDTO::getPuntaje)
                    .max(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);
            BigDecimal puntajeMin = resultados.stream()
                    .map(ResultadoDTO::getPuntaje)
                    .min(BigDecimal::compareTo)
                    .orElse(BigDecimal.ZERO);

            estadisticas.put("totalParticipantes", resultados.size());
            estadisticas.put("promedio", promedio);
            estadisticas.put("puntajeMaximo", puntajeMax);
            estadisticas.put("puntajeMinimo", puntajeMin);
        } else {
            estadisticas.put("totalParticipantes", 0);
            estadisticas.put("promedio", BigDecimal.ZERO);
            estadisticas.put("puntajeMaximo", BigDecimal.ZERO);
            estadisticas.put("puntajeMinimo", BigDecimal.ZERO);
        }

        return estadisticas;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getRankingUsuariosByPromedio() {
        // Implementar ranking - placeholder por ahora
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public long countParticipantesByEvaluacion(Integer idEvaluacion) {
        evaluacionService.getEvaluacionById(idEvaluacion);
        return resultadoDAO.countByEvaluacion(idEvaluacion);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean aproboEvaluacion(Integer usuarioId, Integer evaluacionId, BigDecimal puntajeMinimo) {
        try {
            ResultadoDTO resultado = getResultadoByUsuarioAndEvaluacion(usuarioId, evaluacionId);
            return resultado.getPuntaje().compareTo(puntajeMinimo) >= 0;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public ResultadoDTO calcularResultadoAutomatico(Integer usuarioId, Integer evaluacionId) {
        log.info("Calculando resultado automático para usuario {} en evaluación {}", usuarioId, evaluacionId);

        // Verificar que no tenga ya resultado
        if (existeResultado(usuarioId, evaluacionId)) {
            throw new IllegalArgumentException("El usuario ya tiene resultado para esta evaluación");
        }

        // Calcular puntaje basado en respuestas del usuario
        BigDecimal puntajeTotal = respuestaUsuarioService.calcularPuntajeEvaluacion(usuarioId, evaluacionId);

        ResultadoDTO resultadoDTO = new ResultadoDTO();
        resultadoDTO.setIdUsuario(usuarioId);
        resultadoDTO.setIdEvaluacion(evaluacionId);
        resultadoDTO.setPuntaje(puntajeTotal);

        return registrarResultado(resultadoDTO);
    }

    private void validateResultadoData(ResultadoDTO resultadoDTO) {
        if (resultadoDTO.getIdUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        if (resultadoDTO.getIdEvaluacion() == null) {
            throw new IllegalArgumentException("La evaluación es obligatoria");
        }

        if (resultadoDTO.getPuntaje() == null || resultadoDTO.getPuntaje().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El puntaje debe ser mayor o igual a cero");
        }
    }

    private void validateResultadoUpdateData(ResultadoDTO resultadoDTO) {
        if (resultadoDTO.getPuntaje() != null && resultadoDTO.getPuntaje().compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("El puntaje debe ser mayor o igual a cero");
        }
    }
}