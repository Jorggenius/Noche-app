package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.RespuestaDTO;
import com.schoolDays.noche_app.businessLayer.service.PreguntaService;
import com.schoolDays.noche_app.businessLayer.service.RespuestaService;
import com.schoolDays.noche_app.persistenceLayer.dao.RespuestaDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
            Integer nextOrden = existingRespuestas.size() + 1;
            respuestaDTO.setOrden(nextOrden);
        }

        RespuestaDTO createdRespuesta = respuestaDAO.save(respuestaDTO);
        log.info("Respuesta creada exitosamente con ID: {}", createdRespuesta.getIdRespuesta());
        return createdRespuesta;
    }

    private void validateRespuestaData(RespuestaDTO respuestaDTO) {
        if (respuestaDTO.getTextoRespuesta() == null || respuestaDTO.getTextoRespuesta().trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de la respuesta es obligatorio");
        }

        if (respuestaDTO.getIdPregunta() == null) {
            throw new IllegalArgumentException("La pregunta es obligatoria");
        }
    }
}