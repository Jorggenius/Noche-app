package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.dto.UsuarioBadgeDTO;
import com.schoolDays.noche_app.businessLayer.dto.BadgeDTO;
import com.schoolDays.noche_app.businessLayer.service.UsuarioBadgeService;
import com.schoolDays.noche_app.persistenceLayer.dao.UsuarioBadgeDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.UsuarioDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.BadgeDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.InscripcionDAO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UsuarioBadgeServiceImpl implements UsuarioBadgeService {

    private final UsuarioBadgeDAO usuarioBadgeDAO;
    private final UsuarioDAO usuarioDAO;
    private final BadgeDAO badgeDAO;
    private final InscripcionDAO inscripcionDAO;

    @Override
    public UsuarioBadgeDTO otorgarBadge(Integer idUsuario, Integer idBadge) {
        log.info("Otorgando badge {} a usuario {}", idBadge, idUsuario);

        validateOtorgarBadgeData(idUsuario, idBadge);

        // Validar con DAO directamente
        if (!usuarioDAO.findById(idUsuario).isPresent()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
        if (!badgeDAO.findById(idBadge).isPresent()) {
            throw new RuntimeException("Badge no encontrado con ID: " + idBadge);
        }

        // Verificar que no tenga ya el badge
        if (usuarioBadgeDAO.existeAsignacion(idUsuario, idBadge)) {
            throw new IllegalArgumentException("El usuario ya tiene este badge asignado");
        }

        UsuarioBadgeDTO usuarioBadgeDTO = new UsuarioBadgeDTO();
        usuarioBadgeDTO.setIdUsuario(idUsuario);
        usuarioBadgeDTO.setIdBadge(idBadge);
        usuarioBadgeDTO.setFechaOtorgado(LocalDate.now());

        UsuarioBadgeDTO createdUsuarioBadge = usuarioBadgeDAO.save(usuarioBadgeDTO);
        log.info("Badge otorgado exitosamente con ID: {}", createdUsuarioBadge.getIdUsuarioBadge());

        return createdUsuarioBadge;
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioBadgeDTO getUsuarioBadgeById(Integer id) {
        return usuarioBadgeDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación de badge no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioBadgeDTO> getAllUsuarioBadges() {
        return usuarioBadgeDAO.findAll();
    }

    @Override
    public void revocarBadge(Integer id) {
        log.info("Revocando badge ID: {}", id);

        getUsuarioBadgeById(id); // Verificar existencia

        if (!usuarioBadgeDAO.deleteById(id)) {
            throw new RuntimeException("Error al revocar badge con ID: " + id);
        }

        log.info("Badge revocado exitosamente ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioBadgeDTO> getBadgesByUsuario(Integer idUsuario) {
        if (!usuarioDAO.findById(idUsuario).isPresent()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
        return usuarioBadgeDAO.findByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioBadgeDTO> getUsuariosByBadge(Integer idBadge) {
        if (!badgeDAO.findById(idBadge).isPresent()) {
            throw new RuntimeException("Badge no encontrado con ID: " + idBadge);
        }
        return usuarioBadgeDAO.findByBadge(idBadge);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioBadgeDTO> getAsignacionesRecientes() {
        return usuarioBadgeDAO.findRecientes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioBadgeDTO> getBadgesByDepartamento(String departamento) {
        return usuarioBadgeDAO.findByDepartamento(departamento);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean usuarioTieneBadge(Integer idUsuario, Integer idBadge) {
        return usuarioBadgeDAO.existeAsignacion(idUsuario, idBadge);
    }

    @Override
    public UsuarioBadgeDTO otorgarBadgeAutomatico(Integer idUsuario, Integer idBadge) {
        log.info("Verificando otorgamiento automático de badge {} para usuario {}", idBadge, idUsuario);

        // Verificar si cumple los criterios usando DAO
        if (!cumpleCriterioConDAO(idUsuario, idBadge)) {
            throw new IllegalArgumentException("El usuario no cumple los criterios para este badge");
        }

        return otorgarBadge(idUsuario, idBadge);
    }

    @Override
    public void procesarBadgesAutomaticos(Integer idUsuario) {
        log.info("Procesando badges automáticos para usuario ID: {}", idUsuario);

        List<BadgeDTO> todosLosBadges = badgeDAO.findAll();

        for (BadgeDTO badge : todosLosBadges) {
            try {
                if (!usuarioTieneBadge(idUsuario, badge.getIdBadge()) &&
                        cumpleCriterioConDAO(idUsuario, badge.getIdBadge())) {

                    otorgarBadge(idUsuario, badge.getIdBadge());
                    log.info("Badge automático '{}' otorgado a usuario {}", badge.getNombre(), idUsuario);
                }
            } catch (Exception e) {
                log.warn("Error al procesar badge automático '{}' para usuario {}: {}",
                        badge.getNombre(), idUsuario, e.getMessage());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> getRankingUsuariosBadges() {
        return usuarioBadgeDAO.findUsuariosConMasBadges();
    }

    @Override
    @Transactional(readOnly = true)
    public long getBadgesCountByUsuario(Integer idUsuario) {
        if (!usuarioDAO.findById(idUsuario).isPresent()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
        return usuarioBadgeDAO.countByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public long getUsuariosCountByBadge(Integer idBadge) {
        if (!badgeDAO.findById(idBadge).isPresent()) {
            throw new RuntimeException("Badge no encontrado con ID: " + idBadge);
        }
        return usuarioBadgeDAO.countByBadge(idBadge);
    }

    private boolean cumpleCriterioConDAO(Integer idUsuario, Integer idBadge) {
        BadgeDTO badge = badgeDAO.findById(idBadge)
                .orElseThrow(() -> new RuntimeException("Badge no encontrado con ID: " + idBadge));

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

    private void validateOtorgarBadgeData(Integer idUsuario, Integer idBadge) {
        if (idUsuario == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }

        if (idBadge == null) {
            throw new IllegalArgumentException("El badge es obligatorio");
        }
    }
}
