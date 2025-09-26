package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.BadgeDTO;
import com.schoolDays.noche_app.businessLayer.service.BadgeService;
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

import java.util.List;

@RestController
@RequestMapping("/api/v1/badges")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Badges", description = "Sistema de gamificación - gestión de logros")
@CrossOrigin(origins = "*")
public class BadgeController {

    private final BadgeService badgeService;

    @PostMapping
    @Operation(
            summary = "Crear nuevo badge",
            description = "Crea un nuevo badge con nombre único y criterio específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Badge creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BadgeDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o nombre duplicado"
            )
    })
    public ResponseEntity<BadgeDTO> createBadge(
            @Parameter(description = "Datos del badge a crear", required = true)
            @RequestBody BadgeDTO badgeDTO
    ) {
        log.info("POST /api/v1/badges - Creando badge: {}", badgeDTO.getNombre());

        try {
            BadgeDTO createdBadge = badgeService.createBadge(badgeDTO);
            log.info("Badge creado exitosamente con ID: {}", createdBadge.getIdBadge());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdBadge);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear badge: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar badge por ID",
            description = "Obtiene información completa de un badge específico"
    )
    public ResponseEntity<BadgeDTO> getBadgeById(
            @Parameter(description = "ID del badge", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.debug("GET /api/v1/badges/{} - Buscando badge", id);

        try {
            BadgeDTO badge = badgeService.getBadgeById(id);
            return ResponseEntity.ok(badge);
        } catch (RuntimeException e) {
            log.warn("Badge no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los badges",
            description = "Obtiene lista completa de badges disponibles"
    )
    public ResponseEntity<List<BadgeDTO>> getAllBadges() {
        log.debug("GET /api/v1/badges - Obteniendo todos los badges");

        List<BadgeDTO> badges = badgeService.getAllBadges();
        log.debug("Se encontraron {} badges", badges.size());
        return ResponseEntity.ok(badges);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar badge",
            description = "Actualiza información de un badge existente"
    )
    public ResponseEntity<BadgeDTO> updateBadge(
            @Parameter(description = "ID del badge", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos a actualizar", required = true)
            @RequestBody BadgeDTO badgeDTO
    ) {
        log.info("PUT /api/v1/badges/{} - Actualizando badge", id);

        try {
            BadgeDTO updatedBadge = badgeService.updateBadge(id, badgeDTO);
            log.info("Badge actualizado exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedBadge);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar badge",
            description = "Elimina un badge del sistema. Solo si no tiene asignaciones."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Badge eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Badge no encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "No se puede eliminar: badge tiene asignaciones"
            )
    })
    public ResponseEntity<Void> deleteBadge(
            @Parameter(description = "ID del badge", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("DELETE /api/v1/badges/{} - Eliminando badge", id);

        try {
            badgeService.deleteBadge(id);
            log.info("Badge eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("asignaciones")) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar badges por nombre",
            description = "Busca badges que contengan el texto en el nombre"
    )
    public ResponseEntity<List<BadgeDTO>> searchBadgesByNombre(
            @Parameter(description = "Texto a buscar", required = true, example = "Primer")
            @RequestParam String nombre
    ) {
        log.debug("GET /api/v1/badges/buscar?nombre={} - Buscando badges", nombre);

        try {
            List<BadgeDTO> badges = badgeService.searchBadgesByNombre(nombre);
            return ResponseEntity.ok(badges);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/mas-otorgados")
    @Operation(
            summary = "Badges más otorgados",
            description = "Obtiene badges ordenados por número de asignaciones"
    )
    public ResponseEntity<List<BadgeDTO>> getBadgesMasOtorgados() {
        log.debug("GET /api/v1/badges/mas-otorgados - Badges más otorgados");

        List<BadgeDTO> badges = badgeService.getBadgesMasOtorgados();
        return ResponseEntity.ok(badges);
    }

    @GetMapping("/con-asignaciones")
    @Operation(
            summary = "Badges con asignaciones",
            description = "Obtiene badges que han sido otorgados al menos una vez"
    )
    public ResponseEntity<List<BadgeDTO>> getBadgesConAsignaciones() {
        log.debug("GET /api/v1/badges/con-asignaciones - Badges con asignaciones");

        List<BadgeDTO> badges = badgeService.getBadgesConAsignaciones();
        return ResponseEntity.ok(badges);
    }

    @GetMapping("/sin-asignaciones")
    @Operation(
            summary = "Badges sin asignaciones",
            description = "Obtiene badges que nunca han sido otorgados"
    )
    public ResponseEntity<List<BadgeDTO>> getBadgesSinAsignaciones() {
        log.debug("GET /api/v1/badges/sin-asignaciones - Badges sin asignaciones");

        List<BadgeDTO> badges = badgeService.getBadgesSinAsignaciones();
        return ResponseEntity.ok(badges);
    }

    @GetMapping("/nombre/{nombre}/disponible")
    @Operation(
            summary = "Verificar disponibilidad de nombre",
            description = "Verifica si un nombre de badge está disponible"
    )
    public ResponseEntity<Boolean> isNombreDisponible(
            @Parameter(description = "Nombre a verificar", required = true, example = "Nuevo Badge")
            @PathVariable String nombre
    ) {
        log.debug("GET /api/v1/badges/nombre/{}/disponible - Verificando disponibilidad", nombre);

        boolean disponible = badgeService.isNombreDisponible(nombre);
        return ResponseEntity.ok(disponible);
    }

    @PostMapping("/procesar-automaticos")
    @Operation(
            summary = "Procesar badges automáticos",
            description = "Procesa y otorga badges automáticos para un usuario"
    )
    public ResponseEntity<Void> procesarBadgesAutomaticos(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @RequestParam Integer idUsuario
    ) {
        log.info("POST /api/v1/badges/procesar-automaticos?idUsuario={}", idUsuario);

        try {
            badgeService.procesarBadgesAutomaticos(idUsuario);
            log.info("Badges automáticos procesados para usuario ID: {}", idUsuario);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            log.warn("Error al procesar badges automáticos: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/usuario/{idUsuario}/criterio/{idBadge}")
    @Operation(
            summary = "Verificar criterio de badge",
            description = "Verifica si un usuario cumple el criterio para obtener un badge"
    )
    public ResponseEntity<Boolean> cumpleCriterio(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Integer idUsuario,
            @Parameter(description = "ID del badge", required = true, example = "1")
            @PathVariable Integer idBadge
    ) {
        log.debug("GET /api/v1/badges/usuario/{}/criterio/{} - Verificando criterio", idUsuario, idBadge);

        try {
            boolean cumple = badgeService.cumpleCriterio(idUsuario, idBadge);
            return ResponseEntity.ok(cumple);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
