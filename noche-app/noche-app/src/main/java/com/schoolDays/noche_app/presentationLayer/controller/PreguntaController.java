package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.PreguntaDTO;
import com.schoolDays.noche_app.businessLayer.dto.ErrorResponseData;
import com.schoolDays.noche_app.businessLayer.service.PreguntaService;
import com.schoolDays.noche_app.persistenceLayer.entity.PreguntaEntity;
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
@RequestMapping("/v1/preguntas")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Preguntas", description = "Gestión de preguntas para evaluaciones")
@CrossOrigin(origins = "*")
public class PreguntaController {

    private final PreguntaService preguntaService;

    @PostMapping
    @Operation(
            summary = "Crear nueva pregunta",
            description = "Crea una pregunta asociada a una evaluación específica"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Pregunta creada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PreguntaDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos u orden duplicado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Evaluación no encontrada"
            )
    })
    public ResponseEntity<?> createPregunta(
            @Parameter(description = "Datos de la pregunta", required = true)
            @RequestBody PreguntaDTO preguntaDTO
    ) {
        log.info("POST /v1/preguntas - Creando pregunta para evaluación: {}", preguntaDTO.getIdEvaluacion());

        try {
            PreguntaDTO createdPregunta = preguntaService.createPregunta(preguntaDTO);
            log.info("Pregunta creada exitosamente con ID: {}", createdPregunta.getIdPregunta());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdPregunta);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear pregunta: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("Error al crear pregunta: {}", e.getMessage());
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar pregunta por ID",
            description = "Obtiene información completa de una pregunta específica"
    )
    public ResponseEntity<PreguntaDTO> getPreguntaById(
            @Parameter(description = "ID de la pregunta", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.debug("GET /v1/preguntas/{} - Buscando pregunta", id);

        try {
            PreguntaDTO pregunta = preguntaService.getPreguntaById(id);
            return ResponseEntity.ok(pregunta);
        } catch (RuntimeException e) {
            log.warn("Pregunta no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Listar todas las preguntas",
            description = "Obtiene lista completa de preguntas del sistema"
    )
    public ResponseEntity<List<PreguntaDTO>> getAllPreguntas() {
        log.debug("GET /v1/preguntas - Obteniendo todas las preguntas");

        List<PreguntaDTO> preguntas = preguntaService.getAllPreguntas();
        log.debug("Se encontraron {} preguntas", preguntas.size());
        return ResponseEntity.ok(preguntas);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar pregunta",
            description = "Actualiza información de una pregunta existente"
    )
    public ResponseEntity<PreguntaDTO> updatePregunta(
            @Parameter(description = "ID de la pregunta", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos a actualizar", required = true)
            @RequestBody PreguntaDTO preguntaDTO
    ) {
        log.info("PUT /v1/preguntas/{} - Actualizando pregunta", id);

        try {
            PreguntaDTO updatedPregunta = preguntaService.updatePregunta(id, preguntaDTO);
            log.info("Pregunta actualizada exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedPregunta);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar pregunta",
            description = "Elimina una pregunta del sistema"
    )
    public ResponseEntity<Void> deletePregunta(
            @Parameter(description = "ID de la pregunta", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("DELETE /v1/preguntas/{} - Eliminando pregunta", id);

        try {
            preguntaService.deletePregunta(id);
            log.info("Pregunta eliminada exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/evaluacion/{evaluacionId}")
    @Operation(
            summary = "Preguntas por evaluación",
            description = "Obtiene todas las preguntas de una evaluación ordenadas"
    )
    public ResponseEntity<List<PreguntaDTO>> getPreguntasByEvaluacion(
            @Parameter(description = "ID de la evaluación", required = true, example = "1")
            @PathVariable Integer evaluacionId
    ) {
        log.debug("GET /v1/preguntas/evaluacion/{} - Preguntas por evaluación", evaluacionId);

        try {
            List<PreguntaDTO> preguntas = preguntaService.getPreguntasByEvaluacionOrdenadas(evaluacionId);
            return ResponseEntity.ok(preguntas);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(
            summary = "Preguntas por tipo",
            description = "Obtiene preguntas filtradas por tipo"
    )
    public ResponseEntity<?> getPreguntasByTipo(
            @Parameter(description = "Tipo de pregunta", required = true, example = "MULTIPLE_CHOICE")
            @PathVariable String tipo
    ) {
        log.debug("GET /v1/preguntas/tipo/{} - Preguntas por tipo", tipo);

        try {
            PreguntaEntity.TipoPregunta tipoEnum = PreguntaEntity.TipoPregunta.valueOf(tipo.toUpperCase());
            List<PreguntaDTO> preguntas = preguntaService.getPreguntasByTipo(tipoEnum);
            return ResponseEntity.ok(preguntas);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Tipo de pregunta inválido: " + tipo);
        }
    }

    @GetMapping("/curso/{cursoId}")
    @Operation(
            summary = "Preguntas por curso",
            description = "Obtiene todas las preguntas de un curso"
    )
    public ResponseEntity<List<PreguntaDTO>> getPreguntasByCurso(
            @Parameter(description = "ID del curso", required = true, example = "1")
            @PathVariable Integer cursoId
    ) {
        log.debug("GET /v1/preguntas/curso/{} - Preguntas por curso", cursoId);

        List<PreguntaDTO> preguntas = preguntaService.getPreguntasByCurso(cursoId);
        return ResponseEntity.ok(preguntas);
    }

    @GetMapping("/evaluacion/{evaluacionId}/multiple-choice")
    @Operation(
            summary = "Preguntas de opción múltiple",
            description = "Obtiene solo las preguntas de opción múltiple de una evaluación"
    )
    public ResponseEntity<List<PreguntaDTO>> getPreguntasMultipleChoice(
            @Parameter(description = "ID de la evaluación", required = true, example = "1")
            @PathVariable Integer evaluacionId
    ) {
        log.debug("GET /v1/preguntas/evaluacion/{}/multiple-choice", evaluacionId);

        List<PreguntaDTO> preguntas = preguntaService.getPreguntasMultipleChoice(evaluacionId);
        return ResponseEntity.ok(preguntas);
    }

    @GetMapping("/evaluacion/{evaluacionId}/abiertas")
    @Operation(
            summary = "Preguntas abiertas",
            description = "Obtiene solo las preguntas abiertas de una evaluación"
    )
    public ResponseEntity<List<PreguntaDTO>> getPreguntasAbiertas(
            @Parameter(description = "ID de la evaluación", required = true, example = "1")
            @PathVariable Integer evaluacionId
    ) {
        log.debug("GET /v1/preguntas/evaluacion/{}/abiertas", evaluacionId);

        List<PreguntaDTO> preguntas = preguntaService.getPreguntasAbiertas(evaluacionId);
        return ResponseEntity.ok(preguntas);
    }

    @PutMapping("/{id}/orden")
    @Operation(
            summary = "Cambiar orden de pregunta",
            description = "Cambia la posición de una pregunta dentro de la evaluación"
    )
    public ResponseEntity<?> cambiarOrdenPregunta(
            @Parameter(description = "ID de la pregunta", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Nuevo orden", required = true, example = "2")
            @RequestParam Integer nuevoOrden
    ) {
        log.info("PUT /v1/preguntas/{}/orden?nuevoOrden={}", id, nuevoOrden);

        try {
            PreguntaDTO updatedPregunta = preguntaService.cambiarOrdenPregunta(id, nuevoOrden);
            log.info("Orden cambiado para pregunta ID: {} a orden {}", id, nuevoOrden);
            return ResponseEntity.ok(updatedPregunta);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/{id}/duplicar")
    @Operation(
            summary = "Duplicar pregunta",
            description = "Crea una copia de una pregunta en otra evaluación"
    )
    public ResponseEntity<?> duplicarPregunta(
            @Parameter(description = "ID de la pregunta original", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "ID de la evaluación destino", required = true, example = "2")
            @RequestParam Integer nuevaEvaluacionId
    ) {
        log.info("POST /v1/preguntas/{}/duplicar?nuevaEvaluacionId={}", id, nuevaEvaluacionId);

        try {
            PreguntaDTO duplicatedPregunta = preguntaService.duplicarPregunta(id, nuevaEvaluacionId);
            log.info("Pregunta duplicada exitosamente con ID: {}", duplicatedPregunta.getIdPregunta());
            return ResponseEntity.status(HttpStatus.CREATED).body(duplicatedPregunta);
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
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
