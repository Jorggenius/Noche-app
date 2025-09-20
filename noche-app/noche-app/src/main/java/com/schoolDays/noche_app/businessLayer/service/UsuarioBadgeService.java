package com.schoolDays.noche_app.businessLayer.service;

import com.schoolDays.noche_app.businessLayer.UsuarioBadgeDTO;

import java.util.List;

public interface UsuarioBadgeService {

    /**
     * Otorgar badge a usuario
     *
     * VALIDACIONES:
     * - Usuario no debe tener ya el badge
     * - Verificar criterios del badge
     */
    UsuarioBadgeDTO otorgarBadge(Integer idUsuario, Integer idBadge);

    /**
     * Buscar asignación por ID
     */
    UsuarioBadgeDTO getUsuarioBadgeById(Integer id);

    /**
     * Buscar todas las asignaciones
     */
    List<UsuarioBadgeDTO> getAllUsuarioBadges();

    /**
     * Revocar badge
     */
    void revocarBadge(Integer id);

    /**
     * Buscar badges de usuario
     */
    List<UsuarioBadgeDTO> getBadgesByUsuario(Integer idUsuario);

    /**
     * Buscar usuarios con badge específico
     */
    List<UsuarioBadgeDTO> getUsuariosByBadge(Integer idBadge);

    /**
     * Obtener asignaciones recientes
     */
    List<UsuarioBadgeDTO> getAsignacionesRecientes();

    /**
     * Buscar por departamento
     */
    List<UsuarioBadgeDTO> getBadgesByDepartamento(String departamento);

    /**
     * Verificar si usuario tiene badge
     */
    boolean usuarioTieneBadge(Integer idUsuario, Integer idBadge);

    /**
     * Otorgar badge automáticamente si cumple criterios
     */
    UsuarioBadgeDTO otorgarBadgeAutomatico(Integer idUsuario, Integer idBadge);

    /**
     * Obtener ranking de usuarios con más badges
     */
    List<Object[]> getRankingUsuariosBadges();

    /**
     * Obtener conteo por usuario
     */
    long getBadgesCountByUsuario(Integer idUsuario);

    /**
     * Obtener conteo por badge
     */
    long getUsuariosCountByBadge(Integer idBadge);
}