package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.dto.RespuestaDTO;
import com.schoolDays.noche_app.businessLayer.service.RespuestaService;
import com.schoolDays.noche_app.businessLayer.service.PreguntaService;
import com.schoolDays.noche_app.persistenceLayer.dao.RespuestaDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RespuestaServiceImpl implements RespuestaService {

    private final RespuestaDAO respuestaDAO;
    private final PreguntaService preguntaService;

    @Override
    public RespuestaDTO createRespuesta(RespuestaDTO respuestaDTO) {
        log.info("Creando nueva opción de respuesta para pregunta: {}", respuestaDTO.getIdPregunta());

        validateRespuestaData(respuestaDTO);

        // Validar que la pregunta existe
        preguntaService.getPreguntaById(respuestaDTO.getIdPregunta());

        // Si no se especifica orden, asignar el siguiente disponible
        if (respuestaDTO.getOrden() == null) {
            List<RespuestaDTO> existingRespuestas = respuestaDAO.findByPreguntaOrdenadas(respuestaDTO.getIdPregunta());
            respuestaDTO.setOrden(existingRespuestas.size() + 1);
        }

        RespuestaDTO createdRespuesta = respuestaDAO.save(respuestaDTO);
        log.info("Opción de respuesta creada exitosamente con ID: {}", createdRespuesta.getIdRespuesta());
        return createdRespuesta;
    }

    @Override
    @Transactional(readOnly = true)
    public RespuestaDTO getRespuestaById(Integer id) {
        return respuestaDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Respuesta no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaDTO> getAllRespuestas() {
        return respuestaDAO.findAll();
    }

    @Override
    public RespuestaDTO updateRespuesta(Integer id, RespuestaDTO respuestaDTO) {
        log.info("Actualizando respuesta ID: {}", id);

        getRespuestaById(id); // Verificar existencia
        validateRespuestaUpdateData(respuestaDTO);

        return respuestaDAO.update(id, respuestaDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar respuesta"));
    }

    @Override
    public void deleteRespuesta(Integer id) {
        log.info("Eliminando respuesta ID: {}", id);

        RespuestaDTO respuesta = getRespuestaById(id);

        // Validar que no es la única respuesta correcta
        if (respuesta.getEsCorrecta()) {
            long correctasCount = respuestaDAO.countCorrectasByPregunta(respuesta.getIdPregunta());
            if (correctasCount <= 1) {
                throw new IllegalStateException("No se puede eliminar la única respuesta correcta de la pregunta");
            }
        }

        if (!respuestaDAO.deleteById(id)) {
            throw new RuntimeException("Error al eliminar respuesta con ID: " + id);
        }

        log.info("Respuesta eliminada exitosamente ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaDTO> getRespuestasByPreguntaOrdenadas(Integer idPregunta) {
        preguntaService.getPreguntaById(idPregunta);
        return respuestaDAO.findByPreguntaOrdenadas(idPregunta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaDTO> getRespuestasCorrectas(Integer idPregunta) {
        return respuestaDAO.findCorrectasByPregunta(idPregunta);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RespuestaDTO> getRespuestasIncorrectas(Integer idPregunta) {
        return respuestaDAO.findIncorrectasByPregunta(idPregunta);
    }

    @Override
    public RespuestaDTO cambiarOrdenRespuesta(Integer id, Integer nuevoOrden) {
        log.info("Cambiando orden de respuesta ID: {} a {}", id, nuevoOrden);

        if (nuevoOrden <= 0) {
            throw new IllegalArgumentException("El orden debe ser mayor a 0");
        }

        RespuestaDTO updateDTO = new RespuestaDTO();
        updateDTO.setOrden(nuevoOrden);

        return updateRespuesta(id, updateDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean tieneRespuestaCorrecta(Integer idPregunta) {
        return respuestaDAO.countCorrectasByPregunta(idPregunta) > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public long getCountRespuestasCorrectas(Integer idPregunta) {
        return respuestaDAO.countCorrectasByPregunta(idPregunta);
    }

    private void validateRespuestaData(RespuestaDTO respuestaDTO) {
        if (respuestaDTO.getContenido() == null || respuestaDTO.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido de la respuesta es obligatorio");
        }

        if (respuestaDTO.getEsCorrecta() == null) {
            throw new IllegalArgumentException("Debe especificar si la respuesta es correcta");
        }

        if (respuestaDTO.getIdPregunta() == null) {
            throw new IllegalArgumentException("La pregunta es obligatoria");
        }
    }

    private void validateRespuestaUpdateData(RespuestaDTO respuestaDTO) {
        if (respuestaDTO.getContenido() != null && respuestaDTO.getContenido().trim().isEmpty()) {
            throw new IllegalArgumentException("El contenido no puede estar vacío");
        }

        if (respuestaDTO.getOrden() != null && respuestaDTO.getOrden() <= 0) {
            throw new IllegalArgumentException("El orden debe ser mayor a 0");
        }
    }
}
