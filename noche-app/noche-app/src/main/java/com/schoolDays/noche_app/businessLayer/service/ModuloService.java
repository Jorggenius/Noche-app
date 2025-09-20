package com.schoolDays.noche_app.businessLayer.service;

import com.schoolDays.noche_app.businessLayer.ModuloDTO;
import com.schoolDays.noche_app.persistenceLayer.entity.ModuloEntity;

import java.util.List;

public interface ModuloService {

    /**
     * Crear un nuevo módulo
     *
     * VALIDACIONES:
     * - Curso debe existir
     * - Orden único en el curso
     * - Usuario debe ser creador del curso o admin
     */
    ModuloDTO createModulo(ModuloDTO moduloDTO);

    /**
     * Buscar módulo por ID
     */
    ModuloDTO getModuloById(Integer id);

    /**
     * Buscar todos los módulos
     */
    List<ModuloDTO> getAllModulos();

    /**
     * Actualizar módulo existente
     */
    ModuloDTO updateModulo(Integer id, ModuloDTO moduloDTO);

    /**
     * Eliminar módulo
     *
     * REGLAS:
     * - Reordenar módulos restantes
     * - Solo si no tiene evaluaciones activas
     */
    void deleteModulo(Integer id);

    /**
     * Buscar módulos por curso ordenados
     */
    List<ModuloDTO> getModulosByCursoOrdenados(Integer idCurso);

    /**
     * Buscar módulos por tipo
     */
    List<ModuloDTO> getModulosByTipo(ModuloEntity.TipoModulo tipo);

    /**
     * Obtener primer módulo del curso
     */
    ModuloDTO getPrimerModuloCurso(Integer idCurso);

    /**
     * Obtener último módulo del curso
     */
    ModuloDTO getUltimoModuloCurso(Integer idCurso);

    /**
     * Cambiar orden de módulo
     */
    ModuloDTO cambiarOrdenModulo(Integer id, Integer nuevoOrden);

    /**
     * Obtener siguiente módulo
     */
    ModuloDTO getSiguienteModulo(Integer idModulo);

    /**
     * Obtener módulo anterior
     */
    ModuloDTO getModuloAnterior(Integer idModulo);

    /**
     * Obtener conteo por curso
     */
    long getModulosCountByCurso(Integer idCurso);
}