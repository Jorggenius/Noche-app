package com.schoolDays.unit.service;

import com.schoolDays.noche_app.businessLayer.dto.CursoDTO;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO;
import com.schoolDays.noche_app.businessLayer.service.UsuarioService;
import com.schoolDays.noche_app.businessLayer.service.impl.CursoServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.CursoDAO;
import com.schoolDays.noche_app.persistenceLayer.entity.CursoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CursoServiceImplTest {

    @Mock
    private CursoDAO cursoDAO;

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private CursoServiceImpl cursoService;

    private CursoDTO cursoDTO;
    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cursoDTO = new CursoDTO(1, "Java Avanzado", "Curso completo de Java",
                40, "AVANZADO", LocalDate.now(), 1, 10, "Carlos Pérez");

        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdUsuario(10);
        usuarioDTO.setNombreRol("INSTRUCTOR");
    }

    // --------------------- CREAR CURSO ---------------------

    @Test
    void createCurso_Exitoso() {
        when(usuarioService.getUsuarioById(10)).thenReturn(usuarioDTO);
        when(cursoDAO.save(any())).thenReturn(cursoDTO);

        CursoDTO result = cursoService.createCurso(cursoDTO);

        assertNotNull(result);
        verify(cursoDAO).save(any());
    }

    @Test
    void createCurso_FallaPorRolNoPermitido() {
        usuarioDTO.setNombreRol("ESTUDIANTE");
        when(usuarioService.getUsuarioById(10)).thenReturn(usuarioDTO);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cursoService.createCurso(cursoDTO));

        assertEquals("Solo instructores y administradores pueden crear cursos", ex.getMessage());
    }

    @Test
    void createCurso_FallaPorTituloVacio() {
        cursoDTO.setTitulo("   ");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cursoService.createCurso(cursoDTO));

        assertEquals("El título del curso es obligatorio", ex.getMessage());
    }

    @Test
    void createCurso_FallaPorDuracionInvalida() {
        cursoDTO.setDuracionEstimada(0);
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cursoService.createCurso(cursoDTO));

        assertEquals("La duración debe ser mayor a 0 horas", ex.getMessage());
    }

    // --------------------- OBTENER ---------------------

    @Test
    void getCursoById_Exitoso() {
        when(cursoDAO.findById(1)).thenReturn(Optional.of(cursoDTO));

        CursoDTO result = cursoService.getCursoById(1);

        assertEquals("Java Avanzado", result.getTitulo());
    }

    @Test
    void getCursoById_NoEncontrado() {
        when(cursoDAO.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cursoService.getCursoById(99));

        assertEquals("Curso no encontrado con ID: 99", ex.getMessage());
    }

    @Test
    void getAllCursos_Exitoso() {
        when(cursoDAO.findAll()).thenReturn(List.of(cursoDTO));

        List<CursoDTO> result = cursoService.getAllCursos();

        assertEquals(1, result.size());
        verify(cursoDAO).findAll();
    }

    // --------------------- ACTUALIZAR ---------------------

    @Test
    void updateCurso_Exitoso() {
        when(cursoDAO.findById(1)).thenReturn(Optional.of(cursoDTO));
        when(cursoDAO.update(eq(1), any())).thenReturn(Optional.of(cursoDTO));

        CursoDTO updated = cursoService.updateCurso(1, cursoDTO);

        assertNotNull(updated);
        verify(cursoDAO).update(eq(1), any());
    }

    @Test
    void updateCurso_NoPermiteCambiarCreador() {
        CursoDTO updateDTO = new CursoDTO();
        updateDTO.setCreadoPorId(99);

        when(cursoDAO.findById(1)).thenReturn(Optional.of(cursoDTO));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cursoService.updateCurso(1, updateDTO));

        assertEquals("No se puede cambiar el creador del curso", ex.getMessage());
    }

    @Test
    void updateCurso_ErrorAlActualizar() {
        when(cursoDAO.findById(1)).thenReturn(Optional.of(cursoDTO));
        when(cursoDAO.update(eq(1), any())).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cursoService.updateCurso(1, cursoDTO));

        assertEquals("Error al actualizar curso", ex.getMessage());
    }

    // --------------------- ELIMINAR ---------------------

    @Test
    void deleteCurso_Exitoso() {
        when(cursoDAO.findById(1)).thenReturn(Optional.of(cursoDTO));
        when(cursoDAO.deleteById(1)).thenReturn(true);

        assertDoesNotThrow(() -> cursoService.deleteCurso(1));
    }

    @Test
    void deleteCurso_Error() {
        when(cursoDAO.findById(1)).thenReturn(Optional.of(cursoDTO));
        when(cursoDAO.deleteById(1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cursoService.deleteCurso(1));

        assertEquals("Error al eliminar curso con ID: 1", ex.getMessage());
    }

    // --------------------- FILTROS ---------------------

    @Test
    void getCursosByCreador_Exitoso() {
        when(usuarioService.getUsuarioById(10)).thenReturn(usuarioDTO);
        when(cursoDAO.findByCreador(10)).thenReturn(List.of(cursoDTO));

        List<CursoDTO> result = cursoService.getCursosByCreador(10);

        assertEquals(1, result.size());
        verify(cursoDAO).findByCreador(10);
    }

    @Test
    void getCursosByNivel_Exitoso() {
        when(cursoDAO.findByNivel(CursoEntity.Nivel.AVANZADO)).thenReturn(List.of(cursoDTO));

        List<CursoDTO> result = cursoService.getCursosByNivel(CursoEntity.Nivel.AVANZADO);

        assertEquals(1, result.size());
    }

    @Test
    void searchCursosByTitulo_Exitoso() {
        when(cursoDAO.findByTitulo("Java")).thenReturn(List.of(cursoDTO));

        List<CursoDTO> result = cursoService.searchCursosByTitulo("Java");

        assertEquals(1, result.size());
    }

    @Test
    void searchCursosByTitulo_VacioLanzaExcepcion() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> cursoService.searchCursosByTitulo(" "));

        assertEquals("El título de búsqueda no puede estar vacío", ex.getMessage());
    }
}