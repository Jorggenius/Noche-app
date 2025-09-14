package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de un badge")
public class BadgeDTO {

    @Schema(description = "ID único del badge",
            example = "3",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Schema(description = "Nombre del badge",
            example = "Top Performer")
    private String nombre;

    @Schema(description = "Criterio o condición para otorgar el badge",
            example = "Completar 5 cursos en menos de 1 mes")
    private String criterio;

    @Schema(description = "Ruta o nombre del icono asociado",
            example = "icons/top_performer.png")
    private String icono;
}