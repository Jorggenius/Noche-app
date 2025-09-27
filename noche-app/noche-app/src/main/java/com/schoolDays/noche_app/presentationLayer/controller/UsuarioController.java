package com.schoolDays.noche_app.presentationLayer.controller;

import com.schoolDays.noche_app.businessLayer.dto.UsuarioDTO;
import com.schoolDays.noche_app.businessLayer.dto.ErrorResponseData;
import com.schoolDays.noche_app.businessLayer.service.UsuarioService;
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

/**
 * Controlador REST para operaciones de usuarios
 *
 * RESPONSABILIDADES:
 * - Gestión de usuarios (empleados, instructores, admins)
 * - Autenticación y autorización
 * - Operaciones CRUD de usuarios
 * - Búsquedas por departamento y rol
 */
@RestController
@RequestMapping("v1/usuarios")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Usuarios", description = "Gestión de usuarios del sistema de capacitación")
@CrossOrigin(origins = "*")
public class UsuarioController {

    private final UsuarioService usuarioService;

    /**
     * Crear un nuevo usuario
     */
    @PostMapping
    @Operation(
            summary = "Crear nuevo usuario",
            description = "Crea un nuevo usuario con validación de correo único y rol válido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuario creado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos o correo duplicado"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor"
            )
    })
    public ResponseEntity<?> createUsuario(
            @Parameter(description = "Datos del usuario a crear", required = true)
            @RequestBody UsuarioDTO usuarioDTO
    ) {
        log.info("POST /v1/usuarios - Creando usuario: {}", usuarioDTO.getCorreo());

        try {
            UsuarioDTO createdUsuario = usuarioService.createUsuario(usuarioDTO);
            log.info("Usuario creado exitosamente con ID: {}", createdUsuario.getIdUsuario());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUsuario);
        } catch (IllegalArgumentException e) {
            log.warn("Error de validación al crear usuario: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Obtener usuario por ID
     */
    @GetMapping("/{id}")
    @Operation(
            summary = "Buscar usuario por ID",
            description = "Obtiene la información completa de un usuario específico"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    public ResponseEntity<UsuarioDTO> getUsuarioById(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.debug("GET /v1/usuarios/{} - Buscando usuario", id);

        try {
            UsuarioDTO usuario = usuarioService.getUsuarioById(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            log.warn("Usuario no encontrado con ID: {}", id);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Obtener todos los usuarios
     */
    @GetMapping
    @Operation(
            summary = "Listar todos los usuarios",
            description = "Obtiene la lista completa de usuarios del sistema"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios obtenida exitosamente",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioDTO.class)
            )
    )
    public ResponseEntity<List<UsuarioDTO>> getAllUsuarios() {
        log.debug("GET /v1/usuarios - Obteniendo todos los usuarios");

        List<UsuarioDTO> usuarios = usuarioService.getAllUsuarios();
        log.debug("Se encontraron {} usuarios", usuarios.size());
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Actualizar usuario existente
     */
    @PutMapping("/{id}")
    @Operation(
            summary = "Actualizar usuario",
            description = "Actualiza la información de un usuario. El correo no se puede modificar."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario actualizado exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos inválidos"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    public ResponseEntity<UsuarioDTO> updateUsuario(
            @Parameter(description = "ID del usuario a actualizar", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Datos a actualizar del usuario", required = true)
            @RequestBody UsuarioDTO usuarioDTO
    ) {
        log.info("PUT /v1/usuarios/{} - Actualizando usuario", id);

        try {
            UsuarioDTO updatedUsuario = usuarioService.updateUsuario(id, usuarioDTO);
            log.info("Usuario actualizado exitosamente ID: {}", id);
            return ResponseEntity.ok(updatedUsuario);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Usuario no encontrado para actualizar ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            log.warn("Error al actualizar usuario ID {}: {}", id, e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Eliminar usuario
     */
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Eliminar usuario",
            description = "Elimina un usuario del sistema. No se puede eliminar si es el único admin."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Usuario eliminado exitosamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "No se puede eliminar: es el único administrador"
            )
    })
    public ResponseEntity<Void> deleteUsuario(
            @Parameter(description = "ID del usuario a eliminar", required = true, example = "1")
            @PathVariable Integer id
    ) {
        log.info("DELETE /v1/usuarios/{} - Eliminando usuario", id);

        try {
            usuarioService.deleteUsuario(id);
            log.info("Usuario eliminado exitosamente ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            if (e.getMessage().contains("no encontrado")) {
                log.warn("Usuario no encontrado para eliminar ID: {}", id);
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("único administrador")) {
                log.warn("Intento de eliminar único administrador ID: {}", id);
                return ResponseEntity.status(HttpStatus.CONFLICT).build();
            }
            log.error("Error al eliminar usuario ID {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Buscar usuario por correo
     */
    @GetMapping("/correo/{correo}")
    @Operation(
            summary = "Buscar usuario por correo",
            description = "Obtiene un usuario específico por su dirección de correo"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuario encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado con ese correo"
            )
    })
    public ResponseEntity<UsuarioDTO> getUsuarioByCorreo(
            @Parameter(description = "Correo del usuario", required = true, example = "usuario@empresa.com")
            @PathVariable String correo
    ) {
        log.debug("GET /v1/usuarios/correo/{} - Buscando usuario por correo", correo);

        try {
            UsuarioDTO usuario = usuarioService.getUsuarioByCorreo(correo);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            log.warn("Usuario no encontrado con correo: {}", correo);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * Buscar usuarios por departamento
     */
    @GetMapping("/departamento/{departamento}")
    @Operation(
            summary = "Buscar usuarios por departamento",
            description = "Obtiene todos los usuarios de un departamento específico"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de usuarios del departamento",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioDTO.class)
            )
    )
    public ResponseEntity<List<UsuarioDTO>> getUsuariosByDepartamento(
            @Parameter(description = "Nombre del departamento", required = true, example = "Recursos Humanos")
            @PathVariable String departamento
    ) {
        log.debug("GET /v1/usuarios/departamento/{} - Usuarios por departamento", departamento);

        List<UsuarioDTO> usuarios = usuarioService.getUsuariosByDepartamento(departamento);
        log.debug("Se encontraron {} usuarios en departamento {}", usuarios.size(), departamento);
        return ResponseEntity.ok(usuarios);
    }

    /**
     * Obtener instructores
     */
    @GetMapping("/instructores")
    @Operation(
            summary = "Listar instructores",
            description = "Obtiene todos los usuarios con rol de instructor"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de instructores",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioDTO.class)
            )
    )
    public ResponseEntity<List<UsuarioDTO>> getInstructores() {
        log.debug("GET /v1/usuarios/instructores - Obteniendo instructores");

        List<UsuarioDTO> instructores = usuarioService.getInstructores();
        log.debug("Se encontraron {} instructores", instructores.size());
        return ResponseEntity.ok(instructores);
    }

    /**
     * Obtener estudiantes
     */
    @GetMapping("/estudiantes")
    @Operation(
            summary = "Listar estudiantes",
            description = "Obtiene todos los usuarios con rol de estudiante"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista de estudiantes",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = UsuarioDTO.class)
            )
    )
    public ResponseEntity<List<UsuarioDTO>> getEstudiantes() {
        log.debug("GET /v1/usuarios/estudiantes - Obteniendo estudiantes");

        List<UsuarioDTO> estudiantes = usuarioService.getEstudiantes();
        log.debug("Se encontraron {} estudiantes", estudiantes.size());
        return ResponseEntity.ok(estudiantes);
    }

    /**
     * Verificar disponibilidad de correo
     */
    @GetMapping("/correo/{correo}/disponible")
    @Operation(
            summary = "Verificar disponibilidad de correo",
            description = "Verifica si un correo está disponible para registro"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Verificación realizada",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = Boolean.class)
            )
    )
    public ResponseEntity<Boolean> isCorreoDisponible(
            @Parameter(description = "Correo a verificar", required = true, example = "nuevo@empresa.com")
            @PathVariable String correo
    ) {
        log.debug("GET /v1/usuarios/correo/{}/disponible - Verificando disponibilidad", correo);

        boolean isAvailable = usuarioService.isCorreoAvailable(correo);
        return ResponseEntity.ok(isAvailable);
    }

    /**
     * Buscar usuarios por nombre
     */
    @GetMapping("/buscar")
    @Operation(
            summary = "Buscar usuarios por nombre",
            description = "Busca usuarios que contengan el texto en nombre o apellido"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Búsqueda realizada exitosamente",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = UsuarioDTO.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Parámetro de búsqueda inválido"
            )
    })
    public ResponseEntity<?> buscarUsuarios(
            @Parameter(description = "Texto a buscar en nombre o apellido", required = true, example = "Carlos")
            @RequestParam String texto
    ) {
        log.debug("GET /v1/usuarios/buscar?texto={} - Buscando usuarios", texto);

        try {
            List<UsuarioDTO> usuarios = usuarioService.searchUsuariosByNombre(texto);
            log.debug("Se encontraron {} usuarios con texto: {}", usuarios.size(), texto);
            return ResponseEntity.ok(usuarios);
        } catch (IllegalArgumentException e) {
            log.warn("Parámetro de búsqueda inválido: {}", e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    /**
     * Cambiar contraseña
     */
    @PutMapping("/{id}/cambiar-contrasena")
    @Operation(
            summary = "Cambiar contraseña",
            description = "Permite a un usuario cambiar su contraseña"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Contraseña cambiada exitosamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Contraseña actual incorrecta o nueva contraseña inválida"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuario no encontrado"
            )
    })
    public ResponseEntity<?> cambiarContrasena(
            @Parameter(description = "ID del usuario", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Contraseña actual", required = true)
            @RequestParam String contrasenaActual,
            @Parameter(description = "Nueva contraseña", required = true)
            @RequestParam String nuevaContrasena
    ) {
        log.info("PUT /v1/usuarios/{}/cambiar-contrasena - Cambiando contraseña", id);

        try {
            usuarioService.changePassword(id, contrasenaActual, nuevaContrasena);
            log.info("Contraseña cambiada exitosamente para usuario ID: {}", id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.warn("Error al cambiar contraseña usuario ID {}: {}", id, e.getMessage());
            return createErrorResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (RuntimeException e) {
            log.warn("Usuario no encontrado ID: {}", id);
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
