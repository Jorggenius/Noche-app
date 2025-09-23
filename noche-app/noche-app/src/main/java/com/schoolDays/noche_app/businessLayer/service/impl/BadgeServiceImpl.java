package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.service.BadgeService;
import com.schoolDays.noche_app.persistenceLayer.dao.BadgeDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.InscripcionDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.UsuarioBadgeDAO;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.schoolDays.noche_app.businessLayer.dto.BadgeDTO;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioBadgeDTO;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class BadgeServiceImpl implements BadgeService {

    private final BadgeDAO badgeDAO;
    private final InscripcionDAO inscripcionDAO;
    private final UsuarioBadgeDAO usuarioBadgeDAO;

    @Override
    public BadgeDTO createBadge(BadgeDTO badgeDTO) {
        log.info("Creando nuevo badge: {}", badgeDTO.getNombre());

        validateBadgeData(badgeDTO);

        if (badgeDAO.existsByNombre(badgeDTO.getNombre())) {
            throw new IllegalArgumentException("Ya existe un badge con el nombre: " + badgeDTO.getNombre());
        }

        BadgeDTO createdBadge = badgeDAO.save(badgeDTO);
        log.info("Badge creado exitosamente con ID: {}", createdBadge.getIdBadge());
        return createdBadge;
    }

    @Override
    @Transactional(readOnly = true)
    public BadgeDTO getBadgeById(Integer id) {
        return badgeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Badge no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<BadgeDTO> getAllBadges() {
        return badgeDAO.findAll();
    }

    @Override
    public BadgeDTO updateBadge(Integer id, BadgeDTO badgeDTO) {
        log.info("Actualizando badge ID: {}", id);

        getBadgeById(id); // Verificar existencia
        validateBadgeUpdateData(badgeDTO);

        return badgeDAO.update(id, badgeDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar badge"));
    }

    @Override
    public void deleteBadge(Integer id) {
        log.info("Eliminando badge ID: {}", id);

        getBadgeById(id); // Verificar existencia

        if (!badgeDAO.deleteById(id)) {
            throw new RuntimeException("Error al eliminar badge con ID: " + id);
        }

        log.info("Badge eliminado exitosamente ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BadgeDTO> searchBadgesByNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre de búsqueda no puede estar vacío");
        }
        return badgeDAO.findByNombre(nombre);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BadgeDTO> getBadgesMasOtorgados() {
        return badgeDAO.findMasOtorgados();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BadgeDTO> getBadgesConAsignaciones() {
        return badgeDAO.findConAsignaciones();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BadgeDTO> getBadgesSinAsignaciones() {
        return badgeDAO.findSinAsignaciones();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isNombreDisponible(String nombre) {
        return !badgeDAO.existsByNombre(nombre);
    }

    @Override
    public void procesarBadgesAutomaticos(Integer idUsuario) {
        log.info("Procesando badges automáticos para usuario ID: {}", idUsuario);

        // Usar DAO directamente - NO servicios
        long cursosCompletados = inscripcionDAO.countCursosCompletadosByUsuario(idUsuario);

        if (cursosCompletados == 1) {
            otorgarBadgeSiExiste(idUsuario, "Primer Paso");
        }

        if (cursosCompletados >= 5) {
            otorgarBadgeSiExiste(idUsuario, "Dedicado");
        }

        if (cursosCompletados >= 10) {
            otorgarBadgeSiExiste(idUsuario, "Experto");
        }

        log.info("Procesamiento de badges automáticos completado para usuario ID: {}", idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean cumpleCriterio(Integer idUsuario, Integer idBadge) {
        BadgeDTO badge = getBadgeById(idBadge);

        switch (badge.getNombre()) {
            case "Primer Paso":
                return inscripcionDAO.countCursosCompletadosByUsuario(idUsuario) >= 1;
            case "Dedicado":
                return inscripcionDAO.countCursosCompletadosByUsuario(idUsuario) >= 5;
            case "Experto":
                return inscripcionDAO.countCursosCompletadosByUsuario(idUsuario) >= 10;
            default:
                return false;
        }
    }

    private void otorgarBadgeSiExiste(Integer idUsuario, String nombreBadge) {
        try {
            BadgeDTO badge = badgeDAO.findByNombre(nombreBadge).stream()
                    .findFirst()
                    .orElse(null);

            if (badge != null && !usuarioBadgeDAO.existeAsignacion(idUsuario, badge.getIdBadge())) {
                UsuarioBadgeDTO usuarioBadgeDTO = new UsuarioBadgeDTO();
                usuarioBadgeDTO.setIdUsuario(idUsuario);
                usuarioBadgeDTO.setIdBadge(badge.getIdBadge());
                usuarioBadgeDTO.setFechaOtorgado(LocalDate.now());

                usuarioBadgeDAO.save(usuarioBadgeDTO);
                log.info("Badge {} otorgado automáticamente al usuario {}", nombreBadge, idUsuario);
            }
        } catch (Exception e) {
            log.warn("Error al otorgar badge automático {} al usuario {}: {}", nombreBadge, idUsuario, e.getMessage());
        }
    }

    private void validateBadgeData(BadgeDTO badgeDTO) {
        if (badgeDTO.getNombre() == null || badgeDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del badge es obligatorio");
        }

        if (badgeDTO.getCriterio() == null || badgeDTO.getCriterio().trim().isEmpty()) {
            throw new IllegalArgumentException("El criterio del badge es obligatorio");
        }

        if (badgeDTO.getNombre().length() > 100) {
            throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
        }
    }

    private void validateBadgeUpdateData(BadgeDTO badgeDTO) {
        if (badgeDTO.getNombre() != null && badgeDTO.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }

        if (badgeDTO.getCriterio() != null && badgeDTO.getCriterio().trim().isEmpty()) {
            throw new IllegalArgumentException("El criterio no puede estar vacío");
        }
    }
}
