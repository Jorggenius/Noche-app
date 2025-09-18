package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO único para badges
 * Usado para crear, leer y actualizar badges
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información del badge")
public class BadgeDTO {

    @Schema(description = "ID único del badge",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idBadge;

    @Schema(description = "Nombre del badge",
            example = "Primer Paso",
            required = true,
            maxLength = 100)
    private String nombre;

    @Schema(description = "Criterio para obtener el badge",
            example = "Completar tu primer curso",
            required = true,
            maxLength = 500)
    private String criterio;

    @Schema(description = "Ruta del icono",
            example = "icons/primer_paso.png",
            maxLength = 255)
    private String icono;
}

