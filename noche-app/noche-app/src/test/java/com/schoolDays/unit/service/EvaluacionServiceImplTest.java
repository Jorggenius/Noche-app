package com.schoolDays.unit.service;

import com.schoolDays.noche_app.businessLayer.dto.EvaluacionDTO;
import com.schoolDays.noche_app.businessLayer.service.ModuloService;
import com.schoolDays.noche_app.businessLayer.service.impl.EvaluacionServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.EvaluacionDAO;
import com.schoolDays.noche_app.persistenceLayer.entity.EvaluacionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EvaluacionServiceImplTest {

    @Mock
    private EvaluacionDAO evaluacionDAO;

    @Mock
    private ModuloService moduloService;

    @InjectMocks
    private EvaluacionServiceImpl evaluacionService;

    private EvaluacionDTO evaluacionDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        evaluacionDTO = new EvaluacionDTO(
                1,
                "Evaluación Final Java",
                "Examen teórico-práctico sobre Java avanzado",
                "MCQ",
                BigDecimal.valueOf(100),
                10,
                "POO y Streams"
        );
    }

    // ----------------------- CREAR -----------------------

    @Test
    void createEvaluacion_Exitoso() {
        when(evaluacionDAO.save(any())).thenReturn(evaluacionDTO);

        EvaluacionDTO result = evaluacionService.createEvaluacion(evaluacionDTO);

        assertNotNull(result);
        verify(moduloService).getModuloById(10);
        verify(evaluacionDAO).save(any());
    }

    @Test
    void createEvaluacion_FallaPorTituloVacio() {
        evaluacionDTO.setTitulo(" ");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> evaluacionService.createEvaluacion(evaluacionDTO));

        assertEquals("El título de la evaluación es obligatorio", ex.getMessage());
    }

    @Test
    void createEvaluacion_FallaPorTipoNulo() {
        evaluacionDTO.setTipo(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> evaluacionService.createEvaluacion(evaluacionDTO));

        assertEquals("El tipo de evaluación es obligatorio", ex.getMessage());
    }

    @Test
    void createEvaluacion_FallaPorPuntajeInvalido() {
        evaluacionDTO.setPuntajeMax(BigDecimal.ZERO);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> evaluacionService.createEvaluacion(evaluacionDTO));

        assertEquals("El puntaje máximo debe ser mayor a cero", ex.getMessage());
    }

    @Test
    void createEvaluacion_FallaPorModuloNulo() {
        evaluacionDTO.setIdModulo(null);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> evaluacionService.createEvaluacion(evaluacionDTO));

        assertEquals("El módulo es obligatorio", ex.getMessage());
    }

    // ----------------------- OBTENER -----------------------

    @Test
    void getEvaluacionById_Exitoso() {
        when(evaluacionDAO.findById(1)).thenReturn(Optional.of(evaluacionDTO));

        EvaluacionDTO result = evaluacionService.getEvaluacionById(1);

        assertEquals("Evaluación Final Java", result.getTitulo());
    }

    @Test
    void getEvaluacionById_NoEncontrada() {
        when(evaluacionDAO.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> evaluacionService.getEvaluacionById(99));

        assertEquals("Evaluación no encontrada con ID: 99", ex.getMessage());
    }

    @Test
    void getAllEvaluaciones_Exitoso() {
        when(evaluacionDAO.findAll()).thenReturn(List.of(evaluacionDTO));

        List<EvaluacionDTO> result = evaluacionService.getAllEvaluaciones();

        assertEquals(1, result.size());
        verify(evaluacionDAO).findAll();
    }

    // ----------------------- ACTUALIZAR -----------------------

    @Test
    void updateEvaluacion_Exitoso() {
        when(evaluacionDAO.findById(1)).thenReturn(Optional.of(evaluacionDTO));
        when(evaluacionDAO.update(eq(1), any())).thenReturn(Optional.of(evaluacionDTO));

        EvaluacionDTO result = evaluacionService.updateEvaluacion(1, evaluacionDTO);

        assertNotNull(result);
        verify(evaluacionDAO).update(eq(1), any());
    }

    @Test
    void updateEvaluacion_NoEncontrada() {
        when(evaluacionDAO.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> evaluacionService.updateEvaluacion(1, evaluacionDTO));

        assertEquals("Evaluación no encontrada con ID: 1", ex.getMessage());
    }

    @Test
    void updateEvaluacion_ErrorAlActualizar() {
        when(evaluacionDAO.findById(1)).thenReturn(Optional.of(evaluacionDTO));
        when(evaluacionDAO.update(eq(1), any())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> evaluacionService.updateEvaluacion(1, evaluacionDTO));

        assertEquals("Error al actualizar evaluación", ex.getMessage());
    }

    @Test
    void updateEvaluacion_FallaPorTituloVacio() {
        evaluacionDTO.setTitulo(" ");
        when(evaluacionDAO.findById(1)).thenReturn(Optional.of(evaluacionDTO));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> evaluacionService.updateEvaluacion(1, evaluacionDTO));

        assertEquals("El título no puede estar vacío", ex.getMessage());
    }

    @Test
    void updateEvaluacion_FallaPorPuntajeInvalido() {
        evaluacionDTO.setPuntajeMax(BigDecimal.ZERO);
        when(evaluacionDAO.findById(1)).thenReturn(Optional.of(evaluacionDTO));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> evaluacionService.updateEvaluacion(1, evaluacionDTO));

        assertEquals("El puntaje máximo debe ser mayor a cero", ex.getMessage());
    }

    // ----------------------- ELIMINAR -----------------------

    @Test
    void deleteEvaluacion_Exitoso() {
        when(evaluacionDAO.findById(1)).thenReturn(Optional.of(evaluacionDTO));
        when(evaluacionDAO.deleteById(1)).thenReturn(true);

        assertDoesNotThrow(() -> evaluacionService.deleteEvaluacion(1));
        verify(evaluacionDAO).deleteById(1);
    }

    @Test
    void deleteEvaluacion_ErrorAlEliminar() {
        when(evaluacionDAO.findById(1)).thenReturn(Optional.of(evaluacionDTO));
        when(evaluacionDAO.deleteById(1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> evaluacionService.deleteEvaluacion(1));

        assertEquals("Error al eliminar evaluación con ID: 1", ex.getMessage());
    }

    // ----------------------- CONSULTAS -----------------------

    @Test
    void getEvaluacionesByModulo_Exitoso() {
        when(evaluacionDAO.findByModulo(10)).thenReturn(List.of(evaluacionDTO));

        List<EvaluacionDTO> result = evaluacionService.getEvaluacionesByModulo(10);

        assertEquals(1, result.size());
        verify(moduloService).getModuloById(10);
    }

    @Test
    void getEvaluacionesByTipo_Exitoso() {
        when(evaluacionDAO.findByTipo(EvaluacionEntity.TipoEvaluacion.MCQ))
                .thenReturn(List.of(evaluacionDTO));

        List<EvaluacionDTO> result = evaluacionService.getEvaluacionesByTipo(EvaluacionEntity.TipoEvaluacion.MCQ);

        assertEquals(1, result.size());
    }

    @Test
    void getEvaluacionesByCurso_Exitoso() {
        when(evaluacionDAO.findByCurso(5)).thenReturn(List.of(evaluacionDTO));

        List<EvaluacionDTO> result = evaluacionService.getEvaluacionesByCurso(5);

        assertEquals(1, result.size());
    }

    @Test
    void getEvaluacionesConPreguntas_Exitoso() {
        when(evaluacionDAO.findConPreguntas()).thenReturn(List.of(evaluacionDTO));

        List<EvaluacionDTO> result = evaluacionService.getEvaluacionesConPreguntas();

        assertEquals(1, result.size());
    }

    // ----------------------- OTRAS FUNCIONES -----------------------

    @Test
    void iniciarEvaluacion_Exitoso() {
        when(evaluacionDAO.findById(1)).thenReturn(Optional.of(evaluacionDTO));

        assertDoesNotThrow(() -> evaluacionService.iniciarEvaluacion(1, 99));
    }

    @Test
    void finalizarEvaluacion_Exitoso() {
        assertDoesNotThrow(() -> evaluacionService.finalizarEvaluacion(1, 99));
    }

    @Test
    void puedeTomarEvaluacion_SiempreTrue() {
        boolean result = evaluacionService.puedeTomarEvaluacion(1, 99);
        assertTrue(result);
    }

    @Test
    void getEvaluacionesPendientes_DevuelveTodas() {
        when(evaluacionDAO.findAll()).thenReturn(List.of(evaluacionDTO));

        List<EvaluacionDTO> result = evaluacionService.getEvaluacionesPendientes(99);

        assertEquals(1, result.size());
    }
}
