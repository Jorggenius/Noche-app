package com.Curso.CapacitacionInterna.service;

import com.Curso.CapacitacionInterna.dto.InscripcionCreateDTO;
import com.Curso.CapacitacionInterna.dto.InscripcionDTO;
import com.Curso.CapacitacionInterna.model.Curso;
import com.Curso.CapacitacionInterna.model.Inscripcion;
import com.Curso.CapacitacionInterna.model.ProgresoModulo;
import com.Curso.CapacitacionInterna.model.Usuario;
import com.Curso.CapacitacionInterna.repository.CursoRepository;
import com.Curso.CapacitacionInterna.repository.InscripcionRepository;
import com.Curso.CapacitacionInterna.repository.ProgresoModuloRepository;
import com.Curso.CapacitacionInterna.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class InscripcionService {

    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private ProgresoModuloRepository progresoModuloRepository;

    // Inscribir usuario a curso
    public InscripcionDTO inscribirUsuario(InscripcionCreateDTO inscripcionCreateDTO) {
        // Verificar que no exista la inscripción
        if (inscripcionRepository.existsByUsuarioIdAndCursoId(
                inscripcionCreateDTO.getUsuarioId(), 
                inscripcionCreateDTO.getCursoId())) {
            throw new RuntimeException("El usuario ya está inscrito en este curso");
        }
        
        Usuario usuario = usuarioRepository.findById(inscripcionCreateDTO.getUsuarioId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Curso curso = cursoRepository.findById(inscripcionCreateDTO.getCursoId())
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setUsuario(usuario);
        inscripcion.setCurso(curso);
        inscripcion.setProgreso(0);
        inscripcion.setEstado(Inscripcion.Estado.INSCRITO);

        Inscripcion inscripcionGuardada = inscripcionRepository.save(inscripcion);
        return convertirADTO(inscripcionGuardada);
    }

    // Obtener inscripciones por usuario
    public List<InscripcionDTO> obtenerInscripcionesPorUsuario(Long usuarioId) {
        return inscripcionRepository.findByUsuarioId(usuarioId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener inscripciones por curso
    public List<InscripcionDTO> obtenerInscripcionesPorCurso(Long cursoId) {
        return inscripcionRepository.findByCursoId(cursoId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Actualizar progreso
    public InscripcionDTO actualizarProgreso(Long usuarioId, Long cursoId, Integer progreso) {
        Inscripcion inscripcion = inscripcionRepository.findByUsuarioIdAndCursoId(usuarioId, cursoId)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));

        if (progreso < 0 || progreso > 100) {
            throw new RuntimeException("El progreso debe estar entre 0 y 100");
        }

        inscripcion.setProgreso(progreso);
        
        // Actualizar estado según progreso
        if (progreso == 0) {
            inscripcion.setEstado(Inscripcion.Estado.INSCRITO);
        } else if (progreso > 0 && progreso < 100) {
            inscripcion.setEstado(Inscripcion.Estado.EN_PROGRESO);
        } else if (progreso == 100) {
            inscripcion.setEstado(Inscripcion.Estado.COMPLETADO);
        }

        Inscripcion inscripcionActualizada = inscripcionRepository.save(inscripcion);
        return convertirADTO(inscripcionActualizada);
    }

    // Obtener todas las inscripciones
    public List<InscripcionDTO> obtenerTodasLasInscripciones() {
        return inscripcionRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener inscripciones por estado
    public List<InscripcionDTO> obtenerInscripcionesPorEstado(Inscripcion.Estado estado) {
        return inscripcionRepository.findByEstado(estado)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Eliminar inscripción
    @Transactional
    public void eliminarInscripcion(Long usuarioId, Long cursoId) {
        Inscripcion inscripcion = inscripcionRepository.findByUsuarioIdAndCursoId(usuarioId, cursoId)
                .orElseThrow(() -> new RuntimeException("Inscripción no encontrada"));
        
        // Primero eliminar todos los registros de progreso de módulos asociados
        List<ProgresoModulo> progresosModulos = progresoModuloRepository.findByUsuarioIdAndModuloCursoId(usuarioId, cursoId);
        if (!progresosModulos.isEmpty()) {
            progresoModuloRepository.deleteAll(progresosModulos);
        }
        
        // Luego eliminar la inscripción
        inscripcionRepository.delete(inscripcion);
    }

    // Método auxiliar para convertir entidad a DTO
    private InscripcionDTO convertirADTO(Inscripcion inscripcion) {
        return new InscripcionDTO(
                inscripcion.getId(),
                inscripcion.getUsuario().getId(),
                inscripcion.getUsuario().getNombre(),
                inscripcion.getCurso().getId(),
                inscripcion.getCurso().getTitulo(),
                inscripcion.getCurso().getDescripcion(),
                inscripcion.getCurso().getDuracionEstimada(),
                inscripcion.getCurso().getNivel().toString(),
                inscripcion.getCurso().getInstructor().getNombre(),
                inscripcion.getCurso().getActivo(),
                inscripcion.getProgreso(),
                inscripcion.getFechaInscripcion(),
                inscripcion.getEstado()
        );
    }
}