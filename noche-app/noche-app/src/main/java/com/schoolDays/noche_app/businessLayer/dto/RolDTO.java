package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO único para roles del sistema
 * Usado para crear, leer y actualizar roles
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del rol")
public class RolDTO {

    @Schema(description = "ID único del rol",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idRol;

    @Schema(description = "Nombre del rol",
            example = "ADMIN",
            required = true,
            maxLength = 50)
    private String nombreRol;

    @Schema(description = "Descripción del rol",
            example = "Administrador del sistema con permisos completos",
            maxLength = 255)
    private String descripcion;
}
