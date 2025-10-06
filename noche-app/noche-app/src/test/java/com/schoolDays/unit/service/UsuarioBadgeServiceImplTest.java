package com.schoolDays.unit.service;

import com.schoolDays.noche_app.businessLayer.dto.UsuarioBadgeDTO;
import com.schoolDays.noche_app.businessLayer.dto.BadgeDTO;
import com.schoolDays.noche_app.businessLayer.service.impl.UsuarioBadgeServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.UsuarioBadgeDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.UsuarioDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.BadgeDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.InscripcionDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioBadgeServiceImplTest {

    @Mock
    private UsuarioBadgeDAO usuarioBadgeDAO;
    @Mock
    private UsuarioDAO usuarioDAO;
    @Mock
    private BadgeDAO badgeDAO;
    @Mock
    private InscripcionDAO inscripcionDAO;

    @InjectMocks
    private UsuarioBadgeServiceImpl usuarioBadgeService;

    private UsuarioBadgeDTO usuarioBadgeDTO;
    private BadgeDTO badgeDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioBadgeDTO = new UsuarioBadgeDTO();
        usuarioBadgeDTO.setIdUsuarioBadge(1);
        usuarioBadgeDTO.setIdUsuario(10);
        usuarioBadgeDTO.setIdBadge(5);
        usuarioBadgeDTO.setFechaOtorgado(LocalDate.now());

        badgeDTO = new BadgeDTO(5, "Primer Paso", "Completar tu primer curso", "icon.png");
    }

    // Test otorgarBadge - Caso exitoso
    @Test
    void testOtorgarBadge_Success() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.of(new com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO()));
        when(badgeDAO.findById(5)).thenReturn(Optional.of(badgeDTO));
        when(usuarioBadgeDAO.existeAsignacion(10, 5)).thenReturn(false);
        when(usuarioBadgeDAO.save(any())).thenReturn(usuarioBadgeDTO);

        UsuarioBadgeDTO result = usuarioBadgeService.otorgarBadge(10, 5);

        assertNotNull(result);
        assertEquals(10, result.getIdUsuario());
        verify(usuarioBadgeDAO).save(any());
    }

    // Test otorgarBadge - Usuario no existe
    @Test
    void testOtorgarBadge_UsuarioNoExiste() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.empty());
        when(badgeDAO.findById(5)).thenReturn(Optional.of(badgeDTO));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioBadgeService.otorgarBadge(10, 5));
        assertEquals("Usuario no encontrado con ID: 10", ex.getMessage());
    }

    // Test otorgarBadge - Badge ya asignado
    @Test
    void testOtorgarBadge_UsuarioYaTieneBadge() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.of(new com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO()));
        when(badgeDAO.findById(5)).thenReturn(Optional.of(badgeDTO));
        when(usuarioBadgeDAO.existeAsignacion(10, 5)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioBadgeService.otorgarBadge(10, 5));
        assertEquals("El usuario ya tiene este badge asignado", ex.getMessage());
    }

    // Test getUsuarioBadgeById - Existe
    @Test
    void testGetUsuarioBadgeById_Success() {
        when(usuarioBadgeDAO.findById(1)).thenReturn(Optional.of(usuarioBadgeDTO));

        UsuarioBadgeDTO result = usuarioBadgeService.getUsuarioBadgeById(1);

        assertNotNull(result);
        assertEquals(1, result.getIdUsuarioBadge());
    }

    //  Test getUsuarioBadgeById - No existe
    @Test
    void testGetUsuarioBadgeById_NotFound() {
        when(usuarioBadgeDAO.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioBadgeService.getUsuarioBadgeById(99));
        assertEquals("Asignación de badge no encontrada con ID: 99", ex.getMessage());
    }

    // Test revocarBadge - Éxito
    @Test
    void testRevocarBadge_Success() {
        when(usuarioBadgeDAO.findById(1)).thenReturn(Optional.of(usuarioBadgeDTO));
        when(usuarioBadgeDAO.deleteById(1)).thenReturn(true);

        assertDoesNotThrow(() -> usuarioBadgeService.revocarBadge(1));
        verify(usuarioBadgeDAO).deleteById(1);
    }

    // Test revocarBadge - Falla al eliminar
    @Test
    void testRevocarBadge_DeleteFails() {
        when(usuarioBadgeDAO.findById(1)).thenReturn(Optional.of(usuarioBadgeDTO));
        when(usuarioBadgeDAO.deleteById(1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioBadgeService.revocarBadge(1));
        assertEquals("Error al revocar badge con ID: 1", ex.getMessage());
    }

    // Test otorgarBadgeAutomatico - Cumple criterio
    @Test
    void testOtorgarBadgeAutomatico_Success() {
        when(badgeDAO.findById(5)).thenReturn(Optional.of(badgeDTO));
        when(inscripcionDAO.countCursosCompletadosByUsuario(10)).thenReturn(2L);
        when(usuarioDAO.findById(10)).thenReturn(Optional.of(new com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO()));
        when(usuarioBadgeDAO.existeAsignacion(10, 5)).thenReturn(false);
        when(usuarioBadgeDAO.save(any())).thenReturn(usuarioBadgeDTO);

        UsuarioBadgeDTO result = usuarioBadgeService.otorgarBadgeAutomatico(10, 5);

        assertNotNull(result);
        assertEquals(5, result.getIdBadge());
    }

    // Test otorgarBadgeAutomatico - No cumple criterio
    @Test
    void testOtorgarBadgeAutomatico_NoCumpleCriterio() {
        when(badgeDAO.findById(5)).thenReturn(Optional.of(badgeDTO));
        when(inscripcionDAO.countCursosCompletadosByUsuario(10)).thenReturn(0L);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioBadgeService.otorgarBadgeAutomatico(10, 5));
        assertEquals("El usuario no cumple los criterios para este badge", ex.getMessage());
    }

    // Test getBadgesByUsuario
    @Test
    void testGetBadgesByUsuario_Success() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.of(new com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO()));
        when(usuarioBadgeDAO.findByUsuario(10)).thenReturn(List.of(usuarioBadgeDTO));

        List<UsuarioBadgeDTO> result = usuarioBadgeService.getBadgesByUsuario(10);
        assertEquals(1, result.size());
    }

    // Test getBadgesByUsuario - Usuario no existe
    @Test
    void testGetBadgesByUsuario_UsuarioNoExiste() {
        when(usuarioDAO.findById(10)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioBadgeService.getBadgesByUsuario(10));
        assertEquals("Usuario no encontrado con ID: 10", ex.getMessage());
    }
}
