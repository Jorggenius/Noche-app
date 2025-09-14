package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear un nuevo badge")
public class BadgeCreateDTO {

    @Schema(description = "Nombre del badge",
            example = "Top Performer",
            required = true, maxLength = 100)
    private String nombre;

    @Schema(description = "Criterio o condición para otorgar el badge",
            example = "Completar 5 cursos en menos de 1 mes",
            required = true)
    private String criterio;

    @Schema(description = "Ruta o nombre del icono asociado",
            example = "icons/top_performer.png")
    private String icono;
}