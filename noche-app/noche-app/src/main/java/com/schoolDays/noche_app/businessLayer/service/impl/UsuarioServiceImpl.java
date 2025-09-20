package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.UsuarioDTO;
import com.schoolDays.noche_app.businessLayer.service.RolService;
import com.schoolDays.noche_app.businessLayer.service.UsuarioService;
import com.schoolDays.noche_app.persistenceLayer.dao.UsuarioDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

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
        log.info("Creando nuevo usuario: {}", usuarioDTO.getEmail());

        validateUsuarioData(usuarioDTO);

        if (usuarioDAO.existsByEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + usuarioDTO.getEmail());
        }

        // Validar que el rol existe
        rolService.getRolById(usuarioDTO.getIdRol());

        // Encriptar contraseña
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

        // No permitir cambio de email
        if (usuarioDTO.getEmail() != null && !usuarioDTO.getEmail().equals(existingUser.getEmail())) {
            throw new IllegalArgumentException("No se permite cambiar el email del usuario");
        }

        // Si hay nueva contraseña, encriptarla
        if (usuarioDTO.getContrasena() != null && !usuarioDTO.getContrasena().trim().isEmpty()) {
            usuarioDTO.setContrasena(passwordEncoder.encode(usuarioDTO.getContrasena()));
        }

        // Validar rol si se está cambiando
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

        // Regla de negocio: No eliminar el único admin
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
    public UsuarioDTO getUsuarioByEmail(String email) {
        return usuarioDAO.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
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
    public boolean isEmailAvailable(String email) {
        return !usuarioDAO.existsByEmail(email);
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
        rolService.getRolById(rolId); // Validar que el rol existe
        return usuarioDAO.countByRol(rolId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalUsersCount() {
        return usuarioDAO.count();
    }

    private void validateUsuarioData(UsuarioDTO usuarioDTO) {
        if (usuarioDTO.getNombre() == null || usuarioDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }

        if (usuarioDTO.getApellido() == null || usuarioDTO.getApellido().trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio");
        }

        if (usuarioDTO.getEmail() == null || usuarioDTO.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es obligatorio");
        }

        if (!isValidEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
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

        if (usuarioDTO.getEmail() != null && !isValidEmail(usuarioDTO.getEmail())) {
            throw new IllegalArgumentException("El formato del email no es válido");
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