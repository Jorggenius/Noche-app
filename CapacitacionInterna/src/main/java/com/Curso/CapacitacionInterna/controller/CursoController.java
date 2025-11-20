package com.Curso.CapacitacionInterna.controller;

import com.Curso.CapacitacionInterna.dto.CursoCreateDTO;
import com.Curso.CapacitacionInterna.dto.CursoDTO;
import com.Curso.CapacitacionInterna.model.Curso;
import com.Curso.CapacitacionInterna.service.CursoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cursos")
@CrossOrigin(origins = "*")
@Tag(name = "Curso", description = "Controlador para gestión de cursos")
public class CursoController {

    @Autowired
    private CursoService cursoService;

    @PostMapping
    @Operation(summary = "Crear curso", description = "Crea un nuevo curso en el sistema")
    public ResponseEntity<CursoDTO> crearCurso(@RequestBody CursoCreateDTO cursoCreateDTO) {
        try {
            CursoDTO cursoCreado = cursoService.crearCurso(cursoCreateDTO);
            return ResponseEntity.ok(cursoCreado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    @Operation(summary = "Obtener todos los cursos", description = "Retorna lista de todos los cursos")
    public ResponseEntity<List<CursoDTO>> obtenerTodosLosCursos() {
        List<CursoDTO> cursos = cursoService.obtenerTodosLosCursos();
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/activos")
    @Operation(summary = "Obtener cursos activos", description = "Retorna solo los cursos activos")
    public ResponseEntity<List<CursoDTO>> obtenerCursosActivos() {
        List<CursoDTO> cursos = cursoService.obtenerCursosActivos();
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener curso por ID", description = "Retorna un curso específico por su ID")
    public ResponseEntity<CursoDTO> obtenerCursoPorId(@PathVariable Long id) {
        Optional<CursoDTO> curso = cursoService.obtenerCursoPorId(id);
        return curso.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar curso", description = "Actualiza los datos de un curso existente")
    public ResponseEntity<CursoDTO> actualizarCurso(@PathVariable Long id, @RequestBody CursoCreateDTO cursoCreateDTO) {
        try {
            CursoDTO cursoActualizado = cursoService.actualizarCurso(id, cursoCreateDTO);
            return ResponseEntity.ok(cursoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Desactivar curso", description = "Desactiva un curso (soft delete)")
    public ResponseEntity<Void> eliminarCurso(@PathVariable Long id) {
        try {
            cursoService.eliminarCurso(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}/permanente")
    @Operation(summary = "Eliminar curso permanente", description = "Elimina un curso permanentemente de la base de datos")
    public ResponseEntity<Void> eliminarCursoPermanente(@PathVariable Long id) {
        try {
            cursoService.eliminarCursoPermanente(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nivel/{nivel}")
    @Operation(summary = "Obtener cursos por nivel", description = "Retorna cursos filtrados por nivel")
    public ResponseEntity<List<CursoDTO>> obtenerCursosPorNivel(@PathVariable Curso.Nivel nivel) {
        List<CursoDTO> cursos = cursoService.obtenerCursosPorNivel(nivel);
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/instructor/{instructorId}")
    @Operation(summary = "Obtener cursos por instructor", description = "Retorna cursos asignados a un instructor específico")
    public ResponseEntity<List<CursoDTO>> obtenerCursosPorInstructor(@PathVariable Long instructorId) {
        List<CursoDTO> cursos = cursoService.obtenerCursosPorInstructor(instructorId);
        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/buscar")
    @Operation(summary = "Buscar cursos por título", description = "Busca cursos que contengan el texto en el título")
    public ResponseEntity<List<CursoDTO>> buscarCursosPorTitulo(@RequestParam String titulo) {
        List<CursoDTO> cursos = cursoService.buscarCursosPorTitulo(titulo);
        return ResponseEntity.ok(cursos);
    }
}