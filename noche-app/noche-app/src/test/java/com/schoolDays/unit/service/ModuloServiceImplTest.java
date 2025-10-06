package com.schoolDays.unit.service;

import com.schoolDays.noche_app.businessLayer.dto.ModuloDTO;
import com.schoolDays.noche_app.businessLayer.service.CursoService;
import com.schoolDays.noche_app.businessLayer.service.impl.ModuloServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.ModuloDAO;
import com.schoolDays.noche_app.persistenceLayer.entity.ModuloEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ModuloServiceImplTest {

    @Mock private ModuloDAO moduloDAO;
    @Mock private CursoService cursoService;

    @InjectMocks
    private ModuloServiceImpl moduloService;

    private ModuloDTO moduloDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        moduloDTO = new ModuloDTO();
        moduloDTO.setIdModulo(1);
        moduloDTO.setTitulo("Introducción a Java");
        moduloDTO.setTipo("VIDEO");
        moduloDTO.setOrden(1);
        moduloDTO.setIdCurso(10);
    }

    // ---------------------------------------------------------------------------------
    //  createModulo - éxito
    // ---------------------------------------------------------------------------------
    @Test
    void testCreateModulo_Exito() {
        when(moduloDAO.existeOrdenEnCurso(10, 1)).thenReturn(false);
        when(moduloDAO.save(any(ModuloDTO.class))).thenReturn(moduloDTO);
        when(cursoService.getCursoById(10)).thenReturn(null);

        ModuloDTO result = moduloService.createModulo(moduloDTO);

        assertNotNull(result);
        assertEquals("Introducción a Java", result.getTitulo());
        verify(moduloDAO).save(any(ModuloDTO.class));
    }

    // curso no existe
    @Test
    void testCreateModulo_CursoNoExiste() {
        doThrow(new RuntimeException("Curso no encontrado")).when(cursoService).getCursoById(10);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> moduloService.createModulo(moduloDTO));
        assertEquals("Curso no encontrado", ex.getMessage());
    }

    // orden repetido
    @Test
    void testCreateModulo_OrdenRepetido() {
        when(cursoService.getCursoById(10)).thenReturn(null);
        when(moduloDAO.existeOrdenEnCurso(10, 1)).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> moduloService.createModulo(moduloDTO));
    }

    // datos inválidos
    @Test
    void testCreateModulo_TituloVacio() {
        moduloDTO.setTitulo("");
        assertThrows(IllegalArgumentException.class, () -> moduloService.createModulo(moduloDTO));
    }

    @Test
    void testCreateModulo_OrdenInvalido() {
        moduloDTO.setOrden(0);
        assertThrows(IllegalArgumentException.class, () -> moduloService.createModulo(moduloDTO));
    }

    // ---------------------------------------------------------------------------------
    // getModuloById
    // ---------------------------------------------------------------------------------
    @Test
    void testGetModuloById_Exito() {
        when(moduloDAO.findById(1)).thenReturn(Optional.of(moduloDTO));
        ModuloDTO result = moduloService.getModuloById(1);
        assertEquals(1, result.getIdModulo());
    }

    @Test
    void testGetModuloById_NoExiste() {
        when(moduloDAO.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> moduloService.getModuloById(1));
    }

    // ---------------------------------------------------------------------------------
    // updateModulo
    // ---------------------------------------------------------------------------------
    @Test
    void testUpdateModulo_Exito() {
        when(moduloDAO.findById(1)).thenReturn(Optional.of(moduloDTO));
        when(moduloDAO.update(eq(1), any())).thenReturn(Optional.of(moduloDTO));

        ModuloDTO nuevo = new ModuloDTO();
        nuevo.setTitulo("Actualizado");
        ModuloDTO result = moduloService.updateModulo(1, nuevo);

        assertEquals("Introducción a Java", result.getTitulo());
        verify(moduloDAO).update(eq(1), any());
    }

    @Test
    void testUpdateModulo_NoExiste() {
        when(moduloDAO.findById(1)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> moduloService.updateModulo(1, moduloDTO));
    }

    @Test
    void testUpdateModulo_ErrorEnUpdate() {
        when(moduloDAO.findById(1)).thenReturn(Optional.of(moduloDTO));
        when(moduloDAO.update(eq(1), any())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> moduloService.updateModulo(1, moduloDTO));
    }

    @Test
    void testUpdateModulo_CambiaCurso() {
        when(moduloDAO.findById(1)).thenReturn(Optional.of(moduloDTO));
        ModuloDTO nuevo = new ModuloDTO();
        nuevo.setIdCurso(99);

        assertThrows(IllegalArgumentException.class, () -> moduloService.updateModulo(1, nuevo));
    }

    // ---------------------------------------------------------------------------------
    // deleteModulo
    // ---------------------------------------------------------------------------------
    @Test
    void testDeleteModulo_Exito() {
        when(moduloDAO.findById(1)).thenReturn(Optional.of(moduloDTO));
        when(moduloDAO.deleteById(1)).thenReturn(true);

        assertDoesNotThrow(() -> moduloService.deleteModulo(1));
        verify(moduloDAO).deleteById(1);
    }

    @Test
    void testDeleteModulo_Error() {
        when(moduloDAO.findById(1)).thenReturn(Optional.of(moduloDTO));
        when(moduloDAO.deleteById(1)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> moduloService.deleteModulo(1));
    }

    // ---------------------------------------------------------------------------------
    // getModulosByCursoOrdenados
    // ---------------------------------------------------------------------------------
    @Test
    void testGetModulosByCursoOrdenados_Exito() {
        when(cursoService.getCursoById(10)).thenReturn(null);
        when(moduloDAO.findByCursoOrdenados(10)).thenReturn(List.of(moduloDTO));

        List<ModuloDTO> result = moduloService.getModulosByCursoOrdenados(10);

        assertEquals(1, result.size());
    }

    // ---------------------------------------------------------------------------------
    // getPrimerModuloCurso
    // ---------------------------------------------------------------------------------
    @Test
    void testGetPrimerModuloCurso_Exito() {
        when(cursoService.getCursoById(10)).thenReturn(null);
        when(moduloDAO.findPrimerModulo(10)).thenReturn(Optional.of(moduloDTO));

        ModuloDTO result = moduloService.getPrimerModuloCurso(10);
        assertEquals(1, result.getIdModulo());
    }

    @Test
    void testGetPrimerModuloCurso_Vacio() {
        when(cursoService.getCursoById(10)).thenReturn(null);
        when(moduloDAO.findPrimerModulo(10)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> moduloService.getPrimerModuloCurso(10));
    }

    // ---------------------------------------------------------------------------------
    // getUltimoModuloCurso
    // ---------------------------------------------------------------------------------
    @Test
    void testGetUltimoModuloCurso_Exito() {
        when(cursoService.getCursoById(10)).thenReturn(null);
        when(moduloDAO.findUltimoModulo(10)).thenReturn(Optional.of(moduloDTO));

        ModuloDTO result = moduloService.getUltimoModuloCurso(10);
        assertEquals(1, result.getIdModulo());
    }

    @Test
    void testGetUltimoModuloCurso_Vacio() {
        when(cursoService.getCursoById(10)).thenReturn(null);
        when(moduloDAO.findUltimoModulo(10)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> moduloService.getUltimoModuloCurso(10));
    }

    // ---------------------------------------------------------------------------------
    // cambiarOrdenModulo
    // ---------------------------------------------------------------------------------
    @Test
    void testCambiarOrdenModulo_Exito() {
        when(moduloDAO.findById(1)).thenReturn(Optional.of(moduloDTO));
        when(moduloDAO.update(eq(1), any())).thenReturn(Optional.of(moduloDTO));

        ModuloDTO result = moduloService.cambiarOrdenModulo(1, 2);

        assertNotNull(result);
        verify(moduloDAO).update(eq(1), any());
    }

    @Test
    void testCambiarOrdenModulo_OrdenInvalido() {
        when(moduloDAO.findById(1)).thenReturn(Optional.of(moduloDTO));
        assertThrows(IllegalArgumentException.class, () -> moduloService.cambiarOrdenModulo(1, 0));
    }

    // ---------------------------------------------------------------------------------
    // getModulosCountByCurso
    // ---------------------------------------------------------------------------------
    @Test
    void testGetModulosCountByCurso_Exito() {
        when(cursoService.getCursoById(10)).thenReturn(null);
        when(moduloDAO.countByCurso(10)).thenReturn(5L);

        long count = moduloService.getModulosCountByCurso(10);
        assertEquals(5L, count);
    }
}
