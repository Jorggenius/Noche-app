package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.ModuloDTO;
import com.schoolDays.noche_app.businessLayer.dto.ErrorResponseData;
import com.schoolDays.noche_app.businessLayer.service.ModuloService;
import com.schoolDays.noche_app.persistenceLayer.entity.ModuloEntity;
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
@RequestMapping("/v1/modulos")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Módulos", description = "Gestión de módulos de cursos")
@CrossOrigin(origins = "*")
public class ModuloController {

    private final ModuloService moduloService;

    @PostMapping
    @Operation(
            summary = "Crear nuevo módulo",
            description = "Crea un módulo dentro de un curso existente"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Módulo creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ModuloDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos u orden duplicado"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Curso no encontrado"
            )
    })
    public ResponseEntity<?> createModulo(
            @Parameter(description = "Datos del módulo a crear", required = true)
            @RequestBody ModuloDTO moduloDTO
    ) {
        log.info("POST /v1/modulos - Creando módulo: {}", moduloDTO.getTitulo());

        try {
            ModuloDTO createdModulo = moduloService.createModulo(moduloDTO);
            log.info("Módulo creado exitosamente con ID: {}", createdModulo.getIdModulo());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdModulo);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear módulo: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("Error al crear módulo: {}", e.getMessage());
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar módulo por ID",
            description = "Obtiene información completa de un módulo específico"
    )
    public ResponseEntity<ModuloDTO> getModuloById(
            @Parameter(description = "ID del módulo", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.debug("GET /v1/modulos/{} - Buscando módulo", id);

        try {
            ModuloDTO modulo = moduloService.getModuloById(id);
            return ResponseEntity.ok(modulo);
        } catch (RuntimeException e) {
            log.warn("Módulo no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los módulos",
            description = "Obtiene lista completa de módulos del sistema"
    )
    public ResponseEntity<List<ModuloDTO>> getAllModulos() {
        log.debug("GET /v1/modulos - Obteniendo todos los módulos");

        List<ModuloDTO> modulos = moduloService.getAllModulos();
        log.debug("Se encontraron {} módulos", modulos.size());
        return ResponseEntity.ok(modulos);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar módulo",
            description = "Actualiza información de un módulo. No se puede cambiar de curso."
    )
    public ResponseEntity<ModuloDTO> updateModulo(
            @Parameter(description = "ID del módulo", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos a actualizar", required = true)
            @RequestBody ModuloDTO moduloDTO
    ) {
        log.info("PUT /v1/modulos/{} - Actualizando módulo", id);

        try {
            ModuloDTO updatedModulo = moduloService.updateModulo(id, moduloDTO);
            log.info("Módulo actualizado exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedModulo);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar módulo",
            description = "Elimina un módulo del sistema"
    )
    public ResponseEntity<Void> deleteModulo(
            @Parameter(description = "ID del módulo", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("DELETE /v1/modulos/{} - Eliminando módulo", id);

        try {
            moduloService.deleteModulo(id);
            log.info("Módulo eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/curso/{cursoId}")
    @Operation(
            summary = "Módulos por curso",
            description = "Obtiene todos los módulos de un curso ordenados por orden"
    )
    public ResponseEntity<List<ModuloDTO>> getModulosByCurso(
            @Parameter(description = "ID del curso", required = true, example = "1")
            @PathVariable Integer cursoId
    ) {
        log.debug("GET /v1/modulos/curso/{} - Módulos por curso", cursoId);

        try {
            List<ModuloDTO> modulos = moduloService.getModulosByCursoOrdenados(cursoId);
            return ResponseEntity.ok(modulos);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tipo/{tipo}")
    @Operation(
            summary = "Módulos por tipo",
            description = "Obtiene módulos filtrados por tipo de contenido"
    )
    public ResponseEntity<?> getModulosByTipo(
            @Parameter(description = "Tipo de módulo", required = true, example = "VIDEO")
            @PathVariable String tipo
    ) {
        log.debug("GET /v1/modulos/tipo/{} - Módulos por tipo", tipo);

        try {
            ModuloEntity.TipoModulo tipoEnum = ModuloEntity.TipoModulo.valueOf(tipo.toUpperCase());
            List<ModuloDTO> modulos = moduloService.getModulosByTipo(tipoEnum);
            return ResponseEntity.ok(modulos);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, "Tipo de módulo inválido: " + tipo);
        }
    }

    @GetMapping("/curso/{cursoId}/primero")
    @Operation(
            summary = "Primer módulo del curso",
            description = "Obtiene el primer módulo de un curso (menor orden)"
    )
    public ResponseEntity<ModuloDTO> getPrimerModulo(
            @Parameter(description = "ID del curso", required = true, example = "1")
            @PathVariable Integer cursoId
    ) {
        log.debug("GET /v1/modulos/curso/{}/primero - Primer módulo", cursoId);

        try {
            ModuloDTO modulo = moduloService.getPrimerModuloCurso(cursoId);
            return ResponseEntity.ok(modulo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/curso/{cursoId}/ultimo")
    @Operation(
            summary = "Último módulo del curso",
            description = "Obtiene el último módulo de un curso (mayor orden)"
    )
    public ResponseEntity<ModuloDTO> getUltimoModulo(
            @Parameter(description = "ID del curso", required = true, example = "1")
            @PathVariable Integer cursoId
    ) {
        log.debug("GET /v1/modulos/curso/{}/ultimo - Último módulo", cursoId);

        try {
            ModuloDTO modulo = moduloService.getUltimoModuloCurso(cursoId);
            return ResponseEntity.ok(modulo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/orden")
    @Operation(
            summary = "Cambiar orden de módulo",
            description = "Cambia la posición de un módulo dentro del curso"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Orden cambiado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ModuloDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Orden inválido"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Módulo no encontrado"
            )
    })
    public ResponseEntity<?> cambiarOrdenModulo(
            @Parameter(description = "ID del módulo", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Nuevo orden", required = true, example = "3")
            @RequestParam Integer nuevoOrden
    ) {
        log.info("PUT /v1/modulos/{}/orden?nuevoOrden={}", id, nuevoOrden);

        try {
            ModuloDTO updatedModulo = moduloService.cambiarOrdenModulo(id, nuevoOrden);
            log.info("Orden cambiado para módulo ID: {} a orden {}", id, nuevoOrden);
            return ResponseEntity.ok(updatedModulo);
        } catch (IllegalArgumentException e) {
            log.warn("Orden inválido para módulo ID {}: {}", id, e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("Módulo no encontrado ID: {}", id);
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/siguiente")
    @Operation(
            summary = "Siguiente módulo",
            description = "Obtiene el siguiente módulo en el orden del curso"
    )
    public ResponseEntity<ModuloDTO> getSiguienteModulo(
            @Parameter(description = "ID del módulo actual", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.debug("GET /v1/modulos/{}/siguiente - Siguiente módulo", id);

        try {
            ModuloDTO siguienteModulo = moduloService.getSiguienteModulo(id);
            return ResponseEntity.ok(siguienteModulo);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/anterior")
    @Operation(
            summary = "Módulo anterior",
            description = "Obtiene el módulo anterior en el orden del curso"
    )
    public ResponseEntity<ModuloDTO> getModuloAnterior(
            @Parameter(description = "ID del módulo actual", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.debug("GET /v1/modulos/{}/anterior - Módulo anterior", id);

        try {
            ModuloDTO moduloAnterior = moduloService.getModuloAnterior(id);
            return ResponseEntity.ok(moduloAnterior);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/curso/{cursoId}/count")
    @Operation(
            summary = "Contar módulos del curso",
            description = "Obtiene el número total de módulos en un curso"
    )
    public ResponseEntity<Long> getModulosCountByCurso(
            @Parameter(description = "ID del curso", required = true, example = "1")
            @PathVariable Integer cursoId
    ) {
        log.debug("GET /v1/modulos/curso/{}/count - Conteo de módulos", cursoId);

        try {
            Long count = moduloService.getModulosCountByCurso(cursoId);
            return ResponseEntity.ok(count);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
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
