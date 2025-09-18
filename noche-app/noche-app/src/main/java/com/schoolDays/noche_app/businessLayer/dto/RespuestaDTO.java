package com.schoolDays.noche_app.businessLayer.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO único para respuestas (opciones de MCQ)
 * Usado para crear, leer y actualizar opciones de respuesta
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Información de la opción de respuesta")
public class RespuestaDTO {

    @Schema(description = "ID único de la respuesta",
            example = "1",
            accessMode = Schema.AccessMode.READ_ONLY)
    private Integer idRespuesta;

    @Schema(description = "Contenido de la opción",
            example = "Un espacio en memoria para almacenar datos",
            required = true)
    private String contenido;

    @Schema(description = "Indica si es la respuesta correcta",
            example = "true",
            required = true)
    private Boolean esCorrecta;

    @Schema(description = "Orden de la opción",
            example = "1",
            minimum = "1")
    private Integer orden;

    @Schema(description = "ID de la pregunta",
            example = "1",
            required = true)
    private Integer idPregunta;

    @Schema(description = "Enunciado de la pregunta",
            example = "¿Qué es una variable en Java?",
            accessMode = Schema.AccessMode.READ_ONLY)
    private String preguntaEnunciado;
}
