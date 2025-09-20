package com.schoolDays.noche_app.businessLayer.service;

import com.schoolDays.noche_app.businessLayer.BadgeDTO;

import java.util.List;

public interface BadgeService {

    /**
     * Crear nuevo badge
     *
     * VALIDACIONES:
     * - Nombre único
     * - Criterio válido
     */
    BadgeDTO createBadge(BadgeDTO badgeDTO);

    /**
     * Buscar badge por ID
     */
    BadgeDTO getBadgeById(Integer id);

    /**
     * Buscar todos los badges
     */
    List<BadgeDTO> getAllBadges();

    /**
     * Actualizar badge
     */
    BadgeDTO updateBadge(Integer id, BadgeDTO badgeDTO);

    /**
     * Eliminar badge
     *
     * REGLAS:
     * - Solo si no tiene asignaciones
     */
    void deleteBadge(Integer id);

    /**
     * Buscar badges por nombre
     */
    List<BadgeDTO> searchBadgesByNombre(String nombre);

    /**
     * Obtener badges más otorgados
     */
    List<BadgeDTO> getBadgesMasOtorgados();

    /**
     * Obtener badges con asignaciones
     */
    List<BadgeDTO> getBadgesConAsignaciones();

    /**
     * Obtener badges sin asignaciones
     */
    List<BadgeDTO> getBadgesSinAsignaciones();

    /**
     * Verificar si nombre está disponible
     */
    boolean isNombreDisponible(String nombre);

    /**
     * Procesar badges automáticos para usuario
     *
     * REGLAS AUTOMÁTICAS:
     * - Primer curso completado
     * - 5 cursos completados
     * - Evaluación perfecta
     * - etc.
     */
    void procesarBadgesAutomaticos(Integer idUsuario);

    /**
     * Verificar criterios de badge
     */
    boolean cumpleCriterio(Integer idUsuario, Integer idBadge);
}