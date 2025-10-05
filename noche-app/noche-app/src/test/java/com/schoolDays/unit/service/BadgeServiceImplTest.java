package com.schoolDays.unit.service;

import com.schoolDays.noche_app.businessLayer.dto.BadgeDTO;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioBadgeDTO;
import com.schoolDays.noche_app.businessLayer.service.impl.BadgeServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.BadgeDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.InscripcionDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.UsuarioBadgeDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BadgeServiceImplTest {

    @Mock
    private BadgeDAO badgeDAO;

    @Mock
    private InscripcionDAO inscripcionDAO;

    @Mock
    private UsuarioBadgeDAO usuarioBadgeDAO;

    @Mock
    private Logger log;

    @InjectMocks
    private BadgeServiceImpl badgeService;

    private BadgeDTO badgeDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        badgeDTO = new BadgeDTO(1, "Primer Paso", "Completar tu primer curso", "icon.png");
    }

    // ===========================
    // CREATE
    // ===========================

    @Test
    void testCreateBadge_Success() {
        when(badgeDAO.existsByNombre("Primer Paso")).thenReturn(false);
        when(badgeDAO.save(any(BadgeDTO.class))).thenReturn(badgeDTO);

        BadgeDTO result = badgeService.createBadge(badgeDTO);

        assertNotNull(result);
        assertEquals("Primer Paso", result.getNombre());
        verify(badgeDAO).save(any(BadgeDTO.class));
    }

    @Test
    void testCreateBadge_FailsIfNameExists() {
        when(badgeDAO.existsByNombre("Primer Paso")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> badgeService.createBadge(badgeDTO));

        assertEquals("Ya existe un badge con el nombre: Primer Paso", ex.getMessage());
        verify(badgeDAO, never()).save(any());
    }

    @Test
    void testCreateBadge_FailsIfInvalidData() {
        badgeDTO.setNombre("");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> badgeService.createBadge(badgeDTO));

        assertEquals("El nombre del badge es obligatorio", ex.getMessage());
    }

    // ===========================
    // GET BY ID
    // ===========================

    @Test
    void testGetBadgeById_Success() {
        when(badgeDAO.findById(1)).thenReturn(Optional.of(badgeDTO));

        BadgeDTO result = badgeService.getBadgeById(1);

        assertEquals("Primer Paso", result.getNombre());
        verify(badgeDAO).findById(1);
    }

    @Test
    void testGetBadgeById_NotFound() {
        when(badgeDAO.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> badgeService.getBadgeById(1));

        assertEquals("Badge no encontrado con ID: 1", ex.getMessage());
    }

    // ===========================
    // GET ALL
    // ===========================

    @Test
    void testGetAllBadges() {
        when(badgeDAO.findAll()).thenReturn(List.of(badgeDTO));

        List<BadgeDTO> result = badgeService.getAllBadges();

        assertEquals(1, result.size());
        verify(badgeDAO).findAll();
    }

    // ===========================
    // UPDATE
    // ===========================

    @Test
    void testUpdateBadge_Success() {
        when(badgeDAO.findById(1)).thenReturn(Optional.of(badgeDTO));
        when(badgeDAO.update(eq(1), any(BadgeDTO.class))).thenReturn(Optional.of(badgeDTO));

        BadgeDTO result = badgeService.updateBadge(1, badgeDTO);

        assertEquals("Primer Paso", result.getNombre());
        verify(badgeDAO).update(eq(1), any());
    }

    @Test
    void testUpdateBadge_FailsIfNotFound() {
        when(badgeDAO.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> badgeService.updateBadge(1, badgeDTO));

        assertEquals("Badge no encontrado con ID: 1", ex.getMessage());
    }

    @Test
    void testUpdateBadge_FailsIfInvalidData() {
        when(badgeDAO.findById(1)).thenReturn(Optional.of(badgeDTO));
        badgeDTO.setCriterio("  ");
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> badgeService.updateBadge(1, badgeDTO));

        assertEquals("El criterio no puede estar vacío", ex.getMessage());
    }

    // ===========================
    // DELETE
    // ===========================

    @Test
    void testDeleteBadge_Success() {
        when(badgeDAO.findById(1)).thenReturn(Optional.of(badgeDTO));
        when(badgeDAO.deleteById(1)).thenReturn(true);

        badgeService.deleteBadge(1);

        verify(badgeDAO).deleteById(1);
    }

    @Test
    void testDeleteBadge_FailsIfNotFound() {
        when(badgeDAO.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> badgeService.deleteBadge(1));

        assertEquals("Badge no encontrado con ID: 1", ex.getMessage());
    }

    @Test
    void testDeleteBadge_FailsIfDeleteReturnsFalse() {
        when(badgeDAO.findById(1)).thenReturn(Optional.of(badgeDTO));
        when(badgeDAO.deleteById(1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> badgeService.deleteBadge(1));

        assertEquals("Error al eliminar badge con ID: 1", ex.getMessage());
    }

    // ===========================
    // SEARCH
    // ===========================

    @Test
    void testSearchBadgesByNombre_Success() {
        when(badgeDAO.findByNombre("Paso")).thenReturn(List.of(badgeDTO));

        List<BadgeDTO> result = badgeService.searchBadgesByNombre("Paso");

        assertEquals(1, result.size());
        verify(badgeDAO).findByNombre("Paso");
    }

    @Test
    void testSearchBadgesByNombre_FailsIfEmpty() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> badgeService.searchBadgesByNombre(" "));

        assertEquals("El nombre de búsqueda no puede estar vacío", ex.getMessage());
    }

    // ===========================
    // DISPONIBILIDAD NOMBRE
    // ===========================

    @Test
    void testIsNombreDisponible_True() {
        when(badgeDAO.existsByNombre("Nuevo")).thenReturn(false);

        assertTrue(badgeService.isNombreDisponible("Nuevo"));
    }

    @Test
    void testIsNombreDisponible_False() {
        when(badgeDAO.existsByNombre("Primer Paso")).thenReturn(true);

        assertFalse(badgeService.isNombreDisponible("Primer Paso"));
    }

    // ===========================
    // BADGES AUTOMÁTICOS
    // ===========================

    @Test
    void testProcesarBadgesAutomaticos_Success() {
        when(inscripcionDAO.countCursosCompletadosByUsuario(10)).thenReturn(5L);
        when(badgeDAO.findByNombre(anyString())).thenReturn(List.of(badgeDTO));
        when(usuarioBadgeDAO.existeAsignacion(anyInt(), anyInt())).thenReturn(false);

        badgeService.procesarBadgesAutomaticos(10);

        verify(usuarioBadgeDAO, atLeastOnce()).save(any(UsuarioBadgeDTO.class));
    }

    @Test
    void testCumpleCriterio_PrimerPaso() {
        when(badgeDAO.findById(1)).thenReturn(Optional.of(badgeDTO));
        when(inscripcionDAO.countCursosCompletadosByUsuario(10)).thenReturn(2L);

        assertTrue(badgeService.cumpleCriterio(10, 1));
    }

    @Test
    void testCumpleCriterio_Desconocido() {
        when(badgeDAO.findById(1)).thenReturn(Optional.of(new BadgeDTO(1, "Otro", "x", "i")));

        assertFalse(badgeService.cumpleCriterio(10, 1));
    }

    @Test
    void testOtorgarBadgeSiExiste_FailsSilently() {
        when(badgeDAO.findByNombre("Primer Paso")).thenThrow(new RuntimeException("Error"));

        // método privado -> se prueba indirectamente llamando procesarBadgesAutomaticos
        when(inscripcionDAO.countCursosCompletadosByUsuario(1)).thenReturn(1L);

        assertDoesNotThrow(() -> badgeService.procesarBadgesAutomaticos(1));
    }
}
