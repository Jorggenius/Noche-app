package com.schoolDays.unit.service;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO;
import com.schoolDays.noche_app.businessLayer.service.RolService;
import com.schoolDays.noche_app.businessLayer.service.impl.UsuarioServiceImpl;
import com.schoolDays.noche_app.persistenceLayer.dao.UsuarioDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceImplTest {

    @Mock
    private UsuarioDAO usuarioDAO;

    @Mock
    private RolService rolService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    private UsuarioDTO usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuario = new UsuarioDTO(1, "Carlos", "Pérez", "carlos@example.com", "123456", "Sistemas", 2, "USER");
    }

    @Test
    void createUsuario_exitoso() {
        when(usuarioDAO.existsByCorreo("carlos@example.com")).thenReturn(false);
        when(passwordEncoder.encode(any())).thenReturn("encoded123");
        when(usuarioDAO.save(any())).thenReturn(usuario);

        UsuarioDTO result = usuarioService.createUsuario(usuario);

        assertNotNull(result);
        verify(rolService).getRolById(2);
        verify(usuarioDAO).save(any());
    }

    @Test
    void createUsuario_correoYaExiste() {
        when(usuarioDAO.existsByCorreo("carlos@example.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.createUsuario(usuario));

        assertEquals("Ya existe un usuario con el correo: carlos@example.com", ex.getMessage());
    }

    @Test
    void getUsuarioById_exitoso() {
        when(usuarioDAO.findById(1)).thenReturn(Optional.of(usuario));

        UsuarioDTO result = usuarioService.getUsuarioById(1);

        assertEquals("Carlos", result.getNombre());
        verify(usuarioDAO).findById(1);
    }

    @Test
    void getUsuarioById_noExiste() {
        when(usuarioDAO.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.getUsuarioById(1));

        assertEquals("Usuario no encontrado con ID: 1", ex.getMessage());
    }

    @Test
    void updateUsuario_exitoso() {
        UsuarioDTO actualizado = new UsuarioDTO();
        actualizado.setNombre("Carlos A.");
        actualizado.setContrasena("nueva123");

        when(usuarioDAO.findById(1)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.encode(any())).thenReturn("encodedNew");
        when(usuarioDAO.update(eq(1), any())).thenReturn(Optional.of(actualizado));

        UsuarioDTO result = usuarioService.updateUsuario(1, actualizado);

        assertNotNull(result);
        verify(passwordEncoder).encode(any());
        verify(usuarioDAO).update(eq(1), any());
    }

    @Test
    void updateUsuario_cambioCorreo_noPermitido() {
        UsuarioDTO actualizado = new UsuarioDTO();
        actualizado.setCorreo("otro@example.com");

        when(usuarioDAO.findById(1)).thenReturn(Optional.of(usuario));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.updateUsuario(1, actualizado));

        assertEquals("No se permite cambiar el correo del usuario", ex.getMessage());
    }

    @Test
    void deleteUsuario_exitoso() {
        when(usuarioDAO.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioDAO.deleteById(1)).thenReturn(true);

        usuarioService.deleteUsuario(1);

        verify(usuarioDAO).deleteById(1);
    }

    @Test
    void deleteUsuario_unicoAdmin() {
        usuario.setNombreRol("ADMIN");
        when(usuarioDAO.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioDAO.countByRol(2)).thenReturn(1L);

        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> usuarioService.deleteUsuario(1));

        assertEquals("No se puede eliminar el único administrador del sistema", ex.getMessage());
    }

    @Test
    void deleteUsuario_fallaEliminacion() {
        when(usuarioDAO.findById(1)).thenReturn(Optional.of(usuario));
        when(usuarioDAO.deleteById(1)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.deleteUsuario(1));

        assertEquals("Error al eliminar usuario con ID: 1", ex.getMessage());
    }

    @Test
    void getUsuarioByCorreo_exitoso() {
        when(usuarioDAO.findByCorreo("carlos@example.com")).thenReturn(Optional.of(usuario));

        UsuarioDTO result = usuarioService.getUsuarioByCorreo("carlos@example.com");

        assertEquals("Carlos", result.getNombre());
    }

    @Test
    void getUsuarioByCorreo_noExiste() {
        when(usuarioDAO.findByCorreo("carlos@example.com")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> usuarioService.getUsuarioByCorreo("carlos@example.com"));

        assertEquals("Usuario no encontrado con correo: carlos@example.com", ex.getMessage());
    }

    @Test
    void changePassword_exitoso() {
        when(usuarioDAO.findById(1)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);
        when(passwordEncoder.encode("nueva123")).thenReturn("encodedNew");

        usuarioService.changePassword(1, "123456", "nueva123");

        verify(usuarioDAO).update(eq(1), any());
        verify(passwordEncoder).encode("nueva123");
    }

    @Test
    void changePassword_contraseñaActualIncorrecta() {
        when(usuarioDAO.findById(1)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.changePassword(1, "mal", "nueva123"));

        assertEquals("La contraseña actual es incorrecta", ex.getMessage());
    }

    @Test
    void verificarCredenciales_exitosas() {
        when(usuarioDAO.findByCorreo("carlos@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("123456", "123456")).thenReturn(true);

        boolean valido = usuarioService.verificarCredenciales("carlos@example.com", "123456");

        assertTrue(valido);
    }

    @Test
    void verificarCredenciales_incorrectas() {
        when(usuarioDAO.findByCorreo("carlos@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("mal", "123456")).thenReturn(false);

        boolean valido = usuarioService.verificarCredenciales("carlos@example.com", "mal");

        assertFalse(valido);
    }

    @Test
    void getUserCountByRol_exitoso() {
        when(usuarioDAO.countByRol(2)).thenReturn(5L);

        long count = usuarioService.getUserCountByRol(2);

        assertEquals(5L, count);
        verify(rolService).getRolById(2);
    }

    @Test
    void getTotalUsersCount_exitoso() {
        when(usuarioDAO.count()).thenReturn(15L);

        long total = usuarioService.getTotalUsersCount();

        assertEquals(15L, total);
        verify(usuarioDAO).count();
    }

    @Test
    void searchUsuariosByNombre_exitoso() {
        when(usuarioDAO.findByNombreOrApellido("Car")).thenReturn(Collections.singletonList(usuario));

        List<UsuarioDTO> result = usuarioService.searchUsuariosByNombre("Car");

        assertEquals(1, result.size());
    }

    @Test
    void searchUsuariosByNombre_vacio() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> usuarioService.searchUsuariosByNombre(" "));

        assertEquals("El texto de búsqueda no puede estar vacío", ex.getMessage());
    }

    @Test
    void isCorreoAvailable_disponible() {
        when(usuarioDAO.existsByCorreo("nuevo@example.com")).thenReturn(false);

        assertTrue(usuarioService.isCorreoAvailable("nuevo@example.com"));
    }

    @Test
    void isCorreoAvailable_noDisponible() {
        when(usuarioDAO.existsByCorreo("carlos@example.com")).thenReturn(true);

        assertFalse(usuarioService.isCorreoAvailable("carlos@example.com"));
    }
}
