package com.Curso.CapacitacionInterna.service;

import com.Curso.CapacitacionInterna.dto.ModuloDTO;
import com.Curso.CapacitacionInterna.model.Modulo;
import com.Curso.CapacitacionInterna.model.ProgresoModulo;
import com.Curso.CapacitacionInterna.repository.ModuloRepository;
import com.Curso.CapacitacionInterna.repository.ProgresoModuloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ModuloService {

    @Autowired
    private ModuloRepository moduloRepository;
    
    @Autowired
    private ProgresoModuloRepository progresoModuloRepository;

    // Obtener módulos por curso
    public List<ModuloDTO> obtenerModulosPorCurso(Long cursoId) {
        return moduloRepository.findByCursoIdOrderByOrden(cursoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }
    
    // Obtener módulos por curso con progreso del usuario
    public List<ModuloDTO> obtenerModulosPorCursoConProgreso(Long cursoId, Long usuarioId) {
        return moduloRepository.findByCursoIdOrderByOrden(cursoId)
                .stream()
                .map(modulo -> convertirADTOConProgreso(modulo, usuarioId))
                .collect(Collectors.toList());
    }

    // Eliminar módulo
    public void eliminarModulo(Long id) {
        if (!moduloRepository.existsById(id)) {
            throw new RuntimeException("Módulo no encontrado");
        }
        moduloRepository.deleteById(id);
    }

    // Método auxiliar para convertir entidad a DTO
    private ModuloDTO convertirADTO(Modulo modulo) {
        return new ModuloDTO(
                modulo.getId(),
                modulo.getTitulo(),
                modulo.getDescripcion(),
                modulo.getTipo(),
                modulo.getContenido(),
                modulo.getOrden(),
                "PENDIENTE", // Estado por defecto
                null // Fecha completado
        );
    }
    
    // Método auxiliar para convertir entidad a DTO con progreso
    private ModuloDTO convertirADTOConProgreso(Modulo modulo, Long usuarioId) {
        String estadoProgreso = "PENDIENTE";
        String fechaCompletado = null;
        
        if (usuarioId != null) {
            Optional<ProgresoModulo> progreso = progresoModuloRepository.findByUsuarioIdAndModuloId(usuarioId, modulo.getId());
            if (progreso.isPresent()) {
                estadoProgreso = progreso.get().getEstado().toString();
                if (progreso.get().getFechaCompletado() != null) {
                    fechaCompletado = progreso.get().getFechaCompletado().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                }
            }
        }
        
        return new ModuloDTO(
                modulo.getId(),
                modulo.getTitulo(),
                modulo.getDescripcion(),
                modulo.getTipo(),
                modulo.getContenido(),
                modulo.getOrden(),
                estadoProgreso,
                fechaCompletado
        );
    }
}