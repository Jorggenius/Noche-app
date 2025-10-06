package com.schoolDays.unit.service;

import com.schoolDays.noche_app.businessLayer.dto.RespuestaDTO;
import com.schoolDays.noche_app.businessLayer.dto.RespuestaUsuarioDTO;
import com.schoolDays.noche_app.businessLayer.service.PreguntaService;
import com.schoolDays.noche_app.businessLayer.service.RespuestaService;
import com.schoolDays.noche_app.businessLayer.service.UsuarioService;
import com.schoolDays.noche_app.businessLayer.service.impl.RespuestaUsuarioServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.RespuestaUsuarioDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RespuestaUsuarioServiceImplTest {

    @Mock
    private RespuestaUsuarioDAO respuestaUsuarioDAO;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private PreguntaService preguntaService;

    @Mock
    private RespuestaService respuestaService;

    @InjectMocks
    private RespuestaUsuarioServiceImpl respuestaUsuarioService;

    private RespuestaUsuarioDTO respuestaUsuarioDTO;

    @BeforeEach
    void setUp() {
        respuestaUsuarioDTO = new RespuestaUsuarioDTO();
        respuestaUsuarioDTO.setIdUsuario(1);
        respuestaUsuarioDTO.setIdPregunta(2);
        respuestaUsuarioDTO.setIdEvaluacion(3);
        respuestaUsuarioDTO.setIdRespuestaSeleccionada(5);
    }

    //  Crear respuesta con opción seleccionada (correcta)
    @Test
    void registrarRespuesta_ConOpcionCorrecta_Exitoso() {
        RespuestaDTO respuestaCorrecta = new RespuestaDTO(5, "Opción A", true, 1, 2, null);
        when(respuestaService.getRespuestaById(5)).thenReturn(respuestaCorrecta);
        when(usuarioService.getUsuarioById(1)).thenReturn(null);
        when(preguntaService.getPreguntaById(2)).thenReturn(null);
        when(respuestaUsuarioDAO.save(any())).thenAnswer(inv -> inv.getArgument(0));

        RespuestaUsuarioDTO result = respuestaUsuarioService.registrarRespuesta(respuestaUsuarioDTO);

        assertTrue(result.getCorrecta());
        assertEquals(BigDecimal.TEN, result.getPuntuacion());
        assertNotNull(result.getFecha());
        verify(respuestaUsuarioDAO).save(any());
    }

    //  Crear respuesta con opción incorrecta
    @Test
    void registrarRespuesta_ConOpcionIncorrecta_Exitoso() {
        RespuestaDTO incorrecta = new RespuestaDTO(6, "Opción B", false, 2, 2, null);
        when(respuestaService.getRespuestaById(6)).thenReturn(incorrecta);
        when(usuarioService.getUsuarioById(1)).thenReturn(null);
        when(preguntaService.getPreguntaById(2)).thenReturn(null);
        when(respuestaUsuarioDAO.save(any())).thenAnswer(inv -> inv.getArgument(0));

        respuestaUsuarioDTO.setIdRespuestaSeleccionada(6);
        RespuestaUsuarioDTO result = respuestaUsuarioService.registrarRespuesta(respuestaUsuarioDTO);

        assertFalse(result.getCorrecta());
        assertEquals(BigDecimal.ZERO, result.getPuntuacion());
    }

    // Error: Falta usuario
    @Test
    void registrarRespuesta_FaltaUsuario_LanzaExcepcion() {
        respuestaUsuarioDTO.setIdUsuario(null);
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> respuestaUsuarioService.registrarRespuesta(respuestaUsuarioDTO));
        assertEquals("El usuario es obligatorio", ex.getMessage());
    }

    // Error: Falta pregunta
    @Test
    void registrarRespuesta_FaltaPregunta_LanzaExcepcion() {
        respuestaUsuarioDTO.setIdPregunta(null);
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> respuestaUsuarioService.registrarRespuesta(respuestaUsuarioDTO));
        assertEquals("La pregunta es obligatoria", ex.getMessage());
    }

    // Error: Falta evaluación
    @Test
    void registrarRespuesta_FaltaEvaluacion_LanzaExcepcion() {
        respuestaUsuarioDTO.setIdEvaluacion(null);
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> respuestaUsuarioService.registrarRespuesta(respuestaUsuarioDTO));
        assertEquals("La evaluación es obligatoria", ex.getMessage());
    }

    // Error: Sin respuesta ni texto
    @Test
    void registrarRespuesta_SinContenido_LanzaExcepcion() {
        respuestaUsuarioDTO.setIdRespuestaSeleccionada(null);
        respuestaUsuarioDTO.setRespuestaTexto(null);
        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> respuestaUsuarioService.registrarRespuesta(respuestaUsuarioDTO));
        assertEquals("Debe proporcionar una respuesta", ex.getMessage());
    }

    // Buscar por ID
    @Test
    void getRespuestaUsuarioById_Exitoso() {
        respuestaUsuarioDTO.setIdRespuestaUsuario(1);
        when(respuestaUsuarioDAO.findById(1)).thenReturn(Optional.of(respuestaUsuarioDTO));

        RespuestaUsuarioDTO result = respuestaUsuarioService.getRespuestaUsuarioById(1);

        assertEquals(1, result.getIdRespuestaUsuario());
    }

    // Buscar por ID inexistente
    @Test
    void getRespuestaUsuarioById_NoExiste_LanzaExcepcion() {
        when(respuestaUsuarioDAO.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> respuestaUsuarioService.getRespuestaUsuarioById(1));
    }

    // Calificación automática
    @Test
    void calificarRespuestaAutomatica_Correcta_Exitoso() {
        RespuestaUsuarioDTO existente = new RespuestaUsuarioDTO();
        existente.setIdRespuestaSeleccionada(10);
        when(respuestaUsuarioDAO.findById(1)).thenReturn(Optional.of(existente));

        RespuestaDTO opcion = new RespuestaDTO(10, "Opción correcta", true, 1, 2, null);
        when(respuestaService.getRespuestaById(10)).thenReturn(opcion);
        when(respuestaUsuarioDAO.update(eq(1), any())).thenReturn(Optional.of(new RespuestaUsuarioDTO()));

        RespuestaUsuarioDTO result = respuestaUsuarioService.calificarRespuestaAutomatica(1);

        assertNotNull(result);
        verify(respuestaUsuarioDAO).update(eq(1), any());
    }

    // Calificación manual
    @Test
    void calificarRespuestaManual_Exitoso() {
        RespuestaUsuarioDTO existente = new RespuestaUsuarioDTO();
        existente.setIdRespuestaUsuario(1);
        existente.setPuntuacion(BigDecimal.ZERO);
        when(respuestaUsuarioDAO.findById(1)).thenReturn(Optional.of(existente));

        RespuestaUsuarioDTO actualizado = new RespuestaUsuarioDTO();
        actualizado.setPuntuacion(BigDecimal.TEN);
        when(respuestaUsuarioDAO.update(eq(1), any())).thenReturn(Optional.of(actualizado));

        RespuestaUsuarioDTO result = respuestaUsuarioService.calificarRespuestaManual(1, BigDecimal.TEN, true);

        assertEquals(BigDecimal.TEN, result.getPuntuacion());
        verify(respuestaUsuarioDAO).update(eq(1), any());
    }


    // Calificación manual con puntuación negativa
    @Test
    void calificarRespuestaManual_PuntuacionNegativa_LanzaExcepcion() {
        assertThrows(IllegalArgumentException.class, () ->
                respuestaUsuarioService.calificarRespuestaManual(1, new BigDecimal("-5"), true));
    }

    // Calcular puntaje evaluación
    @Test
    void calcularPuntajeEvaluacion_Exitoso() {
        RespuestaUsuarioDTO r1 = new RespuestaUsuarioDTO();
        r1.setPuntuacion(BigDecimal.valueOf(5));
        RespuestaUsuarioDTO r2 = new RespuestaUsuarioDTO();
        r2.setPuntuacion(BigDecimal.valueOf(7));
        when(respuestaUsuarioDAO.findRespuestasEnEvaluacion(1, 1)).thenReturn(List.of(r1, r2));

        BigDecimal total = respuestaUsuarioService.calcularPuntajeEvaluacion(1, 1);

        assertEquals(BigDecimal.valueOf(12), total);
    }

    // Obtener respuestas correctas
    @Test
    void getRespuestasCorrectasByUsuario_Exitoso() {
        when(respuestaUsuarioDAO.findCorrectasByUsuario(1)).thenReturn(List.of(new RespuestaUsuarioDTO()));
        assertFalse(respuestaUsuarioService.getRespuestasCorrectasByUsuario(1).isEmpty());
    }

    // Obtener conteo correctas
    @Test
    void getCountRespuestasCorrectas_Exitoso() {
        when(respuestaUsuarioDAO.countCorrectasByUsuario(1)).thenReturn(3L);
        assertEquals(3L, respuestaUsuarioService.getCountRespuestasCorrectas(1));
    }
}
