package com.schoolDays.unit.service;

import com.schoolDays.noche_app.businessLayer.dto.PreguntaDTO;
import com.schoolDays.noche_app.businessLayer.service.EvaluacionService;
import com.schoolDays.noche_app.businessLayer.service.impl.PreguntaServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.PreguntaDAO;
import com.schoolDays.noche_app.persistenceLayer.entity.PreguntaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PreguntaServiceImplTest {

    @Mock private PreguntaDAO preguntaDAO;
    @Mock private EvaluacionService evaluacionService;

    @InjectMocks
    private PreguntaServiceImpl preguntaService;

    private PreguntaDTO preguntaDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        preguntaDTO = new PreguntaDTO();
        preguntaDTO.setIdPregunta(1);
        preguntaDTO.setEnunciado("¿Qué es Java?");
        preguntaDTO.setTipoPregunta("MULTIPLE_CHOICE");
        preguntaDTO.setOrden(1);
        preguntaDTO.setIdEvaluacion(10);
    }

    // ---------------------------------------------------------------------------------
    // createPregunta
    // ---------------------------------------------------------------------------------
    @Test
    void testCreatePregunta_Exito() {
        when(preguntaDAO.save(any())).thenReturn(preguntaDTO);
        when(preguntaDAO.countByEvaluacion(10)).thenReturn(2L);

        PreguntaDTO dto = new PreguntaDTO(null, "¿Qué es Java?", "MULTIPLE_CHOICE", null, 10, null);

        PreguntaDTO result = preguntaService.createPregunta(dto);

        assertNotNull(result);
        verify(evaluacionService).getEvaluacionById(10);
        verify(preguntaDAO).save(any());
    }

    @Test
    void testCreatePregunta_EvaluacionNoExiste() {
        doThrow(new RuntimeException("Evaluación no encontrada")).when(evaluacionService).getEvaluacionById(10);

        assertThrows(RuntimeException.class, () -> preguntaService.createPregunta(preguntaDTO));
    }

    @Test
    void testCreatePregunta_EnunciadoVacio() {
        preguntaDTO.setEnunciado("");
        assertThrows(IllegalArgumentException.class, () -> preguntaService.createPregunta(preguntaDTO));
    }

    @Test
    void testCreatePregunta_TipoPreguntaNulo() {
        preguntaDTO.setTipoPregunta(null);
        assertThrows(IllegalArgumentException.class, () -> preguntaService.createPregunta(preguntaDTO));
    }

    @Test
    void testCreatePregunta_IdEvaluacionNulo() {
        preguntaDTO.setIdEvaluacion(null);
        assertThrows(IllegalArgumentException.class, () -> preguntaService.createPregunta(preguntaDTO));
    }

    // ---------------------------------------------------------------------------------
    //  getPreguntaById
    // ---------------------------------------------------------------------------------
    @Test
    void testGetPreguntaById_Exito() {
        when(preguntaDAO.findById(1)).thenReturn(Optional.of(preguntaDTO));

        PreguntaDTO result = preguntaService.getPreguntaById(1);
        assertEquals("¿Qué es Java?", result.getEnunciado());
    }

    @Test
    void testGetPreguntaById_NoExiste() {
        when(preguntaDAO.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> preguntaService.getPreguntaById(1));
    }

    // ---------------------------------------------------------------------------------
    // getAllPreguntas
    // ---------------------------------------------------------------------------------
    @Test
    void testGetAllPreguntas() {
        when(preguntaDAO.findAll()).thenReturn(List.of(preguntaDTO));

        List<PreguntaDTO> result = preguntaService.getAllPreguntas();

        assertEquals(1, result.size());
    }

    // ---------------------------------------------------------------------------------
    //  updatePregunta
    // ---------------------------------------------------------------------------------
    @Test
    void testUpdatePregunta_Exito() {
        when(preguntaDAO.findById(1)).thenReturn(Optional.of(preguntaDTO));
        when(preguntaDAO.update(eq(1), any())).thenReturn(Optional.of(preguntaDTO));

        PreguntaDTO result = preguntaService.updatePregunta(1, preguntaDTO);

        assertEquals("¿Qué es Java?", result.getEnunciado());
    }

    @Test
    void testUpdatePregunta_NoExiste() {
        when(preguntaDAO.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> preguntaService.updatePregunta(1, preguntaDTO));
    }

    @Test
    void testUpdatePregunta_ErrorEnUpdate() {
        when(preguntaDAO.findById(1)).thenReturn(Optional.of(preguntaDTO));
        when(preguntaDAO.update(eq(1), any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> preguntaService.updatePregunta(1, preguntaDTO));
    }

    @Test
    void testUpdatePregunta_EnunciadoVacio() {
        preguntaDTO.setEnunciado("");
        when(preguntaDAO.findById(1)).thenReturn(Optional.of(preguntaDTO));
        assertThrows(IllegalArgumentException.class, () -> preguntaService.updatePregunta(1, preguntaDTO));
    }

    @Test
    void testUpdatePregunta_OrdenInvalido() {
        preguntaDTO.setOrden(0);
        when(preguntaDAO.findById(1)).thenReturn(Optional.of(preguntaDTO));
        assertThrows(IllegalArgumentException.class, () -> preguntaService.updatePregunta(1, preguntaDTO));
    }

    // ---------------------------------------------------------------------------------
    // deletePregunta
    // ---------------------------------------------------------------------------------
    @Test
    void testDeletePregunta_Exito() {
        when(preguntaDAO.findById(1)).thenReturn(Optional.of(preguntaDTO));
        when(preguntaDAO.deleteById(1)).thenReturn(true);

        assertDoesNotThrow(() -> preguntaService.deletePregunta(1));
        verify(preguntaDAO).deleteById(1);
    }

    @Test
    void testDeletePregunta_Error() {
        when(preguntaDAO.findById(1)).thenReturn(Optional.of(preguntaDTO));
        when(preguntaDAO.deleteById(1)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> preguntaService.deletePregunta(1));
    }

    // ---------------------------------------------------------------------------------
    //  getPreguntasByEvaluacionOrdenadas
    // ---------------------------------------------------------------------------------
    @Test
    void testGetPreguntasByEvaluacionOrdenadas_Exito() {
        when(preguntaDAO.findByEvaluacionOrdenadas(10)).thenReturn(List.of(preguntaDTO));

        List<PreguntaDTO> result = preguntaService.getPreguntasByEvaluacionOrdenadas(10);

        assertEquals(1, result.size());
        verify(evaluacionService).getEvaluacionById(10);
    }

    // ---------------------------------------------------------------------------------
    // getPreguntasByTipo
    // ---------------------------------------------------------------------------------
    @Test
    void testGetPreguntasByTipo() {
        when(preguntaDAO.findByTipo(PreguntaEntity.TipoPregunta.MULTIPLE_CHOICE))
                .thenReturn(List.of(preguntaDTO));

        List<PreguntaDTO> result = preguntaService.getPreguntasByTipo(PreguntaEntity.TipoPregunta.MULTIPLE_CHOICE);

        assertEquals(1, result.size());
    }

    // ---------------------------------------------------------------------------------
    //  getPreguntasByCurso
    // ---------------------------------------------------------------------------------
    @Test
    void testGetPreguntasByCurso() {
        when(preguntaDAO.findByCurso(5)).thenReturn(List.of(preguntaDTO));

        List<PreguntaDTO> result = preguntaService.getPreguntasByCurso(5);

        assertEquals(1, result.size());
    }

    // ---------------------------------------------------------------------------------
    // getPreguntasMultipleChoice / Abiertas
    // ---------------------------------------------------------------------------------
    @Test
    void testGetPreguntasMultipleChoice() {
        when(preguntaDAO.findMultipleChoiceByEvaluacion(10)).thenReturn(List.of(preguntaDTO));

        List<PreguntaDTO> result = preguntaService.getPreguntasMultipleChoice(10);
        assertEquals(1, result.size());
    }

    @Test
    void testGetPreguntasAbiertas() {
        when(preguntaDAO.findAbiertasByEvaluacion(10)).thenReturn(List.of(preguntaDTO));

        List<PreguntaDTO> result = preguntaService.getPreguntasAbiertas(10);
        assertEquals(1, result.size());
    }

    // ---------------------------------------------------------------------------------
    //  cambiarOrdenPregunta
    // ---------------------------------------------------------------------------------
    @Test
    void testCambiarOrdenPregunta_Exito() {
        when(preguntaDAO.findById(1)).thenReturn(Optional.of(preguntaDTO));
        when(preguntaDAO.update(eq(1), any())).thenReturn(Optional.of(preguntaDTO));

        PreguntaDTO result = preguntaService.cambiarOrdenPregunta(1, 2);

        assertNotNull(result);
        verify(preguntaDAO).update(eq(1), any());
    }

    @Test
    void testCambiarOrdenPregunta_OrdenInvalido() {
        assertThrows(IllegalArgumentException.class, () -> preguntaService.cambiarOrdenPregunta(1, 0));
    }

    // ---------------------------------------------------------------------------------
    //  getPreguntasCountByEvaluacion
    // ---------------------------------------------------------------------------------
    @Test
    void testGetPreguntasCountByEvaluacion() {
        when(preguntaDAO.countByEvaluacion(10)).thenReturn(5L);

        long count = preguntaService.getPreguntasCountByEvaluacion(10);

        assertEquals(5L, count);
        verify(evaluacionService).getEvaluacionById(10);
    }

    // ---------------------------------------------------------------------------------
    // duplicarPregunta
    // ---------------------------------------------------------------------------------
    @Test
    void testDuplicarPregunta_Exito() {
        PreguntaDTO original = new PreguntaDTO(1, "Original", "MULTIPLE_CHOICE", 1, 10, null);
        PreguntaDTO duplicada = new PreguntaDTO(2, "Original", "MULTIPLE_CHOICE", 1, 20, null);

        when(preguntaDAO.findById(1)).thenReturn(Optional.of(original));
        when(preguntaDAO.save(any())).thenReturn(duplicada);

        PreguntaDTO result = preguntaService.duplicarPregunta(1, 20);

        assertEquals(20, result.getIdEvaluacion());
        verify(evaluacionService, times(2)).getEvaluacionById(20);
    }
}
