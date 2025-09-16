package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO COMPLETO de usuarios - Para LECTURA/RESPUESTAS
 *
 * ¿POR QUÉ ESTE DTO?
 * - Se usa cuando DEVOLVEMOS información completa al cliente
 * - Incluye el ID (solo lectura) y todos los campos de la entidad
 *
 * USO: GET /usuarios/{id}, GET /usuarios, respuestas de POST/PUT
 */
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de un usuario")
public class UsuarioDTO {

    @Schema(description = "ID único del usuario", example = "1")
    private Integer idUsuario;

    @Schema(description = "Nombre del usuario", example = "Carlos")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Pérez")
    private String apellido;

    @Schema(description = "Correo electrónico", example = "carlos.perez@empresa.com")
    private String correo;

    @Schema(description = "Departamento del usuario", example = "Ventas")
    private String departamento;

    @Schema(description = "ID del rol", example = "2")
    private Integer rolId;

    @Schema(description = "Nombre del rol", example = "Instructor")
    private String rolNombre;
}