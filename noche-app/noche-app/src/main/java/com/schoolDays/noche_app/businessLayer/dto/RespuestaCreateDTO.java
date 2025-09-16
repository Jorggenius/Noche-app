package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para crear una nueva opción de respuesta")
public class RespuestaCreateDTO {

    @Schema(description = "ID de la pregunta asociada",
            example = "7",
            required = true)
    private Integer preguntaId;

    @Schema(description = "Contenido de la opción de respuesta",
            example = "Java Development Kit",
            required = true)
    private String contenido;

    @Schema(description = "Indica si esta opción es la correcta",
            example = "true",
            required = true)
    private Boolean esCorrecta;
}