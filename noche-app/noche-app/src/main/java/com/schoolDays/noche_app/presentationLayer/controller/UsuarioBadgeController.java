package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.UsuarioBadgeDTO;
import com.schoolDays.noche_app.businessLayer.dto.ErrorResponseData;
import com.schoolDays.noche_app.businessLayer.service.UsuarioBadgeService;
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
@RequestMapping("/v1/usuario-badges")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuario Badges", description = "Asignación de badges a usuarios")
@CrossOrigin(origins = "*")
public class UsuarioBadgeController {

    private final UsuarioBadgeService usuarioBadgeService;

    @PostMapping
    @Operation(
            summary = "Otorgar badge a usuario",
            description = "Asigna un badge específico a un usuario"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Badge otorgado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioBadgeDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Usuario ya tiene el badge o no cumple criterios"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario o badge no encontrado"
            )
    })
    public ResponseEntity<?> otorgarBadge(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @RequestParam Integer idUsuario,
            @Parameter(description = "ID del badge", required = true, example = "1")
            @RequestParam Integer idBadge
    ) {
        log.info("POST /v1/usuario-badges?idUsuario={}&idBadge={} - Otorgando badge", idUsuario, idBadge);

        try {
            UsuarioBadgeDTO usuarioBadge = usuarioBadgeService.otorgarBadge(idUsuario, idBadge);
            log.info("Badge otorgado exitosamente con ID: {}", usuarioBadge.getIdUsuarioBadge());
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioBadge);
        } catch (IllegalArgumentException e) {
            log.warn("Error al otorgar badge: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("Usuario o badge no encontrado: {}", e.getMessage());
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar asignación por ID",
            description = "Obtiene información de una asignación específica"
    )
    public ResponseEntity<UsuarioBadgeDTO> getUsuarioBadgeById(
            @Parameter(description = "ID de la asignación", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.debug("GET /v1/usuario-badges/{} - Buscando asignación", id);

        try {
            UsuarioBadgeDTO usuarioBadge = usuarioBadgeService.getUsuarioBadgeById(id);
            return ResponseEntity.ok(usuarioBadge);
        } catch (RuntimeException e) {
            log.warn("Asignación no encontrada con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Listar todas las asignaciones",
            description = "Obtiene lista completa de asignaciones de badges"
    )
    public ResponseEntity<List<UsuarioBadgeDTO>> getAllUsuarioBadges() {
        log.debug("GET /v1/usuario-badges - Obteniendo todas las asignaciones");

        List<UsuarioBadgeDTO> usuarioBadges = usuarioBadgeService.getAllUsuarioBadges();
        log.debug("Se encontraron {} asignaciones", usuarioBadges.size());
        return ResponseEntity.ok(usuarioBadges);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Revocar badge",
            description = "Revoca un badge asignado a un usuario"
    )
    public ResponseEntity<Void> revocarBadge(
            @Parameter(description = "ID de la asignación", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("DELETE /v1/usuario-badges/{} - Revocando badge", id);

        try {
            usuarioBadgeService.revocarBadge(id);
            log.info("Badge revocado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    @Operation(
            summary = "Badges por usuario",
            description = "Obtiene todos los badges de un usuario específico"
    )
    public ResponseEntity<List<UsuarioBadgeDTO>> getBadgesByUsuario(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Integer usuarioId
    ) {
        log.debug("GET /v1/usuario-badges/usuario/{} - Badges por usuario", usuarioId);

        try {
            List<UsuarioBadgeDTO> badges = usuarioBadgeService.getBadgesByUsuario(usuarioId);
            return ResponseEntity.ok(badges);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/badge/{badgeId}")
    @Operation(
            summary = "Usuarios por badge",
            description = "Obtiene todos los usuarios que tienen un badge específico"
    )
    public ResponseEntity<List<UsuarioBadgeDTO>> getUsuariosByBadge(
            @Parameter(description = "ID del badge", required = true, example = "1")
            @PathVariable Integer badgeId
    ) {
        log.debug("GET /v1/usuario-badges/badge/{} - Usuarios por badge", badgeId);

        try {
            List<UsuarioBadgeDTO> usuarios = usuarioBadgeService.getUsuariosByBadge(badgeId);
            return ResponseEntity.ok(usuarios);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/recientes")
    @Operation(
            summary = "Asignaciones recientes",
            description = "Obtiene las asignaciones más recientes ordenadas por fecha"
    )
    public ResponseEntity<List<UsuarioBadgeDTO>> getAsignacionesRecientes() {
        log.debug("GET /v1/usuario-badges/recientes - Asignaciones recientes");

        List<UsuarioBadgeDTO> asignaciones = usuarioBadgeService.getAsignacionesRecientes();
        return ResponseEntity.ok(asignaciones);
    }

    @PostMapping("/otorgar-automatico")
    @Operation(
            summary = "Otorgar badge automático",
            description = "Otorga un badge si el usuario cumple automáticamente los criterios"
    )
    public ResponseEntity<?> otorgarBadgeAutomatico(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @RequestParam Integer idUsuario,
            @Parameter(description = "ID del badge", required = true, example = "1")
            @RequestParam Integer idBadge
    ) {
        log.info("POST /v1/usuario-badges/otorgar-automatico?idUsuario={}&idBadge={}", idUsuario, idBadge);

        try {
            UsuarioBadgeDTO usuarioBadge = usuarioBadgeService.otorgarBadgeAutomatico(idUsuario, idBadge);
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioBadge);
        } catch (IllegalArgumentException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/procesar-automaticos/{usuarioId}")
    @Operation(
            summary = "Procesar todos los badges automáticos",
            description = "Procesa y otorga todos los badges automáticos para un usuario"
    )
    public ResponseEntity<?> procesarBadgesAutomaticos(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Integer usuarioId
    ) {
        log.info("POST /v1/usuario-badges/procesar-automaticos/{}", usuarioId);

        try {
            usuarioBadgeService.procesarBadgesAutomaticos(usuarioId);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/ranking-usuarios")
    @Operation(
            summary = "Ranking de usuarios con más badges",
            description = "Obtiene el ranking de usuarios ordenado por cantidad de badges"
    )
    public ResponseEntity<List<Object[]>> getRankingUsuariosBadges() {
        log.debug("GET /v1/usuario-badges/ranking-usuarios - Ranking de usuarios");

        List<Object[]> ranking = usuarioBadgeService.getRankingUsuariosBadges();
        return ResponseEntity.ok(ranking);
    }

    @GetMapping("/tiene-badge")
    @Operation(
            summary = "Verificar si usuario tiene badge",
            description = "Verifica si un usuario específico tiene un badge determinado"
    )
    public ResponseEntity<Boolean> usuarioTieneBadge(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @RequestParam Integer idUsuario,
            @Parameter(description = "ID del badge", required = true, example = "1")
            @RequestParam Integer idBadge
    ) {
        log.debug("GET /v1/usuario-badges/tiene-badge?idUsuario={}&idBadge={}", idUsuario, idBadge);

        boolean tiene = usuarioBadgeService.usuarioTieneBadge(idUsuario, idBadge);
        return ResponseEntity.ok(tiene);
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
