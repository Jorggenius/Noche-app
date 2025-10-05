package com.schoolDays.unit.service;

import com.schoolDays.noche_app.businessLayer.dto.*;
import com.schoolDays.noche_app.businessLayer.service.impl.InscripcionServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.*;
import com.schoolDays.noche_app.persistenceLayer.entity.InscripcionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class InscripcionServiceImplTest {

    @Mock private InscripcionDAO inscripcionDAO;
    @Mock private UsuarioDAO usuarioDAO;
    @Mock private CursoDAO cursoDAO;
    @Mock private BadgeDAO badgeDAO;
    @Mock private UsuarioBadgeDAO usuarioBadgeDAO;
    @InjectMocks
    private InscripcionServiceImpl inscripcionService;

    private InscripcionDTO inscripcionDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        inscripcionDTO = new InscripcionDTO();
        inscripcionDTO.setIdInscripcion(1);
        inscripcionDTO.setIdUsuario(10);
        inscripcionDTO.setIdCurso(20);
        inscripcionDTO.setEstado("INSCRITO");
        inscripcionDTO.setProgreso(BigDecimal.ZERO);
        inscripcionDTO.setFechaInscripcion(LocalDate.now());
    }

    // ---------------------------------------------------------------------------------
    // ✅ TEST: inscribirUsuario - éxito
    // ---------------------------------------------------------------------------------
    @Test
    void testInscribirUsuario_Exito() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.of(new UsuarioDTO()));
        when(cursoDAO.findById(20)).thenReturn(Optional.of(new CursoDTO()));
        when(inscripcionDAO.existeInscripcion(10, 20)).thenReturn(false);
        when(inscripcionDAO.save(any(InscripcionDTO.class))).thenReturn(inscripcionDTO);

        InscripcionDTO result = inscripcionService.inscribirUsuario(inscripcionDTO);

        assertNotNull(result);
        assertEquals("INSCRITO", result.getEstado());
        verify(inscripcionDAO).save(any());
    }

    // ---------------------------------------------------------------------------------
    // ❌ TEST: inscribirUsuario - usuario no encontrado
    // ---------------------------------------------------------------------------------
    @Test
    void testInscribirUsuario_UsuarioNoEncontrado() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.empty());
        when(cursoDAO.findById(20)).thenReturn(Optional.of(new CursoDTO()));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> inscripcionService.inscribirUsuario(inscripcionDTO));

        assertEquals("Usuario no encontrado con ID: 10", ex.getMessage());
    }

    // ❌ TEST: inscribirUsuario - curso no encontrado
    @Test
    void testInscribirUsuario_CursoNoEncontrado() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.of(new UsuarioDTO()));
        when(cursoDAO.findById(20)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> inscripcionService.inscribirUsuario(inscripcionDTO));

        assertEquals("Curso no encontrado con ID: 20", ex.getMessage());
    }

    // ❌ TEST: inscribirUsuario - ya inscrito
    @Test
    void testInscribirUsuario_YaInscrito() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.of(new UsuarioDTO()));
        when(cursoDAO.findById(20)).thenReturn(Optional.of(new CursoDTO()));
        when(inscripcionDAO.existeInscripcion(10, 20)).thenReturn(true);

        assertThrows(IllegalArgumentException.class,
                () -> inscripcionService.inscribirUsuario(inscripcionDTO));
    }

    // ❌ TEST: inscribirUsuario - datos inválidos
    @Test
    void testInscribirUsuario_DatosInvalidos() {
        InscripcionDTO dto = new InscripcionDTO();
        dto.setIdUsuario(null);

        assertThrows(IllegalArgumentException.class,
                () -> inscripcionService.inscribirUsuario(dto));
    }

    // ---------------------------------------------------------------------------------
    // ✅ TEST: getInscripcionById
    // ---------------------------------------------------------------------------------
    @Test
    void testGetInscripcionById_Exito() {
        when(inscripcionDAO.findById(1)).thenReturn(Optional.of(inscripcionDTO));

        InscripcionDTO result = inscripcionService.getInscripcionById(1);

        assertEquals(1, result.getIdInscripcion());
    }

    @Test
    void testGetInscripcionById_NoExiste() {
        when(inscripcionDAO.findById(1)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> inscripcionService.getInscripcionById(1));
    }

    // ---------------------------------------------------------------------------------
    // ✅ TEST: updateProgreso - éxito y cambio de estado
    // ---------------------------------------------------------------------------------
    @Test
    void testUpdateProgreso_EnProgreso() {
        when(inscripcionDAO.findById(1)).thenReturn(Optional.of(inscripcionDTO));
        when(inscripcionDAO.update(eq(1), any())).thenReturn(Optional.of(inscripcionDTO));

        InscripcionDTO result = inscripcionService.updateProgreso(1, new BigDecimal("50"));

        assertNotNull(result);
        verify(inscripcionDAO).update(eq(1), any());
    }

    @Test
    void testUpdateProgreso_Completado() {
        when(inscripcionDAO.findById(1)).thenReturn(Optional.of(inscripcionDTO));
        when(inscripcionDAO.update(eq(1), any())).thenReturn(Optional.of(inscripcionDTO));
        when(inscripcionDAO.countCursosCompletadosByUsuario(10)).thenReturn(1L);
        when(badgeDAO.findAll()).thenReturn(Collections.emptyList());

        InscripcionDTO result = inscripcionService.updateProgreso(1, new BigDecimal("100"));

        assertNotNull(result);
        verify(inscripcionDAO).update(eq(1), any());
    }

    @Test
    void testUpdateProgreso_ValorInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> inscripcionService.updateProgreso(1, new BigDecimal("200")));
    }

    // ---------------------------------------------------------------------------------
    // ✅ TEST: cancelarInscripcion
    // ---------------------------------------------------------------------------------
    @Test
    void testCancelarInscripcion() {
        inscripcionService.cancelarInscripcion(1);
        verify(inscripcionDAO).update(eq(1), any());
    }

    // ---------------------------------------------------------------------------------
    // ✅ TEST: puedeInscribirse
    // ---------------------------------------------------------------------------------
    @Test
    void testPuedeInscribirse_True() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.of(new UsuarioDTO()));
        when(cursoDAO.findById(20)).thenReturn(Optional.of(new CursoDTO()));
        when(inscripcionDAO.existeInscripcion(10, 20)).thenReturn(false);

        boolean result = inscripcionService.puedeInscribirse(10, 20);

        assertTrue(result);
    }

    @Test
    void testPuedeInscribirse_False() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.empty());
        boolean result = inscripcionService.puedeInscribirse(10, 20);
        assertFalse(result);
    }

    // ---------------------------------------------------------------------------------
    // ✅ TEST: getCursosCompletadosByUsuario
    // ---------------------------------------------------------------------------------
    @Test
    void testGetCursosCompletadosByUsuario_Exito() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.of(new UsuarioDTO()));
        when(inscripcionDAO.countCursosCompletadosByUsuario(10)).thenReturn(3L);

        long count = inscripcionService.getCursosCompletadosByUsuario(10);

        assertEquals(3L, count);
    }

    @Test
    void testGetCursosCompletadosByUsuario_NoExiste() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> inscripcionService.getCursosCompletadosByUsuario(10));
    }
}
