package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para actualizar una pregunta existente")
public class PreguntaUpdateDTO {

    @Schema(description = "Nuevo texto del enunciado de la pregunta",
            example = "¿Qué significa JVM en Java?")
    private String enunciado;
}