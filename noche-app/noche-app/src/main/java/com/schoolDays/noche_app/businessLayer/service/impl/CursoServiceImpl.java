package com.schoolDays.noche_app.businessLayer.service.impl;


import com.schoolDays.noche_app.businessLayer.CursoDTO;
import com.schoolDays.noche_app.businessLayer.UsuarioDTO;
import com.schoolDays.noche_app.businessLayer.service.CursoService;
import com.schoolDays.noche_app.businessLayer.service.UsuarioService;
import com.schoolDays.noche_app.persistenceLayer.dao.CursoDAO;
import com.schoolDays.noche_app.persistenceLayer.entity.CursoEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CursoServiceImpl implements CursoService {

    private final CursoDAO cursoDAO;
    private final UsuarioService usuarioService;

    @Override
    public CursoDTO createCurso(CursoDTO cursoDTO) {
        log.info("Creando nuevo curso: {}", cursoDTO.getTitulo());

        validateCursoData(cursoDTO);

        // Validar que el creador existe y tiene permisos
        UsuarioDTO creador = usuarioService.getUsuarioById(cursoDTO.getCreadoPorId());
        if (!"INSTRUCTOR".equals(creador.getNombreRol()) && !"ADMIN".equals(creador.getNombreRol())) {
            throw new IllegalArgumentException("Solo instructores y administradores pueden crear cursos");
        }

        // Establecer fecha de creación actual
        cursoDTO.setFechaCreacion(LocalDate.now());
        cursoDTO.setVersion(1);

        CursoDTO createdCurso = cursoDAO.save(cursoDTO);
        log.info("Curso creado exitosamente con ID: {}", createdCurso.getIdCurso());
        return createdCurso;
    }

    @Override
    @Transactional(readOnly = true)
    public CursoDTO getCursoById(Integer id) {
        return cursoDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> getAllCursos() {
        return cursoDAO.findAll();
    }

    @Override
    public CursoDTO updateCurso(Integer id, CursoDTO cursoDTO) {
        log.info("Actualizando curso ID: {}", id);

        CursoDTO existingCurso = getCursoById(id);
        validateCursoUpdateData(cursoDTO);

        // No permitir cambio de creador
        if (cursoDTO.getCreadoPorId() != null && !cursoDTO.getCreadoPorId().equals(existingCurso.getCreadoPorId())) {
            throw new IllegalArgumentException("No se puede cambiar el creador del curso");
        }

        return cursoDAO.update(id, cursoDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar curso"));
    }

    @Override
    public void deleteCurso(Integer id) {
        log.info("Eliminando curso ID: {}", id);

        getCursoById(id); // Verificar existencia

        // Aquí podrías agregar validaciones adicionales como:
        // - No eliminar si tiene inscripciones activas
        // - Solo el creador o admin puede eliminar

        if (!cursoDAO.deleteById(id)) {
            throw new RuntimeException("Error al eliminar curso con ID: " + id);
        }

        log.info("Curso eliminado exitosamente ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> getCursosByCreador(Integer idUsuario) {
        usuarioService.getUsuarioById(idUsuario); // Validar que el usuario existe
        return cursoDAO.findByCreador(idUsuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> getCursosByNivel(CursoEntity.Nivel nivel) {
        return cursoDAO.findByNivel(nivel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> searchCursosByTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título de búsqueda no puede estar vacío");
        }
        return cursoDAO.findByTitulo(titulo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> getCursosMasPopulares() {
        return cursoDAO.findMasPopulares();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> getCursosMasRecientes() {
        return cursoDAO.findMasRecientes();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CursoDTO> getCursosConModulos() {
        return cursoDAO.findCursosConModulos();
    }

    @Override
    @Transactional(readOnly = true)
    public long getCursosCountByNivel(CursoEntity.Nivel nivel) {
        return cursoDAO.countByNivel(nivel);
    }

    @Override
    public CursoDTO publicarCurso(Integer id) {
        log.info("Publicando curso ID: {}", id);
        // Implementar lógica de publicación
        CursoDTO curso = getCursoById(id);
        // Aquí podrías cambiar un estado 'publicado' en el DTO
        return curso;
    }

    @Override
    public CursoDTO despublicarCurso(Integer id) {
        log.info("Despublicando curso ID: {}", id);
        // Implementar lógica de despublicación
        return getCursoById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long getTotalCursosCount() {
        return cursoDAO.count();
    }

    private void validateCursoData(CursoDTO cursoDTO) {
        if (cursoDTO.getTitulo() == null || cursoDTO.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título del curso es obligatorio");
        }

        if (cursoDTO.getTitulo().length() > 150) {
            throw new IllegalArgumentException("El título no puede exceder 150 caracteres");
        }

        if (cursoDTO.getDuracion() == null || cursoDTO.getDuracion() <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0 horas");
        }

        if (cursoDTO.getNivel() == null || cursoDTO.getNivel().trim().isEmpty()) {
            throw new IllegalArgumentException("El nivel es obligatorio");
        }

        if (cursoDTO.getCreadoPorId() == null) {
            throw new IllegalArgumentException("El creador es obligatorio");
        }
    }

    private void validateCursoUpdateData(CursoDTO cursoDTO) {
        if (cursoDTO.getTitulo() != null && cursoDTO.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }

        if (cursoDTO.getTitulo() != null && cursoDTO.getTitulo().length() > 150) {
            throw new IllegalArgumentException("El título no puede exceder 150 caracteres");
        }

        if (cursoDTO.getDuracion() != null && cursoDTO.getDuracion() <= 0) {
            throw new IllegalArgumentException("La duración debe ser mayor a 0 horas");
        }
    }
}
