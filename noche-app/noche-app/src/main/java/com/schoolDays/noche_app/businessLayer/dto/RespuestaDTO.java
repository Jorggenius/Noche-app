package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información completa de una opción de respuesta")
public class RespuestaDTO {

    @Schema(description = "ID único de la opción de respuesta",
            example = "15",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idRespuesta;

    @Schema(description = "Contenido de la opción de respuesta",
            example = "Java Development Kit")
    private String contenido;

    @Schema(description = "Indica si la opción es correcta",
            example = "true")
    private Boolean esCorrecta;

    @Schema(description = "ID de la pregunta asociada",
            example = "7")
    private Integer preguntaId;
}