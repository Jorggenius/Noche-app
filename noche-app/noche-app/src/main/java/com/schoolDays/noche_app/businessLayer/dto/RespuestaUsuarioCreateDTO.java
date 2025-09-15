package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Datos para registrar la respuesta de un usuario a una pregunta")
public class RespuestaUsuarioCreateDTO {

    @Schema(description = "ID del usuario que responde",
            example = "12",
            required = true)
    private Integer usuarioId;

    @Schema(description = "ID de la pregunta respondida",
            example = "7",
            required = true)
    private Integer preguntaId;

    @Schema(description = "ID de la opción seleccionada (para preguntas MCQ)",
            example = "15")
    private Integer respuestaId;

    @Schema(description = "Texto de la respuesta (para preguntas abiertas)",
            example = "Java Development Kit es un conjunto de herramientas...")
    private String respuestaTexto;
}
