package com.schoolDays.noche_app.businessLayer.service.impl;

import com.schoolDays.noche_app.businessLayer.EvaluacionDTO;
import com.schoolDays.noche_app.businessLayer.service.EvaluacionService;
import com.schoolDays.noche_app.businessLayer.service.ModuloService;
import com.schoolDays.noche_app.persistenceLayer.dao.EvaluacionDAO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class EvaluacionServiceImpl implements EvaluacionService {

    private final EvaluacionDAO evaluacionDAO;
    private final ModuloService moduloService;

    @Override
    public EvaluacionDTO createEvaluacion(EvaluacionDTO evaluacionDTO) {
        log.info("Creando nueva evaluación: {}", evaluacionDTO.getTitulo());

        validateEvaluacionData(evaluacionDTO);

        // Validar que el módulo existe
        moduloService.getModuloById(evaluacionDTO.getIdModulo());

        EvaluacionDTO createdEvaluacion = evaluacionDAO.save(evaluacionDTO);
        log.info("Evaluación creada exitosamente con ID: {}", createdEvaluacion.getIdEvaluacion());
        return createdEvaluacion;
    }

    @Override
    @Transactional(readOnly = true)
    public EvaluacionDTO getEvaluacionById(Integer id) {
        return evaluacionDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Evaluación no encontrada con ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionDTO> getAllEvaluaciones() {
        return evaluacionDAO.findAll();
    }

    @Override
    public EvaluacionDTO updateEvaluacion(Integer id, EvaluacionDTO evaluacionDTO) {
        log.info("Actualizando evaluación ID: {}", id);

        getEvaluacionById(id); // Verificar existencia
        validateEvaluacionUpdateData(evaluacionDTO);

        return evaluacionDAO.update(id, evaluacionDTO)
                .orElseThrow(() -> new RuntimeException("Error al actualizar evaluación"));
    }

    @Override
    public void deleteEvaluacion(Integer id) {
        log.info("Eliminando evaluación ID: {}", id);

        getEvaluacionById(id); // Verificar existencia

        if (!evaluacionDAO.deleteById(id)) {
            throw new RuntimeException("Error al eliminar evaluación con ID: " + id);
        }

        log.info("Evaluación eliminada exitosamente ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionDTO> getEvaluacionesByModulo(Integer idModulo) {
        moduloService.getModuloById(idModulo); // Validar que el módulo existe
        return evaluacionDAO.findByModulo(idModulo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionDTO> getEvaluacionesByTipo(EvaluacionEntity.TipoEvaluacion tipo) {
        return evaluacionDAO.findByTipo(tipo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionDTO> getEvaluacionesByCurso(Integer idCurso) {
        return evaluacionDAO.findByCurso(idCurso);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionDTO> getEvaluacionesConPreguntas() {
        return evaluacionDAO.findConPreguntas();
    }

    @Override
    public void iniciarEvaluacion(Integer idEvaluacion, Integer idUsuario) {
        log.info("Iniciando evaluación {} para usuario {}", idEvaluacion, idUsuario);

        getEvaluacionById(idEvaluacion); // Validar que existe

        // Aquí podrías implementar lógica adicional como:
        // - Registrar inicio de evaluación
        // - Establecer tiempo límite
        // - Verificar prerrequisitos

        log.info("Evaluación iniciada exitosamente");
    }

    @Override
    public void finalizarEvaluacion(Integer idEvaluacion, Integer idUsuario) {
        log.info("Finalizando evaluación {} para usuario {}", idEvaluacion, idUsuario);

        // Aquí implementarías:
        // - Calcular puntaje total
        // - Determinar si aprobó
        // - Actualizar progreso del curso
        // - Generar certificado si corresponde

        log.info("Evaluación finalizada exitosamente");
    }

    @Override
    @Transactional(readOnly = true)
    public boolean puedeTomarEvaluacion(Integer idEvaluacion, Integer idUsuario) {
        // Implementar validaciones como:
        // - Usuario inscrito en el curso
        // - Módulos previos completados
        // - No ha tomado la evaluación antes
        return true; // Placeholder
    }

    @Override
    @Transactional(readOnly = true)
    public List<EvaluacionDTO> getEvaluacionesPendientes(Integer idUsuario) {
        // Implementar lógica para obtener evaluaciones pendientes
        return getAllEvaluaciones(); // Placeholder
    }

    private void validateEvaluacionData(EvaluacionDTO evaluacionDTO) {
        if (evaluacionDTO.getTitulo() == null || evaluacionDTO.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título de la evaluación es obligatorio");
        }

        if (evaluacionDTO.getTipo() == null || evaluacionDTO.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo de evaluación es obligatorio");
        }

        if (evaluacionDTO.getPuntaje() == null || evaluacionDTO.getPuntaje().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El puntaje máximo debe ser mayor a cero");
        }

        if (evaluacionDTO.getIdModulo() == null) {
            throw new IllegalArgumentException("El módulo es obligatorio");
        }
    }

    private void validateEvaluacionUpdateData(EvaluacionDTO evaluacionDTO) {
        if (evaluacionDTO.getTitulo() != null && evaluacionDTO.getTitulo().trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío");
        }

        if (evaluacionDTO.getPuntaje() != null && evaluacionDTO.getPuntaje().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El puntaje máximo debe ser mayor a cero");
        }
    }
}