package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.dto.RespuestaUsuarioDTO;
import com.schoolDays.noche_app.businessLayer.dto.RespuestaDTO;
import com.schoolDays.noche_app.businessLayer.service.RespuestaUsuarioService;
import com.schoolDays.noche_app.businessLayer.service.UsuarioService;
import com.schoolDays.noche_app.businessLayer.service.PreguntaService;
import com.schoolDays.noche_app.businessLayer.service.RespuestaService;
import com.schoolDays.noche_app.persistenceLayer.dao.RespuestaUsuarioDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RespuestaUsuarioServiceImpl implements RespuestaUsuarioService {

    private final RespuestaUsuarioDAO respuestaUsuarioDAO;
    private final UsuarioService usuarioService;
    private final PreguntaService preguntaService;
    private final RespuestaService respuestaService;

    @Override
    public RespuestaUsuarioDTO registrarRespuesta(RespuestaUsuarioDTO respuestaUsuarioDTO) {
        log.info("Registrando respuesta de usuario {} para pregunta {}",
                respuestaUsuarioDTO.getIdUsuario(), respuestaUsuarioDTO.getIdPregunta());

        validateRespuestaUsuarioData(respuestaUsuarioDTO);

        // Validar que usuario y pregunta existen
        usuarioService.getUsuarioById(respuestaUsuarioDTO.getIdUsuario());
        preguntaService.getPreguntaById(respuestaUsuarioDTO.getIdPregunta());

        // Establecer fecha actual
        respuestaUsuarioDTO.setFecha(LocalDate.now());

        // Calificar automáticamente si es MCQ
        if (respuestaUsuarioDTO.getIdRespuestaSeleccionada() != null) {
            RespuestaDTO opcionSeleccionada = respuestaService.getRespuestaById(respuestaUsuarioDTO.getIdRespuestaSeleccionada());
            respuestaUsuarioDTO.setCorrecta(opcionSeleccionada.getEsCorrecta());
            respuestaUsuarioDTO.setPuntuacion(opcionSeleccionada.getEsCorrecta() ? BigDecimal.TEN : BigDecimal.ZERO);
        }

        RespuestaUsuarioDTO createdRespuesta = respuestaUsuarioDAO.save(respuestaUsuarioDTO);
        log.info("Respuesta de usuario registrada exitosamente con ID: {}", createdRespuesta.getIdRespuestaUsuario());

        return createdRespuesta;
    }

    @Override
    @Transactional(readOnly = true)
    public RespuestaUsuarioDTO getRespuestaUsuarioById(Integer id) {
        return respuestaUsuarioDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Respuesta de usuario no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaUsuarioDTO> getAllRespuestasUsuario() {
        return respuestaUsuarioDAO.findAll();
    }

    @Override
    public RespuestaUsuarioDTO updateRespuestaUsuario(Integer id, RespuestaUsuarioDTO respuestaUsuarioDTO) {
        log.info("Actualizando respuesta de usuario ID: {}", id);

        getRespuestaUsuarioById(id); // Verificar existencia

        return respuestaUsuarioDAO.update(id, respuestaUsuarioDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar respuesta de usuario"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaUsuarioDTO> getRespuestasByUsuario(Integer idUsuario) {
        usuarioService.getUsuarioById(idUsuario);
        return respuestaUsuarioDAO.findByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaUsuarioDTO> getRespuestasByEvaluacion(Integer idEvaluacion) {
        return respuestaUsuarioDAO.findByEvaluacion(idEvaluacion);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaUsuarioDTO> getRespuestasUsuarioEnEvaluacion(Integer usuarioId, Integer evaluacionId) {
        return respuestaUsuarioDAO.findRespuestasEnEvaluacion(usuarioId, evaluacionId);
    }

    @Override
    public RespuestaUsuarioDTO calificarRespuestaAutomatica(Integer id) {
        log.info("Calificando respuesta automáticamente ID: {}", id);

        RespuestaUsuarioDTO respuestaUsuario = getRespuestaUsuarioById(id);

        if (respuestaUsuario.getIdRespuestaSeleccionada() != null) {
            RespuestaDTO opcionSeleccionada = respuestaService.getRespuestaById(respuestaUsuario.getIdRespuestaSeleccionada());

            RespuestaUsuarioDTO updateDTO = new RespuestaUsuarioDTO();
            updateDTO.setCorrecta(opcionSeleccionada.getEsCorrecta());
            updateDTO.setPuntuacion(opcionSeleccionada.getEsCorrecta() ? BigDecimal.TEN : BigDecimal.ZERO);

            return updateRespuestaUsuario(id, updateDTO);
        }

        return respuestaUsuario;
    }

    @Override
    public RespuestaUsuarioDTO calificarRespuestaManual(Integer id, BigDecimal puntuacion, boolean correcta) {
        log.info("Calificando respuesta manualmente ID: {} - Puntuación: {} - Correcta: {}", id, puntuacion, correcta);

        if (puntuacion.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La puntuación no puede ser negativa");
        }

        RespuestaUsuarioDTO updateDTO = new RespuestaUsuarioDTO();
        updateDTO.setPuntuacion(puntuacion);
        updateDTO.setCorrecta(correcta);

        return updateRespuestaUsuario(id, updateDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaUsuarioDTO> getRespuestasCorrectasByUsuario(Integer idUsuario) {
        return respuestaUsuarioDAO.findCorrectasByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaUsuarioDTO> getRespuestasIncorrectasByUsuario(Integer idUsuario) {
        return respuestaUsuarioDAO.findIncorrectasByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal calcularPuntajeEvaluacion(Integer usuarioId, Integer evaluacionId) {
        List<RespuestaUsuarioDTO> respuestas = getRespuestasUsuarioEnEvaluacion(usuarioId, evaluacionId);

        return respuestas.stream()
                .map(RespuestaUsuarioDTO::getPuntuacion)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean completoEvaluacion(Integer usuarioId, Integer evaluacionId) {
        // Aquí implementarías la lógica para verificar si completó todas las preguntas
        List<RespuestaUsuarioDTO> respuestas = getRespuestasUsuarioEnEvaluacion(usuarioId, evaluacionId);
        return !respuestas.isEmpty(); // Placeholder
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountRespuestasCorrectas(Integer usuarioId) {
        return respuestaUsuarioDAO.countCorrectasByUsuario(usuarioId);
    }

    private void validateRespuestaUsuarioData(RespuestaUsuarioDTO respuestaUsuarioDTO) {
        if (respuestaUsuarioDTO.getIdUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        if (respuestaUsuarioDTO.getIdPregunta() == null) {
            throw new IllegalArgumentException("La pregunta es obligatoria");
        }

        if (respuestaUsuarioDTO.getIdEvaluacion() == null) {
            throw new IllegalArgumentException("La evaluación es obligatoria");
        }

        // Validar que tenga respuesta (MCQ o texto)
        if (respuestaUsuarioDTO.getIdRespuestaSeleccionada() == null &&
                (respuestaUsuarioDTO.getRespuestaTexto() == null || respuestaUsuarioDTO.getRespuestaTexto().trim().isEmpty())) {
            throw new IllegalArgumentException("Debe proporcionar una respuesta");
        }
    }
}

