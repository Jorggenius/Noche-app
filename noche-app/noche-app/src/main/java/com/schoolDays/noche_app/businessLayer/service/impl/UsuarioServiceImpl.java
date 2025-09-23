package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO;
import com.schoolDays.noche_app.businessLayer.service.RolService;
import com.schoolDays.noche_app.businessLayer.service.UsuarioService;
import com.schoolDays.noche_app.persistenceLayer.dao.UsuarioDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioDAO usuarioDAO;
    private final RolService rolService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioDTO createUsuario(UsuarioDTO usuarioDTO) {
        log.info("Creando nuevo usuario: {}", usuarioDTO.getCorreo()); // CORREGIDO

        validateUsuarioData(usuarioDTO);

        if (usuarioDAO.existsByCorreo(usuarioDTO.getCorreo())) { // CORREGIDO
            throw new IllegalArgumentException("Ya existe un usuario con el correo: " + usuarioDTO.getCorreo());
        }

        rolService.getRolById(usuarioDTO.getIdRol());
        usuarioDTO.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));

        UsuarioDTO createdUsuario = usuarioDAO.save(usuarioDTO);
        log.info("Usuario creado exitosamente con ID: {}", createdUsuario.getIdUsuario());
        return createdUsuario;
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO getUsuarioById(Integer id) {
        return usuarioDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getAllUsuarios() {
        return usuarioDAO.findAll();
    }

    @Override
    public UsuarioDTO updateUsuario(Integer id, UsuarioDTO usuarioDTO) {
        log.info("Actualizando usuario ID: {}", id);

        UsuarioDTO existingUser = getUsuarioById(id);
        validateUsuarioUpdateData(usuarioDTO);

        // No permitir cambio de correo
        if (usuarioDTO.getCorreo() != null && !usuarioDTO.getCorreo().equals(existingUser.getCorreo())) { // CORREGIDO
            throw new IllegalArgumentException("No se permite cambiar el correo del usuario");
        }

        if (usuarioDTO.getContrasena() != null && !usuarioDTO.getContrasena().trim().isEmpty()) {
            usuarioDTO.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
        }

        if (usuarioDTO.getIdRol() != null) {
            rolService.getRolById(usuarioDTO.getIdRol());
        }

        return usuarioDAO.update(id, usuarioDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar usuario"));
    }

    @Override
    public void deleteUsuario(Integer id) {
        log.info("Eliminando usuario ID: {}", id);

        UsuarioDTO usuario = getUsuarioById(id);

        if ("ADMIN".equals(usuario.getNombreRol())) {
            long adminCount = usuarioDAO.countByRol(usuario.getIdRol());
            if (adminCount <= 1) {
                throw new IllegalStateException("No se puede eliminar el único administrador del sistema");
            }
        }

        if (!usuarioDAO.deleteById(id)) {
            throw new RuntimeException("Error al eliminar usuario con ID: " + id);
        }

        log.info("Usuario eliminado exitosamente ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioDTO getUsuarioByCorreo(String correo) { // CORREGIDO: método renombrado
        return usuarioDAO.findByCorreo(correo) // CORREGIDO
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + correo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> searchUsuariosByNombre(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El texto de búsqueda no puede estar vacío");
        }
        return usuarioDAO.findByNombreOrApellido(texto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getUsuariosByDepartamento(String departamento) {
        return usuarioDAO.findByDepartamento(departamento);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getInstructores() {
        return usuarioDAO.findInstructores();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getEstudiantes() {
        return usuarioDAO.findEstudiantes();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isCorreoAvailable(String correo) { // CORREGIDO: método renombrado
        return !usuarioDAO.existsByCorreo(correo); // CORREGIDO
    }

    @Override
    public void changePassword(Integer usuarioId, String oldPassword, String newPassword) {
        log.info("Cambiando contraseña para usuario ID: {}", usuarioId);

        UsuarioDTO usuario = getUsuarioById(usuarioId);

        if (!passwordEncoder.matches(oldPassword, usuario.getContrasena())) {
            throw new IllegalArgumentException("La contraseña actual es incorrecta");
        }

        validatePassword(newPassword);

        UsuarioDTO updateDTO = new UsuarioDTO();
        updateDTO.setContrasena(passwordEncoder.encode(newPassword));

        usuarioDAO.update(usuarioId, updateDTO);
        log.info("Contraseña actualizada exitosamente para usuario ID: {}", usuarioId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUserCountByRol(Integer rolId) {
        rolService.getRolById(rolId);
        return usuarioDAO.countByRol(rolId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalUsersCount() {
        return usuarioDAO.count();
    }

    // NUEVOS MÉTODOS DE LA INTERFAZ CORREGIDA
    @Override
    public UsuarioDTO toggleUsuarioActivo(Integer id) {
        // Implementar lógica de activar/desactivar
        return getUsuarioById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioDTO> getUsuariosByRol(Integer idRol) {
        return usuarioDAO.findByRol(idRol);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean verificarCredenciales(String correo, String contrasena) {
        try {
            UsuarioDTO usuario = getUsuarioByCorreo(correo);
            return passwordEncoder.matches(contrasena, usuario.getContrasena());
        } catch (Exception e) {
            return false;
        }
    }

    private void validateUsuarioData(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getNombre() == null || usuarioDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (usuarioDTO.getApellido() == null || usuarioDTO.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }

        if (usuarioDTO.getCorreo() == null || usuarioDTO.getCorreo().trim().isEmpty()) { // CORREGIDO
            throw new IllegalArgumentException("El correo es obligatorio");
        }

        if (!isValidEmail(usuarioDTO.getCorreo())) { // CORREGIDO
            throw new IllegalArgumentException("El formato del correo no es válido");
        }

        if (usuarioDTO.getIdRol() == null) {
            throw new IllegalArgumentException("El rol es obligatorio");
        }

        validatePassword(usuarioDTO.getContrasena());
    }

    private void validateUsuarioUpdateData(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getNombre() != null && usuarioDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (usuarioDTO.getApellido() != null && usuarioDTO.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede estar vacío");
        }

        if (usuarioDTO.getCorreo() != null && !isValidEmail(usuarioDTO.getCorreo())) { // CORREGIDO
            throw new IllegalArgumentException("El formato del correo no es válido");
        }
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        if (password.length() < 6) {
            throw new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres");
        }
    }

    private boolean isValidEmail(String email) {
        return email.contains("@") && email.contains(".");
    }
}
