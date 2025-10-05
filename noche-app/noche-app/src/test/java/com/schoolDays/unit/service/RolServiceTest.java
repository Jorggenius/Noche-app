package com.schoolDays.unit.service;

import com.schoolDays.noche_app.businessLayer.dto.RolDTO;
import com.schoolDays.noche_app.businessLayer.service.impl.RolServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.RolDAO;
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

class RolServiceImplTest {

    @Mock
    private RolDAO rolDAO;

    @InjectMocks
    private RolServiceImpl rolService;

    private RolDTO adminRol;
    private RolDTO instructorRol;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        adminRol = new RolDTO(1, "ADMIN", "Administrador del sistema");
        instructorRol = new RolDTO(2, "INSTRUCTOR", "Instructor del sistema");
    }

    // -------------------------
    // CREATE
    // -------------------------

    @DisplayName("Crear rol exitosamente")
    @Test
    void createRol_ok() {
        when(rolDAO.existsByNombreRol("ADMIN")).thenReturn(false);
        when(rolDAO.save(adminRol)).thenReturn(adminRol);

        RolDTO result = rolService.createRol(adminRol);

        assertNotNull(result);
        assertEquals("ADMIN", result.getNombreRol());
        verify(rolDAO).save(adminRol);
    }

    @DisplayName("Crear rol con nombre duplicado lanza excepción")
    @Test
    void createRol_duplicateName_throwsException() {
        when(rolDAO.existsByNombreRol("ADMIN")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> rolService.createRol(adminRol));
        assertEquals("Ya existe un rol con el nombre: ADMIN", ex.getMessage());
    }

    @DisplayName("Crear rol con nombre vacío lanza excepción")
    @Test
    void createRol_emptyName_throwsException() {
        RolDTO invalidRol = new RolDTO(null, "   ", "Sin nombre");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> rolService.createRol(invalidRol));
        assertEquals("El nombre del rol es obligatorio", ex.getMessage());
    }

    @DisplayName("Crear rol con nombre no permitido lanza excepción")
    @Test
    void createRol_invalidRoleName_throwsException() {
        RolDTO invalidRol = new RolDTO(null, "MANAGER", "Rol no permitido");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> rolService.createRol(invalidRol));
        assertEquals("Rol no válido. Solo se permiten: ADMIN, INSTRUCTOR, USER", ex.getMessage());
    }

    // -------------------------
    // GET BY ID
    // -------------------------

    @DisplayName("Obtener rol por ID existente")
    @Test
    void getRolById_ok() {
        when(rolDAO.findById(1)).thenReturn(Optional.of(adminRol));

        RolDTO result = rolService.getRolById(1);

        assertEquals("ADMIN", result.getNombreRol());
        verify(rolDAO).findById(1);
    }

    @DisplayName("Obtener rol por ID inexistente lanza excepción")
    @Test
    void getRolById_notFound_throwsException() {
        when(rolDAO.findById(99)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> rolService.getRolById(99));
        assertEquals("Rol no encontrado con ID: 99", ex.getMessage());
    }

    // -------------------------
    // GET ALL
    // -------------------------

    @DisplayName("Listar todos los roles")
    @Test
    void getAllRoles_ok() {
        when(rolDAO.findAll()).thenReturn(List.of(adminRol, instructorRol));

        List<RolDTO> result = rolService.getAllRoles();

        assertEquals(2, result.size());
        verify(rolDAO).findAll();
    }

    // -------------------------
    // UPDATE
    // -------------------------

    @DisplayName("Actualizar rol exitosamente")
    @Test
    void updateRol_ok() {
        RolDTO updatedRol = new RolDTO(1, "ADMIN", "Administrador actualizado");

        when(rolDAO.findById(1)).thenReturn(Optional.of(adminRol));
        when(rolDAO.existsByNombreRol("ADMIN")).thenReturn(false);
        when(rolDAO.update(eq(1), any(RolDTO.class))).thenReturn(Optional.of(updatedRol));

        RolDTO result = rolService.updateRol(1, updatedRol);

        assertEquals("Administrador actualizado", result.getDescripcion());
        verify(rolDAO).update(1, updatedRol);
    }

    @DisplayName("Actualizar rol inexistente lanza excepción")
    @Test
    void updateRol_notFound_throwsException() {
        when(rolDAO.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> rolService.updateRol(1, adminRol));
        assertEquals("Rol no encontrado con ID: 1", ex.getMessage());
    }

    @DisplayName("Actualizar rol con nombre duplicado lanza excepción")
    @Test
    void updateRol_duplicateName_throwsException() {
        RolDTO anotherRol = new RolDTO(2, "ADMIN", "Otro rol");

        when(rolDAO.findById(1)).thenReturn(Optional.of(adminRol));
        when(rolDAO.existsByNombreRol("ADMIN")).thenReturn(true);
        when(rolDAO.findByNombreRol("ADMIN")).thenReturn(Optional.of(anotherRol));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> rolService.updateRol(1, adminRol));
        assertEquals("Ya existe otro rol con el nombre: ADMIN", ex.getMessage());
    }

    // -------------------------
    // DELETE
    // -------------------------

    @DisplayName("Eliminar rol exitosamente")
    @Test
    void deleteRol_ok() {
        when(rolDAO.findById(1)).thenReturn(Optional.of(adminRol));
        when(rolDAO.deleteById(1)).thenReturn(true);

        rolService.deleteRol(1);

        verify(rolDAO).findById(1);
        verify(rolDAO).deleteById(1);
    }

    @DisplayName("Eliminar rol inexistente lanza excepción")
    @Test
    void deleteRol_notFound_throwsException() {
        when(rolDAO.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> rolService.deleteRol(1));
        assertEquals("Rol no encontrado con ID: 1", ex.getMessage());

        verify(rolDAO, never()).deleteById(anyInt());
    }

    // -------------------------
    // GET BY NAME
    // -------------------------

    @DisplayName("Buscar rol por nombre existente")
    @Test
    void getRolByNombre_ok() {
        when(rolDAO.findByNombreRol("ADMIN")).thenReturn(Optional.of(adminRol));

        RolDTO result = rolService.getRolByNombre("ADMIN");

        assertEquals("ADMIN", result.getNombreRol());
    }

    @DisplayName("Buscar rol por nombre inexistente lanza excepción")
    @Test
    void getRolByNombre_notFound_throwsException() {
        when(rolDAO.findByNombreRol("USER")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> rolService.getRolByNombre("USER"));
        assertEquals("Rol no encontrado con nombre: USER", ex.getMessage());
    }

    // -------------------------
    // IS NOMBRE TAKEN
    // -------------------------

    @DisplayName("Verificar si nombre de rol ya está tomado")
    @Test
    void isNombreRolTaken_ok() {
        when(rolDAO.existsByNombreRol("ADMIN")).thenReturn(true);

        assertTrue(rolService.isNombreRolTaken("ADMIN"));
    }

    // -------------------------
    // COUNT
    // -------------------------

    @DisplayName("Contar total de roles")
    @Test
    void getTotalRolesCount_ok() {
        when(rolDAO.count()).thenReturn(5L);

        long count = rolService.getTotalRolesCount();

        assertEquals(5L, count);
    }
}
