package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.RolDTO;
import com.schoolDays.noche_app.businessLayer.dto.ErrorResponseData;
import com.schoolDays.noche_app.businessLayer.service.RolService;
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
@RequestMapping("/v1/roles")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Roles", description = "Gestión de roles y permisos del sistema")
@CrossOrigin(origins = "*")
public class RolController {

    private final RolService rolService;

    @PostMapping
    @Operation(
            summary = "Crear nuevo rol",
            description = "Crea un rol del sistema con nombre único"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Rol creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RolDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o nombre duplicado"
            )
    })
    public ResponseEntity<?> createRol(
            @Parameter(description = "Datos del rol a crear", required = true)
            @RequestBody RolDTO rolDTO
    ) {
        log.info("POST /v1/roles - Creando rol: {}", rolDTO.getNombreRol());

        try {
            RolDTO createdRol = rolService.createRol(rolDTO);
            log.info("Rol creado exitosamente con ID: {}", createdRol.getIdRol());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRol);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear rol: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar rol por ID",
            description = "Obtiene información completa de un rol específico"
    )
    public ResponseEntity<RolDTO> getRolById(
            @Parameter(description = "ID del rol", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.debug("GET /v1/roles/{} - Buscando rol", id);

        try {
            RolDTO rol = rolService.getRolById(id);
            return ResponseEntity.ok(rol);
        } catch (RuntimeException e) {
            log.warn("Rol no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @Operation(
            summary = "Listar todos los roles",
            description = "Obtiene lista completa de roles del sistema"
    )
    public ResponseEntity<List<RolDTO>> getAllRoles() {
        log.debug("GET /v1/roles - Obteniendo todos los roles");

        List<RolDTO> roles = rolService.getAllRoles();
        log.debug("Se encontraron {} roles", roles.size());
        return ResponseEntity.ok(roles);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar rol",
            description = "Actualiza información de un rol existente"
    )
    public ResponseEntity<RolDTO> updateRol(
            @Parameter(description = "ID del rol", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos a actualizar", required = true)
            @RequestBody RolDTO rolDTO
    ) {
        log.info("PUT /v1/roles/{} - Actualizando rol", id);

        try {
            RolDTO updatedRol = rolService.updateRol(id, rolDTO);
            log.info("Rol actualizado exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedRol);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar rol",
            description = "Elimina un rol del sistema"
    )
    public ResponseEntity<Void> deleteRol(
            @Parameter(description = "ID del rol", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("DELETE /v1/roles/{} - Eliminando rol", id);

        try {
            rolService.deleteRol(id);
            log.info("Rol eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/nombre/{nombreRol}")
    @Operation(
            summary = "Buscar rol por nombre",
            description = "Obtiene un rol específico por su nombre"
    )
    public ResponseEntity<RolDTO> getRolByNombre(
            @Parameter(description = "Nombre del rol", required = true, example = "ADMIN")
            @PathVariable String nombreRol
    ) {
        log.debug("GET /v1/roles/nombre/{} - Buscando rol por nombre", nombreRol);

        try {
            RolDTO rol = rolService.getRolByNombre(nombreRol);
            return ResponseEntity.ok(rol);
        } catch (RuntimeException e) {
            log.warn("Rol no encontrado con nombre: {}", nombreRol);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/nombre/{nombreRol}/disponible")
    @Operation(
            summary = "Verificar disponibilidad de nombre",
            description = "Verifica si un nombre de rol está disponible"
    )
    public ResponseEntity<Boolean> isNombreRolDisponible(
            @Parameter(description = "Nombre a verificar", required = true, example = "NUEVO_ROL")
            @PathVariable String nombreRol
    ) {
        log.debug("GET /v1/roles/nombre/{}/disponible - Verificando disponibilidad", nombreRol);

        boolean disponible = !rolService.isNombreRolTaken(nombreRol);
        return ResponseEntity.ok(disponible);
    }

    @GetMapping("/count")
    @Operation(
            summary = "Contar roles",
            description = "Obtiene el número total de roles en el sistema"
    )
    public ResponseEntity<Long> getTotalRolesCount() {
        log.debug("GET /v1/roles/count - Conteo de roles");

        long count = rolService.getTotalRolesCount();
        return ResponseEntity.ok(count);
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
