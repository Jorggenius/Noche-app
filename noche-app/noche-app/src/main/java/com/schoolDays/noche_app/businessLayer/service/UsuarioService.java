package com.schoolDays.noche_app.businessLayer.service;

import java.util.List;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO;

public interface UsuarioService {

    /**
     * Crear un nuevo usuario
     *
     * VALIDACIONES:
     * - Correo único
     * - Rol debe existir
     * - Contraseña debe cumplir políticas
     */
    UsuarioDTO createUsuario(UsuarioDTO usuarioDTO);

    /**
     * Buscar usuario por ID
     */
    UsuarioDTO getUsuarioById(Integer id);

    /**
     * Buscar todos los usuarios
     */
    List<UsuarioDTO> getAllUsuarios();

    /**
     * Actualizar usuario existente
     */
    UsuarioDTO updateUsuario(Integer id, UsuarioDTO usuarioDTO);

    /**
     * Eliminar usuario
     *
     * REGLAS:
     * - No eliminar si es el único admin
     * - No eliminar si tiene cursos creados activos
     */
    void deleteUsuario(Integer id);

    /**
     * Buscar usuario por correo (para login)
     * CORREGIDO: email → correo
     */
    UsuarioDTO getUsuarioByCorreo(String correo);

    /**
     * Buscar usuarios por nombre o apellido
     */
    List<UsuarioDTO> searchUsuariosByNombre(String texto);

    /**
     * Buscar usuarios por departamento
     */
    List<UsuarioDTO> getUsuariosByDepartamento(String departamento);

    /**
     * Obtener solo instructores
     */
    List<UsuarioDTO> getInstructores();

    /**
     * Obtener solo estudiantes
     */
    List<UsuarioDTO> getEstudiantes();

    /**
     * Verificar si correo está disponible
     * CORREGIDO: email → correo
     */
    boolean isCorreoAvailable(String correo);

    /**
     * Cambiar contraseña
     */
    void changePassword(Integer usuarioId, String oldPassword, String newPassword);

    /**
     * Obtener conteo de usuarios por rol
     */
    long getUserCountByRol(Integer rolId);

    /**
     * Obtener total de usuarios
     */
    long getTotalUsersCount();

    /**
     * Activar/desactivar usuario
     */
    UsuarioDTO toggleUsuarioActivo(Integer id);

    /**
     * Buscar usuarios por rol
     */
    List<UsuarioDTO> getUsuariosByRol(Integer idRol);

    /**
     * Verificar credenciales de login
     */
    boolean verificarCredenciales(String correo, String contrasena);
}