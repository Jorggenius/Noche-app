package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.CursoDTO;
import com.schoolDays.noche_app.businessLayer.dto.ErrorResponseData;
import com.schoolDays.noche_app.businessLayer.service.CursoService;
import com.schoolDays.noche_app.persistenceLayer.entity.CursoEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/v1/cursos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cursos", description = "Gestión de cursos de capacitación")
@CrossOrigin(origins = "*")
public class CursoController {

    private final CursoService cursoService;

    @PostMapping
    @Operation(
            summary = "Crear nuevo curso",
            description = "Crea un nuevo curso. Solo instructores y administradores pueden crear cursos."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Curso creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CursoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o usuario sin permisos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Usuario sin permisos para crear cursos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseData.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "id no existe",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseData.class)
                    )
            )
    })
    public ResponseEntity<?> createCurso(
            @Parameter(description = "Datos del curso a crear", required = true)
            @RequestBody CursoDTO cursoDTO
    ) {
        log.info("POST /v1/cursos - Creando curso: {}", cursoDTO.getTitulo());

        try {
            CursoDTO createdCurso = cursoService.createCurso(cursoDTO);
            log.info("Curso creado exitosamente con ID: {}", createdCurso.getIdCurso());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdCurso);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear curso: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar curso por ID",
            description = "Obtiene información completa de un curso específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Curso encontrado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CursoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curso no encontrado"
            )
    })
    public ResponseEntity<CursoDTO> getCursoById(
            @Parameter(description = "ID del curso", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.debug("GET /v1/cursos/{} - Buscando curso", id);

        try {
            CursoDTO curso = cursoService.getCursoById(id);
            return ResponseEntity.ok(curso);
        } catch (RuntimeException e) {
            log.warn("Curso no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los cursos",
            description = "Obtiene lista completa de cursos disponibles"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de cursos obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CursoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron cursos"
            )
    })
    public ResponseEntity<List<CursoDTO>> getAllCursos() {
        log.debug("GET /v1/cursos - Obteniendo todos los cursos");

        List<CursoDTO> cursos = cursoService.getAllCursos();
        log.debug("Se encontraron {} cursos", cursos.size());

        if (cursos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cursos);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar curso",
            description = "Actualiza información de un curso. Solo el creador o admin pueden modificar."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Curso actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CursoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curso no encontrado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseData.class)
                    )
            )
    })
    public ResponseEntity<CursoDTO> updateCurso(
            @Parameter(description = "ID del curso", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos a actualizar", required = true)
            @RequestBody CursoDTO cursoDTO
    ) {
        log.info("PUT /v1/cursos/{} - Actualizando curso", id);

        try {
            CursoDTO updatedCurso = cursoService.updateCurso(id, cursoDTO);
            log.info("Curso actualizado exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedCurso);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar curso",
            description = "Elimina un curso. No se puede eliminar si tiene inscripciones activas."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Curso eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curso no encontrado"
            ),


    })
    public ResponseEntity<Void> deleteCurso(
            @Parameter(description = "ID del curso", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("DELETE /v1/cursos/{} - Eliminando curso", id);

        try {
            cursoService.deleteCurso(id);
            log.info("Curso eliminado exitosamente ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("inscripciones")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/instructor/{instructorId}")
    @Operation(
            summary = "Cursos por instructor",
            description = "Obtiene todos los cursos creados por un instructor"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de cursos del instructor obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CursoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron cursos para el instructor especificado"
            )
    })
    public ResponseEntity<List<CursoDTO>> getCursosByCreador(
            @Parameter(description = "ID del instructor", required = true, example = "1")
            @PathVariable Integer instructorId
    ) {
        log.debug("GET /v1/cursos/instructor/{} - Cursos por instructor", instructorId);

        try {
            List<CursoDTO> cursos = cursoService.getCursosByCreador(instructorId);

            if (cursos.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(cursos);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nivel/{nivel}")
    @Operation(
            summary = "Cursos por nivel",
            description = "Obtiene cursos filtrados por nivel de dificultad"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de cursos del nivel obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CursoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron cursos para el nivel especificado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Nivel inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseData.class)
                    )
            )
    })
    public ResponseEntity<?> getCursosByNivel(
            @Parameter(description = "Nivel del curso", required = true, example = "BASICO")
            @PathVariable String nivel
    ) {
        log.debug("GET /v1/cursos/nivel/{} - Cursos por nivel", nivel);

        try {
            CursoEntity.Nivel nivelEnum = CursoEntity.Nivel.valueOf(nivel.toUpperCase());
            List<CursoDTO> cursos = cursoService.getCursosByNivel(nivelEnum);

            if (cursos.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(cursos);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Nivel inválido: " + nivel);
        }
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar cursos por título",
            description = "Busca cursos que contengan el texto en el título"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Búsqueda completada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CursoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron cursos con el título especificado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetro de búsqueda inválido",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseData.class)
                    )
            )
    })
    public ResponseEntity<?> searchCursosByTitulo(
            @Parameter(description = "Texto a buscar", required = true, example = "Java")
            @RequestParam String titulo
    ) {
        log.debug("GET /v1/cursos/buscar?titulo={} - Buscando cursos", titulo);

        try {
            List<CursoDTO> cursos = cursoService.searchCursosByTitulo(titulo);

            if (cursos.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(cursos);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/populares")
    @Operation(
            summary = "Cursos más populares",
            description = "Obtiene cursos ordenados por número de inscripciones"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de cursos populares obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CursoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron cursos"
            )
    })
    public ResponseEntity<List<CursoDTO>> getCursosMasPopulares() {
        log.debug("GET /v1/cursos/populares - Cursos más populares");

        List<CursoDTO> cursos = cursoService.getCursosMasPopulares();

        if (cursos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cursos);
    }

    @GetMapping("/recientes")
    @Operation(
            summary = "Cursos más recientes",
            description = "Obtiene cursos ordenados por fecha de creación descendente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista de cursos recientes obtenida exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CursoDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "No se encontraron cursos"
            )
    })
    public ResponseEntity<List<CursoDTO>> getCursosMasRecientes() {
        log.debug("GET /v1/cursos/recientes - Cursos más recientes");

        List<CursoDTO> cursos = cursoService.getCursosMasRecientes();

        if (cursos.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cursos);
    }

    private ResponseEntity<ErrorResponseData> createErrorResponse(HttpStatus status, String message) {
        ErrorResponseData error = new ErrorResponseData(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message
        );
        return ResponseEntity.status(status).body(error);
    }
}
