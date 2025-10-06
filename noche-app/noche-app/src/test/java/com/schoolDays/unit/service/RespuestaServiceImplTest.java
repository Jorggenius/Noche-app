package com.schoolDays.unit.service;

import com.schoolDays.noche_app.businessLayer.dto.RespuestaDTO;
import com.schoolDays.noche_app.businessLayer.service.PreguntaService;
import com.schoolDays.noche_app.businessLayer.service.impl.RespuestaServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.RespuestaDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RespuestaServiceImplTest {

    @Mock
    private RespuestaDAO respuestaDAO;

    @Mock
    private PreguntaService preguntaService;

    @InjectMocks
    private RespuestaServiceImpl respuestaService;

    private RespuestaDTO correcta;
    private RespuestaDTO incorrecta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        correcta = new RespuestaDTO(1, "Respuesta correcta", true, 1, 10, "Pregunta ejemplo");
        incorrecta = new RespuestaDTO(2, "Respuesta incorrecta", false, 2, 10, "Pregunta ejemplo");
    }

    // -------------------------
    // CREATE
    // -------------------------

    @DisplayName("Crear respuesta exitosamente con orden asignado automáticamente")
    @Test
    void createRespuesta_ok_conOrdenAsignado() {
        RespuestaDTO nueva = new RespuestaDTO(null, "Nueva respuesta", false, null, 10, null);

        when(respuestaDAO.findByPreguntaOrdenadas(10)).thenReturn(List.of(correcta, incorrecta));
        when(respuestaDAO.save(any())).thenAnswer(inv -> {
            RespuestaDTO dto = inv.getArgument(0);
            dto.setIdRespuesta(3);
            return dto;
        });

        RespuestaDTO result = respuestaService.createRespuesta(nueva);

        assertNotNull(result.getIdRespuesta());
        assertEquals(3, result.getOrden());
        verify(preguntaService).getPreguntaById(10);
        verify(respuestaDAO).save(any());
    }

    @DisplayName("Crear respuesta con contenido vacío lanza excepción")
    @Test
    void createRespuesta_contenidoVacio_lanzaExcepcion() {
        RespuestaDTO vacia = new RespuestaDTO(null, "   ", true, 1, 10, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                respuestaService.createRespuesta(vacia));

        assertEquals("El contenido de la respuesta es obligatorio", ex.getMessage());
    }

    @DisplayName("Crear respuesta sin definir si es correcta lanza excepción")
    @Test
    void createRespuesta_sinEsCorrecta_lanzaExcepcion() {
        RespuestaDTO invalida = new RespuestaDTO(null, "Texto", null, 1, 10, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                respuestaService.createRespuesta(invalida));

        assertEquals("Debe especificar si la respuesta es correcta", ex.getMessage());
    }

    @DisplayName("Crear respuesta sin pregunta asociada lanza excepción")
    @Test
    void createRespuesta_sinPregunta_lanzaExcepcion() {
        RespuestaDTO invalida = new RespuestaDTO(null, "Texto", true, 1, null, null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                respuestaService.createRespuesta(invalida));

        assertEquals("La pregunta es obligatoria", ex.getMessage());
    }

    // -------------------------
    // GET BY ID
    // -------------------------

    @DisplayName("Obtener respuesta por ID existente")
    @Test
    void getRespuestaById_ok() {
        when(respuestaDAO.findById(1)).thenReturn(Optional.of(correcta));

        RespuestaDTO result = respuestaService.getRespuestaById(1);

        assertEquals("Respuesta correcta", result.getContenido());
        verify(respuestaDAO).findById(1);
    }

    @DisplayName("Obtener respuesta inexistente lanza excepción")
    @Test
    void getRespuestaById_notFound_lanzaExcepcion() {
        when(respuestaDAO.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                respuestaService.getRespuestaById(99));

        assertEquals("Respuesta no encontrada con ID: 99", ex.getMessage());
    }

    // -------------------------
    // UPDATE
    // -------------------------

    @DisplayName("Actualizar respuesta exitosamente")
    @Test
    void updateRespuesta_ok() {
        RespuestaDTO actualizada = new RespuestaDTO(1, "Actualizada", true, 1, 10, null);

        when(respuestaDAO.findById(1)).thenReturn(Optional.of(correcta));
        when(respuestaDAO.update(eq(1), any())).thenReturn(Optional.of(actualizada));

        RespuestaDTO result = respuestaService.updateRespuesta(1, actualizada);

        assertEquals("Actualizada", result.getContenido());
        verify(respuestaDAO).update(1, actualizada);
    }

    @DisplayName("Actualizar respuesta inexistente lanza excepción")
    @Test
    void updateRespuesta_notFound_lanzaExcepcion() {
        when(respuestaDAO.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                respuestaService.updateRespuesta(1, correcta));

        assertEquals("Respuesta no encontrada con ID: 1", ex.getMessage());
    }

    // -------------------------
    // DELETE
    // -------------------------

    @DisplayName("Eliminar respuesta correctamente cuando hay más de una correcta")
    @Test
    void deleteRespuesta_ok() {
        when(respuestaDAO.findById(1)).thenReturn(Optional.of(correcta));
        when(respuestaDAO.countCorrectasByPregunta(10)).thenReturn(2L);
        when(respuestaDAO.deleteById(1)).thenReturn(true);

        respuestaService.deleteRespuesta(1);

        verify(respuestaDAO).deleteById(1);
    }

    @DisplayName("Eliminar única respuesta correcta lanza excepción")
    @Test
    void deleteRespuesta_unicaCorrecta_lanzaExcepcion() {
        when(respuestaDAO.findById(1)).thenReturn(Optional.of(correcta));
        when(respuestaDAO.countCorrectasByPregunta(10)).thenReturn(1L);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                respuestaService.deleteRespuesta(1));

        assertEquals("No se puede eliminar la única respuesta correcta de la pregunta", ex.getMessage());
    }

    @DisplayName("Eliminar respuesta inexistente lanza excepción")
    @Test
    void deleteRespuesta_notFound_lanzaExcepcion() {
        when(respuestaDAO.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                respuestaService.deleteRespuesta(99));

        assertEquals("Respuesta no encontrada con ID: 99", ex.getMessage());
    }

    // -------------------------
    // CAMBIAR ORDEN
    // -------------------------

    @DisplayName("Cambiar orden de respuesta exitosamente")
    @Test
    void cambiarOrdenRespuesta_ok() {
        RespuestaDTO nueva = new RespuestaDTO(1, null, null, 2, 10, null);

        when(respuestaDAO.findById(1)).thenReturn(Optional.of(correcta));
        when(respuestaDAO.update(eq(1), any())).thenReturn(Optional.of(nueva));

        RespuestaDTO result = respuestaService.cambiarOrdenRespuesta(1, 2);

        assertEquals(2, result.getOrden());
    }

    @DisplayName("Cambiar orden con valor inválido lanza excepción")
    @Test
    void cambiarOrdenRespuesta_ordenInvalido_lanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                respuestaService.cambiarOrdenRespuesta(1, 0));

        assertEquals("El orden debe ser mayor a 0", ex.getMessage());
    }

    // -------------------------
    // MÉTODOS DE CONSULTA
    // -------------------------

    @DisplayName("Listar todas las respuestas")
    @Test
    void getAllRespuestas_ok() {
        when(respuestaDAO.findAll()).thenReturn(List.of(correcta, incorrecta));

        List<RespuestaDTO> result = respuestaService.getAllRespuestas();

        assertEquals(2, result.size());
        verify(respuestaDAO).findAll();
    }

    @DisplayName("Obtener respuestas por pregunta ordenadas")
    @Test
    void getRespuestasByPreguntaOrdenadas_ok() {
        when(respuestaDAO.findByPreguntaOrdenadas(10)).thenReturn(List.of(correcta, incorrecta));

        List<RespuestaDTO> result = respuestaService.getRespuestasByPreguntaOrdenadas(10);

        assertEquals(2, result.size());
        verify(preguntaService).getPreguntaById(10);
    }

    @DisplayName("Verificar si la pregunta tiene respuestas correctas")
    @Test
    void tieneRespuestaCorrecta_ok() {
        when(respuestaDAO.countCorrectasByPregunta(10)).thenReturn(1L);

        boolean result = respuestaService.tieneRespuestaCorrecta(10);

        assertTrue(result);
    }

    @DisplayName("Contar respuestas correctas")
    @Test
    void getCountRespuestasCorrectas_ok() {
        when(respuestaDAO.countCorrectasByPregunta(10)).thenReturn(3L);

        long count = respuestaService.getCountRespuestasCorrectas(10);

        assertEquals(3L, count);
    }
}
