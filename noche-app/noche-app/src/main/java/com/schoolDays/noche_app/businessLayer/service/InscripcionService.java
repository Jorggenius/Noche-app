package com.schoolDays.noche_app.businessLayer.service;

import com.schoolDays.noche_app.businessLayer.InscripcionDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.InscripcionEntity;

import java.math.BigDecimal;
import java.util.List;

public interface InscripcionService {

    /**
     * Inscribir usuario a curso
     *
     * VALIDACIONES:
     * - Usuario no puede estar ya inscrito
     * - Curso debe estar activo
     * - Verificar prerrequisitos si existen
     */
    InscripcionDTO inscribirUsuario(InscripcionDTO inscripcionDTO);

    /**
     * Buscar inscripción por ID
     */
    InscripcionDTO getInscripcionById(Integer id);

    /**
     * Buscar todas las inscripciones
     */
    List<InscripcionDTO> getAllInscripciones();

    /**
     * Actualizar progreso
     */
    InscripcionDTO updateProgreso(Integer id, BigDecimal nuevoProgreso);

    /**
     * Cancelar inscripción
     */
    void cancelarInscripcion(Integer id);

    /**
     * Buscar inscripciones por usuario
     */
    List<InscripcionDTO> getInscripcionesByUsuario(Integer idUsuario);

    /**
     * Buscar inscripciones por curso
     */
    List<InscripcionDTO> getInscripcionesByCurso(Integer idCurso);

    /**
     * Buscar inscripción específica
     */
    InscripcionDTO getInscripcionByUsuarioAndCurso(Integer idUsuario, Integer idCurso);

    /**
     * Buscar por estado
     */
    List<InscripcionDTO> getInscripcionesByEstado(InscripcionEntity.Estado estado);

    /**
     * Obtener inscripciones en progreso
     */
    List<InscripcionDTO> getInscripcionesEnProgreso();

    /**
     * Marcar curso como completado
     */
    InscripcionDTO completarCurso(Integer idInscripcion);

    /**
     * Calcular y actualizar progreso automáticamente
     */
    InscripcionDTO calcularProgreso(Integer idUsuario, Integer idCurso);

    /**
     * Obtener estadísticas por departamento
     */
    List<InscripcionDTO> getInscripcionesByDepartamento(String departamento);

    /**
     * Verificar si usuario puede inscribirse
     */
    boolean puedeInscribirse(Integer idUsuario, Integer idCurso);

    /**
     * Obtener cursos completados por usuario
     */
    long getCursosCompletadosByUsuario(Integer usuarioId);
}
