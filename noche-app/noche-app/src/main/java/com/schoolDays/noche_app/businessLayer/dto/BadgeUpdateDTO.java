package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar un badge existente")
public class BadgeUpdateDTO {

    @Schema(description = "Nuevo nombre del badge",
            example = "Super Learner", maxLength = 100)
    private String nombre;

    @Schema(description = "Nuevo criterio para otorgar el badge",
            example = "Completar 10 cursos con más del 90% de calificación")
    private String criterio;

    @Schema(description = "Nueva ruta o nombre del icono asociado",
            example = "icons/super_learner.png")
    private String icono;
}