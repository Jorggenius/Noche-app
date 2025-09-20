package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.ModuloDTO;
import com.schoolDays.noche_app.businessLayer.service.CursoService;
import com.schoolDays.noche_app.businessLayer.service.ModuloService;
import com.schoolDays.noche_app.persistenceLayer.dao.ModuloDAO;
import com.schoolDays.noche_app.persistenceLayer.entity.ModuloEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ModuloServiceImpl implements ModuloService {

    private final ModuloDAO moduloDAO;
    private final CursoService cursoService;

    @Override
    public ModuloDTO createModulo(ModuloDTO moduloDTO) {
        log.info("Creando nuevo módulo: {}", moduloDTO.getTitulo());

        validateModuloData(moduloDTO);

        // Validar que el curso existe
        cursoService.getCursoById(moduloDTO.getIdCurso());

        // Validar que el orden es único en el curso
        if (moduloDAO.existeOrdenEnCurso(moduloDTO.getIdCurso(), moduloDTO.getOrden())) {
            throw new IllegalArgumentException("Ya existe un módulo con ese orden en el curso");
        }

        moduloDTO.setVersion(1);

        ModuloDTO createdModulo = moduloDAO.save(moduloDTO);
        log.info("Módulo creado exitosamente con ID: {}", createdModulo.getIdModulo());
        return createdModulo;
    }

    @Override
    @Transactional(readOnly = true)
    public ModuloDTO getModuloById(Integer id) {
        return moduloDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Módulo no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuloDTO> getAllModulos() {
        return moduloDAO.findAll();
    }

    @Override
    public ModuloDTO updateModulo(Integer id, ModuloDTO moduloDTO) {
        log.info("Actualizando módulo ID: {}", id);

        ModuloDTO existingModulo = getModuloById(id);
        validateModuloUpdateData(moduloDTO);

        // No permitir cambio de curso
        if (moduloDTO.getIdCurso() != null && !moduloDTO.getIdCurso().equals(existingModulo.getIdCurso())) {
            throw new IllegalArgumentException("No se puede cambiar el curso del módulo");
        }

        return moduloDAO.update(id, moduloDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar módulo"));
    }

    @Override
    public void deleteModulo(Integer id) {
        log.info("Eliminando módulo ID: {}", id);

        ModuloDTO modulo = getModuloById(id);

        if (!moduloDAO.deleteById(id)) {
            throw new RuntimeException("Error al eliminar módulo con ID: " + id);
        }

        // Aquí podrías implementar reordenamiento automático
        log.info("Módulo eliminado exitosamente ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuloDTO> getModulosByCursoOrdenados(Integer idCurso) {
        cursoService.getCursoById(idCurso); // Validar que el curso existe
        return moduloDAO.findByCursoOrdenados(idCurso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuloDTO> getModulosByTipo(ModuloEntity.TipoModulo tipo) {
        return moduloDAO.findByTipo(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public ModuloDTO getPrimerModuloCurso(Integer idCurso) {
        cursoService.getCursoById(idCurso);
        return moduloDAO.findPrimerModulo(idCurso)
                .orElseThrow(() -> new RuntimeException("No hay módulos en el curso"));
    }

    @Override
    @Transactional(readOnly = true)
    public ModuloDTO getUltimoModuloCurso(Integer idCurso) {
        cursoService.getCursoById(idCurso);
        return moduloDAO.findUltimoModulo(idCurso)
                .orElseThrow(() -> new RuntimeException("No hay módulos en el curso"));
    }

    @Override
    public ModuloDTO cambiarOrdenModulo(Integer id, Integer nuevoOrden) {
        log.info("Cambiando orden del módulo ID: {} a {}", id, nuevoOrden);

        ModuloDTO modulo = getModuloById(id);

        if (nuevoOrden <= 0) {
            throw new IllegalArgumentException("El orden debe ser mayor a 0");
        }

        // Aquí implementarías lógica compleja de reordenamiento
        ModuloDTO updateDTO = new ModuloDTO();
        updateDTO.setOrden(nuevoOrden);

        return updateModulo(id, updateDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public ModuloDTO getSiguienteModulo(Integer idModulo) {
        ModuloDTO modulo = getModuloById(idModulo);
        // Implementar lógica para obtener el siguiente módulo por orden
        return modulo; // Placeholder
    }

    @Override
    @Transactional(readOnly = true)
    public ModuloDTO getModuloAnterior(Integer idModulo) {
        ModuloDTO modulo = getModuloById(idModulo);
        // Implementar lógica para obtener el módulo anterior por orden
        return modulo; // Placeholder
    }

    @Override
    @Transactional(readOnly = true)
    public long getModulosCountByCurso(Integer idCurso) {
        cursoService.getCursoById(idCurso);
        return moduloDAO.countByCurso(idCurso);
    }

    private void validateModuloData(ModuloDTO moduloDTO) {
        if (moduloDTO.getTitulo() == null || moduloDTO.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del módulo es obligatorio");
        }

        if (moduloDTO.getTitulo().length() > 150) {
            throw new IllegalArgumentException("El título no puede exceder 150 caracteres");
        }

        if (moduloDTO.getTipo() == null || moduloDTO.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de módulo es obligatorio");
        }

        if (moduloDTO.getOrden() == null || moduloDTO.getOrden() <= 0) {
            throw new IllegalArgumentException("El orden debe ser mayor a 0");
        }

        if (moduloDTO.getIdCurso() == null) {
            throw new IllegalArgumentException("El curso es obligatorio");
        }
    }
            }