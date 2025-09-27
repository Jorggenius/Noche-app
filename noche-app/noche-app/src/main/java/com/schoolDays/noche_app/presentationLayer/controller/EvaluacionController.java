package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.EvaluacionDTO;
import com.schoolDays.noche_app.businessLayer.dto.ErrorResponseData;
import com.schoolDays.noche_app.businessLayer.service.EvaluacionService;
import com.schoolDays.noche_app.persistenceLayer.entity.EvaluacionEntity;
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
@RequestMapping("/v1/evaluaciones")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Evaluaciones", description = "Gestión de evaluaciones y exámenes")
@CrossOrigin(origins = "*")
public class EvaluacionController {

    private final EvaluacionService evaluacionService;

    @PostMapping
    @Operation(
            summary = "Crear nueva evaluación",
            description = "Crea una evaluación asociada a un módulo específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Evaluación creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = EvaluacionDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Módulo no encontrado"
            )
    })
    public ResponseEntity<?> createEvaluacion(
            @Parameter(description = "Datos de la evaluación", required = true)
            @RequestBody EvaluacionDTO evaluacionDTO
    ) {
        log.info("POST /v1/evaluaciones - Creando evaluación: {}", evaluacionDTO.getTitulo());

        try {
            EvaluacionDTO createdEvaluacion = evaluacionService.createEvaluacion(evaluacionDTO);
            log.info("Evaluación creada exitosamente con ID: {}", createdEvaluacion.getIdEvaluacion());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdEvaluacion);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear evaluación: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("Error al crear evaluación: {}", e.getMessage());
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar evaluación por ID",
            description = "Obtiene información completa de una evaluación"
    )
    public ResponseEntity<EvaluacionDTO> getEvaluacionById(
            @Parameter(description = "ID de la evaluación", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.debug("GET /v1/evaluaciones/{} - Buscando evaluación", id);

        try {
            EvaluacionDTO evaluacion = evaluacionService.getEvaluacionById(id);
            return ResponseEntity.ok(evaluacion);
        } catch (RuntimeException e) {
            log.warn("Evaluación no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Listar todas las evaluaciones",
            description = "Obtiene lista completa de evaluaciones del sistema"
    )
    public ResponseEntity<List<EvaluacionDTO>> getAllEvaluaciones() {
        log.debug("GET /v1/evaluaciones - Obteniendo todas las evaluaciones");

        List<EvaluacionDTO> evaluaciones = evaluacionService.getAllEvaluaciones();
        log.debug("Se encontraron {} evaluaciones", evaluaciones.size());
        return ResponseEntity.ok(evaluaciones);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar evaluación",
            description = "Actualiza información de una evaluación existente"
    )
    public ResponseEntity<EvaluacionDTO> updateEvaluacion(
            @Parameter(description = "ID de la evaluación", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos a actualizar", required = true)
            @RequestBody EvaluacionDTO evaluacionDTO
    ) {
        log.info("PUT /v1/evaluaciones/{} - Actualizando evaluación", id);

        try {
            EvaluacionDTO updatedEvaluacion = evaluacionService.updateEvaluacion(id, evaluacionDTO);
            log.info("Evaluación actualizada exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedEvaluacion);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar evaluación",
            description = "Elimina una evaluación del sistema"
    )
    public ResponseEntity<Void> deleteEvaluacion(
            @Parameter(description = "ID de la evaluación", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("DELETE /v1/evaluaciones/{} - Eliminando evaluación", id);

        try {
            evaluacionService.deleteEvaluacion(id);
            log.info("Evaluación eliminada exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/modulo/{moduloId}")
    @Operation(
            summary = "Evaluaciones por módulo",
            description = "Obtiene todas las evaluaciones de un módulo específico"
    )
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesByModulo(
            @Parameter(description = "ID del módulo", required = true, example = "1")
            @PathVariable Integer moduloId
    ) {
        log.debug("GET /v1/evaluaciones/modulo/{} - Evaluaciones por módulo", moduloId);

        try {
            List<EvaluacionDTO> evaluaciones = evaluacionService.getEvaluacionesByModulo(moduloId);
            return ResponseEntity.ok(evaluaciones);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(
            summary = "Evaluaciones por tipo",
            description = "Obtiene evaluaciones filtradas por tipo"
    )
    public ResponseEntity<?> getEvaluacionesByTipo(
            @Parameter(description = "Tipo de evaluación", required = true, example = "MCQ")
            @PathVariable String tipo
    ) {
        log.debug("GET /v1/evaluaciones/tipo/{} - Evaluaciones por tipo", tipo);

        try {
            EvaluacionEntity.TipoEvaluacion tipoEnum = EvaluacionEntity.TipoEvaluacion.valueOf(tipo.toUpperCase());
            List<EvaluacionDTO> evaluaciones = evaluacionService.getEvaluacionesByTipo(tipoEnum);
            return ResponseEntity.ok(evaluaciones);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Tipo de evaluación inválido: " + tipo);
        }
    }

    @GetMapping("/curso/{cursoId}")
    @Operation(
            summary = "Evaluaciones por curso",
            description = "Obtiene todas las evaluaciones de un curso"
    )
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesByCurso(
            @Parameter(description = "ID del curso", required = true, example = "1")
            @PathVariable Integer cursoId
    ) {
        log.debug("GET /v1/evaluaciones/curso/{} - Evaluaciones por curso", cursoId);

        List<EvaluacionDTO> evaluaciones = evaluacionService.getEvaluacionesByCurso(cursoId);
        return ResponseEntity.ok(evaluaciones);
    }

    @PostMapping("/{id}/iniciar")
    @Operation(
            summary = "Iniciar evaluación",
            description = "Inicia una evaluación para un usuario específico"
    )
    public ResponseEntity<?> iniciarEvaluacion(
            @Parameter(description = "ID de la evaluación", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @RequestParam Integer usuarioId
    ) {
        log.info("POST /v1/evaluaciones/{}/iniciar?usuarioId={}", id, usuarioId);

        try {
            evaluacionService.iniciarEvaluacion(id, usuarioId);
            log.info("Evaluación iniciada para usuario {} en evaluación {}", usuarioId, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.warn("Error al iniciar evaluación: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PostMapping("/{id}/finalizar")
    @Operation(
            summary = "Finalizar evaluación",
            description = "Finaliza una evaluación y calcula el resultado"
    )
    public ResponseEntity<?> finalizarEvaluacion(
            @Parameter(description = "ID de la evaluación", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @RequestParam Integer usuarioId
    ) {
        log.info("POST /v1/evaluaciones/{}/finalizar?usuarioId={}", id, usuarioId);

        try {
            evaluacionService.finalizarEvaluacion(id, usuarioId);
            log.info("Evaluación finalizada para usuario {} en evaluación {}", usuarioId, id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.warn("Error al finalizar evaluación: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}/pendientes")
    @Operation(
            summary = "Evaluaciones pendientes",
            description = "Obtiene evaluaciones pendientes de un usuario"
    )
    public ResponseEntity<List<EvaluacionDTO>> getEvaluacionesPendientes(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Integer usuarioId
    ) {
        log.debug("GET /v1/evaluaciones/usuario/{}/pendientes", usuarioId);

        List<EvaluacionDTO> evaluaciones = evaluacionService.getEvaluacionesPendientes(usuarioId);
        return ResponseEntity.ok(evaluaciones);
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
