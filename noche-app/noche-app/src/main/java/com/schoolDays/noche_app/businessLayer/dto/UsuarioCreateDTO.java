package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para CREAR usuarios - Solo campos que el cliente debe proporcionar
 *
 * ¿POR QUÉ SEPARAR CREATE DEL DTO COMPLETO?
 * 1. SEGURIDAD: Cliente NO puede enviar ID (lo genera la BD)
 * 2. SIMPLICIDAD: Solo lo esencial para crear el usuario
 * 3. VALIDACIÓN: Campos obligatorios claramente definidos
 *
 * CAMPOS INCLUIDOS:
 * - nombre, email, rol, departamento (opcional)
 *
 * CAMPOS EXCLUIDOS:
 * - id: Lo genera la base de datos
 *
 * USO: POST /usuarios
 */
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos necesarios para crear un usuario")
public class UsuarioCreateDTO {

    @Schema(description = "Nombre del usuario", required = true, example = "Ana")
    private String nombre;

    @Schema(description = "Apellido del usuario", required = true, example = "Martínez")
    private String apellido;

    @Schema(description = "Correo electrónico único", required = true, example = "ana.martinez@empresa.com")
    private String correo;

    @Schema(description = "Contraseña", required = true, example = "password123")
    private String contrasena;

    @Schema(description = "Departamento del usuario", example = "Recursos Humanos")
    private String departamento;

    @Schema(description = "ID del rol asignado al usuario", required = true, example = "1")
    private Integer rolId;
}