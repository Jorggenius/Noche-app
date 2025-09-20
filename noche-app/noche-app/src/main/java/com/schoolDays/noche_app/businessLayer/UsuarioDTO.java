package com.schoolDays.noche_app.businessLayer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO único para usuarios del sistema
 * Usado para crear, leer y actualizar usuarios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del usuario")
public class UsuarioDTO {

    @Schema(description = "ID único del usuario",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idUsuario;

    @Schema(description = "Nombre del usuario",
            example = "Carlos",
            required = true,
            maxLength = 100)
    private String nombre;

    @Schema(description = "Apellido del usuario",
            example = "Pérez",
            required = true,
            maxLength = 100)
    private String apellido;

    @Schema(description = "Correo electrónico del usuario",
            example = "carlos.perez@empresa.com",
            required = true,
            maxLength = 150)
    private String email;

    @Schema(description = "Contraseña del usuario",
            example = "password123",
            required = true)
    private String contrasena;

    @Schema(description = "Departamento del usuario",
            example = "Recursos Humanos",
            maxLength = 100)
    private String departamento;

    @Schema(description = "ID del rol asignado",
            example = "2",
            required = true)
    private Integer idRol;

    @Schema(description = "Nombre del rol",
            example = "USER",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String nombreRol;
}
