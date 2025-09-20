package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.InscripcionDTO;
import com.schoolDays.noche_app.businessLayer.service.CursoService;
import com.schoolDays.noche_app.businessLayer.service.InscripcionService;
import com.schoolDays.noche_app.businessLayer.service.UsuarioBadgeService;
import com.schoolDays.noche_app.businessLayer.service.UsuarioService;
import com.schoolDays.noche_app.persistenceLayer.dao.InscripcionDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class InscripcionServiceImpl implements InscripcionService {

    private final InscripcionDAO inscripcionDAO;
    private final UsuarioService usuarioService;
    private final CursoService cursoService;
    private final UsuarioBadgeService usuarioBadgeService;

    @Override
    public InscripcionDTO inscribirUsuario(InscripcionDTO inscripcionDTO) {
        log.info("Inscribiendo usuario {} al curso {}", inscripcionDTO.getIdUsuario(), inscripcionDTO.getIdCurso());

        validateInscripcionData(inscripcionDTO);

        // Validar que usuario y curso existen
        usuarioService.getUsuarioById(inscripcionDTO.getIdUsuario());
        cursoService.getCursoById(inscripcionDTO.getIdCurso());

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

        // Si completó el curso, procesar badges automáticos
        if (nuevoProgreso.compareTo(new BigDecimal("100")) == 0) {
            usuarioBadgeService.procesarBadgesAutomaticos(inscripcion.getIdUsuario());
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
        usuarioService.getUsuarioById(idUsuario);
        return inscripcionDAO.findByUsuario(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<InscripcionDTO> getInscripcionesByCurso(Integer idCurso) {
        cursoService.getCursoById(idCurso);
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
    @Transactional(readOnly = true)
    public long getCursosCompletadosByUsuario(Integer usuarioId) {
        usuarioService.getUsuarioById(usuarioId);
        return inscripcionDAO.countCursosCompletadosByUsuario(usuarioId);
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