package com.Curso.CapacitacionInterna.service;

import com.Curso.CapacitacionInterna.dto.CursoCreateDTO;
import com.Curso.CapacitacionInterna.dto.CursoDTO;
import com.Curso.CapacitacionInterna.dto.ModuloCreateDTO;
import com.Curso.CapacitacionInterna.model.Curso;
import com.Curso.CapacitacionInterna.model.Modulo;
import com.Curso.CapacitacionInterna.model.Usuario;
import com.Curso.CapacitacionInterna.repository.CursoRepository;
import com.Curso.CapacitacionInterna.repository.ModuloRepository;
import com.Curso.CapacitacionInterna.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoService {

    @Autowired
    private CursoRepository cursoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ModuloRepository moduloRepository;

    // Crear curso con módulos
    @Transactional
    public CursoDTO crearCurso(CursoCreateDTO cursoCreateDTO) {
        Usuario instructor = usuarioRepository.findById(cursoCreateDTO.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor no encontrado"));
        
        if (instructor.getRol() != Usuario.Rol.INSTRUCTOR && instructor.getRol() != Usuario.Rol.ADMIN) {
            throw new RuntimeException("Solo usuarios con rol INSTRUCTOR o ADMIN pueden ser asignados como instructores");
        }

        Curso curso = new Curso();
        curso.setTitulo(cursoCreateDTO.getTitulo());
        curso.setDescripcion(cursoCreateDTO.getDescripcion());
        curso.setDuracionEstimada(cursoCreateDTO.getDuracionEstimada());
        curso.setNivel(cursoCreateDTO.getNivel());
        curso.setInstructor(instructor);
        curso.setActivo(true);

        Curso cursoGuardado = cursoRepository.save(curso);
        
        // Crear módulos si vienen en el DTO
        if (cursoCreateDTO.getModulos() != null && !cursoCreateDTO.getModulos().isEmpty()) {
            for (ModuloCreateDTO moduloDTO : cursoCreateDTO.getModulos()) {
                Modulo modulo = new Modulo();
                modulo.setTitulo(moduloDTO.getTitulo());
                modulo.setDescripcion(moduloDTO.getDescripcion());
                modulo.setTipo(moduloDTO.getTipo());
                modulo.setContenido(moduloDTO.getContenido());
                modulo.setOrden(moduloDTO.getOrden());
                modulo.setCurso(cursoGuardado);
                
                moduloRepository.save(modulo);
            }
        }
        
        return convertirADTO(cursoGuardado);
    }

    // Obtener todos los cursos
    public List<CursoDTO> obtenerTodosLosCursos() {
        return cursoRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener solo cursos activos
    public List<CursoDTO> obtenerCursosActivos() {
        return cursoRepository.findByActivoTrue()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener curso por ID
    public Optional<CursoDTO> obtenerCursoPorId(Long id) {
        return cursoRepository.findById(id)
                .map(this::convertirADTO);
    }

    // Actualizar curso con módulos
    @Transactional
    public CursoDTO actualizarCurso(Long id, CursoCreateDTO cursoCreateDTO) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));

        Usuario instructor = usuarioRepository.findById(cursoCreateDTO.getInstructorId())
                .orElseThrow(() -> new RuntimeException("Instructor no encontrado"));
        
        if (instructor.getRol() != Usuario.Rol.INSTRUCTOR && instructor.getRol() != Usuario.Rol.ADMIN) {
            throw new RuntimeException("Solo usuarios con rol INSTRUCTOR o ADMIN pueden ser asignados como instructores");
        }

        curso.setTitulo(cursoCreateDTO.getTitulo());
        curso.setDescripcion(cursoCreateDTO.getDescripcion());
        curso.setDuracionEstimada(cursoCreateDTO.getDuracionEstimada());
        curso.setNivel(cursoCreateDTO.getNivel());
        curso.setInstructor(instructor);

        Curso cursoActualizado = cursoRepository.save(curso);
        
        // Eliminar todos los módulos existentes del curso
        List<Modulo> modulosExistentes = moduloRepository.findByCursoIdOrderByOrden(id);
        moduloRepository.deleteAll(modulosExistentes);
        
        // Crear los nuevos módulos si vienen en el DTO
        if (cursoCreateDTO.getModulos() != null && !cursoCreateDTO.getModulos().isEmpty()) {
            for (ModuloCreateDTO moduloDTO : cursoCreateDTO.getModulos()) {
                Modulo modulo = new Modulo();
                modulo.setTitulo(moduloDTO.getTitulo());
                modulo.setDescripcion(moduloDTO.getDescripcion());
                modulo.setTipo(moduloDTO.getTipo());
                modulo.setContenido(moduloDTO.getContenido());
                modulo.setOrden(moduloDTO.getOrden());
                modulo.setCurso(cursoActualizado);
                
                moduloRepository.save(modulo);
            }
        }
        
        return convertirADTO(cursoActualizado);
    }

    // Eliminar curso (soft delete)
    public void eliminarCurso(Long id) {
        Curso curso = cursoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Curso no encontrado"));
        
        curso.setActivo(false);
        cursoRepository.save(curso);
    }

    // Eliminar curso permanentemente
    @Transactional
    public void eliminarCursoPermanente(Long id) {
        if (!cursoRepository.existsById(id)) {
            throw new RuntimeException("Curso no encontrado");
        }
        
        // Primero eliminar todos los módulos asociados al curso
        List<Modulo> modulos = moduloRepository.findByCursoIdOrderByOrden(id);
        if (!modulos.isEmpty()) {
            moduloRepository.deleteAll(modulos);
        }
        
        // Luego eliminar el curso
        cursoRepository.deleteById(id);
    }

    // Obtener cursos por nivel
    public List<CursoDTO> obtenerCursosPorNivel(Curso.Nivel nivel) {
        return cursoRepository.findByActivoTrueAndNivel(nivel)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Obtener cursos por instructor
    public List<CursoDTO> obtenerCursosPorInstructor(Long instructorId) {
        return cursoRepository.findByInstructorId(instructorId)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Buscar cursos por título
    public List<CursoDTO> buscarCursosPorTitulo(String titulo) {
        return cursoRepository.findByTituloContainingIgnoreCase(titulo)
                .stream()
                .filter(curso -> curso.getActivo())
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    // Método auxiliar para convertir entidad a DTO
    private CursoDTO convertirADTO(Curso curso) {
        return new CursoDTO(
                curso.getId(),
                curso.getTitulo(),
                curso.getDescripcion(),
                curso.getDuracionEstimada(),
                curso.getNivel(),
                curso.getInstructor().getId(),
                curso.getInstructor().getNombre(),
                curso.getActivo()
        );
    }
}