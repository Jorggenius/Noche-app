package com.schoolDays.unit.service;

import com.schoolDays.noche_app.businessLayer.dto.ResultadoDTO;
import com.schoolDays.noche_app.businessLayer.service.EvaluacionService;
import com.schoolDays.noche_app.businessLayer.service.RespuestaUsuarioService;
import com.schoolDays.noche_app.businessLayer.service.UsuarioService;
import com.schoolDays.noche_app.businessLayer.service.impl.ResultadoServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.ResultadoDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ResultadoServiceImplTest {

    @Mock
    private ResultadoDAO resultadoDAO;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private EvaluacionService evaluacionService;

    @Mock
    private RespuestaUsuarioService respuestaUsuarioService;

    @InjectMocks
    private ResultadoServiceImpl resultadoService;

    private ResultadoDTO resultadoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        resultadoDTO = new ResultadoDTO();
        resultadoDTO.setIdUsuario(1);
        resultadoDTO.setIdEvaluacion(10);
        resultadoDTO.setPuntaje(BigDecimal.valueOf(80));
    }

    /**
     * ✅ Caso exitoso: se registra un resultado nuevo correctamente
     */
    @Test
    void registrarResultado_Exitoso() {
        when(resultadoDAO.existeResultado(1, 10)).thenReturn(false);
        when(resultadoDAO.save(any(ResultadoDTO.class))).thenAnswer(invocation -> {
            ResultadoDTO arg = invocation.getArgument(0);
            arg.setIdResultado(100);
            return arg;
        });

        ResultadoDTO result = resultadoService.registrarResultado(resultadoDTO);

        assertNotNull(result.getFechaRealizacion());
        assertEquals(100, result.getIdResultado());
        verify(usuarioService).getUsuarioById(1);
        verify(evaluacionService).getEvaluacionById(10);
        verify(resultadoDAO).save(any(ResultadoDTO.class));
    }

    /**
     * 🚫 Caso de error: usuario ya tiene resultado
     */
    @Test
    void registrarResultado_YaExisteDebeFallar() {
        when(resultadoDAO.existeResultado(1, 10)).thenReturn(true);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> resultadoService.registrarResultado(resultadoDTO)
        );

        assertEquals("El usuario ya tiene resultado para esta evaluación", exception.getMessage());
        verify(resultadoDAO, never()).save(any());
    }

    /**
     * 🚫 Caso de error: puntaje inválido
     */
    @Test
    void registrarResultado_PuntajeInvalidoDebeFallar() {
        resultadoDTO.setPuntaje(BigDecimal.valueOf(-5));

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> resultadoService.registrarResultado(resultadoDTO)
        );

        assertEquals("El puntaje debe ser mayor o igual a cero", exception.getMessage());
        verify(resultadoDAO, never()).save(any());
    }

    /**
     * ✅ Obtener resultado existente
     */
    @Test
    void getResultadoById_Exitoso() {
        ResultadoDTO mockResultado = new ResultadoDTO(1, BigDecimal.TEN, LocalDate.now(), 1, null, 10, null, null, null, null);
        when(resultadoDAO.findById(1)).thenReturn(Optional.of(mockResultado));

        ResultadoDTO result = resultadoService.getResultadoById(1);

        assertEquals(1, result.getIdResultado());
        verify(resultadoDAO).findById(1);
    }

    /**
     * 🚫 Error al buscar resultado inexistente
     */
    @Test
    void getResultadoById_NoExisteDebeFallar() {
        when(resultadoDAO.findById(99)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> resultadoService.getResultadoById(99)
        );

        assertEquals("Resultado no encontrado con ID: 99", exception.getMessage());
    }

    /**
     * ✅ Calcular resultado automático exitoso
     */
    @Test
    void calcularResultadoAutomatico_Exitoso() {
        when(resultadoDAO.existeResultado(1, 10)).thenReturn(false);
        when(respuestaUsuarioService.calcularPuntajeEvaluacion(1, 10))
                .thenReturn(BigDecimal.valueOf(90));
        when(resultadoDAO.save(any(ResultadoDTO.class))).thenAnswer(invocation -> {
            ResultadoDTO arg = invocation.getArgument(0);
            arg.setIdResultado(101);
            return arg;
        });

        ResultadoDTO result = resultadoService.calcularResultadoAutomatico(1, 10);

        assertEquals(BigDecimal.valueOf(90), result.getPuntaje());
        assertNotNull(result.getFechaRealizacion());
        verify(respuestaUsuarioService).calcularPuntajeEvaluacion(1, 10);
        verify(resultadoDAO).save(any(ResultadoDTO.class));
    }

    /**
     * 🚫 Calcular resultado automático: ya existe resultado
     */
    @Test
    void calcularResultadoAutomatico_YaExisteDebeFallar() {
        when(resultadoDAO.existeResultado(1, 10)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> resultadoService.calcularResultadoAutomatico(1, 10));
    }
}
