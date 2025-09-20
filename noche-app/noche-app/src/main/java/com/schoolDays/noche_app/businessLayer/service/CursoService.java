package com.schoolDays.noche_app.businessLayer.service;

import com.schoolDays.noche_app.businessLayer.CursoDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.CursoEntity;

import java.util.List;

public interface CursoService {

    /**
     * Crear un nuevo curso
     *
     * VALIDACIONES:
     * - Creador debe ser INSTRUCTOR o ADMIN
     * - Título único por creador
     * - Duración válida
     */
    CursoDTO createCurso(CursoDTO cursoDTO);

    /**
     * Buscar curso por ID
     */
    CursoDTO getCursoById(Integer id);

    /**
     * Buscar todos los cursos
     */
    List<CursoDTO> getAllCursos();

    /**
     * Actualizar curso existente
     *
     * RESTRICCIONES:
     * - Solo el creador o admin puede modificar
     * - No cambiar creador
     */
    CursoDTO updateCurso(Integer id, CursoDTO cursoDTO);

    /**
     * Eliminar curso
     *
     * REGLAS:
     * - Solo si no tiene inscripciones activas
     * - Solo creador o admin
     */
    void deleteCurso(Integer id);

    /**
     * Buscar cursos por creador
     */
    List<CursoDTO> getCursosByCreador(Integer idUsuario);

    /**
     * Buscar cursos por nivel
     */
    List<CursoDTO> getCursosByNivel(CursoEntity.Nivel nivel);

    /**
     * Buscar cursos por título
     */
    List<CursoDTO> searchCursosByTitulo(String titulo);

    /**
     * Obtener cursos más populares
     */
    List<CursoDTO> getCursosMasPopulares();

    /**
     * Obtener cursos más recientes
     */
    List<CursoDTO> getCursosMasRecientes();

    /**
     * Obtener cursos con módulos
     */
    List<CursoDTO> getCursosConModulos();

    /**
     * Obtener conteo por nivel
     */
    long getCursosCountByNivel(CursoEntity.Nivel nivel);

    /**
     * Publicar curso (cambiar estado)
     */
    CursoDTO publicarCurso(Integer id);

    /**
     * Despublicar curso
     */
    CursoDTO despublicarCurso(Integer id);

    /**
     * Obtener total de cursos
     */
    long getTotalCursosCount();
}