package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para ACTUALIZAR usuarios - Solo campos modificables
 *
 * ¿POR QUÉ SEPARAR UPDATE DEL CREATE?
 * 1. FLEXIBILIDAD: Todos los campos son OPCIONALES (permite actualización parcial)
 * 2. LÓGICA DE NEGOCIO: Email NO se cambia con este DTO (identificador de usuario)
 * 3. SEGURIDAD: Evita cambios no autorizados de email
 *
 * CAMPOS INCLUIDOS:
 * - nombre, rol, departamento (todos opcionales)
 *
 * CAMPOS EXCLUIDOS:
 * - id: Inmutable
 * - email: Política de negocio: no se cambia con este endpoint
 *
 * USO: PUT/PATCH /usuarios/{id}
 */
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar un usuario existente")
public class UsuarioUpdateDTO {

    @Schema(description = "Nombre del usuario", example = "Ana")
    private String nombre;

    @Schema(description = "Apellido del usuario", example = "Martínez")
    private String apellido;

    @Schema(description = "Correo electrónico único", example = "ana.martinez@empresa.com")
    private String correo;

    @Schema(description = "Contraseña", example = "nuevaPassword123")
    private String contrasena;

    @Schema(description = "Departamento del usuario", example = "Recursos Humanos")
    private String departamento;
}