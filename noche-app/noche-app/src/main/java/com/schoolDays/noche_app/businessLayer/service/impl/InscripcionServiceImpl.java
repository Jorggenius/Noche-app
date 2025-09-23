package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.dto.InscripcionDTO;
import com.schoolDays.noche_app.businessLayer.dto.BadgeDTO;
import com.schoolDays.noche_app.businessLayer.dto.UsuarioBadgeDTO;
import com.schoolDays.noche_app.businessLayer.service.InscripcionService;
import com.schoolDays.noche_app.persistenceLayer.dao.InscripcionDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.UsuarioDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.CursoDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.BadgeDAO;
import com.schoolDays.noche_app.persistenceLayer.dao.UsuarioBadgeDAO;
import com.schoolDays.noche_app.persistenceLayer.entity.InscripcionEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InscripcionServiceImpl implements InscripcionService {

    private final InscripcionDAO inscripcionDAO;
    private final UsuarioDAO usuarioDAO;
    private final CursoDAO cursoDAO;
    private final BadgeDAO badgeDAO;
    private final UsuarioBadgeDAO usuarioBadgeDAO;

    @Override
    public InscripcionDTO inscribirUsuario(InscripcionDTO inscripcionDTO) {
        log.info("Inscribiendo usuario {} al curso {}", inscripcionDTO.getIdUsuario(), inscripcionDTO.getIdCurso());

        validateInscripcionData(inscripcionDTO);

        // Validar con DAO directamente
        if (!usuarioDAO.findById(inscripcionDTO.getIdUsuario()).isPresent()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + inscripcionDTO.getIdUsuario());
        }

        if (!cursoDAO.findById(inscripcionDTO.getIdCurso()).isPresent()) {
            throw new RuntimeException("Curso no encontrado con ID: " + inscripcionDTO.getIdCurso());
        }

        // Verificar que no esté ya inscrito
        if (inscripcionDAO.existeInscripcion(inscripcionDTO.getIdUsuario(), inscripcionDTO.getIdCurso())) {
            throw new IllegalArgumentException("El usuario ya está inscrito en este curso");
        }

        inscripcionDTO.setFechaInscripcion(LocalDate.now());
        inscripcionDTO.setProgreso(BigDecimal.ZERO);
        inscripcionDTO.setEstado("INSCRITO");

        InscripcionDTO createdInscripcion = inscripcionDAO.save(inscripcionDTO);
        log.info("Inscripción creada exitosamente con ID: {}", createdInscripcion.getIdInscripcion());

        return createdInscripcion;
    }

    @Override
    @Transactional(readOnly = true)
    public InscripcionDTO getInscripcionById(Integer id) {
        return inscripcionDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> getAllInscripciones() {
        return inscripcionDAO.findAll();
    }

    @Override
    public InscripcionDTO updateProgreso(Integer id, BigDecimal nuevoProgreso) {
        log.info("Actualizando progreso de inscripción ID: {} a {}%", id, nuevoProgreso);

        if (nuevoProgreso.compareTo(BigDecimal.ZERO) < 0 || nuevoProgreso.compareTo(new BigDecimal("100")) > 0) {
            throw new IllegalArgumentException("El progreso debe estar entre 0 y 100");
        }

        InscripcionDTO inscripcion = getInscripcionById(id);

        InscripcionDTO updateDTO = new InscripcionDTO();
        updateDTO.setProgreso(nuevoProgreso);

        // Cambiar estado según progreso
        if (nuevoProgreso.compareTo(BigDecimal.ZERO) > 0) {
            updateDTO.setEstado("EN_PROGRESO");
        }
        if (nuevoProgreso.compareTo(new BigDecimal("100")) == 0) {
            updateDTO.setEstado("COMPLETADO");
        }

        InscripcionDTO updatedInscripcion = inscripcionDAO.update(id, updateDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar progreso"));

        // Si completó el curso, procesar badges automáticos usando DAO
        if (nuevoProgreso.compareTo(new BigDecimal("100")) == 0) {
            procesarBadgesAutomaticosPorDAO(inscripcion.getIdUsuario());
        }

        return updatedInscripcion;
    }

    @Override
    public void cancelarInscripcion(Integer id) {
        log.info("Cancelando inscripción ID: {}", id);

        InscripcionDTO updateDTO = new InscripcionDTO();
        updateDTO.setEstado("SUSPENDIDO");

        inscripcionDAO.update(id, updateDTO);
        log.info("Inscripción cancelada exitosamente ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> getInscripcionesByUsuario(Integer idUsuario) {
        if (!usuarioDAO.findById(idUsuario).isPresent()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }
        return inscripcionDAO.findByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> getInscripcionesByCurso(Integer idCurso) {
        if (!cursoDAO.findById(idCurso).isPresent()) {
            throw new RuntimeException("Curso no encontrado con ID: " + idCurso);
        }
        return inscripcionDAO.findByCurso(idCurso);
    }

    @Override
    @Transactional(readOnly = true)
    public InscripcionDTO getInscripcionByUsuarioAndCurso(Integer idUsuario, Integer idCurso) {
        return inscripcionDAO.findByUsuarioAndCurso(idUsuario, idCurso)
                .orElseThrow(() -> new RuntimeException("No se encontró inscripción para el usuario y curso especificados"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> getInscripcionesByEstado(InscripcionEntity.Estado estado) {
        return inscripcionDAO.findByEstado(estado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> getInscripcionesEnProgreso() {
        return inscripcionDAO.findEnProgreso();
    }

    @Override
    public InscripcionDTO completarCurso(Integer idInscripcion) {
        log.info("Completando curso para inscripción ID: {}", idInscripcion);
        return updateProgreso(idInscripcion, new BigDecimal("100"));
    }

    @Override
    public InscripcionDTO calcularProgreso(Integer idUsuario, Integer idCurso) {
        log.info("Calculando progreso automático para usuario {} en curso {}", idUsuario, idCurso);

        InscripcionDTO inscripcion = getInscripcionByUsuarioAndCurso(idUsuario, idCurso);
        return inscripcion; // Placeholder por ahora
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> getInscripcionesByDepartamento(String departamento) {
        return inscripcionDAO.findByDepartamento(departamento);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean puedeInscribirse(Integer idUsuario, Integer idCurso) {
        try {
            if (!usuarioDAO.findById(idUsuario).isPresent()) {
                return false;
            }
            if (!cursoDAO.findById(idCurso).isPresent()) {
                return false;
            }
            return !inscripcionDAO.existeInscripcion(idUsuario, idCurso);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public long getCursosCompletadosByUsuario(Integer usuarioId) {
        if (!usuarioDAO.findById(usuarioId).isPresent()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + usuarioId);
        }
        return inscripcionDAO.countCursosCompletadosByUsuario(usuarioId);
    }

    private void procesarBadgesAutomaticosPorDAO(Integer idUsuario) {
        try {
            long cursosCompletados = inscripcionDAO.countCursosCompletadosByUsuario(idUsuario);

            List<BadgeDTO> badges = badgeDAO.findAll();
            for (BadgeDTO badge : badges) {
                if (!usuarioBadgeDAO.existeAsignacion(idUsuario, badge.getIdBadge())) {
                    boolean cumpleCriterio = false;

                    switch (badge.getNombre()) {
                        case "Primer Paso":
                            cumpleCriterio = cursosCompletados >= 1;
                            break;
                        case "Dedicado":
                            cumpleCriterio = cursosCompletados >= 5;
                            break;
                        case "Experto":
                            cumpleCriterio = cursosCompletados >= 10;
                            break;
                    }

                    if (cumpleCriterio) {
                        UsuarioBadgeDTO usuarioBadgeDTO = new UsuarioBadgeDTO();
                        usuarioBadgeDTO.setIdUsuario(idUsuario);
                        usuarioBadgeDTO.setIdBadge(badge.getIdBadge());
                        usuarioBadgeDTO.setFechaOtorgado(LocalDate.now());
                        usuarioBadgeDAO.save(usuarioBadgeDTO);
                        log.info("Badge automático '{}' otorgado a usuario {}", badge.getNombre(), idUsuario);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Error al procesar badges automáticos para usuario {}: {}", idUsuario, e.getMessage());
        }
    }

    private void validateInscripcionData(InscripcionDTO inscripcionDTO) {
        if (inscripcionDTO.getIdUsuario() == null) {
            throw new IllegalArgumentException("El usuario es obligatorio");
        }
        if (inscripcionDTO.getIdCurso() == null) {
            throw new IllegalArgumentException("El curso es obligatorio");
        }
    }
}
