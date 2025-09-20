package com.schoolDays.noche_app.businessLayer;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO único para usuario-badge (asignaciones)
 * Usado para crear, leer y actualizar asignaciones de badges
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de asignación de badge")
public class UsuarioBadgeDTO {

    @Schema(description = "ID único de la asignación",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idUsuarioBadge;

    @Schema(description = "Fecha de otorgamiento",
            example = "2025-09-16",
            required = true)
    private LocalDate fechaOtorgado;

    @Schema(description = "ID del usuario",
            example = "1",
            required = true)
    private Integer idUsuario;

    @Schema(description = "Nombre del usuario",
            example = "Carlos Pérez",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String usuarioNombre;

    @Schema(description = "ID del badge",
            example = "1",
            required = true)
    private Integer idBadge;

    @Schema(description = "Nombre del badge",
            example = "Primer Paso",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String badgeNombre;

    @Schema(description = "Icono del badge",
            example = "icons/primer_paso.png",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String badgeIcono;
}
