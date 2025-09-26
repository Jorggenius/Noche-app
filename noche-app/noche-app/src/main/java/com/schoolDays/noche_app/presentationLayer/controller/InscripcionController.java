package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.InscripcionDTO;
import com.schoolDays.noche_app.businessLayer.service.InscripcionService;
import com.schoolDays.noche_app.persistenceLayer.entity.InscripcionEntity;
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

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/inscripciones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Inscripciones", description = "Gestión de inscripciones a cursos")
@CrossOrigin(origins = "*")
public class InscripcionController {

    private final InscripcionService inscripcionService;

    @PostMapping
    @Operation(
            summary = "Inscribir usuario a curso",
            description = "Registra la inscripción de un usuario a un curso específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Inscripción creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InscripcionDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Usuario ya inscrito o datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario o curso no encontrado"
            )
    })
    public ResponseEntity<InscripcionDTO> inscribirUsuario(
            @Parameter(description = "Datos de inscripción", required = true)
            @RequestBody InscripcionDTO inscripcionDTO
    ) {
        log.info("POST /api/v1/inscripciones - Inscribiendo usuario {} al curso {}",
                inscripcionDTO.getIdUsuario(), inscripcionDTO.getIdCurso());

        try {
            InscripcionDTO createdInscripcion = inscripcionService.inscribirUsuario(inscripcionDTO);
            log.info("Inscripción creada exitosamente con ID: {}", createdInscripcion.getIdInscripcion());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInscripcion);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación en inscripción: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("Error en inscripción: {}", e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar inscripción por ID",
            description = "Obtiene información completa de una inscripción específica"
    )
    public ResponseEntity<InscripcionDTO> getInscripcionById(
            @Parameter(description = "ID de la inscripción", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.debug("GET /api/v1/inscripciones/{} - Buscando inscripción", id);

        try {
            InscripcionDTO inscripcion = inscripcionService.getInscripcionById(id);
            return ResponseEntity.ok(inscripcion);
        } catch (RuntimeException e) {
            log.warn("Inscripción no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Listar todas las inscripciones",
            description = "Obtiene lista completa de inscripciones del sistema"
    )
    public ResponseEntity<List<InscripcionDTO>> getAllInscripciones() {
        log.debug("GET /api/v1/inscripciones - Obteniendo todas las inscripciones");

        List<InscripcionDTO> inscripciones = inscripcionService.getAllInscripciones();
        log.debug("Se encontraron {} inscripciones", inscripciones.size());
        return ResponseEntity.ok(inscripciones);
    }

    @PutMapping("/{id}/progreso")
    @Operation(
            summary = "Actualizar progreso",
            description = "Actualiza el progreso de una inscripción (0-100%)"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Progreso actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InscripcionDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Progreso inválido (debe estar entre 0 y 100)"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Inscripción no encontrada"
            )
    })
    public ResponseEntity<InscripcionDTO> updateProgreso(
            @Parameter(description = "ID de la inscripción", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Nuevo progreso (0-100)", required = true, example = "75.5")
            @RequestParam BigDecimal progreso
    ) {
        log.info("PUT /api/v1/inscripciones/{}/progreso?progreso={}", id, progreso);

        try {
            InscripcionDTO updatedInscripcion = inscripcionService.updateProgreso(id, progreso);
            log.info("Progreso actualizado para inscripción ID: {} a {}%", id, progreso);
            return ResponseEntity.ok(updatedInscripcion);
        } catch (IllegalArgumentException e) {
            log.warn("Progreso inválido para inscripción ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            log.warn("Inscripción no encontrada ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/cancelar")
    @Operation(
            summary = "Cancelar inscripción",
            description = "Cambia el estado de una inscripción a SUSPENDIDO"
    )
    public ResponseEntity<Void> cancelarInscripcion(
            @Parameter(description = "ID de la inscripción", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("PUT /api/v1/inscripciones/{}/cancelar - Cancelando inscripción", id);

        try {
            inscripcionService.cancelarInscripcion(id);
            log.info("Inscripción cancelada exitosamente ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.warn("Error al cancelar inscripción ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(
            summary = "Inscripciones por usuario",
            description = "Obtiene todas las inscripciones de un usuario específico"
    )
    public ResponseEntity<List<InscripcionDTO>> getInscripcionesByUsuario(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Integer usuarioId
    ) {
        log.debug("GET /api/v1/inscripciones/usuario/{} - Inscripciones por usuario", usuarioId);

        try {
            List<InscripcionDTO> inscripciones = inscripcionService.getInscripcionesByUsuario(usuarioId);
            return ResponseEntity.ok(inscripciones);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/curso/{cursoId}")
    @Operation(
            summary = "Inscripciones por curso",
            description = "Obtiene todas las inscripciones de un curso específico"
    )
    public ResponseEntity<List<InscripcionDTO>> getInscripcionesByCurso(
            @Parameter(description = "ID del curso", required = true, example = "1")
            @PathVariable Integer cursoId
    ) {
        log.debug("GET /api/v1/inscripciones/curso/{} - Inscripciones por curso", cursoId);

        try {
            List<InscripcionDTO> inscripciones = inscripcionService.getInscripcionesByCurso(cursoId);
            return ResponseEntity.ok(inscripciones);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estado/{estado}")
    @Operation(
            summary = "Inscripciones por estado",
            description = "Obtiene inscripciones filtradas por estado"
    )
    public ResponseEntity<List<InscripcionDTO>> getInscripcionesByEstado(
            @Parameter(description = "Estado de inscripción", required = true, example = "EN_PROGRESO")
            @PathVariable String estado
    ) {
        log.debug("GET /api/v1/inscripciones/estado/{} - Inscripciones por estado", estado);

        try {
            InscripcionEntity.Estado estadoEnum = InscripcionEntity.Estado.valueOf(estado.toUpperCase());
            List<InscripcionDTO> inscripciones = inscripcionService.getInscripcionesByEstado(estadoEnum);
            return ResponseEntity.ok(inscripciones);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/en-progreso")
    @Operation(
            summary = "Inscripciones en progreso",
            description = "Obtiene todas las inscripciones que están en progreso"
    )
    public ResponseEntity<List<InscripcionDTO>> getInscripcionesEnProgreso() {
        log.debug("GET /api/v1/inscripciones/en-progreso - Inscripciones en progreso");

        List<InscripcionDTO> inscripciones = inscripcionService.getInscripcionesEnProgreso();
        return ResponseEntity.ok(inscripciones);
    }

    @PutMapping("/{id}/completar")
    @Operation(
            summary = "Completar curso",
            description = "Marca un curso como completado (progreso 100%)"
    )
    public ResponseEntity<InscripcionDTO> completarCurso(
            @Parameter(description = "ID de la inscripción", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("PUT /api/v1/inscripciones/{}/completar - Completando curso", id);

        try {
            InscripcionDTO completedInscripcion = inscripcionService.completarCurso(id);
            log.info("Curso completado para inscripción ID: {}", id);
            return ResponseEntity.ok(completedInscripcion);
        } catch (RuntimeException e) {
            log.warn("Error al completar curso para inscripción ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }
}
