package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear una nueva pregunta")
public class PreguntaCreateDTO {

    @Schema(description = "ID de la evaluación asociada",
            example = "3",
            required = true)
    private Integer evaluacionId;

    @Schema(description = "Texto del enunciado de la pregunta",
            example = "¿Qué significa JDK en Java?",
            required = true)
    private String enunciado;
}
